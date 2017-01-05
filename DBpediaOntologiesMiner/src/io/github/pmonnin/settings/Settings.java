package io.github.pmonnin.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent the possible settings of the various programs
 * @author Pierre Monnin
 */
public class Settings {
    public static Map<String, OntologySettings> ontologySettings = new HashMap<>();
    static {
        ontologySettings.put("dbc", new OntologySettings("http://dbpedia.org/resource/Category:",
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ",
                "?class rdf:type skos:Concept . ",
                "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ",
                "skos:broader"
        ));

        ontologySettings.put("dbo", new OntologySettings("http://dbpedia.org/ontology/",
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl:<http://www.w3.org/2002/07/owl#> ",
                "?class rdf:type owl:Class . ",
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ",
                "rdfs:subClassOf"
        ));

        ontologySettings.put("yago", new OntologySettings("http://dbpedia.org/class/yago/",
                "PREFIX owl:<http://www.w3.org/2002/07/owl#> ",
                "?class owl:equivalentClass ?ec . " +
                        "FILTER (REGEX(STR(?ec), \"http://yago-knowledge.org\", \"i\")) . ",
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ",
                "rdfs:subClassOf"
        ));
    }
}
