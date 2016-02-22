package dbpediaanalyzer.serverlink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class SparqlResponse {
    private SparqlHeader head;
    private SparqlResults results;

    public SparqlHeader getHead() {
        return head;
    }

    public void setHead(SparqlHeader head) {
        this.head = head;
    }

    public SparqlResults getResults() {
        return results;
    }

    public void setResults(SparqlResults results) {
        this.results = results;
    }
}
