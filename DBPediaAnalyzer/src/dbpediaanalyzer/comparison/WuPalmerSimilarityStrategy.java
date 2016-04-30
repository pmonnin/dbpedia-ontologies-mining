package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class WuPalmerSimilarityStrategy extends EvaluationStrategy {

    @Override
    public String getName() {
        return "WuPalmerSimilarity";
    }

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        // If bottom is a parent or an ancestor of top, the proposition creates a cycle (forbidden)
        if(subsumption.getTop().hasAncestor(subsumption.getBottom())) {
            return 0.0;
        }

        // First compute LCA and distance from each hierarchy element
        HierarchyElement lca = null;
        int lcaDistanceFromBottom = 0, lcaDistanceFromTop = 0;

        if(subsumption.getBottom().hasAncestor(subsumption.getTop())) {
            lca = subsumption.getTop();
            lcaDistanceFromBottom = subsumption.getBottom().getAncestorsAndDistances().get(subsumption.getTop());
        }

        else {
            List<HierarchyElement> commonAncestors = new ArrayList<>(subsumption.getTop().getAncestorsAndDistances().keySet());
            commonAncestors.retainAll(subsumption.getBottom().getAncestorsAndDistances().keySet());

            int distanceViaLCA = -1;

            for(HierarchyElement commonAncestor : commonAncestors) {
                int currentDistance = subsumption.getTop().getAncestorsAndDistances().get(commonAncestor) +
                        subsumption.getBottom().getAncestorsAndDistances().get(commonAncestor);

                if(currentDistance >= 0 && currentDistance < distanceViaLCA || distanceViaLCA == -1) {
                    distanceViaLCA = currentDistance;
                    lca = commonAncestor;
                    lcaDistanceFromBottom = subsumption.getBottom().getAncestorsAndDistances().get(lca);
                    lcaDistanceFromTop = subsumption.getTop().getAncestorsAndDistances().get(lca);
                }
            }
        }

        // If there is no common ancestor
        if(lca == null) {
            return 0.0;
        }

        // Then compute distance from LCA and root
        int rootDistanceFromLca = 0;
        for(Map.Entry<HierarchyElement, Integer> ancestorAndDistance : lca.getAncestorsAndDistances().entrySet()) {
            if(ancestorAndDistance.getKey().getParents().isEmpty() && ancestorAndDistance.getValue() > rootDistanceFromLca) {
                rootDistanceFromLca = ancestorAndDistance.getValue();
            }
        }

        return ((double) 2 * rootDistanceFromLca) / ((double) lcaDistanceFromBottom + lcaDistanceFromTop + 2 * rootDistanceFromLca);
    }

}
