package dbpediaanalyzer.io;

import dbpediaanalyzer.statistics.HierarchyStatistics;

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
public class HierarchiesStatisticsWriter {
    private PrintWriter writer;

    public HierarchiesStatisticsWriter(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save hierarchies statistics");
        }
    }

    public void writeHierarchyStatistics(HierarchyStatistics hs, String hierarchyName) {
        this.writer.println("--- " + hierarchyName + " statistics ---");
        this.writer.println("Elements number: " + hs.getElementsNumber());
        this.writer.println("Orphans number: " + hs.getOrphansNumber());
        this.writer.println("Depth: " + hs.getDepth());
        this.writer.println("Direct subsumptions number: " + hs.getDirectSubsumptions());
        this.writer.println("Inferred subsumptions number: " + hs.getInferredSubsumptions());
    }

    public void close() {
        writer.close();
    }

}
