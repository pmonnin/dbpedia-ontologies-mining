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
	private String name = "";
	private String uri = "";
	private ArrayList<String> parents = new ArrayList<String>();
	private ArrayList<String> children = new ArrayList<String>();
	private int depth = 0;
	
	public DBCategory(String name, String uri) {
        super();
        this.name = name;
        this.uri = uri;
        this.parents = new ArrayList<String>();
        this.children = new ArrayList<String>();
        this.depth = 0;
    }
	
	public String getName() {
		return name;
	}

	public void addParent(String parent) {
		parents.add(parent);
	}
	
	public void setParents(ArrayList<String> parents) {
		this.parents = parents;
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
	
	public int getParentsNumber() {
		return parents.size();
	}
	
	public ArrayList<String> getParents() {
		return this.parents;
	}
	
	@Override
    public String toString() {
        return "DBCategory [name=" + name + ", uri=" + uri + ", parents=" + parents + "]";
    }

    public boolean hasParent(String parent) {
        return parents.contains(parent);
    }
    
    public void addChild(String uri) {
    	this.children.add(uri);
    }
    
    public int getChildrenNumber() {
    	return this.children.size();
    }
    
    public ArrayList<String> getChildren() {
    	return this.children;
    }
    
    public void setDepth(int depth) {
    	if(depth >= 0)
    		this.depth = depth;
    }
    
    public int getDepth() {
    	return this.depth;
    }
}
