package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DistanceViaLCAStrategy extends EvaluationStrategy {

    @Override
    public String getName() {
        return "DistanceViaLCA";
    }

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        // If bottom is a parent or an ancestor of top, the proposition creates a cycle (forbidden)
        if(subsumption.getTop().hasAncestor(subsumption.getBottom())) {
            return 0.0;
        }

        HierarchyElement lca = subsumption.getBottom().getLowestCommonAncestor(subsumption.getTop());

        int distanceViaLCA = 0;

        if(lca != null) {
            if(lca != subsumption.getBottom()) {
                distanceViaLCA += subsumption.getBottom().getDistanceFromAncestor(lca);
            }

            if(lca != subsumption.getTop()) {
                distanceViaLCA += subsumption.getTop().getDistanceFromAncestor(lca);
            }
        }

        // owl:Thing is the lca
        else {
            distanceViaLCA = subsumption.getBottom().getDistanceFromClosestTopLevelClass() + 1
                    + subsumption.getTop().getDistanceFromClosestTopLevelClass() + 1;
        }

        return 1.0 / (double) distanceViaLCA;
    }

}
