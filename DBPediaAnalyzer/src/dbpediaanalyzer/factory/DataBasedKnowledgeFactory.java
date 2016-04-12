package dbpediaanalyzer.factory;

import dbpediaanalyzer.databasedknowledge.DataBasedKnowledgeManager;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedKnowledgeFactory {

    public static DataBasedKnowledgeManager createDataBasedKnowledge(Lattice lattice) {
        DataBasedKnowledgeManager dbkm = new DataBasedKnowledgeManager();

        HashMap<Concept, Boolean> seen = new HashMap<>();
        for(Concept c : lattice.getConcepts()) {
            seen.put(c, false);
        }

        Queue<Concept> queue = new LinkedList<>();
        queue.add(lattice.getTop());
        seen.put(lattice.getTop(), true);

        while(!queue.isEmpty()) {
            Concept concept = queue.poll();

            for(Concept child : concept.getChildren()) {
                analyzeEdge(concept, child, dbkm);

                // Add child for next processing steps
                if(!seen.get(child)) {
                    seen.put(child, true);
                    queue.add(child);
                }
            }
        }

        return dbkm;
    }

    private static void analyzeEdge(Concept upper, Concept lower, DataBasedKnowledgeManager dbkm) {
        double extensionsRatio = (double) upper.getObjects().size() / (double) lower.getObjects().size();

        // Categories analysis
        for(Category lCategory : lower.getCategories()) {
            for(Category uCategory : upper.getCategories()) {
                dbkm.addSubsumption(lCategory, uCategory, extensionsRatio);
            }
        }

        // Ontology classes analysis
        for(OntologyClass lOntologyClass : lower.getOntologyClasses()) {
            for(OntologyClass uOntologyClass : upper.getOntologyClasses()) {
                dbkm.addSubsumption(lOntologyClass, uOntologyClass, extensionsRatio);
            }
        }

        // Yago classes analysis
        for(YagoClass lYagoClass : lower.getYagoClasses()) {
            for(YagoClass uYagoClass : upper.getYagoClasses()) {
                dbkm.addSubsumption(lYagoClass, uYagoClass, extensionsRatio);
            }
        }
    }

}
