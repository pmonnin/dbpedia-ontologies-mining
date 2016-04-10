package dbpediaanalyzer.io;

import dbpediaanalyzer.statistic.ComparisonResultsStatistics;

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
public class ComparisonResultsStatisticsWriter {
    private PrintWriter writer;

    public ComparisonResultsStatisticsWriter(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save comparison results statistics");
        }
    }

    public void writeComparisonResultsStatistics(ComparisonResultsStatistics crs) {
        this.writer.println("--- Comparison results statistics ---");
        this.writer.println("Number of comparison results: " + crs.getResultsNumber());
        this.writer.println("Number of confirmed direct relationships: " + crs.getConfirmedDirectNumber());
        this.writer.println("Average value of confirmed direct relationships: " + crs.getConfirmedDirectAverageValue());
        this.writer.println("Number of relationships proposed to be changed from inferred to direct: " + crs.getProposedInferredToDirectNumber());
        this.writer.println("Average value of relationships proposed to be changed from inferred to direct: " + crs.getProposedInferredToDirectAverageValue());
        this.writer.println("Number of new relationships proposed: " + crs.getProposedNewNumber());
        this.writer.println("Average value of new relationships proposed: " + crs.getProposedNewAverageValue());
    }

    public void close() {
        this.writer.close();
    }

}
