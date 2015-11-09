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
    private String uri;
    private int depth;
    private boolean seen;
    private ArrayList<DBYagoClass> parents;
    private ArrayList<DBYagoClass> children;
    
    public DBYagoClass(String uri) {
        this.uri = uri;
        this.depth = -1;
        this.seen = false;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public String getUri() {
        return this.uri;
    }

    public void addParent(DBYagoClass parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public ArrayList<DBYagoClass> getParents() {
        return this.parents;
    }

    public int getParentsNumber() {
        return this.parents.size();
    }

    @Override
    public String toString() {
        return "DBYagoClass [uri=" + this.uri + "]";
    }
    
    public void addChild(DBYagoClass child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }
    
    public ArrayList<DBYagoClass> getChildren() {
    	return this.children;
    }
    
    public void setDepth(int depth) {
    	if(depth >= -1)
    		this.depth = depth;
    }
    
    public int getDepth() {
    	return this.depth;
    }
    
    public boolean getSeen() {
    	return this.seen;
    }
    
    public void setSeen(boolean seen) {
    	this.seen = seen;
    }
}
