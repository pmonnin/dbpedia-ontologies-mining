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
public class LatticeStatisticsWriter extends AbstractStatisticsWriter {
    public LatticeStatisticsWriter(String fileName) {
        super(fileName, "lattice");
    }

    public void writeLatticeStatistics(LatticeStatistics statistics) {
        println("--- Lattice statistics ---");
        println("Depth: " + statistics.getDepth());
        println("Number of concepts: " + statistics.getConceptsNumber());
        println("Number of edges: " + statistics.getEdgesNumber());
        println("Number of attributes in Top concept: " + statistics.getTopAttributesNumber());
        println("Number of attributes in Bottom concept: " + statistics.getBottomAttributesNumber());
        println("Number of concepts without categories: " + statistics.getConceptsWithoutCategoriesNumber());
        println("Number of concepts without ontology classes: " + statistics.getConceptsWithoutOntologyClassesNumber());
        println("Number of concepts without yago classes: " + statistics.getConceptsWithoutYagoClassesNumber());
        println("Number of gap concepts in categories: " + statistics.getGapConceptsInCategories());
        println("Number of gap concepts in ontology classes: " + statistics.getGapConceptsInOntologyClasses());
        println("Number of gap concepts in yago classes: " + statistics.getGapConceptsInYagoClasses());
        println("Average number of categories per concept: " + statistics.getAverageCategoriesNumberPerConcept());
        println("Average number of ontology classes per concept: " + statistics.getAverageOntologyClassesNumberPerConcept());
        println("Average number of yago classes per concept: " + statistics.getAverageYagoClassesNumberPerConcept());
        println("Average number of pages per concept: " + statistics.getAveragePageNumberPerConcept());
        println("Average number of relationships per concept: " + statistics.getAverageRelationshipsNumberPerConcept());
    }
}
