package dbpediaobjects;

import java.util.ArrayList;

public class DBCategory {
	private String name = "";
	private String uri = "";
	private ArrayList<DBCategory> parents;
	
	public String getName() {
		return name;
	}
	
	public ArrayList<DBCategory> getParents() {
		return parents;
	}

	public void setParents(ArrayList<DBCategory> parents) {
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
}
