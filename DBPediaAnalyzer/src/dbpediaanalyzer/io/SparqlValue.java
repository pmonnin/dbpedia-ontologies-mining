package dbpediaanalyzer.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * TODO JAVADOC
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
