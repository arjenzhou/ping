package de.xab.ping.connector.test;

import de.xab.ping.connector.DBConnector;
import de.xab.ping.common.ConnectorLoader;
import org.junit.jupiter.api.Test;

public class ConnectorTest {
    @Test
    public void testPing() {
        DBConnector connector = ConnectorLoader.getConnectorLoader(DBConnector.class).loadExtension("h2");
        connector.test("jdbc:h2:mem:ping", "", "", "SELECT 1", null);
    }
}
