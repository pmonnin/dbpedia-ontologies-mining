package dbpediaanalyzer.io;

import dbpediaanalyzer.statistic.ComparisonResultsStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
        this.writer.println("--- Comparison results statistics " + crs.getUriPrefix() + " " + crs.getType() + " ---");
        this.writer.println("Number of invalid results: " + crs.getInvalidNumber() + " (creating cycles)");
        this.writer.println("Number of valid results: " + crs.getValidNumber() + " with the following values:");
        this.writer.println("Averages values: ");
        for(Map.Entry<String, Double> avg : crs.getAverageValues().entrySet()) {
            this.writer.println("\t" + avg.getKey() + ": " + avg.getValue());
        }

        this.writer.println("Minimum values: ");
        for(Map.Entry<String, Double> min : crs.getMinimumValues().entrySet()) {
            this.writer.println("\t" + min.getKey() + ": " + min.getValue());
        }

        this.writer.println("Maximum values: ");
        for(Map.Entry<String, Double> max : crs.getMaximumValues().entrySet()) {
            this.writer.println("\t" + max.getKey() + ": " + max.getValue());
        }

    }

    public void close() {
        this.writer.close();
    }

}
