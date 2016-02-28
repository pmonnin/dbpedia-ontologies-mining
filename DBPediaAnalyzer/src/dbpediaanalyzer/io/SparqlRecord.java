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
    private HashMap<String, SparqlValue> fields;

    public HashMap<String, SparqlValue> getFields() {
        return this.fields;
    }

    public void setFields(HashMap<String, SparqlValue> fields) {
        this.fields = fields;
    }
}
