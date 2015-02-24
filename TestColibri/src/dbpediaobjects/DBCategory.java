package dbpediaobjects;

import java.util.ArrayList;

public class DBCategory {
    private String name = "";
    private String uri = "";
    private ArrayList<DBCategory> parents;

    public DBCategory(String name, String uri) {
        super();
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ArrayList<DBCategory> getParents() {
        return parents;
    }

    public void setParents(ArrayList<DBCategory> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "DBCategory [name=" + name + ", uri=" + uri + ", parents=" + parents + "]";
    }
}