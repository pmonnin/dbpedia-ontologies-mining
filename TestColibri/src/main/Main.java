package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import latticecreation.PediaLattice;

import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;
import dbpediaobjects.DBYagoClass;

/**
 * Main class for the application
 *  - Crawl DBPedia categories
 *  - Crawl DBPedia ontology classes
 *  - Crawl DBPedia yago classes
 *  - 
 * 
 * 
 * @author Soline Blanc
 * @author Damien Flament
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class Main {

	/**
	 * Main function of the algorithm
	 * @param args not used
	 * @throws ParseException thrown when there is a JSON parse exception from the server output parsing
	 * @throws IOException thrown when the server is unavailable
	 */
    public static void main(String[] args) throws ParseException, IOException {
        Date startDate = new Date();
        
        // Crawling DB categories
        System.out.println("DEBUT PARSAGE CATEGORIES");
        DBCategoriesCrawler dbCategoriesCrawler = new DBCategoriesCrawler();
        dbCategoriesCrawler.computeParents();
        HashMap<String, DBCategory> dbcategories = dbCategoriesCrawler.getDbcategories();

        // Crawling DB ontologies
        System.out.println("DEBUT PARSAGE ONTOLOGIES");
        DBOntologyClassesCrawler dbOntologyClasses = new DBOntologyClassesCrawler();
        dbOntologyClasses.computeParents();
        HashMap<String, DBOntologyClass> dbontologies = dbOntologyClasses.getDbontologies();
        
        // Crawling Yago classes
        System.out.println("DEBUT PARSAGE ONTOLOGIES");
        DBYagoClassesCrawler dbYagoClasses = new DBYagoClassesCrawler();
        dbYagoClasses.computeParents();
        HashMap<String, DBYagoClass> dbyagoclasses = dbYagoClasses.getDbYagoClasses();

        // We create the lattice
        System.out.println("DEBUT CREATION LATTICE");
        PediaLattice lattice = new PediaLattice();
        // lattice.deleteFirstIterationAttributes();
        ArrayList<PediaConcept> lc = lattice.execIterator();

        // For each concept
        for (PediaConcept c : lc) {
        	// We get the union of parents ontology classes
            ArrayList<String> latticeOntologyParents = c.unionOntologiesParent();
            ArrayList<String> latticeOntologies = c.getOntologies();

            // For each present concept ontology class
            for (String ontoChild : latticeOntologies) {
                for (String ontoParent : latticeOntologyParents) {
                    if (dbontologies.get(ontoChild).hasParent(ontoParent)) {
                        System.out.println("La relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) a bien été trouvée dans les classes d'ontologie.");
                    } else {
                        System.out.println("Il manque la relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) dans les classes d'ontologie.");
                    }
                }
            }
        }
        
        // Break between the two comparison (so that text can be read)
        pause();
        
        for(PediaConcept c : lc) {
        	// We get the union of parents categories
            ArrayList<String> latticeCategoryParents = c.unionCategoriesParent();
            ArrayList<String> latticeCategories = c.getCategories();

            // For each concept category
            for (String cateChild : latticeCategories) {
                for (String cateParent : latticeCategoryParents) {
                    if (dbcategories.get(cateChild).hasParent(cateParent)) {
                        System.out.println("La relation " + cateChild + " (enfant) -> " + cateParent + " (parent) a bien été trouvée dans les catégories.");
                    } else {
                        System.out.println("Il manque la relation " + cateChild + " (enfant) -> " + cateParent + " (parent) dans les catégories.");
                    }
                }
            }
        }
        
        // Break between the two comparison (so that text can be read)
        pause();
        
        for (PediaConcept c : lc) {
            /* Récupérer l'union des parents des classes yago */
            ArrayList<String> latticeYagoClassesParents = c.unionYagoClassesParent();
            ArrayList<String> latticeYagoClasses = c.getYagoClasses();

            /* Pour chaque classe yago du concept */
            for (String yagoChild : latticeYagoClasses) {
                for (String yagoParent : latticeYagoClassesParents) {
                    if (dbyagoclasses.get(yagoChild).hasParent(yagoParent)) {
                        System.out.println("La relation " + yagoChild + " (enfant) -> " + yagoParent + " (parent) a bien été trouvée dans les classes yago.");
                    } else {
                        System.out.println("Il manque la relation " + yagoChild + " (enfant) -> " + yagoParent + " (parent) dans les classes yago.");
                    }
                }
            }
        }
        
        System.out.println("FIN DU PROGRAMME EN : " + (new Date().getTime() - startDate.getTime()) / 1000 + " SECONDES.");
    }
    
    /**
     * Function used to break between two comparison and give time to 
     * read the text output
     */
    public static final void pause() {
        System.out.flush();
        System.out.print("Press Enter to continue");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();
    }
}
