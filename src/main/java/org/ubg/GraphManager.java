package org.ubg;

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
import java.math.*;
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

    public void processCategory(String userId, String category) throws UException {
        ArangoGraph ag = this.arangoInstance.createGraph("user_graph", userId);

        String nodes[] = category.split("/");
        Collection<VertexEntity> vNodes = new ArrayList<>();
        Collection<EdgeEntity> vEdges = new ArrayList<>();

        for(String n: nodes) {
            try {
                VertexEntity ve = ag.vertexCollection("vertex").getVertex(n, VertexEntity.class);
                vNodes.add(ve);
            } catch (ArangoDBException adbe) {
                vNodes.add(ag.vertexCollection("vertex").insertVertex(new CategoryVertex(n)));
            } catch (Exception ex) {
                throw new UException(ex);
            }

        }

        FTrending fTrening = (step, param) -> (float)(Math.exp(-param*step) + 0.005*step);

        int index = 0;
        while(index++ < vNodes.size()-1) {
            VertexEntity lNode = (VertexEntity)((ArrayList)vNodes).get(index-1);
            VertexEntity fNode = (VertexEntity)((ArrayList)vNodes).get(index);

            try {
                CategoryEdge ee = ag.edgeCollection("edges").getEdge(lNode.getKey() + "_" + fNode.getKey(), CategoryEdge.class);
                ee.incCount(1L);
                // ee.incWeight(fTrening.calculate(ee.getCount(), 2L));
                ee.incWeight(fTrening.calculate(ee.getCount(), 2L));
                ag.edgeCollection("edges").updateEdge(lNode.getKey() + "_" + fNode.getKey(), ee);
            } catch (ArangoDBException adbe) {
                if(adbe.getCause() != null && adbe.getCause().getClass().getName() == VPackParserException.class.getName()) {
                    throw new UException(adbe);
                }
                vEdges.add(ag.edgeCollection("edges").insertEdge(new CategoryEdge(lNode, fNode)));
            } catch (Exception ex) {
                throw new UException(ex);
            }

        }
    }
}
