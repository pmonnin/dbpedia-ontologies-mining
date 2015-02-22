package LatticeCreation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import ServerLink.AFileReader;
import ServerLink.AJsonParser;
import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.LatticeImpl;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;

public class ALattice {
	
	private Lattice lattice;
	
	public ALattice() throws ParseException, IOException
	{
		Relation rel = new TreeRelation();
		
		createLattice(rel);
		
		lattice = new HybridLattice(rel);
	}
	
	public void createLattice(Relation rel) throws ParseException, IOException
	{
		// TODO : get the json from our server
		// The current request :
		// SELECT DISTINCT ?chose WHERE {?chose a owl:Thing}
		AFileReader fileReader = new AFileReader("1Response.txt");
		String jsonResponse = fileReader.readFile();
		
		// We parse it to get the different results
		AJsonParser parser = new AJsonParser(jsonResponse);
		ArrayList<String> results = parser.getResults("chose");
		
		// For each result
		String request = "";
		String response = "";
		int i = 0;
		for (i=0 ; i<results.size() ; i++)
		{	
			// We create an object
			AnObject obj = new AnObject(results.get(i));
			
			request = parser.makeRequestAtt(results.get(i));

			// TODO : run the request on the server and get the response
			
			// We get the response
			fileReader.setNameFile("1"+i+"Response.txt");
			response = fileReader.readFile();
			
			// We parse it to get the different attributes of the thing
			parser.setStringToParse(response);
			ArrayList<String> attributes = parser.getResults("att");
			
			int j = 0;
			for (j=0 ; j<attributes.size() ; j++)
			{
				// We add the attributes to the object
				obj.addAttribute(attributes.get(j));
			}
			

			// We add the object to the lattice
			obj.addToRelation(rel);
		}
	}

	public void execIterator()
	{
		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);
	
		while(it.hasNext())
		{
		    Concept c = it.next();
		    // While we have 2 objects in 1 concept and at least one attribute, 
		    // we display it
	        if (c.getObjects().size() >= 2 && c.getAttributes().size()>0)
	        {
	        	//System.out.println(c.getObjects());
	        	Iterator<Comparable> ite = c.getObjects().iterator();
	        	System.out.println("OBJECTS______");
	        	while (ite.hasNext())
	        	{
	        		Comparable comp = ite.next();
		        	System.out.println(comp);
	        	}
	        	
	        	ite = c.getAttributes().iterator();
	        	System.out.println("ATTRIBUTES______");
	        	while (ite.hasNext())
	        	{
	        		Comparable comp = ite.next();
		        	System.out.println(comp);
	        	}
	        	System.out.println("---------------------------------");
	        	System.out.println("");
	        }
	        // When we have less than 2 objects, we stop it
	        else if (c.getObjects().size() < 2)
	        {
	                break;
	        }
		}
	}
	
}