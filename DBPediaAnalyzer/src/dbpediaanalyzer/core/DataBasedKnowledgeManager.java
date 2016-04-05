package dbpediaanalyzer.core;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedKnowledgeManager {
    private Map<String, DataBasedSubsumption> knowledge;

    public DataBasedKnowledgeManager() {
        this.knowledge = new HashMap<>();
    }

    public void addSubsumption(HierarchyElement bottom, HierarchyElement top) {
        DataBasedSubsumption dbs = knowledge.get(bottom.getUri() + top.getUri());

        if(dbs == null) {
            dbs = new DataBasedSubsumption(bottom, top);
            this.knowledge.put(bottom.getUri() + top.getUri(), dbs);
        }

        else {
            dbs.incrementNumberOfSubmissions();
        }
    }
}
