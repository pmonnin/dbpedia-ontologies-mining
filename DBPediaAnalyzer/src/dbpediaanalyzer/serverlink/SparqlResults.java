package dbpediaanalyzer.serverlink;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class SparqlResults {
    private boolean distinct;
    private boolean ordered;
    private ArrayList<ChildAndParent> bindings;

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public ArrayList<ChildAndParent> getBindings() {
        return bindings;
    }

    public void setBindings(ArrayList<ChildAndParent> bindings) {
        this.bindings = bindings;
    }
}
