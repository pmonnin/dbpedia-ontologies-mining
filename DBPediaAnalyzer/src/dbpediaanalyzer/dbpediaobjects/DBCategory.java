package dbpediaanalyzer.dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia category
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
public class DBCategory {
	private String uri;
	private ArrayList<DBCategory> parents;
	private ArrayList<DBCategory> children;
	private int depth;
	private boolean seen;
	
	public DBCategory(String uri) {
        this.uri = uri;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.depth = -1;
        this.seen = false;
    }

	public void addParent(DBCategory parent) {
		if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public int getParentsNumber() {
		return this.parents.size();
	}
	
	public ArrayList<DBCategory> getParents() {
		return this.parents;
	}
	
	@Override
    public String toString() {
        return "DBCategory [uri=" + this.uri + "]";
    }
    
    public void addChild(DBCategory child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }
    
    public ArrayList<DBCategory> getChildren() {
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
