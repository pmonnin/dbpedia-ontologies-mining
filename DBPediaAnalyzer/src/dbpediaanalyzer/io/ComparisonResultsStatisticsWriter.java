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

        this.writer.println("\n- Confirmed direct relationships");
        this.writer.println("Number: " + crs.getConfirmedDirectNumber());
        this.writer.println("Average value: " + crs.getConfirmedDirectAverageValue());
        this.writer.println("Minimum value: " + crs.getConfirmedDirectMinimumValue());
        this.writer.println("Maximum value: " + crs.getConfirmedDirectMaximumValue());

        this.writer.println("\n- Proposed to be changed from inferred to direct relationships");
        this.writer.println("Number: " + crs.getProposedInferredToDirectNumber());
        this.writer.println("Average value: " + crs.getProposedInferredToDirectAverageValue());
        this.writer.println("Minimum value: " + crs.getProposedInferredToDirectMinimumValue());
        this.writer.println("Maximum value: " + crs.getProposedInferredToDirectMaximumValue());

        this.writer.println("\n- Proposed new relationships");
        this.writer.println("Number: " + crs.getProposedNewNumber());
        this.writer.println("Average value: " + crs.getProposedNewAverageValue());
        this.writer.println("Minimum value: " + crs.getProposedNewMinimumValue());
        this.writer.println("Maximum value: " + crs.getProposedNewMaximumValue());
    }

    public void close() {
        this.writer.close();
    }

}
