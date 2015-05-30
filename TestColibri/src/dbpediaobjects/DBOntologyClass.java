package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia Ontology class
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBOntologyClass {
    private String label = "";
    private String uri = "";
    private int depth = -1;
    private boolean seen = false;
    private ArrayList<String> parents = new ArrayList<String>();
    private ArrayList<String> children = new ArrayList<String>();

    public DBOntologyClass(String label, String uri) {
        this.label = label;
        this.uri = uri;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setParents(ArrayList<String> parents) {
        this.parents = parents;
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
    
    public int getChildrenNumber() {
    	return this.children.size();
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
