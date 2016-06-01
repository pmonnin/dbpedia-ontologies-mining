package dbpediaanalyzer.io;

import dbpediaanalyzer.statistic.DataSetStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        println("Number of directly linked categories: " + statistics.getDirectLinkedCategoriesNumber());
        println("Number of directly linked ontology classes: " + statistics.getDirectLinkedOntologyClassNumber());
        println("Number of directly linked yago classes: " + statistics.getDirectLinkedYagoClassesNumber());
        println("Number of directly and indirectly linked categories: " + statistics.getIndirectLinkedCategoriesNumber());
        println("Number of directly and indirectly linked ontology classes: " + statistics.getIndirectLinkedOntologyClassesNumber());
        println("Number of directly and indirectly linked yago classes: " + statistics.getIndirectLinkedYagoClassesNumber());
    }
}
