package dbpediaanalyzer.analysis;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
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
public class LatticeAnalyzer {

    public void analyze(Lattice lattice, HierarchiesManager hm) {
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
                analyzeEdge(concept, child);

                // Add child for next processing steps
                if(!seen.get(child)) {
                    seen.put(child, true);
                    queue.add(child);
                }
            }
        }
    }

    private void analyzeEdge(Concept upper, Concept lower) {

    }

}
