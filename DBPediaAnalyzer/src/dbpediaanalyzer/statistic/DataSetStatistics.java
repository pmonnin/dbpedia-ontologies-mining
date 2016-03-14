package dbpediaanalyzer.statistic;

import dbpediaanalyzer.dbpediaobject.*;

import java.util.HashMap;

/**
 * Class computing statistics over the selected DBPedia data set
 *
 * @author Pierre Monnin
 *
 */
public class DataSetStatistics {
    private int pagesNumber;
    private double averageCategoriesNumberPerPage;
    private double averageOntologyClassesNumberPerPage;
    private double averageYagoClassesNumberPerPage;
    private int dataSetDirectLinkCategoriesNumber;
    private int dataSetDirectLinkOntologyClassNumber;
    private int dataSetDirectLinkYagoClassesNumber;
    private int dataSetIndirectLinkCategoriesNumber;
    private int dataSetIndirectLinkOntologyClassesNumber;
    private int dataSetIndirectLinkYagoClassesNumber;

    public DataSetStatistics(HashMap<String, Page> dataSet, HierarchiesManager hierarchiesManager) {
        this.pagesNumber = dataSet.size();

        this.averageCategoriesNumberPerPage = 0;
        this.averageOntologyClassesNumberPerPage = 0;
        this.averageYagoClassesNumberPerPage = 0;

        // Computing averages
        for(String uri : dataSet.keySet()) {
            this.averageCategoriesNumberPerPage += dataSet.get(uri).getCategories().size();
            this.averageOntologyClassesNumberPerPage += dataSet.get(uri).getOntologyClasses().size();
            this.averageYagoClassesNumberPerPage += dataSet.get(uri).getYagoClasses().size();
        }

        this.averageCategoriesNumberPerPage /= (double) this.pagesNumber;
        this.averageOntologyClassesNumberPerPage /= (double) this.pagesNumber;
        this.averageYagoClassesNumberPerPage /= (double) this.pagesNumber;

        // Computing direct links
        HashMap<String, Category> dataSetDirectCategories = new HashMap<>();
        HashMap<String, OntologyClass> dataSetDirectOntologyClasses = new HashMap<>();
        HashMap<String, YagoClass> dataSetDirectYagoClasses = new HashMap<>();

        for(String uri : dataSet.keySet()) {
            /* If a category, ontology class or yago class is put multiple times inside the hashmaps, this will still be
            counted as 1 hashmap entry */
            for(Category c : dataSet.get(uri).getCategories()) {
                dataSetDirectCategories.put(c.getUri(), c);
            }

            for(OntologyClass o : dataSet.get(uri).getOntologyClasses()) {
                dataSetDirectOntologyClasses.put(o.getUri(), o);
            }

            for(YagoClass y : dataSet.get(uri).getYagoClasses()) {
                dataSetDirectYagoClasses.put(y.getUri(), y);
            }
        }

        this.dataSetDirectLinkCategoriesNumber = dataSetDirectCategories.size();
        this.dataSetDirectLinkOntologyClassNumber = dataSetDirectOntologyClasses.size();
        this.dataSetDirectLinkYagoClassesNumber = dataSetDirectYagoClasses.size();

        // Computing indirect links

    }

    public int getPagesNumber() {
        return this.pagesNumber;
    }

    public double getAverageCategoriesNumberPerPage() {
        return this.averageCategoriesNumberPerPage;
    }

    public double getAverageOntologyClassesNumberPerPage() {
        return this.averageOntologyClassesNumberPerPage;
    }

    public double getAverageYagoClassesNumberPerPage() {
        return this.averageYagoClassesNumberPerPage;
    }

    /**
     * Returns the number of categories linked to the data set directly
     * @return the number of categories linked to the data set directly
     */
    public int getDataSetDirectLinkCategoriesNumber() {
        return this.dataSetDirectLinkCategoriesNumber;
    }

    /**
     * Returns the number of ontology classes linked to the data set directly
     * @return the number of ontology classes linked to the data set directly
     */
    public int getDataSetDirectLinkOntologyClassNumber() {
        return this.dataSetDirectLinkOntologyClassNumber;
    }

    /**
     * Returns the number of yago classes linked to the data set directly
     * @return the number of yago classes linked to the data set directly
     */
    public int getDataSetDirectLinkYagoClassesNumber() {
        return this.dataSetDirectLinkYagoClassesNumber;
    }

    /**
     * Returns the number of categories linked to the data set directly and indirectly
     * @return the number of categories linked to the data set directly and indirectly
     */
    public int getDataSetIndirectLinkCategoriesNumber() {
        return this.dataSetIndirectLinkCategoriesNumber;
    }

    /**
     * Returns the number of ontology classes linked to the data set directly and indirectly
     * @return the number of ontology classes linked to the data set directly and indirectly
     */
    public int getDataSetIndirectLinkOntologyClassesNumber() {
        return this.dataSetIndirectLinkOntologyClassesNumber;
    }

    /**
     * Returns the number of yago classes linked to the data set directly and indirectly
     * @return the number of yago classes linked to the data set directly and indirectly
     */
    public int getDataSetIndirectLinkYagoClassesNumber() {
        return this.dataSetIndirectLinkYagoClassesNumber;
    }
}
