package de.xab.ping.connector.impl;

public class BuiltinConnector extends CustomConnector {

    @Override
    protected String parseDriver(String url, String driverFolder) {
        return "drivers/" + url.split(":")[1] + "/";
    }
}
