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
		rowNum : 10,
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
		showList : true
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
			location.href = "sys/generator/code?tables="
					+ JSON.stringify(tableNames);
		},
		dynamic : function() {
			var that = this;
			that.showList = false;
		},
		dynamicSubmit : function() {
			var that = this;
			if (vm.q.ip === null || vm.q.port === null
					|| vm.q.database === null || vm.q.username === null
					|| vm.q.password === null) {
				alert("请校验输入的参数或者类型是否正确");
				return;
			} else {
				// 数据校验
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
					}
				}).trigger("reloadGrid");
				that.showList = true;
			}
		},
		dynamicTest : function() {
			$.ajax({
			   type: "get",
			   url: "sys/generator/dsynamicTest",
			   data: {
				   	ip : vm.q.ip,
					port : vm.q.port,
					database : vm.q.database,
					username : vm.q.username,
					password : vm.q.password,
					type : vm.q.type
			   },
			   success: function(json){
				   alert(json.msg);
			   },
			   error: function(msg){
				   console.log(msg);
			   }
			});
		},
		dynamicCancel : function() {
			var that = this;
			that.showList = true;
		}
	}
});

// ip: "localhost",
// port: 3306,
// database: "renren_fast",
// username: "root",
// password: "root",
// type: "mysql",

