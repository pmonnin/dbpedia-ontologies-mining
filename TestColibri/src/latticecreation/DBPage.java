package latticecreation;

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

    public void addOntology(String newOntologies) {
        this.ontologies.add(newOntologies);
    }

    public ArrayList<String> getOntologies() {
        return this.ontologies;
    }

    public ArrayList<String> getYagoClasses() {
        return this.yagoClasses;
    }

    public void setOntologies(ArrayList<String> ontologies) {
        this.ontologies = ontologies;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getURI() {
        return this.uri;
    }
}
