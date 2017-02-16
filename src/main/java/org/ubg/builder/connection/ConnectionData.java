package org.ubg.builder.connection;

import org.ubg.builder.connection.exception.UException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dincaus on 2/3/17.
 */
public class ConnectionData {

    private String host = "127.0.0.1";
    private Integer port = 8529;
    private String username;
    private String password;
    private Boolean useSSL = false;

    public ConnectionData(String host, Integer port, String username, String password, Boolean ssl) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.useSSL = ssl;
    }

    public ConnectionData() throws UException, IOException {
        InputStream input = ConnectionData.class.getClassLoader().getResourceAsStream("arangodb.properties");
        Properties prop = new Properties();

        if (input == null) {
            throw new UException("The property file doesn't exists.");
        }

        prop.load(input);

        this.host = prop.getProperty("arangodb.host", "127.0.0.1");
        this.port = Integer.valueOf(prop.getProperty("arangodb.port", "8529"));
        this.username = prop.getProperty("arangodb.username", "root");
        this.password = prop.getProperty("arangodb.password", "");
        this.useSSL = Boolean.valueOf(prop.getProperty("arangodb.useSSL", "false"));
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {

        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }
}
