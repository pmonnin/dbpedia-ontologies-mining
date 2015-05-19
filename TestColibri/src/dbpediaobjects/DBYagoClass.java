package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia Yago class
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBYagoClass {
    private String uri = "";
    private ArrayList<String> parents = new ArrayList<String>();
    private ArrayList<String> children = new ArrayList<String>();
    private int depth = -1;

    public DBYagoClass() {
    }
    
    public DBYagoClass(String uri) {
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

    @Override
    public String toString() {
        return "DBYagoClass [uri=" + this.uri + "]";
    }
    
    public void addChild(String child) {
    	this.children.add(child);
    }
    
    public ArrayList<String> getChildren() {
    	return this.children;
    }
    
    public int getChildrenNumber() {
    	return this.children.size();
    }
    
    public void setDepth(int depth) {
    	if(depth >= -1)
    		this.depth = depth;
    }
    
    public int getDepth() {
    	return this.depth;
    }
}
