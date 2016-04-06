package dbpediaanalyzer.comparison;

import dbpediaanalyzer.extraction.DataBasedKnowledgeManager;
import dbpediaanalyzer.extraction.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class KnowledgesComparator {
    public void compareKnowledges(DataBasedKnowledgeManager dbkm, HierarchiesManager hm) {

        for(DataBasedSubsumption dbs : dbkm.getDataBasedKnowledge()) {

            // Is this an already existing direct relationship?

            // Is this an already existing inferred relationship?

            // Is this a new relationship?

        }

    }
}
