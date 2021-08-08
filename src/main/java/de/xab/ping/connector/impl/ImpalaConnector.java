package de.xab.ping.connector.impl;

import java.util.Properties;

public class ImpalaConnector extends HiveConnector {
    @Override
    protected String getDriverClass(Properties properties) {
        return "com.cloudera.impala.jdbc.Driver";
    }
}
