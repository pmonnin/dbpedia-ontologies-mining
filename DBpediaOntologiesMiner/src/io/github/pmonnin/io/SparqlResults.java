package io.github.pmonnin.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Body of a SPARQL response
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlResults {
    private boolean distinct;
    private boolean ordered;
    @JsonDeserialize(contentAs = SparqlRecord.class)
    private ArrayList<SparqlRecord> bindings;

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public ArrayList<SparqlRecord> getBindings() {
        return this.bindings;
    }

    public void setBindings(ArrayList<SparqlRecord> bindings) {
        this.bindings = bindings;
    }
}
