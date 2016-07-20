package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;
import dbpediaanalyzer.io.SparqlValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds one ontology among the three considered
 *
 * @author Pierre Monnin
 *
 */
class HierarchyFactory<T extends HierarchyElement> {
    private static final String[] QUERY_FILTER_SUFFIXES = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "[^a-z]"};

    private HierarchyElementFactory<T> hierarchyElementFactory;
    private String hierarchyElementsName;

    public HierarchyFactory(HierarchyElementFactory<T> hierarchyElementFactory, String hierarchyElementsName) {
        this.hierarchyElementFactory = hierarchyElementFactory;
        this.hierarchyElementsName = hierarchyElementsName;
    }

    public Map<String, T> createHierarchy(String creationQueryPrefixes, String creationQueryWhereClause,
                                          String creationQueryUriPrefix, String parentsQueryPrefixes,
                                          String parentRelationship, String parentsQueryUriPrefix) {

        Map<String, T> hierarchy = queryHierarchyElements(creationQueryPrefixes, creationQueryWhereClause, creationQueryUriPrefix);
        queryElementsParents(parentsQueryPrefixes, parentRelationship, parentsQueryUriPrefix, hierarchy);

        return hierarchy;
    }

    private Map<String, T> queryHierarchyElements(String queryPrefixes, String whereClause, String uriPrefix) {
        Map<String, T> hierarchy = new HashMap<>();

        for(String suffix : QUERY_FILTER_SUFFIXES) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        queryPrefixes +
                        "select distinct ?class where { " +
                        whereClause +
                        "FILTER (REGEX(STR(?class), \"" + uriPrefix + suffix + "\", \"i\")) ." +
                        "}"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue classUri = r.getFields().get("class");

                        if(classUri != null && !hierarchy.containsKey(classUri.getValue())) {
                            hierarchy.put(classUri.getValue(), hierarchyElementFactory.createHierarchyElement(classUri.getValue()));
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying " + this.hierarchyElementsName + " hierarchy... New " +
                            "try... (" + e.getMessage() + ")");
                }
            }
        }

        return hierarchy;
    }

    private void queryElementsParents(String queryPrefixes, String parentRelationship, String uriPrefix,
                                      Map<String, T> hierarchy) {

        Map<String, Boolean> missingElements = new HashMap<>();

        for(Map.Entry<String, T> entry : hierarchy.entrySet()) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response =  (new ServerQuerier()).runQuery(
                        queryPrefixes +
                        "select distinct ?parent where {" +
                        "<" + entry.getKey() + "> " + parentRelationship + " ?parent . " +
                        "FILTER (REGEX(STR(?parent), \"" + uriPrefix + "\", \"i\")) . " +
                        "}"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue parentUri = r.getFields().get("parent");

                        if(parentUri != null) {
                            if(hierarchy.containsKey(parentUri.getValue())) {
                                T parent = hierarchy.get(parentUri.getValue());
                                T child = entry.getValue();

                                child.addParent(parent);
                                parent.addChild(child);
                            }

                            else {
                                missingElements.put(parentUri.getValue(), true);
                            }
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying " + this.hierarchyElementsName + " parents... New " +
                            "try... (" + e.getMessage() + ")");
                }
            }
        }

        if(missingElements.size() != 0) {
            System.err.println(missingElements.size() + " " + this.hierarchyElementsName + " were discovered as " +
                    "parents but not found in all " + hierarchyElementsName);
        }

    }
}
