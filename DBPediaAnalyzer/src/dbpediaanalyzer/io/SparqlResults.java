package dbpediaanalyzer.io;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * TODO JAVADOC
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlResults {
    private boolean distinct;
    private boolean ordered;
    private ArrayList<HashMap<String, SparqlValue>> bindings;

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

    public ArrayList<HashMap<String, SparqlValue>> getBindings() {
        return this.bindings;
    }

    public void setBindings(ArrayList<HashMap<String, SparqlValue>> bindings) {
        this.bindings = bindings;
    }
}
