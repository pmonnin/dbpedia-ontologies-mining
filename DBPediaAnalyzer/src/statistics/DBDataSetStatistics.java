package statistics;

import dbpediaobjects.DBCategoriesManager;
import dbpediaobjects.DBOntologiesManager;
import dbpediaobjects.DBPage;
import dbpediaobjects.DBYagoClassesManager;

import java.util.HashMap;

/**
 * Class computing statistics over the DB Data set selected
 *
 * @author Pierre Monnin
 */
@Deprecated
public class DBDataSetStatistics {
    private int pagesNumber;
    private int dataSetCategoriesNumber;
    private int dataSetOntologiesNumber;
    private int dataSetYagoClassesNumber;
    private double averageCategoriesNumber;
    private double averageOntologiesNumber;
    private double averageYagoClassesNumber;

    public DBDataSetStatistics() {
        this.pagesNumber = -1;
        this.dataSetCategoriesNumber = -1;
        this.dataSetOntologiesNumber = -1;
        this.dataSetYagoClassesNumber = -1;
        this.averageCategoriesNumber = -1;
        this.averageOntologiesNumber = -1;
        this.averageYagoClassesNumber = -1;
    }

    public void computeStatistics(HashMap<String, DBPage> pages, DBCategoriesManager dbcategories, DBOntologiesManager dbontologies,
                                  DBYagoClassesManager dbyagoClasses) {
        this.pagesNumber = pages.size();

        this.dataSetCategoriesNumber = dbcategories.getDataSetCategoriesNumber(pages);
        this.dataSetOntologiesNumber = dbontologies.getDataSetOntologiesNumber(pages);
        this.dataSetYagoClassesNumber = dbyagoClasses.getDataSetYagoClassesNumber(pages);

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
        System.out.println("=== DATA SET STATISTICS ===");
        System.out.println("Selected pages number: " + this.pagesNumber);
        System.out.println("Categories number (direct & inferred): " + this.dataSetCategoriesNumber);
        System.out.println("Ontologies number (direct & inferred): " + this.dataSetOntologiesNumber);
        System.out.println("Yago classes number (direct & inferred): " + this.dataSetYagoClassesNumber);
        System.out.println("Average categories number per page: " + this.averageCategoriesNumber);
        System.out.println("Average ontologies number per page: " + this.averageOntologiesNumber);
        System.out.println("Average yago classes number per page: " + this.averageYagoClassesNumber);
        System.out.println("==========================");
    }
}
