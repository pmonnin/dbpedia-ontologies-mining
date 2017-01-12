package io.github.pmonnin.io;

import io.github.pmonnin.statistics.MiningStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writes computed mining and annotated lattice statistics into a file
 * @author Pierre Monnin
 */
public class MiningStatisticsWriter {
    private PrintWriter writer;

    public MiningStatisticsWriter() {

    }

    public void open(String fileName) {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName);
            writer = null;
        }
    }

    public void writeStatistics(MiningStatistics statistics) {
        if (writer != null) {
            writer.println("--- Mining statistics ---");
            writer.println("--- Lattice");
            writer.println("Common predicates: " + statistics.getCommonPredicates());
            writer.println("Concepts: " + statistics.getConceptsNumber());
            writer.println("Lattice depth: " + statistics.getLatticeDepth());
            writer.println("Empty proper annotations: " + statistics.getEmptyProperAnnotations());
            writer.println("Average classes / annotation: " + statistics.getAvgClassesPerAnnotation());
            writer.println("Averages classes / proper annotation: " + statistics.getAvgClassesPerProperAnnotation());
            writer.println("--- Mining results");
            writer.println("Number of new axioms: " + statistics.getNewNumber());
            writer.println("Number of direct axioms: " + statistics.getDirectNumber());
            writer.println("Number of inferred axioms: " + statistics.getInferredNumber());
            writer.println("Number of reversed axioms: " + statistics.getReversedNumber());
        }
    }

    public void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
