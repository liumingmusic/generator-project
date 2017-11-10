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
    
    // FIXME 后续需要读取配置文件并且数据库链接使用数据库连接池
    // 数据库ip地址
    private String ip = "127.0.0.1";
    
    // 用户名
    private String name = "root";
    
    // 用户名
    private String password = "root";
    
    // 数据库端口号
    private String port = "3306";
    
    // 数据库名字
    private String database = "generator_fast";
    
    // 数据库类型 mysql oracle pg
    private String type = "mysql";
    
    public DynamicDataEntity() {
        super();
    }
    
    public DynamicDataEntity(String ip, String name, String password,
        String port, String database, String type) {
        super();
        if (ip.trim().length() > 0) {
            this.ip = ip;
        }
        if (name.trim().length() > 0) {
            this.name = name;
        }
        if (password.trim().length() > 0) {
            this.password = password;
        }
        if (port.trim().length() > 0) {
            this.port = port;
        }
        if (database.trim().length() > 0) {
            this.database = database;
        }
        if (type.trim().length() > 0) {
            this.type = type;
        }
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
        if (getType().equals("mysql")) {
            driver = "com.mysql.jdbc.Driver";
        }
        return driver;
    }
    
    public String getUrl() {
        String url = "";
        if (getType().equals("mysql")) {
            url = "jdbc:mysql://" + getIp() + ":" + getPort() + "/"
                + getDatabase()
                + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        }
        return url;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        if (ip.trim() != "") {
            this.ip = ip;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name.trim() != "") {
            this.name = name;
        }
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        if (password.trim() != "") {
            this.password = password;
        }
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        if (port.trim() != "") {
            this.port = port;
        }
    }
    
    public String getDatabase() {
        return database;
    }
    
    public void setDatabase(String database) {
        if (database.trim() != "") {
            this.database = database;
        }
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        if (type.trim() != "") {
            this.type = type;
        }
    }
    
}
