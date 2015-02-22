package LatticeCreation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import ServerLink.AFileReader;
import ServerLink.AJsonParser;
import ServerLink.AnURLReader;
import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.LatticeImpl;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;

public class ALattice {
	
	private Lattice lattice;
	private ArrayList<AnObject> objects;
	
	public ALattice() throws ParseException, IOException
	{
		objects = new ArrayList<AnObject>();
		Relation rel = new TreeRelation();
		
		createLattice(rel);
		
		lattice = new HybridLattice(rel);
	}
	
	public void createLattice(Relation rel) throws ParseException, IOException
	{
		// The current request :
		// SELECT DISTINCT ?chose WHERE {?chose a owl:Thing}
		
		
		//AFileReader fileReader = new AFileReader("1Response.txt");
		//String jsonResponse = fileReader.readFile();
		AnURLReader urlReader = new AnURLReader();
		// String jsonResponse = urlReader.getJSON("select+distinct+%3Fchose+where+%7B%3Fchose+a+owl%3AThing%7D+LIMIT+10");
		String jsonResponse = urlReader.getJSON(URLEncoder.encode("select distinct ?chose where { ?chose a owl:Thing } LIMIT 50", "UTF-8"));
		
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
			
			// We get the response
			response = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
			// fileReader.setNameFile("1"+i+"Response.txt");
			// response = fileReader.readFile();
			
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
			objects.add(obj);
		}
	}

	public void deleteFirstIterationAttributes()
	{
		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);
		if (it.hasNext())
		{
			Concept c = it.next();
			if (it.hasNext())
				c = it.next();
			
			Iterator<Comparable> it2 = c.getAttributes().iterator();
			
			while (it2.hasNext())
			{
				Comparable att = it2.next();
	
				int i = 0;
				for (i=0; i<objects.size(); i++)
					objects.get(i).deleteAttribute(att.toString());
			}
		}
		
		Relation rel = new TreeRelation();
		
		int i = 0;
		for (i=0; i<objects.size(); i++)
			objects.get(i).addToRelation(rel);
		
		lattice = new HybridLattice(rel);
	}
	
	public void execIterator()
	{
		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);
	
		while(it.hasNext())
		{
		    Concept c = it.next();
		    // While we have 2 objects in 1 concept and at least one attribute, 
		    // we display it
	        if (c.getObjects().size() >= 10 && c.getAttributes().size()>2)
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