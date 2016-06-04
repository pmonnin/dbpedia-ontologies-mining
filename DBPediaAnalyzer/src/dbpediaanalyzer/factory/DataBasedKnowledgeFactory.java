package dbpediaanalyzer.factory;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedKnowledgeFactory {

    public static List<DataBasedSubsumption> createDataBasedKnowledge(Lattice lattice) {
        HashMap<String, DataBasedSubsumption> dataBasedKnowledge = new HashMap<>();

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
                analyzeEdge(concept, child, dataBasedKnowledge);

                // Add child for next processing steps
                if(!seen.get(child)) {
                    seen.put(child, true);
                    queue.add(child);
                }
            }
        }

        return new ArrayList<>(dataBasedKnowledge.values());
    }

    private static void analyzeEdge(Concept upper, Concept lower, HashMap<String, DataBasedSubsumption> dataBasedKnowledge) {
        double extensionsRatio = (double) lower.getObjects().size() / (double) upper.getObjects().size();
        double intensionsRatio = (double) upper.getAttributes().size() / (double) lower.getAttributes().size();

        // Categories analysis
        for(Category lCategory : lower.getCategories()) {
            for(Category uCategory : upper.getCategories()) {
                addSubsumption(lCategory, uCategory, extensionsRatio, intensionsRatio, dataBasedKnowledge);
            }
        }

        // Ontology classes analysis
        for(OntologyClass lOntologyClass : lower.getOntologyClasses()) {
            for(OntologyClass uOntologyClass : upper.getOntologyClasses()) {
                addSubsumption(lOntologyClass, uOntologyClass, extensionsRatio, intensionsRatio, dataBasedKnowledge);
            }
        }

        // Yago classes analysis
        for(YagoClass lYagoClass : lower.getYagoClasses()) {
            for(YagoClass uYagoClass : upper.getYagoClasses()) {
                addSubsumption(lYagoClass, uYagoClass, extensionsRatio, intensionsRatio, dataBasedKnowledge);
            }
        }
    }

    private static void addSubsumption(HierarchyElement bottom, HierarchyElement top, double extensionsRatio,
                                       double intensionsRatio, HashMap<String, DataBasedSubsumption> dataBasedKnowledge) {

        DataBasedSubsumption dbs = dataBasedKnowledge.get(bottom.getUri() + top.getUri());

        if(dbs == null) {
            dbs = new DataBasedSubsumption(bottom, top, extensionsRatio, intensionsRatio);
            dataBasedKnowledge.put(bottom.getUri() + top.getUri(), dbs);
        }

        else {
            dbs.newSubmission(extensionsRatio, intensionsRatio);
        }
    }

}
