package com.c503.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.c503.datasources.MysqlGenerator;
import com.c503.service.SysGeneratorService;
import com.c503.utils.PageUtils;
import com.c503.utils.Query;
import com.c503.utils.R;

/**
 * 
 * 〈扩展代码代码自定义修改数据库链接地址〉
 * 〈功能详细描述〉
 * 
 * @author liumingming
 * @version [版本号, Nov 8, 2017]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    
    @Autowired
    private SysGeneratorService sysGeneratorService;
    
    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);
        PageUtils pageUtil =
            new PageUtils(list, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 生成代码
     * 
     * @throws Exception
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        String[] tableNames = new String[] {};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        
        // 链接参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ip", request.getParameter("ip"));
        map.put("port", request.getParameter("port"));
        map.put("database", request.getParameter("database"));
        map.put("username", request.getParameter("username"));
        map.put("password", request.getParameter("password"));
        map.put("type", request.getParameter("type"));
        
        // TODO 调用实现代码 根据数据库类型判断mysql oracle pgsql
        byte[] data = MysqlGenerator.generatorCodeByMysql(tableNames, map);
        
        response.reset();
        response.setHeader("Content-Disposition",
            "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        
        IOUtils.write(data, response.getOutputStream());
    }
    
    /**
     * 
     * 〈自定义链接数据库〉
     * 〈功能详细描述〉
     * 
     * @param params
     * @return
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping("/dsynamicList")
    public R dsynamicList(@RequestParam Map<String, Object> params)
        throws Exception {
        // 查询列表数据
        Query query = new Query(params);
        // 数据库链接配置
        Connection con = MysqlGenerator.getMysqlConn(params);
        // 链接判断
        if (null == con) {
            return R.error("链接失败，请查看填写的数据是否正确");
        }
        else {
            // 数据查询
            List<Map<String, Object>> list =
                MysqlGenerator.queryList(con, query);
            int total = MysqlGenerator.queryTotal(con, query);
            PageUtils pageUtil =
                new PageUtils(list, total, query.getLimit(), query.getPage());
            // 关闭数据库
            con.close();
            return R.ok().put("page", pageUtil);
        }
    }
    
    /**
     * 
     * 〈数据库链接测试〉
     * 〈功能详细描述〉
     * 
     * @param params
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping("/dsynamicTest")
    public R dsynamicTest(@RequestParam Map<String, Object> params)
        throws Exception {
        // 数据库链接配置
        Connection con = null;
        // 数据查询
        con = MysqlGenerator.getMysqlConn(params);
        if (null == con) {
            return R.error("链接失败，请查看填写的数据是否正确");
        }
        else {
            con.close();
            // 数据返回
            return R.ok("链接成功，请继续操作");
        }
    }
    
}
