package latticecreation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import colibri.lib.Concept;
import colibri.lib.Edge;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;
import main.PediaConcept;

public class PediaLattice {

    private Lattice lattice;
    private ArrayList<LatticeObject> objects;

    public PediaLattice() throws ParseException, IOException {
        objects = new ArrayList<>();
        Relation rel = new TreeRelation();
        
        this.createLattice(rel);

        lattice = new HybridLattice(rel);
    }

    public void createLattice(Relation rel) throws ParseException, IOException {
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader.getJSON(URLEncoder.encode("select distinct ?chose where { ?chose a <http://www.w3.org/2002/07/owl#Thing> } LIMIT 10", "UTF-8"));

        // We parse it to get the different results
        JsonParser parser = new JsonParser(jsonResponse);
//        ArrayList<String> results = parser.getResults("chose");
        
        
        /****************TESTS*********************************/
        ArrayList<String> results = new ArrayList<>();
        results.add("http://dbpedia.org/resource/Alex_Reid_(fighter)");
        results.add("http://dbpedia.org/resource/Alex_Tait_(rugby_union)");
        results.add("http://dbpedia.org/resource/Alexander_Smit");
        results.add("http://dbpedia.org/resource/Allan_La_Fontaine");
        results.add("http://dbpedia.org/resource/Tim_Hames");
        results.add("http://dbpedia.org/resource/Alina_Cho");
        results.add("http://dbpedia.org/resource/Andrew_Heintzman");         
        /******************************************************/        
        
        
        // For each result
        String request;
        String response;

        for (int i = 0; i < results.size(); i++) {
        	
            // We create an object
            LatticeObject obj = new LatticeObject(results.get(i));

            request = parser.makeRequestAtt(results.get(i));

            // We get the response
            response = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));

            // We parse it to get the different attributes of the thing
            parser.setStringToParse(response);
            ArrayList<String> attributes = parser.getResults("att");

            for (int j = 0; j < attributes.size(); j++) {
                // We add the attributes to the object
                obj.addAttribute(attributes.get(j));
            }

            // We add the object to the lattice
            obj.addToRelation(rel);
            objects.add(obj);
        }
    }

    public void deleteFirstIterationAttributes() {
        Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);
        
        if (it.hasNext()) {
            Concept c = it.next();
            if (it.hasNext()) {
                c = it.next();
            }

            Iterator<Comparable> it2 = c.getAttributes().iterator();

            while (it2.hasNext()) {
                Comparable att = it2.next();

                for (int i = 0; i < objects.size(); i++) {
                    objects.get(i).deleteAttribute(att.toString());
                }
            }
        }

        Relation rel = new TreeRelation();

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).addToRelation(rel);
        }

        lattice = new HybridLattice(rel);
    }

    public ArrayList<PediaConcept> execIterator() throws IOException, ParseException {
    	Iterator<Edge> it = lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        ArrayList<PediaConcept> res = new ArrayList<>();

        while (it.hasNext()) {
            Edge e = it.next();

            // We take the 1st object
//            System.out.println("* * * * First * * * *");
            Concept c = e.getUpper();

            Iterator<Comparable> ite = c.getObjects().iterator();
//            System.out.println("---- OBJECTS ----");
            ArrayList<String> obj1 = new ArrayList<>();
            ArrayList<String> att1 = new ArrayList<>();
            while (ite.hasNext()) {
                String comp = (String) ite.next();
                obj1.add(comp);
//                System.out.println(comp);
            }

            ite = c.getAttributes().iterator();
//            System.out.println("---- ATTRIBUTES ----");
            while (ite.hasNext()) {
                String comp = (String) ite.next();  
                att1.add(comp);
//                System.out.println(comp);
            }
            PediaConcept pc1 = new PediaConcept(obj1, att1);

            // We take the 2nd object
//            System.out.println("* * * * Second * * * *");
            c = e.getLower();

            ite = c.getObjects().iterator();
//            System.out.println("---- OBJECTS ----");
            ArrayList<String> obj2 = new ArrayList<>();
            ArrayList<String> att2 = new ArrayList<>();
            while (ite.hasNext()) {
                String comp = (String) ite.next();
                obj2.add(comp);
//                System.out.println(comp);
            }

            ite = c.getAttributes().iterator();
//            System.out.println("---- ATTRIBUTES ----");
            while (ite.hasNext()) {
                String comp = (String) ite.next();
                att2.add(comp);
//                System.out.println(comp);
            }
            PediaConcept pc2 = new PediaConcept(obj2, att2);
            
//            System.out.println("---------------------------------\n\n");
            
            //On ne doit pas avoir plusieurs fois le même concept      
            if(!res.contains(pc1)) {
            	ArrayList<String> listeUri = new ArrayList<>();
            	if (pc1.getListeObjets().size() > 0)
            	{
	            	// We get the JSON of the shared categories
	            	String request = pc1.makeRequestCategory();
	            	URLReader urlReader = new URLReader();
	            	String jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
	                
	                //parse jsonResponse to retrieve URIs to the concept
	            	JsonParser jsp = new JsonParser(jsonResponse);
	                listeUri = jsp.getResults("categ");
            	}
                pc1.addCategoriesPediaConcept(listeUri);
                
            	// We add it to the array of results
                res.add(pc1);
            }
            
            if(!res.contains(pc2)){
            	ArrayList<String> listeUri = new ArrayList<>();
            	if(pc2.getListeObjets().size()>0)
            	{
	            	// We get the JSON of the shared categories
	            	String request = pc2.makeRequestCategory();
	            	URLReader urlReader = new URLReader();
	            	String jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
	            	
	                //parse jsonResponse to retrieve URIs to the concept
	            	JsonParser jsp = new JsonParser(jsonResponse);
	                listeUri = jsp.getResults("categ");
	            }
                pc2.addCategoriesPediaConcept(listeUri);
                
                //pc2 a comme parent pc1
                pc2.addParentPediaConcept(pc1);
                
                // We add it to the array of results
                res.add(pc2);
            }else{
                /*si pc2 est deja contenu dans la liste, on le récupère et on
                 *lui ajoute pc1 dans sa liste de parents
                 */
                //enlever les catégories de pc1 à pc2
                res.get(res.indexOf(pc2)).addParentPediaConcept(pc1);               
            }          
        }
        return res;
    }
}