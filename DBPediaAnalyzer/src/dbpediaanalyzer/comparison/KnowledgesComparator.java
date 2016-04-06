package dbpediaanalyzer.comparison;

import dbpediaanalyzer.extraction.DataBasedKnowledgeManager;
import dbpediaanalyzer.extraction.DataBasedSubsumption;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class KnowledgesComparator {

    public List<ComparisonResult> compareKnowledges(DataBasedKnowledgeManager dbkm) {
        List<ComparisonResult> results = new ArrayList<>();

        for(DataBasedSubsumption dbs : dbkm.getDataBasedKnowledge()) {

            // Is this an already existing direct relationship?
            if(dbs.getBottom().getParents().contains(dbs.getTop())) {
                results.add(new ConfirmedDirectRelationship(dbs.getBottom(), dbs.getTop()));
            }

            // Is this an already existing inferred relationship?
            else if(dbs.getBottom().getAncestors().contains(dbs.getTop())) {
                results.add(new ProposedInferredToDirectRelationship(dbs.getBottom(), dbs.getTop()));
            }

            // Is this a new relationship?
            else {
                results.add(new ProposedNewRelationship(dbs.getBottom(), dbs.getTop()));
            }

        }

        return results;
    }
}
