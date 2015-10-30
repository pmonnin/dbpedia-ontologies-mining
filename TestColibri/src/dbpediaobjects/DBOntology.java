package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia Ontology class
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBOntology {
    private String uri = "";
    private int depth = -1;
    private boolean seen = false;
    private ArrayList<String> parents = new ArrayList<String>();
    private ArrayList<String> children = new ArrayList<String>();

    public DBOntology(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void addParent(String parent) {
        this.parents.add(parent);
    }
    
    public boolean hasParent(String parent) {
        return this.parents.contains(parent);
    }

    public ArrayList<String> getParents() {
        return this.parents;
    }

    public int getParentsNumber() {
        return this.parents.size();
    }
    
    public void setDepth(int depth) {
    	if(depth >= -1) {
    		this.depth = depth;
    	}
    }
    
    public int getDepth() {
    	return this.depth;
    }
    
    public ArrayList<String> getChildren() {
    	return this.children;
    }
    
    public void addChildren(String child) {
    	this.children.add(child);
    }
    
    public boolean getSeen() {
    	return this.seen;
    }
    
    public void setSeen(boolean seen) {
    	this.seen = seen;
    }
}
