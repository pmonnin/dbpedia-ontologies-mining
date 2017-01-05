package io.github.pmonnin.io;

import io.github.pmonnin.semanticwebobjects.OntologyClass;
import io.github.pmonnin.statistics.OntologyStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Writes computed ontology statistics into a file
 * @author Pierre Monnin
 */
public class OntologyStatisticsWriter {
    private PrintWriter writer;

    public OntologyStatisticsWriter() {

    }

    public void open(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName);
            this.writer = null;
        }
    }

    public void writeStatistics(OntologyStatistics statistics) {
        if (this.writer != null) {
            this.writer.println("--- Ontology statistics ---");
            this.writer.println("Elements: " + statistics.getElementsNumber());
            this.writer.println("Top level classes: " + statistics.getTopLevelClassesNumber());
            this.writer.println("Cycles: " + statistics.getCyclesNumber());
            this.writer.println("Depth: " + statistics.getDepth());
            this.writer.println("Direct subsumptions: " + statistics.getDirectSubsumptionsNumber());
            this.writer.println("Inferred subsumptions: " + statistics.getInferredSubsumptionsNumber());

            this.writer.println("\n\n\nCycles:");

            for (List<OntologyClass> cycle : statistics.getCycles()) {
                for(OntologyClass ontologyClass : cycle) {
                    this.writer.print(ontologyClass + "/");
                }

                this.writer.println();
            }
        }
    }

    public void close() {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }
}
