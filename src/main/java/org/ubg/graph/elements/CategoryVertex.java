package org.ubg.graph.elements;

import com.arangodb.entity.BaseDocument;
import java.util.Map;

/**
 * Created by dincaus on 2/12/17.
 */
public class CategoryVertex extends BaseDocument {

    private String name;

    public CategoryVertex() {

    }

    public CategoryVertex(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
