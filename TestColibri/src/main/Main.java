package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import latticecreation.PediaLattice;

import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;

public class Main {

    // Ontology :
    // http://wiki.dbpedia.org/Ontology
    // http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
    // http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
    // http://dbpedia.org/ontology/

    public static void main(String[] args) throws ParseException, IOException
	{
		//Crawling DB categories
		DBCategoriesCrawler dbCategoriesCrawler = new DBCategoriesCrawler();
		dbCategoriesCrawler.computeParents();
        HashMap<String, DBCategory> dbcategories = dbCategoriesCrawler.getDbcategories();
		
		//Crawling DB ontologies
		DBOntologyClassesCrawler dbOntologyClasses = new DBOntologyClassesCrawler();
		dbOntologyClasses.computeParents();
		HashMap<String, DBOntologyClass> dbontologies = dbOntologyClasses.getDbontologies();
		
		// We create the lattice
		PediaLattice lattice = new PediaLattice();
		//lattice.deleteFirstIterationAttributes();
		ArrayList<PediaConcept> lc = lattice.execIterator();
                
//                for(PediaConcept c : lc){
//                    System.out.println("/***********Concept************/");
//                    for(String obj : c.getListeObjets())
//                        System.out.println("objet: "+obj);
//                    System.out.println("\n");
//                    
//                    for(String cat : c.getCategories())                       
//                        System.out.println("catégorie: "+cat);
//                    System.out.println("\n");
//                    
//                    for(PediaConcept par : c.getParents()){
//                        for(String op :  par.getListeObjets())
//                            System.out.println("objet du parent: "+op);
//                    }
//                    System.out.println("/***********Fin concept*********/\n\n");                  
//                }
		
		for(PediaConcept c : lc) {
			ArrayList<String> unionCategoriesParent = c.unionCategoriesParent();
			ArrayList<String> categories = c.getCategories();
			
			for(String cat : categories) {
				if(unionCategoriesParent.contains(cat)) {
				    
				} else {
				    
				}
			}
		}
		
	}
}
