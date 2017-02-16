package org.ubg.builder.connection;

import com.arangodb.*;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.EdgeDefinition;
import com.sun.javafx.geom.Edge;
import org.ubg.builder.connection.exception.UException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dincaus on 2/3/17.
 */
public class ConnectionBuilder {

    private ArangoDB arangoDB;

    public ConnectionBuilder(ArangoDB db) {
        this.arangoDB = db;
    }

    public ArangoDatabase createDatabase(String name) throws UException {

        if(name == null || (name != null && name.length() == 0))
            throw new UException("The name for database isn't valid.");

        if(this.arangoDB.createDatabase(name)) {
            return this.arangoDB.db(name);
        }

        throw new UException("The database isn't created.");
    }

    public ArangoDatabase getDatabase(String name)  {
        ArangoDatabase db = this.arangoDB.db(name);

        try {
            this.arangoDB.db(name).getInfo();
        } catch (ArangoDBException adbException) {
            return null;
        }

        return db;
    }

    public ArangoCollection createCollection(String dbName, String nameCollection) throws UException {

        if(dbName == null || (dbName != null && dbName.length() == 0))
            throw new UException("The database name isn't correct.");

        if(nameCollection == null || (nameCollection != null && nameCollection.length() == 0))
            throw new UException("The collection name isn't correct");

        ArangoDatabase db = this.getDatabase(dbName);

        if(db == null)
            db = this.createDatabase(dbName);

        try {
            db.createCollection(nameCollection);
        } catch (ArangoDBException adbException) {
            return this.getCollection(dbName, nameCollection);
        }

        return this.getDatabase(dbName).collection(nameCollection);
    }

    public ArangoCollection getCollection(String dbName, String nameCollection) {

        return this.getDatabase(dbName).collection(nameCollection);
    }

    public Boolean insertDocument(String dbName, String collectionName, BaseDocument baseDocument) {
        Boolean result = true;

        try {
            this.getCollection(dbName, collectionName).insertDocument(baseDocument);
        } catch (ArangoDBException adbException) {
            result = false;
        }

        return result;
    }

    public BaseDocument getDocument(String dbName, String collectionname, String key) {
        return this.getCollection(dbName, collectionname).getDocument(key, BaseDocument.class);
    }

    public ArangoGraph createGraph(String db, String graphName) {
        try {
            this.arangoDB.db(db).graph(graphName).getInfo();
        } catch (Exception ex) {
            // Doesn't exists create
            final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<EdgeDefinition>();
            final EdgeDefinition edgeDefinition = new EdgeDefinition().collection("edges").from("vertex").to("vertex");
            edgeDefinitions.add(edgeDefinition);
            this.arangoDB.db(db).createGraph(graphName, edgeDefinitions, null);
        }

        return this.arangoDB.db(db).graph(graphName);
    }

    public static class Builder {
        private ConnectionData connectionData;

        public Builder() throws UException, IOException {
            this.connectionData = new ConnectionData();
        }

        public Builder host(String host) {
            this.connectionData.setHost(host);
            return this;
        }

        public Builder port(Integer port) {
            this.connectionData.setPort(port);
            return this;
        }

        public Builder password(String password) {
            this.connectionData.setPassword(password);
            return this;
        }

        public Builder username(String username) {
            this.connectionData.setUsername(username);
            return this;
        }

        public Builder useSSL(Boolean ssl) {
            this.connectionData.setUseSSL(ssl);
            return this;
        }

        public ConnectionBuilder build() {
            return new ConnectionBuilder(new ArangoDB.Builder().user(this.connectionData.getUsername()).password(this.connectionData.getPassword()).useSsl(this.connectionData.getUseSSL()).host(this.connectionData.getHost()).build());
        }

    }

}
