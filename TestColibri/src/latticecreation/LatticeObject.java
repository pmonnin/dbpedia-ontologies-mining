package latticecreation;

import java.util.ArrayList;

import colibri.lib.Relation;

public class LatticeObject {

	private String name;
	private ArrayList<String> attributes;
	
	public LatticeObject(String name)
	{
		this.name = name;
		this.attributes = new ArrayList<String>();
	}
	
	public void addAttribute(String att)
	{
		//if (!att.split("/")[3].equals("ontology"))
		if (att.split("/")[3].equals("property"))
			this.attributes.add(att);
	}
	
	public void addToRelation(Relation rel)
	{	
		int i = 0;
		for (i=0 ; i<this.attributes.size() ; i++)
		{
			rel.add(this.name, this.attributes.get(i));
		}
	}
	
	public void deleteAttribute(String att)
	{
		this.attributes.remove(att);
	}
}
