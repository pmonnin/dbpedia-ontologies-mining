package dbpediaobjects;

import java.util.ArrayList;
import java.util.Collection;

public class DBCategory {
	private String name = "";
	private String uri = "";
	private ArrayList<DBCategory> parents = new ArrayList<DBCategory>();
	
	public String getName() {
		return name;
	}
	
	public ArrayList<DBCategory> getParents() {
		return parents;
	}

	public void addParent(DBCategory parent) {
		parents.add(parent);
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
}
