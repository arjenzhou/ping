package de.xab.ping.connector.impl;

import com.google.common.base.Throwables;
import de.xab.ping.api.Context;
import de.xab.ping.common.KerberosAuth;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.function.Function;

import static de.xab.ping.common.Constants.*;

public class HiveConnector extends BuiltinConnector {
    Logger logger = LoggerFactory.getLogger("PING");

    @Override
    public boolean test(String url, String username, String password, String sql, Properties properties) {
        try {
            Context context = new Context(url, username, password, sql, properties);
            return exec(context, (funcContext -> {
                try (Connection connection = getConnection(url, null, null, properties);
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                    return printResultSet(resultSet);
                } catch (SQLException e) {
                    logger.error(Throwables.getStackTraceAsString(e));
                    return false;
                }
            }));
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
        return false;
    }

    @Override
    protected String getDriverClass(Properties properties) {
        return "org.apache.hive.jdbc.HiveDriver";
    }

    protected boolean exec(Context context, Function<Context, Boolean> function) throws Exception {
        Properties properties = context.getProperties();
        String keytabPath = (String) properties.get(KEYTAB_PATH);
        String clientPrincipal = (String) properties.get(CLIENT_PRINCIPAL);
        String krb5Path = (String) properties.get(KRB5_PATH);
        UserGroupInformation ugi = KerberosAuth.getUgi(keytabPath, clientPrincipal, krb5Path);
        if (ugi == null) {
            function.apply(context);
        } else {
            ugi.doAs((PrivilegedAction<?>) () -> function.apply(context));
        }
        return false;
    }
}
