package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia Ontology class
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
public class DBOntology {
    private String uri;
    private int depth;
    private boolean seen;
    private ArrayList<DBOntology> parents;
    private ArrayList<DBOntology> children;

    public DBOntology(String uri) {
        this.uri = uri;
        this.depth = -1;
        this.seen = false;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public String getUri() {
        return this.uri;
    }

    public void addParent(DBOntology parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public ArrayList<DBOntology> getParents() {
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
    
    public ArrayList<DBOntology> getChildren() {
    	return this.children;
    }
    
    public void addChild(DBOntology child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }
    
    public boolean getSeen() {
    	return this.seen;
    }
    
    public void setSeen(boolean seen) {
    	this.seen = seen;
    }
}
