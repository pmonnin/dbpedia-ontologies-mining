package dbpediaanalyzer.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlRecord {
    private HashMap<String, SparqlField> fields;

    public HashMap<String, SparqlField> getFields() {
        return this.fields;
    }

    public void setFields(HashMap<String, SparqlField> fields) {
        this.fields = fields;
    }
}
