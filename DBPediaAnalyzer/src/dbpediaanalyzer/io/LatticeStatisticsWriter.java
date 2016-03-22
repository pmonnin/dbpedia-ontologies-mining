package dbpediaanalyzer.io;

import dbpediaanalyzer.statistic.LatticeStatistics;

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
public class LatticeStatisticsWriter {
    private PrintWriter writer;

    public LatticeStatisticsWriter(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save lattice statistics");
        }
    }

    public void writeLatticeStatistics(LatticeStatistics statistics) {
        this.writer.println("--- Lattice statistics ---");
        this.writer.println("Depth: " + statistics.getDepth());
        this.writer.println("Number of concepts: " + statistics.getConceptsNumber());
        this.writer.println("Number of edges: " + statistics.getEdgesNumber());
        this.writer.println("Number of concepts without categories: " + statistics.getConceptsWithoutCategoriesNumber());
        this.writer.println("Number of concepts without ontology classes: " + statistics.getConceptsWithoutOntologyClassesNumber());
        this.writer.println("Number of concepts without yago classes: " + statistics.getConceptsWithoutYagoClassesNumber());
        this.writer.println("Average number of categories per concept: " + statistics.getAverageCategoriesNumberPerConcept());
        this.writer.println("Average number of ontology classes per concept: " + statistics.getAverageOntologyClassesNumberPerConcept());
        this.writer.println("Average number of yago classes per concept: " + statistics.getAverageYagoClassesNumberPerConcept());
        this.writer.println("Average number of pages per concept: " + statistics.getAveragePageNumberPerConcept());
        this.writer.println("Average number of relationships per concept: " + statistics.getAverageRelationshipsNumberPerConcept());
    }

    public void close() {
        this.writer.close();
    }
}
