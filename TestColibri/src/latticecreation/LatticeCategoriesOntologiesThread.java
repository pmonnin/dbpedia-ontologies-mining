package latticecreation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.PediaConcept;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

public class LatticeCategoriesOntologiesThread extends Thread {
    private ArrayList<PediaConcept> threadConcepts;

    public LatticeCategoriesOntologiesThread(ArrayList<PediaConcept> threadConcepts) {
        super();
        this.threadConcepts = threadConcepts;
    }

    @Override
    public void run() {
        for(int i=0; i < threadConcepts.size(); i++) {
            PediaConcept concept = threadConcepts.get(i);
            String jsonParents = new String();
            
            ArrayList<String> listeCateg = new ArrayList<>();
			ArrayList<String> listeOnto = new ArrayList<>();
			
			
			if (concept.getListeObjets().size() > 0) {
				
				/** Categories **/
//            		// We get the JSON of the shared categories
//            		String request = concept.makeRequestCategory();
//            		URLReader urlReader = new URLReader();
//            		String jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
//            	
//            		//parse jsonResponse to retrieve URIs to the concept
//            		JsonParser jsp = new JsonParser(jsonResponse);
//            		listeUri = jsp.getResults("categ");
				
				// We make the intersection of the different categories
				// of the concept's object
				// TODO : ligne suivante avec la Hashmap
				//listeCateg = concept.intersectCategories();
				
				/** Ontologies **/
//            		// We get the JSON of the shared ontologies
//            		request = concept.makeRequestOntology();
//            		jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
//            		
//            		// parse jsonResponse to retrieve URIs to the concept's list of ontologies
//            		jsp.setStringToParse(jsonResponse);
//            		listeOnto = jsp.getResults("onto");
				
				// We make the intersection of the different ontologies
				// of the concept's objects
				// TODO : ligne suivante avec la Hashmap
				// listeOnto = concept.intersectOntologies();
				
			}
			
			concept.addCategoriesPediaConcept(listeCateg);
			concept.addOntologiesPediaConcept(listeOnto);
        }
    }

    public ArrayList<PediaConcept> getThreadConcepts() {
        return threadConcepts;
    }
}
