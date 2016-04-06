package dbpediaanalyzer.comparison;

import dbpediaanalyzer.extraction.DataBasedKnowledgeManager;
import dbpediaanalyzer.extraction.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class KnowledgesComparator {

    public List<ComparisonResult> compareKnowledges(DataBasedKnowledgeManager dbkm, HierarchiesManager hm) {
        List<ComparisonResult> results = new ArrayList<>();

        for(DataBasedSubsumption dbs : dbkm.getDataBasedKnowledge()) {

            // Is this an already existing direct relationship?

            // Is this an already existing inferred relationship?

            // Is this a new relationship?

        }

        return results;
    }
}
