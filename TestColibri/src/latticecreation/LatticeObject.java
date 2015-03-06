package latticecreation;

import java.util.ArrayList;
import colibri.lib.Relation;

public class LatticeObject {

	private String name;
	private ArrayList<String> attributes;
	private ArrayList<String> ontologies;
	private ArrayList<String> categories;
	
	public LatticeObject(String name)
	{
		this.name = name;
		this.attributes = new ArrayList<>();
		this.ontologies = new ArrayList<String>();
		this.categories = new ArrayList<String>();
	}
	
	public void addAttribute(String att)
	{
		this.attributes.add(att);
	}
	
	public void addToRelation(Relation rel)
	{	
		for (int i=0 ; i<this.attributes.size() ; i++)
		{
			rel.add(this.name, this.attributes.get(i));
		}
	}
	
	public void deleteAttribute(String att)
	{
		this.attributes.remove(att);
	}
	
	public void setOntologies(ArrayList<String> ontologies) {
		this.ontologies = ontologies;
	}
	
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	
	public String getName() {
		return name;
	}
}
