package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DistanceFromLCAStrategy extends EvaluationStrategy {

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        List<HierarchyElement> commonAncestors = new ArrayList<>(subsumption.getBottom().getAncestors());
        commonAncestors.retainAll(subsumption.getTop().getAncestors());

        if(commonAncestors.isEmpty()) {
            return 0;
        }

        return -1.0;
    }

}
