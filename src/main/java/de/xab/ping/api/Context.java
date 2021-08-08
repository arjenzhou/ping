package de.xab.ping.api;

import java.util.Properties;

public class Context {
    private String url;
    private String username;
    private String password;
    private String sql;
    private Properties properties;

    public Context(String url, String username, String password, String sql, Properties properties) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.sql = sql;
        this.properties = properties;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
