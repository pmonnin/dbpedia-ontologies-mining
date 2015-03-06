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
		//if (!att.split("/")[3].equals("ontology"))
		//if (att.split("/")[3].equals("property"))
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
	
	public ArrayList<String> getCategories()
	{
		return this.categories;
	}
	
	public void addOntology(String newOntologies)
	{
		this.ontologies.add(newOntologies);
	}
	
	public ArrayList<String> getOntologies()
	{
		return this.ontologies;
	}
}
