package dbpediaanalyzer.main;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import dbpediaanalyzer.dbpediaobject.DBCategoriesManager;
import dbpediaanalyzer.dbpediaobject.DBOntologiesManager;
import dbpediaanalyzer.dbpediaobject.DBYagoClassesManager;

/**
 * Main class for the application
 *  - Crawl DBPedia categories
 *  - Crawl DBPedia ontology classes
 *  - Crawl DBPedia yago classes
 *  - Comparison between our lattice and the existing hierarchy
 * 
 * 
 * @author Soline Blanc
 * @author Damien Flament
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
public class Main {
	/**
	 * Main function of the algorithm
	 * @param args not used
	 * @throws IOException thrown when the server is unavailable
	 */
    @Deprecated
    public static void main(String[] args) throws IOException {
        Date startDate = new Date();
        
        System.out.println("=== MAIN ALGORITHM OF COMPARISON ===");
        
        // Crawling DB categories
        System.out.println("=== CATEGORIES CRAWLING AND PARSING ===");
        DBCategoriesCrawler dbCategoriesCrawler = new DBCategoriesCrawler();
        dbCategoriesCrawler.computeCategoriesHierarchy();
        DBCategoriesManager dbcategories = dbCategoriesCrawler.getDBCategoriesManager();

        // Crawling DB ontologies
        System.out.println("=== ONTOLOGIES CRAWLING AND PARSING ===");
        DBOntologiesCrawler dbOntologyClasses = new DBOntologiesCrawler();
        dbOntologyClasses.computeOntologiesHierarchy();
        DBOntologiesManager dbontologies = dbOntologyClasses.getDBOntologiesManager();
        
        // Crawling Yago classes
        System.out.println("=== YAGO CLASSES CRAWLING AND PARSING ===");
        DBYagoClassesCrawler dbYagoClasses = new DBYagoClassesCrawler();
        dbYagoClasses.computeYagoCLassesHierarchy();
        DBYagoClassesManager dbyagoclasses = dbYagoClasses.getDBYagoClassesManager();

        // Lattice creation
        System.out.println("=== LATTICE CREATION ===");

        // For each concept
        /*found = 0;
        proposed = 0;
        for (PediaConcept c : lc) {
        	// We get the union of parents ontology classes
            ArrayList<String> latticeOntologyParents = c.unionOntologiesParent();
            ArrayList<String> latticeOntologies = c.getOntologyClasses();

            // For each present concept ontology class
            for (String ontoChild : latticeOntologies) {
                for (String ontoParent : latticeOntologyParents) {
                    if (dbontologies.get(ontoChild) != null && dbontologies.get(ontoChild).hasParent(ontoParent)) {
                        found++;
                        System.out.println("La relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) a bien été trouvée dans les classes d'ontologie.");
                    } else {
                        proposed++;
                        System.out.println("Il manque la relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) dans les classes d'ontologie.");
                    }
                }
            }
        }
        //stats of comparison
        comparisonStats.setNbOntologiesProposed(proposed);
        comparisonStats.setNbOntologiesFound(found);
        
        // Break between the two comparison (so that text can be read)
        pause();
        
        found = 0;
        proposed = 0;
        for(PediaConcept c : lc) {
        	// We get the union of parents categories
            ArrayList<String> latticeCategoryParents = c.unionCategoriesParent();
            ArrayList<String> latticeCategories = c.getCategories();

            // For each concept category
            for (String cateChild : latticeCategories) {
                for (String cateParent : latticeCategoryParents) {
                    if (dbcategories.get(cateChild) != null && dbcategories.get(cateChild).hasParent(cateParent)) {
                        found++;
                        System.out.println("La relation " + cateChild + " (enfant) -> " + cateParent + " (parent) a bien été trouvée dans les catégories.");
                    } else {
                        proposed++;
                        System.out.println("Il manque la relation " + cateChild + " (enfant) -> " + cateParent + " (parent) dans les catégories.");
                    }
                }
            }
        }
        // Stats of comparison
        comparisonStats.setNbCategoriesFound(found);
        comparisonStats.setNbCategoriesProposed(proposed);
        
        // Break between the two comparison (so that text can be read)
        pause();
        
        found = 0;
        proposed = 0;
        for (PediaConcept c : lc) {
        	// We get the union of parents categorie
            ArrayList<String> latticeYagoClassesParents = c.unionYagoClassesParent();
            ArrayList<String> latticeYagoClasses = c.getYagoClasses();

         // For each concept category
            for (String yagoChild : latticeYagoClasses) {
                for (String yagoParent : latticeYagoClassesParents) {
                    if (dbyagoclasses.get(yagoChild) != null && dbyagoclasses.get(yagoChild).hasParent(yagoParent)) {
                        found++;
                        System.out.println("La relation " + yagoChild + " (enfant) -> " + yagoParent + " (parent) a bien été trouvée dans les classes yago.");
                    } else {
                        proposed++;
                        System.out.println("Il manque la relation " + yagoChild + " (enfant) -> " + yagoParent + " (parent) dans les classes yago.");
                    }
                }
            }
        }
        // Stats of comparison
        comparisonStats.setNbYagoFound(found);
        comparisonStats.setNbYagoProposed(proposed);
        */
        
        System.out.println("=== PROPOSAL STATISTICS ===");
        System.out.println("Nombre d'ontologies trouvées: ");
        System.out.println("Nombre d'ontologies proposées: ");
        System.out.println("Nombre de catégories trouvées: ");
        System.out.println("Nombre de catégories proposées:  ");
        System.out.println("Nombre de classes yago trouvées: ");
        System.out.println("Nombre de classes yago proposées: ");
        System.out.println("===========================");
        System.out.println("\nPROGRAM EXECUTION TIME: " + (new Date().getTime() - startDate.getTime()) / 1000 + " SECONDS");
    }
    
    /**
     * Function used to break between two comparison and give time to 
     * read the text output
     */
    public static void pause() {
        System.out.flush();
        System.out.print("Press Enter to continue");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();
    }
}
