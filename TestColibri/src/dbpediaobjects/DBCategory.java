package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia category
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBCategory {
	private String uri;
	private ArrayList<String> parents;
	private ArrayList<String> children;
	private int depth;
	private boolean seen;
	
	public DBCategory(String uri) {
        this.uri = uri;
        this.parents = new ArrayList<String>();
        this.children = new ArrayList<String>();
        this.depth = -1;
        this.seen = false;
    }

	public void addParent(String parent) {
		this.parents.add(parent);
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int getParentsNumber() {
		return this.parents.size();
	}
	
	public ArrayList<String> getParents() {
		return this.parents;
	}
	
	@Override
    public String toString() {
        return "DBCategory [uri=" + this.uri + ", parents=" + this.parents + "]";
    }

    public boolean hasParent(String parent) {
        return this.parents.contains(parent);
    }
    
    public void addChild(String uri) {
    	this.children.add(uri);
    }
    
    public ArrayList<String> getChildren() {
    	return this.children;
    }

    public void setDepth(int depth) {
    	if(depth >= -1) {
            this.depth = depth;
        }
    }
    
    public int getDepth() {
    	return this.depth;
    }
    
    public void setSeen(boolean seen) {
		this.seen = seen;
    }
    
    public boolean getSeen() {
    	return this.seen;
    }
}
