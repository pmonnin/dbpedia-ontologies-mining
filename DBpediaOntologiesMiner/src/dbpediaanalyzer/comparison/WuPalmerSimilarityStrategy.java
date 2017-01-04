package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * Evaluates an axiom suggested by the annotated lattice with the Wu-Palmer similarity
 *
 * Value = (2*dist(lca, root)) / (dist(lca, subsumption.top) + dist(lca, subsumption.bottom) + 2*dist(lca, root))
 * where root is owl:Thing
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
    protected double computeValue(DataBasedSubsumption subsumption) {
        HierarchyElement lca = subsumption.getBottom().getLowestCommonAncestor(subsumption.getTop());

        // If there is no common ancestor, owl:Thing is the LCA then dist(lca, root) = 0 as root is owl:Thing
        if(lca == null) {
            return 0.0;
        }

        int rootDistanceFromLca = lca.getDistanceFromClosestTopLevelClass() + 1;

        // If rootDistanceFromLca = 0 that means there is a cycle in LCA ancestors which are top level classes...
        if(rootDistanceFromLca == 0) {
            return INVALID_RESULT_VALUE;
        }

        int lcaDistanceFromBottom = (lca == subsumption.getBottom()) ? 0 : subsumption.getBottom().getDistanceFromAncestor(lca);
        int lcaDistanceFromTop = (lca == subsumption.getTop()) ? 0 : subsumption.getTop().getDistanceFromAncestor(lca);

        return ((double) 2 * rootDistanceFromLca) / ((double) lcaDistanceFromBottom + lcaDistanceFromTop + 2 * rootDistanceFromLca);
    }

}
