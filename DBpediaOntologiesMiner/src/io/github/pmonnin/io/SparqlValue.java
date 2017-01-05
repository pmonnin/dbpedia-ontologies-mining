package io.github.pmonnin.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * SPARQL value (composed of the actual value and its type) of a field of a record of the SPARQL response
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlValue {
    private String type;
    private String value;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
