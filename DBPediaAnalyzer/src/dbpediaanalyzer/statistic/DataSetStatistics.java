package dbpediaanalyzer.statistic;

import dbpediaanalyzer.dbpediaobject.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    private int directLinkedCategoriesNumber;
    private int directLinkedOntologyClassNumber;
    private int directLinkedYagoClassesNumber;
    private HierarchyStatistics linkedCategoriesStatistics;
    private HierarchyStatistics linkedOntologyClassesStatistics;
    private HierarchyStatistics linkedYagoClassesStatistics;

    public DataSetStatistics(Map<String, Page> dataSet) {
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

        this.directLinkedCategoriesNumber = dataSetDirectCategories.size();
        this.directLinkedOntologyClassNumber = dataSetDirectOntologyClasses.size();
        this.directLinkedYagoClassesNumber = dataSetDirectYagoClasses.size();

        // Computing direct & indirect links
        Collection<Category> dataSetDirectAndIndirectCategories = Category.getAccessibleUpwardCategories(dataSetDirectCategories.values());
        HashMap<String, Category> linkedCategories = new HashMap<>();
        for(Category category : dataSetDirectAndIndirectCategories) {
            linkedCategories.put(category.getUri(), category);
        }
        this.linkedCategoriesStatistics = new HierarchyStatistics(linkedCategories);

        Collection<OntologyClass> dataSetDirectAndIndirectOntologyClasses = OntologyClass.getAccessibleUpwardOntologyClasses(dataSetDirectOntologyClasses.values());
        HashMap<String, OntologyClass> linkedOntologyClasses = new HashMap<>();
        for(OntologyClass ontologyClass : dataSetDirectAndIndirectOntologyClasses) {
            linkedOntologyClasses.put(ontologyClass.getUri(), ontologyClass);
        }
        this.linkedOntologyClassesStatistics = new HierarchyStatistics(linkedOntologyClasses);

        Collection<YagoClass> dataSetDirectAndIndirectYagoClasses = YagoClass.getAccessibleUpwardYagoClasses(dataSetDirectYagoClasses.values());
        HashMap<String, YagoClass> linkedYagoClasses = new HashMap<>();
        for(YagoClass yagoClass : dataSetDirectAndIndirectYagoClasses) {
            linkedYagoClasses.put(yagoClass.getUri(), yagoClass);
        }
        this.linkedYagoClassesStatistics = new HierarchyStatistics(linkedYagoClasses);
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
    public int getDirectLinkedCategoriesNumber() {
        return this.directLinkedCategoriesNumber;
    }

    /**
     * Returns the number of ontology classes linked to the data set directly
     * @return the number of ontology classes linked to the data set directly
     */
    public int getDirectLinkedOntologyClassNumber() {
        return this.directLinkedOntologyClassNumber;
    }

    /**
     * Returns the number of yago classes linked to the data set directly
     * @return the number of yago classes linked to the data set directly
     */
    public int getDirectLinkedYagoClassesNumber() {
        return this.directLinkedYagoClassesNumber;
    }

    public HierarchyStatistics getLinkedCategoriesStatistics() {
        return this.linkedCategoriesStatistics;
    }

    public HierarchyStatistics getLinkedOntologyClassesStatistics() {
        return this.linkedOntologyClassesStatistics;
    }

    public HierarchyStatistics getLinkedYagoClassesStatistics() {
        return this.linkedYagoClassesStatistics;
    }
}
