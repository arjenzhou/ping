package de.xab.ping;

import de.xab.ping.common.ConnectorLoader;
import de.xab.ping.connector.DBConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Entry {
    private static final Logger logger = LoggerFactory.getLogger("PING");

    public static void main(String[] args) {
        if (args.length % 2 != 0) {
            logger.error("illegal input");
            return;
        }
        String type = null;
        String url = null;
        String username = null;
        String password = null;
        String sql = null;
        Properties properties = new Properties();
        for (int i = 0; i < args.length; i += 2) {
            if (!args[i].startsWith("--")) {
                logger.error("invalid argument: {}", args[i]);
                return;
            }
            String arg = args[i].substring(2);
            switch (arg) {
                case "type":
                    type = args[i + 1];
                    break;
                case "url":
                    url = args[i + 1];
                    break;
                case "username":
                    username = args[i + 1];
                    break;
                case "password":
                    password = args[i + 1];
                    break;
                case "sql":
                    sql = args[i + 1];
                    break;
                default:
                    properties.put(args[i].substring(2), args[i + 1]);
            }
        }
        DBConnector connector = ConnectorLoader.getConnectorLoader(DBConnector.class).loadExtension(type);
        connector.test(url, username, password, sql, properties);
    }
}