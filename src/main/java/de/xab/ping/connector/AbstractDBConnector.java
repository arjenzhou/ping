package de.xab.ping.connector;

public abstract class AbstractDBConnector implements DBConnector {
    private String type;

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
