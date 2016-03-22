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
public class DataSetStatisticsWriter {
    private PrintWriter writer;

    public DataSetStatisticsWriter(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save data set statistics");
        }
    }

    public void writeDataSetStatistics(DataSetStatistics statistics) {
        this.writer.println("--- Data set statistics ---");
        this.writer.println("Number of selected pages: " + statistics.getPagesNumber());
        this.writer.println("Average number of categories per page: " + statistics.getAverageCategoriesNumberPerPage());
        this.writer.println("Average number of ontology classes per page: " + statistics.getAverageOntologyClassesNumberPerPage());
        this.writer.println("Average number of yago classes per page: " + statistics.getAverageYagoClassesNumberPerPage());
        this.writer.println("Number of directly linked categories: " + statistics.getDirectLinkedCategoriesNumber());
        this.writer.println("Number of directly linked ontology classes: " + statistics.getDirectLinkedOntologyClassNumber());
        this.writer.println("Number of directly linked yago classes: " + statistics.getDirectLinkedYagoClassesNumber());
        this.writer.println("Number of directly and indirectly linked categories: " + statistics.getIndirectLinkedCategoriesNumber());
        this.writer.println("Number of directly and indirectly linked ontology classes: " + statistics.getIndirectLinkedOntologyClassesNumber());
        this.writer.println("Number of directly and indirectly linked yago classes: " + statistics.getIndirectLinkedYagoClassesNumber());
    }

    public void close() {
        this.writer.close();
    }
}
