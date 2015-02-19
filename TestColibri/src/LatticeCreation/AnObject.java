package LatticeCreation;

import java.util.ArrayList;

import colibri.lib.Relation;

public class AnObject {

	private String name;
	private ArrayList<String> attributes;
	
	public AnObject(String name)
	{
		this.name = name;
		this.attributes = new ArrayList<String>();
	}
	
	public void addAttribute(String att)
	{
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
}
