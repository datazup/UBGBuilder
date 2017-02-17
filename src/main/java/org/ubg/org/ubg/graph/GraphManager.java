package org.ubg.org.ubg.graph;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.entity.VertexEntity;
import com.arangodb.velocypack.exception.VPackParserException;
import org.ubg.builder.connection.ConnectionBuilder;
import org.ubg.builder.connection.exception.UException;
import org.ubg.graph.elements.CategoryEdge;
import org.ubg.graph.elements.CategoryVertex;
import org.ubg.org.ubg.math.FTrending;
import org.ubg.org.ubg.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dincaus on 2/16/17.
 */
public class GraphManager {

    private ConnectionBuilder arangoInstance;

    public GraphManager() throws IOException, UException {
        this.arangoInstance = new ConnectionBuilder.Builder().build();
    }

    public GraphManager(String host) throws IOException, UException {
        this.arangoInstance = new ConnectionBuilder.Builder().host(host).build();
    }

    public GraphManager(String host, String username, String password, Boolean useSsl) throws IOException, UException {
        if(useSsl != null && useSsl != true)
            this.arangoInstance = new ConnectionBuilder.Builder().host(host).username(username).password(password).build();
        else
            this.arangoInstance = new ConnectionBuilder.Builder().host(host).useSSL(true).username(username).password(password).build();
    }

    public VertexEntity addNode(ArangoGraph ag, String userId, CategoryVertex cv) throws UException {

        if(ag == null)
            throw new UException("The graph isn't provided.");

        return ag.vertexCollection(Constants.getVertexCollectionName(userId)).insertVertex(cv);
    }

    public void processCategory(String userId, String category) throws UException {
        ArangoGraph ag = this.arangoInstance.createGraph(Constants.GRAPH_DB_NAME, userId);

        String nodes[] = category.split("/");
        Collection<VertexEntity> vNodes = new ArrayList<>();
        Collection<EdgeEntity> vEdges = new ArrayList<>();

        for(String n: nodes) {

            try {
                VertexEntity ve = ag.vertexCollection(Constants.getVertexCollectionName(userId)).getVertex(n, VertexEntity.class);
                vNodes.add(ve);
            } catch (ArangoDBException adbe) {
                vNodes.add(addNode(ag, userId, new CategoryVertex(n)));
            } catch (Exception ex) {
                throw new UException(ex);
            }

        }

        FTrending fTrening = (step, param) -> (float)(Math.exp(-param*step) + 0.005*step);

        int index = 0;
        while(index++ < vNodes.size()-1) {
            VertexEntity lNode = (VertexEntity)((ArrayList)vNodes).get(index-1);
            VertexEntity fNode = (VertexEntity)((ArrayList)vNodes).get(index);

            if(lNode == null || fNode == null)
                continue;

            try {
                String edgeKey = lNode.getKey() + "_" + fNode.getKey();

                CategoryEdge ee = ag.edgeCollection(Constants.getEdgeCollectionName(userId)).getEdge(edgeKey, CategoryEdge.class);
                ee.incCount(1L);
                ee.incWeight(fTrening.calculate(ee.getCount(), 2L));
                ag.edgeCollection(Constants.getEdgeCollectionName(userId)).updateEdge(edgeKey, ee);
            } catch (ArangoDBException adbe) {

                if(adbe.getCause() != null && adbe.getCause().getClass().getName().compareTo(VPackParserException.class.getName()) == 0)
                    throw new UException(adbe);

                vEdges.add(ag.edgeCollection(Constants.getEdgeCollectionName(userId)).insertEdge(new CategoryEdge(lNode, fNode)));
            } catch (Exception ex) {
                throw new UException(ex);
            }

        }
    }

    public void processCategory(String userId, Collection<String> categories) {
        categories.forEach(category -> {
            try {
                this.processCategory(userId, category);
            } catch (UException ue) {

            }
        });
    }

}
