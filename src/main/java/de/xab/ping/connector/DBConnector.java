package de.xab.ping.connector;

import java.util.Properties;

public interface DBConnector {
    boolean test(String url, String username, String password, String sql, Properties properties);

    String getType();

    void setType(String type);
}
