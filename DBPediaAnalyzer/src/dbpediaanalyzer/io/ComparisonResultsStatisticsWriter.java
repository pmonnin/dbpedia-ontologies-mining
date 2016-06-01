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
public class ComparisonResultsStatisticsWriter extends AbstractStatisticsWriter {

    public ComparisonResultsStatisticsWriter(String fileName) {
        super(fileName, "comparison results");
    }

    public void writeComparisonResultsStatistics(ComparisonResultsStatistics crs) {
        println("--- Comparison results statistics " + crs.getUriPrefix() + " " + crs.getType() + " ---");
        println("Number of invalid results: " + crs.getInvalidNumber() + " (creating cycles or cycles " +
                "between top level classes for Wu-Palmer similarity)");
        println("Number of valid results: " + crs.getValidNumber() + " with the following values:");
        println("Averages values: ");
        for(Map.Entry<String, Double> avg : crs.getAverageValues().entrySet()) {
            println("\t" + avg.getKey() + ": " + avg.getValue());
        }

        println("Minimum values: ");
        for(Map.Entry<String, Double> min : crs.getMinimumValues().entrySet()) {
            println("\t" + min.getKey() + ": " + min.getValue());
        }

        println("Maximum values: ");
        for(Map.Entry<String, Double> max : crs.getMaximumValues().entrySet()) {
            println("\t" + max.getKey() + ": " + max.getValue());
        }

    }

}
