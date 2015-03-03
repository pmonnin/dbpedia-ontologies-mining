package dbpediaobjects;

import java.util.ArrayList;

public class DBOntologyClass {
	private String label = "";
	private String uri = "";
	private ArrayList<DBOntologyClass> parents = new ArrayList<DBOntologyClass>();
	
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
	
	public ArrayList<DBOntologyClass> getParents() {
		return parents;
	}
	
	public void setParents(ArrayList<DBOntologyClass> parents) {
		this.parents = parents;
	}
}
