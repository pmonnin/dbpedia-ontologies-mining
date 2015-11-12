package statistics;

import dbpediaobjects.DBPage;

import java.util.HashMap;

/**
 * Class computing statistics over the DB Data set selected
 *
 * @author Pierre Monnin
 */
public class DBDataSetStatistics {
    private int pagesNumber;
    private double averageCategoriesNumber;
    private double averageOntologiesNumber;
    private double averageYagoClassesNumber;

    public DBDataSetStatistics() {
        this.pagesNumber = -1;
        this.averageCategoriesNumber = -1;
        this.averageOntologiesNumber = -1;
        this.averageYagoClassesNumber = -1;
    }

    public void computeStatistics(HashMap<String, DBPage> pages) {
        this.pagesNumber = pages.size();

        for(String uri : pages.keySet()) {
            this.averageCategoriesNumber += pages.get(uri).getCategories().size();
            this.averageOntologiesNumber += pages.get(uri).getOntologies().size();
            this.averageYagoClassesNumber += pages.get(uri).getYagoClasses().size();
        }

        this.averageCategoriesNumber /= (double) pages.size();
        this.averageOntologiesNumber /= (double) pages.size();
        this.averageYagoClassesNumber /= (double) pages.size();
    }

    public void displayStatistics() {
        System.out.println("=== LATTICE STATISTICS ===");
        System.out.println("Selected pages number: " + this.pagesNumber);
        System.out.println("Average categories number per page: " + this.averageCategoriesNumber);
        System.out.println("Average ontologies number per page: " + this.averageOntologiesNumber);
        System.out.println("Average yago classes number per page: " + this.averageYagoClassesNumber);
        System.out.println("==========================");
    }
}
