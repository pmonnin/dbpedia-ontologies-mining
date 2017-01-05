package io.github.pmonnin.factories;

import io.github.pmonnin.semanticwebobjects.OntologyClass;
import io.github.pmonnin.io.ServerQuerier;
import io.github.pmonnin.io.SparqlRecord;
import io.github.pmonnin.io.SparqlResponse;
import io.github.pmonnin.io.SparqlValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates an ontology based on a given query
 * @author Pierre Monnin
 */
public class OntologyFactory {
    private static final String[] QUERY_FILTER_SUFFIXES = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "[^a-z]"};

    public Map<String, OntologyClass> buildOntology(String ontologyUriPrefix, String creationPrefixes,
                                                    String creationWhereConditions, String parentsPrefixes,
                                                    String parentsPredicate) {
        Map<String, OntologyClass> ontology = queryOntologyClasses(creationPrefixes, creationWhereConditions,
                ontologyUriPrefix);
        queryClassesParents(parentsPrefixes, parentsPredicate, ontologyUriPrefix, ontology);
        return ontology;
    }

    private Map<String, OntologyClass> queryOntologyClasses(String queryPrefixes, String whereConditions,
                                                            String uriPrefix) {
        Map<String, OntologyClass> ontology = new HashMap<>();

        for(String suffix : QUERY_FILTER_SUFFIXES) {
            boolean done = false;

            while (!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                            queryPrefixes +
                                    "select distinct ?class where { " +
                                    whereConditions +
                                    "FILTER (REGEX(STR(?class), \"" + uriPrefix + suffix + "\", \"i\")) ." +
                                    "}"
                    );


                    for (SparqlRecord r : response.getRecords()) {
                        SparqlValue classUri = r.getFields().get("class");

                        if (classUri != null && !ontology.containsKey(classUri.getValue())) {
                            ontology.put(classUri.getValue(), new OntologyClass(classUri.getValue()));
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("[ERROR] Exception while querying " + uriPrefix + " ontology... New " +
                            "try... (" + e.getMessage() + ")");
                }
            }
        }

        return ontology;
    }

    private void queryClassesParents(String queryPrefixes, String parentsPredicate, String uriPrefix,
                                     Map<String, OntologyClass> ontology) {
        Map<String, Boolean> missingElements = new HashMap<>();

        for (OntologyClass ontologyClass : ontology.values()) {
            boolean done = false;

            while (!done) {
                try {
                    SparqlResponse response =  (new ServerQuerier()).runQuery(
                            queryPrefixes +
                                    "select distinct ?parent where {" +
                                    "<" + ontologyClass.getName() + "> " + parentsPredicate + " ?parent . " +
                                    "FILTER (REGEX(STR(?parent), \"" + uriPrefix + "\", \"i\")) . " +
                                    "}"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue parentUri = r.getFields().get("parent");

                        if(parentUri != null) {
                            if(ontology.containsKey(parentUri.getValue())) {
                                OntologyClass parent = ontology.get(parentUri.getValue());

                                ontologyClass.addParent(parent);
                                parent.addChild(ontologyClass);
                            }

                            else {
                                missingElements.put(parentUri.getValue(), true);
                            }
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    if (ontologyClass.getName().contains("\"")) {
                        done = true;
                        System.err.println("[ERROR] Quote inside ontology class name (" + ontologyClass.getName() +
                                "). Aborting getting parents for it.");
                        // Hack for the case of http://dbpedia.org/class/yago/WikicatAlbumsProducedByDonald"Duck"Dunn
                    }

                    else {
                        System.err.println("[ERROR] Exception while querying " + uriPrefix + " parents... New " +
                                "try... (" + e.getMessage() + ")");
                    }
                }
            }
        }
    }
}
