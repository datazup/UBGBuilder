package org.ubg.graph.elements;

import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.VertexEntity;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

/**
 * Created by dincaus on 2/12/17.
 */
public class CategoryEdge extends BaseDocument {

    @DocumentField(Type.FROM)
    private String from;

    @DocumentField(Type.TO)
    private String to;

    private long count;
    private float weight;

    public CategoryEdge() {
        this.weight = 0.1f;
    }

    public CategoryEdge(VertexEntity from, VertexEntity to) {
        super(from.getKey() + "_" + to.getKey());
        this.from = from.getId();
        this.to = to.getId();
        this.count = 1L;
        this.weight = 0.1f;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void incCount(long d) {
        this.count += d;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void incWeight(float t) {
        this.weight += t;
    }

}
