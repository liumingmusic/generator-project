/**
 * 文件名：DynamicDataEntity.java
 * 版权： www.liumingmusic.win
 * 描述：〈描述〉
 * 修改时间：Nov 7, 2017
 * 修改内容：〈修改内容〉
 */
package com.c503.datasources;

/**
 * 
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * 
 * @author liumingming
 * @version [版本号, Nov 7, 2017]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DynamicDataEntity {
    
    // 数据库ip地址
    private String ip;
    
    // 用户名
    private String name;
    
    // 用户名
    private String password;
    
    // 数据库端口号
    private String port;
    
    // 数据库名字
    private String database;
    
    // 数据库类型 mysql oracle pg
    private String type;
    
    public DynamicDataEntity() {
        super();
    }
    
    public DynamicDataEntity(String ip, String port, String database,
        String type) {
        super();
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.type = type;
    }
    
    public String getDriver() {
        String driver = "";
        if (type.equals("mysql")) {
            driver = "com.mysql.jdbc.Driver";
        }
        return driver;
    }
    
    public String getUrl() {
        String url = "";
        if (type.equals("mysql")) {
            url = "jdbc:mysql://" + ip + ":3306/" + database
                + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        }
        return url;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public void setDatabase(String database) {
        this.database = database;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
}
