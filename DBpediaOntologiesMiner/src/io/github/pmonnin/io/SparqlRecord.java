package io.github.pmonnin.io;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Row of the body of a SPARQL answer
 *
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlRecord {
    private HashMap<String, SparqlValue> fields = new HashMap<>();

    public HashMap<String, SparqlValue> getFields() {
        return this.fields;
    }

    @JsonAnySetter
    public void set(String name, SparqlValue value) {
        this.fields.put(name, value);
    }
}
