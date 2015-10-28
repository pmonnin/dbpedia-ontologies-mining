package dbpediaobjects;

import java.util.ArrayList;

/**
 * Represents a page of DBPedia with its URI, relationships, categories, ontologies and yago classes
 * 
 * @author Pierre Monnin
 *
 */
public class DBPage {

    private String uri;
    private ArrayList<String> relationships;
    private ArrayList<String> ontologies;
    private ArrayList<String> categories;
    private ArrayList<String> yagoClasses;

    public DBPage(String uri) {
        this.uri = uri;
        this.relationships = new ArrayList<>();
        this.ontologies = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();
    }

    public void addRelationship(String r) {
        this.relationships.add(r);
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public void addOntology(String ontology) {
        this.ontologies.add(ontology);
    }

    public ArrayList<String> getOntologies() {
        return this.ontologies;
    }

    public void addYagoClass(String yagoClass) {
    	this.yagoClasses.add(yagoClass);
    }
    
    public ArrayList<String> getYagoClasses() {
        return this.yagoClasses;
    }

    public void addCategory(String category) {
    	this.categories.add(category);
    }

    public String getURI() {
        return this.uri;
    }
}
