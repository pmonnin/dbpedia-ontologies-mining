package dbpediaanalyzer.io;

import dbpediaanalyzer.statistic.DataSetStatistics;
import dbpediaanalyzer.statistic.HierarchyStatistics;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataSetStatisticsWriter extends AbstractStatisticsWriter {

    public DataSetStatisticsWriter(String fileName) {
        super(fileName, "data set");
    }

    public void writeDataSetStatistics(DataSetStatistics statistics) {
        println("--- Data set statistics ---");
        println("Number of selected pages: " + statistics.getPagesNumber());
        println("Average number of categories per page: " + statistics.getAverageCategoriesNumberPerPage());
        println("Average number of ontology classes per page: " + statistics.getAverageOntologyClassesNumberPerPage());
        println("Average number of yago classes per page: " + statistics.getAverageYagoClassesNumberPerPage());

        println("- Linked categories statistics");
        HierarchyStatistics linkedCategoriesStatistics = statistics.getLinkedCategoriesStatistics();
        println("Number of directly linked categories: " + statistics.getDirectLinkedCategoriesNumber());
        println("Number of directly and indirectly linked categories: " + linkedCategoriesStatistics.getElementsNumber());
        println("Number of directly and indirectly linked orphans: " + linkedCategoriesStatistics.getOrphansNumber());
        println("Number of direct subsumptions: " + linkedCategoriesStatistics.getDirectSubsumptions());
        println("Number of inferred subsumptions: " + linkedCategoriesStatistics.getInferredSubsumptions());

        println("- Linked ontology classes statistics");
        HierarchyStatistics linkedOntologyClassesStatistics = statistics.getLinkedOntologyClassesStatistics();
        println("Number of directly linked ontology classes: " + statistics.getDirectLinkedOntologyClassNumber());
        println("Number of directly and indirectly linked ontology classes: " + linkedOntologyClassesStatistics.getElementsNumber());
        println("Number of directly and indirectly linked orphans: " + linkedOntologyClassesStatistics.getOrphansNumber());
        println("Number of direct subsumptions: " + linkedOntologyClassesStatistics.getDirectSubsumptions());
        println("Number of inferred subsumptions: " + linkedOntologyClassesStatistics.getInferredSubsumptions());

        println("- Linked yago classes statistics");
        HierarchyStatistics linkedYagoClassesStatistics = statistics.getLinkedYagoClassesStatistics();
        println("Number of directly linked yago classes: " + statistics.getDirectLinkedYagoClassesNumber());
        println("Number of directly and indirectly linked yago classes: " + linkedYagoClassesStatistics.getElementsNumber());
        println("Number of directly and indirectly linked orphans: " + linkedYagoClassesStatistics.getOrphansNumber());
        println("Number of direct subsumptions: " + linkedYagoClassesStatistics.getDirectSubsumptions());
        println("Number of inferred subsumptions: " + linkedYagoClassesStatistics.getInferredSubsumptions());
    }
}
