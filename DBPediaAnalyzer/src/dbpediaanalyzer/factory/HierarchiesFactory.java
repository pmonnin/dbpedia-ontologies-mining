package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.*;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;
import dbpediaanalyzer.io.SparqlValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds the three considered ontologies from the SPARQL endpoint
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesFactory {

    public static HierarchiesManager createHierarchies() {
        return new HierarchiesManager(createCategoriesHierarchy(), createOntologyClassesHierarchy(),
                createYagoClassesHierarchy());
    }

    private static Map<String, Category> createCategoriesHierarchy() {
        HierarchyFactory<Category> factory = new HierarchyFactory<>(new HierarchyElementFactory<Category>() {
            @Override
            public Category createHierarchyElement(String uri) {
                return new Category(uri);
            }
        }, "categories");

        String creationQueryPrefixes = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ";
        String creationQueryWhereClause = "?class rdf:type skos:Concept . ";
        String creationQueryUriPrefix = "http://dbpedia.org/resource/Category:";
        String parentsQueryPrefixes = "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> ";
        String parentRelationship = "skos:broader";
        String parentsQueryUriPrefix = "http://dbpedia.org/resource/Category";

        return factory.createHierarchy(creationQueryPrefixes, creationQueryWhereClause, creationQueryUriPrefix,
                parentsQueryPrefixes, parentRelationship, parentsQueryUriPrefix);
    }

    private static Map<String, OntologyClass> createOntologyClassesHierarchy() {
        HierarchyFactory<OntologyClass> factory = new HierarchyFactory<>(new HierarchyElementFactory<OntologyClass>() {
            @Override
            public OntologyClass createHierarchyElement(String uri) {
                return new OntologyClass(uri);
            }
        }, "ontology classes");

        String creationQueryPrefixes = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX owl:<http://www.w3.org/2002/07/owl#> ";
        String creationQueryWhereClause = "?class rdf:type owl:Class . ";
        String creationQueryUriPrefix = "http://dbpedia.org/ontology/";
        String parentsQueryPrefixes = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ";
        String parentRelationship = "rdfs:subClassOf";
        String parentsQueryUriPrefix = "http://dbpedia.org/ontology";

        return factory.createHierarchy(creationQueryPrefixes, creationQueryWhereClause, creationQueryUriPrefix,
                parentsQueryPrefixes, parentRelationship, parentsQueryUriPrefix);
    }

    private static Map<String, YagoClass> createYagoClassesHierarchy() {
        HierarchyFactory<YagoClass> factory = new HierarchyFactory<>(new HierarchyElementFactory<YagoClass>() {
            @Override
            public YagoClass createHierarchyElement(String uri) {
                return new YagoClass(uri);
            }
        }, "yago classes");

        String creationQueryPrefixes = "PREFIX owl:<http://www.w3.org/2002/07/owl#> ";
        String creationQueryWhereClause = "?class owl:equivalentClass ?ec . " +
                "FILTER (REGEX(STR(?ec), \"http://yago-knowledge.org\", \"i\")) . ";
        String creationQueryUriPrefix = "http://dbpedia.org/class/yago/";
        String parentsQueryPrefixes = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ";
        String parentRelationship = "rdfs:subClassOf";
        String parentsQueryUriPrefix = "http://dbpedia.org/class/yago";

        return factory.createHierarchy(creationQueryPrefixes, creationQueryWhereClause, creationQueryUriPrefix,
                parentsQueryPrefixes, parentRelationship, parentsQueryUriPrefix);
    }
}
