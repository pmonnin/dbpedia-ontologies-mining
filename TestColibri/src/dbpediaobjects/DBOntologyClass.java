package dbpediaobjects;

import java.util.ArrayList;

public class DBOntologyClass {
    private String label = "";
    private String uri = "";
    private ArrayList<String> parents = new ArrayList<String>();

    public DBOntologyClass(String label, String uri) {
        this.label = label;
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void addParent(String parent) {
        parents.add(parent);
    }

    public void setParents(ArrayList<String> parents) {
        this.parents = parents;
    }
    
    public boolean hasParent(String parent) {
        return parents.contains(parent);
    }

    public ArrayList<String> getParents() {
        return parents;
    }

    public int getParentsNumber() {
        return parents.size();
    }
}
