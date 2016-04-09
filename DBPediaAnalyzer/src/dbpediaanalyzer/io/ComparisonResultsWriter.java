package dbpediaanalyzer.io;

import dbpediaanalyzer.comparison.ComparisonResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class ComparisonResultsWriter {
    private PrintWriter writer;

    public ComparisonResultsWriter(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save comparison results");
        }
    }

    public void writeColumnsHeader() {
        this.writer.println("TYPE,BOTTOM,TOP");
    }

    public void writeComparisonResults(List<ComparisonResult> comparisonResults) {
        for(ComparisonResult result : comparisonResults) {
            this.writer.println(result.getType() + "," + result.getBottom().getUri() + "," + result.getTop().getUri());
        }
    }

    public void close() {
        this.writer.close();
    }
}
