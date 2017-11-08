package com.c503.controller;

import com.alibaba.fastjson.JSON;
import com.c503.datasources.MysqlGenerator;
import com.c503.service.SysGeneratorService;
import com.c503.utils.PageUtils;
import com.c503.utils.Query;
import com.c503.utils.R;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

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
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        String[] tableNames = new String[] {};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        byte[] data = sysGeneratorService.generatorCode(tableNames);
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
        // 数据查询
        List<Map<String, Object>> list = MysqlGenerator.queryList(con, query);
        int total = MysqlGenerator.queryTotal(con, query);
        PageUtils pageUtil =
            new PageUtils(list, total, query.getLimit(), query.getPage());
        // 关闭数据库
        con.close();
        return R.ok().put("page", pageUtil);
    }
    
}
