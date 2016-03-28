package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.LatticeStatistics;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeAnalysis {

    /**
     * TODO JAVADOC
     * @param args should contain
     *             - name of JSON file corresponding to the lattice to analyze
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage:\n java LatticeAnalysis lattice");
            System.out.println("\t lattice\n\t\t JSON file corresponding to the lattice to analyze");
        }

        else {
            System.out.println("=== LATTICE ANALYSIS ===");

            System.out.println("Data loading...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();
            System.out.println("\t Loading lattice from file...");
            Lattice lattice = (new LatticeReader()).readLattice(args[0], hm);

            LatticeStatistics statistics = new LatticeStatistics(lattice);
            System.out.println("Depth: " + statistics.getDepth());
            System.out.println("Number of concepts: " + statistics.getConceptsNumber());
            System.out.println("Number of edges: " + statistics.getEdgesNumber());
            System.out.println("Number of concepts without categories: " + statistics.getConceptsWithoutCategoriesNumber());
            System.out.println("Number of concepts without ontology classes: " + statistics.getConceptsWithoutOntologyClassesNumber());
            System.out.println("Number of concepts without yago classes: " + statistics.getConceptsWithoutYagoClassesNumber());
            System.out.println("Average number of categories per concept: " + statistics.getAverageCategoriesNumberPerConcept());
            System.out.println("Average number of ontology classes per concept: " + statistics.getAverageOntologyClassesNumberPerConcept());
            System.out.println("Average number of yago classes per concept: " + statistics.getAverageYagoClassesNumberPerConcept());
            System.out.println("Average number of pages per concept: " + statistics.getAveragePageNumberPerConcept());
            System.out.println("Average number of relationships per concept: " + statistics.getAverageRelationshipsNumberPerConcept());
        }
    }
}
