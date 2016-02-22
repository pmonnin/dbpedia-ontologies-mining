package dbpediaanalyzer.serverlink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class ChildAndParent {
    private SparqlValue child;
    private SparqlValue parent;
    private SparqlValue label;

    public SparqlValue getChild() {
        return child;
    }

    public void setChild(SparqlValue child) {
        this.child = child;
    }

    public SparqlValue getParent() {
        return parent;
    }

    public void setParent(SparqlValue parent) {
        this.parent = parent;
    }

    public SparqlValue getLabel() {
        return label;
    }

    public void setLabel(SparqlValue label) {
        this.label = label;
    }

}
