package main;

import java.util.ArrayList;
import java.util.Objects;


public class PediaConcept {

    private ArrayList<String> listeObjets, listeAttributs, categories, ontologies;
    private ArrayList<PediaConcept> parents;

    public PediaConcept(ArrayList<String> objets, ArrayList<String> attributs) {
        listeObjets = objets;
        listeAttributs = attributs;
        parents = new ArrayList<>();
        categories = new ArrayList<>();
        ontologies = new ArrayList<>();
    }

    public ArrayList<String> getListeObjets() {
        return listeObjets;
    }

    public void setListeObjets(ArrayList<String> listeObjets) {
        this.listeObjets = listeObjets;
    }

    public ArrayList<String> getListeAttributs() {
        return listeAttributs;
    }

    public void setListeAttributs(ArrayList<String> listeAttributs) {
        this.listeAttributs = listeAttributs;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
    
    public ArrayList<String> getOntologies()
    {
    	return ontologies;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }   

    public void setOntologies(ArrayList<String> ontologies)
    {
    	this.ontologies = ontologies;
    }
    
    public ArrayList<PediaConcept> getParents() {
        return parents;
    }

    public void addParentPediaConcept(PediaConcept parent) {
//        System.out.println("concept1: "+this.getCategories());
//        System.out.println("parent: "+parent.getCategories());
        removeDoublonsCategories(parent.getCategories());
        removeDoublonsOntologies(parent.getOntologies());
//        System.out.println("concept2: "+this.getCategories()+"\n\n");
        parents.add(parent);
    }

    public void addCategoriesPediaConcept(ArrayList<String> cats) {
        categories = cats;
    }
    
    public void addOntologiesPediaConcept(ArrayList<String> ontos)
    {
    	ontologies = ontos;
    }

    public void removeDoublonsCategories(ArrayList<String> catP){
        ArrayList<String> categoriesASupprimees = new ArrayList<>();

        for(String cat : this.getCategories()){
            if(catP.contains(cat))
                categoriesASupprimees.add(cat);
        }
        
        for(String cat : categoriesASupprimees)
            this.getCategories().remove(cat);        
    }
    
    public void removeDoublonsOntologies(ArrayList<String> ontP){
        ArrayList<String> ontologiesASupprimer = new ArrayList<>();

        for(String ont : this.getOntologies()){
            if(ontP.contains(ont))
                ontologiesASupprimer.add(ont);
        }
        
        for(String ont : ontologiesASupprimer)
            this.getOntologies().remove(ont);        
    }
    
    public String makeRequestOntology() {
        // Begin of the request
        String request = "SELECT DISTINCT ?onto";
        request += " WHERE {";

        // For each object, we link it to the categ
        for (int i = 0; i < listeObjets.size(); i++) {
            request += "<" + listeObjets.get(i) + "> a ?onto";
            // TODO

            if (i < listeObjets.size() - 1) {
                request += ".";
            }
        }

        // End of the request
        request += "}";

        return request;
    }
     
    public String makeRequestCategory() {
        // Begin of the request
    		
        String request = "SELECT DISTINCT ?categ";
        request += " WHERE {";

        // For each object, we link it to the categ
        for (int i = 0; i < listeObjets.size(); i++) {
            request += "<" + listeObjets.get(i) + "> <http://purl.org/dc/terms/subject> ?categ";

            if (i < listeObjets.size() - 1) {
                request += ".";
            }
        }

        // End of the request
        request += "}";

        return request;
    }
    
    public boolean isEquivalentTo(PediaConcept pc) {
    	ArrayList<String> objets = pc.getListeObjets();
    	ArrayList<String> atts = pc.getListeAttributs();
        
    	if (objets.size() != this.listeObjets.size())
    		return false;
    	if (atts.size() != this.listeAttributs.size())
    		return false;
        
        
    	for (int i=0 ; i<listeObjets.size() ; i++)
    	{
    		if (!objets.contains(listeObjets.get(i)))
    			return false;
    	}
    	
    	for (int i=0 ; i<listeAttributs.size() ; i++)
    	{
    		if (!atts.contains(listeAttributs.get(i)))
    			return false;
    	}
    	
    	return true;
    }  
}
