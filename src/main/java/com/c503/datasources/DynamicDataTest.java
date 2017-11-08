package com.c503.datasources;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicDataTest {
    
    public static void main(String[] args)
        throws ClassNotFoundException {
        // 声明Connection对象
        Connection con;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ip", "localhost");
        params.put("port", "3306");
        params.put("database", "generator_fast");
        params.put("username", "root");
        params.put("password", "root");
        params.put("type", "mysql");
        // 遍历查询结果集
        try {
            // 1.开启链接
            // TODO 重写service查询sql的语句，将con作为参数进行查询，返回结果完成之后关闭数据库链接
            con = MysqlGenerator.getMysqlConn(params);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tableName", "");
            map.put("limit", 10);
            map.put("offset", 0);
            List<Map<String, Object>> list = MysqlGenerator.queryList(con, map);
            for (Map<String, Object> map2 : list) {
                System.out.println(map2);
            }
            con.close();
        }
        catch (SQLException e) {
            // 数据库连接失败异常处理
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            
        }
    }
    
}