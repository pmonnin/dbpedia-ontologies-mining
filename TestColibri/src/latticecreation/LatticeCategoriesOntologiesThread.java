package latticecreation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.PediaConcept;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;

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
            
            try {
            	ArrayList<String> listeUri = new ArrayList<>();
            	ArrayList<String> listeOnto = new ArrayList<>();
            	
            	
            	if (concept.getListeObjets().size() > 0) {
            		// We get the JSON of the shared categories
            		String request = concept.makeRequestCategory();
            		URLReader urlReader = new URLReader();
            		String jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
            	
            		//parse jsonResponse to retrieve URIs to the concept
            		JsonParser jsp = new JsonParser(jsonResponse);
            		listeUri = jsp.getResults("categ");
            		
            		// We get the JSON of the shared ontologies
            		request = concept.makeRequestOntology();
            		jsonResponse = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));
            		
            		// parse jsonResponse to retrieve URIs to the concept's list of ontologies
            		jsp.setStringToParse(jsonResponse);
            		listeOnto = jsp.getResults("onto");
            	}
            	
            	concept.addCategoriesPediaConcept(listeUri);
            	concept.addOntologiesPediaConcept(listeOnto);
            	
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<PediaConcept> getThreadCategories() {
        return threadConcepts;
    }
}
