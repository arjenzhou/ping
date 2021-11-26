package de.xab.ping.connector.impl;

import java.util.Properties;

public class MySQLConnector extends BuiltinConnector {

    @Override
    protected String getDriverClass(Properties properties) {
        return "com.mysql.cj.jdbc.Driver";
    }
}
