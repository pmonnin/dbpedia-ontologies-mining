package dbpediaanalyzer.serverlink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbpediaanalyzer.io.SparqlField;

@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class ChildAndParent {
    private SparqlField child;
    private SparqlField parent;
    private SparqlField label;

    public SparqlField getChild() {
        return child;
    }

    public void setChild(SparqlField child) {
        this.child = child;
    }

    public SparqlField getParent() {
        return parent;
    }

    public void setParent(SparqlField parent) {
        this.parent = parent;
    }

    public SparqlField getLabel() {
        return label;
    }

    public void setLabel(SparqlField label) {
        this.label = label;
    }

}
