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
    private int depth = 0;

    public DBYagoClass() {
    }
    
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

    @Override
    public String toString() {
        return "DBYagoClass [uri=" + uri + "]";
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
    	if(depth >= 0)
    		this.depth = depth;
    }
    
    public int getDepth() {
    	return this.depth;
    }
}
