package de.xab.ping.connector;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class JDBCConnector extends AbstractDBConnector {
    Logger logger = LoggerFactory.getLogger("PING");

    @Override
    public boolean test(String url, String username, String password, String sql, Properties properties) {
        try (Connection connection = getConnection(url, username, password, properties);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return printResultSet(resultSet);
        } catch (SQLException e) {
            logger.error(Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    protected Connection getConnection(String url, String username, String password, Properties properties) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    protected boolean printResultSet(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        StringBuilder column = new StringBuilder();
        for (int i = 0; i < columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i + 1);
            column.append(" ").append(columnName);
        }

        logger.info(column.toString());
        logger.info("---");
        while (resultSet.next()) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < columnCount; i++) {
                row.append(" ").append(resultSet.getString(i + 1));
            }
            logger.info(row.toString());
        }
        return true;
    }
}
