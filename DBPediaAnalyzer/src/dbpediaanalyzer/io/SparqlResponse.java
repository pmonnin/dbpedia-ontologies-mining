package dbpediaanalyzer.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Object built from the JSON of the SPARQL response
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlResponse {
    private SparqlHead head;
    private SparqlResults results;

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
}
