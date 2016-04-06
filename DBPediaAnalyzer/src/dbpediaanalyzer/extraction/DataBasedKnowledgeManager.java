package dbpediaanalyzer.extraction;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.Collection;
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

    public void addSubsumption(HierarchyElement bottom, HierarchyElement top, double extensionsRatio) {
        DataBasedSubsumption dbs = knowledge.get(bottom.getUri() + top.getUri());

        if(dbs == null) {
            dbs = new DataBasedSubsumption(bottom, top, extensionsRatio);
            this.knowledge.put(bottom.getUri() + top.getUri(), dbs);
        }

        else {
            dbs.newSubmission(extensionsRatio);
        }
    }

    public Collection<DataBasedSubsumption> getDataBasedKnowledge() {
        return knowledge.values();
    }
}
