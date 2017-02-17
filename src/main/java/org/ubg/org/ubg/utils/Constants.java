package org.ubg.org.ubg.utils;

/**
 * Created by dincaus on 2/17/17.
 */
public class Constants {

    public static String EDGE_COLLECTION_NAME = "category_edges";
    public static String VERTEX_COLLECTION_NAME = "category_nodes";
    public static String GRAPH_DB_NAME = "user_graph";

    public static String getVertexCollectionName(String userId) {
        return Constants.VERTEX_COLLECTION_NAME + "_" + userId;
    }

    public static String getEdgeCollectionName(String userId) {
        return Constants.EDGE_COLLECTION_NAME + "_" + userId;
    }

}
