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
    private int depth = 0;
    private ArrayList<String> parents = new ArrayList<String>();
    private ArrayList<String> children = new ArrayList<String>();

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
    
    public void setDepth(int depth) {
    	if(depth >= 0) {
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
    
}
