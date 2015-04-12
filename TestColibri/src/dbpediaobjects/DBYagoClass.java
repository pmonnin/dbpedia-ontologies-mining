package dbpediaobjects;

import java.util.ArrayList;

public class DBYagoClass {
    private String uri = "";
    private ArrayList<String> parents = new ArrayList<String>();

    public DBYagoClass(String uri) {
        this.uri = uri;
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
