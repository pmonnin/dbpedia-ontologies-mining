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
public class DistanceFromLCAStrategy extends EvaluationStrategy {

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        Map<HierarchyElement, Integer> ancestorsTop = subsumption.getTop().getAncestorsAndDistances();
        Map<HierarchyElement, Integer> ancestorsBottom = subsumption.getBottom().getAncestorsAndDistances();

        List<HierarchyElement> commonAncestors = new ArrayList<>(ancestorsTop.keySet());
        commonAncestors.retainAll(ancestorsBottom.keySet());

        if(commonAncestors.isEmpty()) {
            return 0;
        }

        int distanceFromLCA = -1;

        for(HierarchyElement commonAncestor : commonAncestors) {
            int currentDistance = ancestorsTop.get(commonAncestor) + ancestorsBottom.get(commonAncestor);

            if(currentDistance >= 0 && currentDistance < distanceFromLCA || distanceFromLCA == -1) {
                distanceFromLCA = currentDistance;
            }
        }

        return 1.0 / (double) distanceFromLCA;
    }

}
