//默认值
var bak = {
	ip : "",
	port : 3306,
	database : "",
	username : "",
	password : "",
	type : "mysql"
};

$(function() {
	$("#jqGrid").jqGrid({
		url : 'sys/generator/list',
		datatype : "json",
		colModel : [ {
			label : '表名',
			name : 'tableName',
			width : 100,
			key : true
		}, {
			label : 'Engine',
			name : 'engine',
			width : 70
		}, {
			label : '表备注',
			name : 'tableComment',
			width : 100
		}, {
			label : '创建时间',
			name : 'createTime',
			width : 100
		} ],
		viewrecords : true,
		height : 700,
		rowNum : 24,
		rowList : [ 10, 30, 50, 100, 200 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});
// vue事件绑定
var vm = new Vue({
	el : '#generator-main',
	data : {
		q : {
			tableName : null,
			ip : null,
			port : null,
			database : null,
			username : null,
			password : null,
			type : "mysql"
		},
		bak : null,
		showList : true
	},
	mounted : function() {
		// 双向绑定配置信息
		var that = this;
		if (bak) {
			that.q.ip = bak.ip;
			that.q.port = bak.port;
			that.q.database = bak.database;
			that.q.username = bak.username;
			that.q.password = bak.password;
			that.q.type = bak.type;
		}
	},
	methods : {
		query : function() {
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'tableName' : vm.q.tableName
				},
				page : 1
			}).trigger("reloadGrid");
		},
		generator : function() {
			var tableNames = getSelectedRows();
			if (tableNames == null) {
				return;
			}
			// 参数拼接
			var params = "tables=" + JSON.stringify(tableNames) + "&";
			params += "ip=" + bak.ip + "&";
			params += "port=" + bak.port + "&";
			params += "database=" + bak.database + "&";
			params += "username=" + bak.username + "&";
			params += "password=" + bak.password + "&";
			params += "type=" + bak.type;
			// 执行下载
			location.href = "sys/generator/code?" + params;
		},
		dynamic : function() {
			var that = this;
			that.showList = false;
		},
		dynamicSubmit : function() {
			var that = this;
			// 参数合法性校验
			if (vm.q.ip === null || vm.q.ip === "" || vm.q.port === null
					|| vm.q.port === "" || vm.q.database === null
					|| vm.q.database === "" || vm.q.username === null
					|| vm.q.username === "" || vm.q.password === null
					|| vm.q.password === "") {
				alert("请校验输入的参数或者类型是否正确");
			}
			// 参数请求是否成功校验
			$.ajax({
				type : "get",
				url : "sys/generator/dsynamicTest",
				async : false,
				data : {
					ip : vm.q.ip,
					port : vm.q.port,
					database : vm.q.database,
					username : vm.q.username,
					password : vm.q.password,
					type : vm.q.type
				},
				success : function(json) {
					if (json.code === 0) {
						// 数据校验，防止之前的请求被覆盖表格
						vm.q.tableName = "";
						$("#jqGrid").jqGrid("setGridParam", {
							url : "sys/generator/dsynamicList",
							page : 1,
							postData : {
								limit : 10,
								page : 1,
								ip : vm.q.ip,
								port : vm.q.port,
								database : vm.q.database,
								username : vm.q.username,
								password : vm.q.password,
								type : vm.q.type,
								tableName : vm.q.tableName
							},
							loadComplete : function(json) {
								if (json.code === 3) {
									alert(json.msg);
								} else {
									// 备份数据，后续下载模板代码使用
									bak.ip = vm.q.ip;
									bak.port = vm.q.port;
									bak.database = vm.q.database;
									bak.username = vm.q.username;
									bak.password = vm.q.password;
									bak.type = vm.q.type;
									// 成功取消显示
									that.showList = true;
								}
							}
						}).trigger("reloadGrid");
					} else {
						// 主要防止修改原始备份数据以及请求错误返回页面表格没有数据
						alert("请校验输入的参数或者类型是否正确");
					}
				}
			});
		},
		dynamicTest : function() {
			if (vm.q.ip === null || vm.q.ip === "" || vm.q.port === null
					|| vm.q.port === "" || vm.q.database === null
					|| vm.q.database === "" || vm.q.username === null
					|| vm.q.username === "" || vm.q.password === null
					|| vm.q.password === "") {
				alert("请校验输入的参数或者类型是否正确");
			} else {
				$.ajax({
					type : "get",
					url : "sys/generator/dsynamicTest",
					async : false,
					data : {
						ip : vm.q.ip,
						port : vm.q.port,
						database : vm.q.database,
						username : vm.q.username,
						password : vm.q.password,
						type : vm.q.type
					},
					success : function(json) {
						alert(json.msg);
					}
				});
			}
		},
		dynamicCancel : function() {
			// 输入的表单是双向绑定的，需要还原备份的数据文件
			var that = this;
			// 还原值
			if (bak) {
				vm.q.ip = bak.ip;
				vm.q.port = bak.port;
				vm.q.database = bak.database;
				vm.q.username = bak.username;
				vm.q.password = bak.password;
				vm.q.type = bak.type;
			}
			that.showList = true;
		}
	}
});
