package io.github.pmonnin.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Class representing the SPARQL response to a query on the SPARQL endpoint
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlResponse {
    private SparqlHead head;
    private SparqlResults results;
    @JsonProperty(value="boolean")
    private boolean askResult;

    public SparqlHead getHead() {
        return this.head;
    }

    public void setHead(SparqlHead head) {
        this.head = head;
    }

    public SparqlResults getResults() {
        return this.results;
    }

    public void setResults(SparqlResults results) {
        this.results = results;
    }

    public ArrayList<SparqlRecord> getRecords() {
        if(this.results != null && this.results.getBindings() != null) {
            return this.results.getBindings();
        }

        return new ArrayList<>();
    }

    public boolean getAskResult() {
        return askResult;
    }

    public void setAskResult(boolean askResult) {
        this.askResult = askResult;
    }
}
