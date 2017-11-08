/**
 * 文件名：MysqlConnnector.java
 * 版权： www.liumingmusic.win
 * 描述：〈描述〉
 * 修改时间：Nov 7, 2017
 * 修改内容：〈修改内容〉
 */
package com.c503.datasources;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.c503.utils.GenUtils;

/**
 * 
 * 〈mysql链接查询〉
 * 〈功能详细描述〉
 * 
 * @author liumingming
 * @version [版本号, Nov 7, 2017]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MysqlGenerator {
    
    /**
     * 
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * 
     * @param map 配置参数
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Connection getMysqlConn(Map<String, Object> map) {
        Connection con = null;
        // 获取参数信息
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String database = (String) map.get("database");
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String type = (String) map.get("type");
        // 数据库链接实体封装
        DynamicDataEntity dynamicDataEntity =
            new DynamicDataEntity(ip, username, password, port, database, type);
        try {
            // 加载驱动程序
            Class.forName(dynamicDataEntity.getDriver());
            con = DriverManager.getConnection(dynamicDataEntity.getUrl(),
                dynamicDataEntity.getName(),
                dynamicDataEntity.getPassword());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
    
    /**
     * 
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * 
     * @param conn 链接对象
     * @param map sql拼接参数
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static List<Map<String, Object>> queryList(Connection con,
        Map<String, Object> map)
        throws Exception {
        int offset = (int) map.get("offset");
        int limit = (int) map.get("limit");
        String tableName = (String) map.get("tableName");
        // 1.创建statement类对象，用来执行SQL语句！！
        Statement statement = con.createStatement();
        // 2.要执行的SQL语句
        StringBuffer sql = new StringBuffer(
            "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database())  ");
        if (tableName != null && tableName.trim() != "") {
            sql.append(
                "and table_name like concat('%','" + tableName + "', '%')  ");
        }
        sql.append("order by create_time desc  ");
        if (String.valueOf(offset) != null && String.valueOf(limit) != null) {
            sql.append("limit " + offset + ", " + limit);
        }
        // 3.用来存放获取的结果集！！
        ResultSet rs = statement.executeQuery(sql.toString());
        // 4.表实体存储
        List<Map<String, Object>> mapList =
            new ArrayList<Map<String, Object>>();
        // 5.循环赋值获取表信息
        while (rs.next()) {
            HashMap<String, Object> tableMap = new HashMap<String, Object>();
            tableMap.put("engine", rs.getString("engine"));
            tableMap.put("createTime", rs.getString("createTime"));
            tableMap.put("tableComment", rs.getString("tableComment"));
            tableMap.put("tableName", rs.getString("tableName"));
            mapList.add(tableMap);
        }
        // 6.关闭链接
        rs.close();
        return mapList;
    }
    
    /**
     * 
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * 
     * @param conn 链接对象
     * @param map sql拼接参数
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int queryTotal(Connection con, Map<String, Object> map)
        throws Exception {
        int result = 0;
        String tableName = (String) map.get("tableName");
        // 1.创建statement类对象，用来执行SQL语句！！
        Statement statement = con.createStatement();
        // 2.要执行的SQL语句
        StringBuffer sql = new StringBuffer(
            "SELECT count(*) FROM information_schema.TABLES WHERE table_schema = (SELECT DATABASE()) ");
        if (tableName != null && tableName.trim() != "") {
            sql.append(
                " AND table_name LIKE concat('%', '" + tableName + "', '%')");
        }
        // 3.用来存放获取的结果集！！
        ResultSet rs = statement.executeQuery(sql.toString());
        while (rs.next()) {
            result = rs.getInt(1);
        }
        // 4.关闭链接
        rs.close();
        return result;
    }
    
    /**
     * 
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * 
     * @param conn 链接对象
     * @param tableName 表名称
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, String> queryTable(Connection con,
        String tableName)
        throws Exception {
        // 1.创建statement类对象，用来执行SQL语句！！
        Statement statement = con.createStatement();
        // 2.要执行的SQL语句
        String sql =
            "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = '"
                + tableName + "'";
        // 3.用来存放获取的结果集！！
        ResultSet rs = statement.executeQuery(sql);
        // 4.表实体存储
        HashMap<String, String> tableMap = new HashMap<String, String>();
        // 5.循环赋值获取表信息
        while (rs.next()) {
            tableMap.put("engine", rs.getString("engine"));
            tableMap.put("createTime", rs.getString("createTime"));
            tableMap.put("tableComment", rs.getString("tableComment"));
            tableMap.put("tableName", rs.getString("tableName"));
        }
        // 6.关闭链接
        rs.close();
        return tableMap;
    }
    
    /**
     * 
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * 
     * @param conn 链接对象
     * @param tableName 表名称
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static List<Map<String, String>> queryColumns(Connection con,
        String tableName)
        throws Exception {
        // 1.创建statement对象，用来执行SQL语句
        Statement statement = con.createStatement();
        // 2.执行的sql语句
        String sql = "SELECT  " + "column_name columnName,  "
            + "data_type dataType,  " + "column_comment columnComment,  "
            + "column_key columnKey,  " + "extra  " + "FROM  "
            + "information_schema.COLUMNS  " + "WHERE  " + "table_name = '"
            + tableName
            + "' and table_schema = (select database()) order by ordinal_position";
        // 3.用来存放获取的结果集！！
        ResultSet rs = statement.executeQuery(sql);
        // 4.表实体存储
        List<Map<String, String>> mapList =
            new ArrayList<Map<String, String>>();
        // 5.循环赋值获取表信息
        while (rs.next()) {
            HashMap<String, String> tableMap = new HashMap<String, String>();
            tableMap.put("columnName", rs.getString("columnName"));
            tableMap.put("dataType", rs.getString("dataType"));
            tableMap.put("columnComment", rs.getString("columnComment"));
            tableMap.put("columnKey", rs.getString("columnKey"));
            tableMap.put("extra", rs.getString("extra"));
            mapList.add(tableMap);
        }
        // 6.关闭链接
        rs.close();
        return mapList;
    }
    
    /**
     * 
     * 〈下载代码模板〉
     * 〈功能详细描述〉
     * 
     * @param tableNames
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] generatorCodeByMysql(String[] tableNames,
        Map<String, Object> map)
        throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 数据库链接
        Connection con = MysqlGenerator.getMysqlConn(map);
        // 循环查询数据库表资源
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table =
                MysqlGenerator.queryTable(con, tableName);
            // 查询列信息
            List<Map<String, String>> columns =
                MysqlGenerator.queryColumns(con, tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    
}
