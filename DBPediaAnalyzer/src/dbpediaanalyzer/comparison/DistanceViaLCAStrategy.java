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
    protected double computeValue(DataBasedSubsumption subsumption) {
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
            if(subsumption.getBottom().getDistanceFromClosestTopLevelClass() == -1 ||
                    subsumption.getTop().getDistanceFromClosestTopLevelClass() == -1) {
                return INVALID_RESULT_VALUE;
            }

            distanceViaLCA = subsumption.getBottom().getDistanceFromClosestTopLevelClass() + 1
                    + subsumption.getTop().getDistanceFromClosestTopLevelClass() + 1;
        }

        return 1.0 / (double) distanceViaLCA;
    }

}
