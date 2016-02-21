package dbpediaobjects;

import java.util.ArrayList;

/**
 * Represents a page of DBPedia.
 * Stores
 *  - its URI
 *  - the predicates used in its relationships
 *  - the categories associated with the page
 *  - the ontology classes associated with the page
 *  - the yago classes associated with the page
 * 
 * @author Pierre Monnin
 *
 */
public class Page {
    private String uri;
    private ArrayList<String> relationships;
    private ArrayList<DBCategory> categories;
    private ArrayList<DBOntology> ontologyClasses;
    private ArrayList<DBYagoClass> yagoClasses;

    /**
     * Constructs a basic DBPedia page object. All information (relationships, categories, ontology classes, yago
     * classes are left empty)
     * @param uri URI of the page
     */
    public Page(String uri) {
        this.uri = uri;
        this.relationships = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.ontologyClasses = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();
    }

    /**
     * Returns the page URI
     * @return the page URI
     */
    public String getURI() {
        return this.uri;
    }

    /**
     * Adds a relationship predicate to the page relationships.
     * If the predicate has already been added, nothing happens
     * @param r the relationship predicate to add
     */
    public void addRelationship(String r) {
        if(!this.relationships.contains(r)) {
            this.relationships.add(r);
        }
    }

    /**
     * Adds a category to the page categories.
     * If the category has already been added, nothing happens
     * @param category the category to add
     */
    public void addCategory(DBCategory category) {
        if(!this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    /**
     * Returns the categories associated with the page
     * @return the categories associated with the page
     */
    public ArrayList<DBCategory> getCategories() {
        return this.categories;
    }

    /**
     * Adds an ontology class to the page ontology classes.
     * If the ontology class has already been added, nothing happens
     * @param ontology the ontology class to add
     */
    public void addOntology(DBOntology ontology) {
        if(!this.ontologyClasses.contains(ontology)) {
            this.ontologyClasses.add(ontology);
        }
    }

    /**
     * Returns the ontology classes associated with the page
     * @return the ontology classes associated with the page
     */
    public ArrayList<DBOntology> getOntologyClasses() {
        return this.ontologyClasses;
    }

    /**
     * Adds an yago class to the page yago classes.
     * If the yago class has already been added, nothing happens
     * @param yagoClass the yago class to add
     */
    public void addYagoClass(DBYagoClass yagoClass) {
        if(!this.yagoClasses.contains(yagoClass)) {
            this.yagoClasses.add(yagoClass);
        }
    }

    /**
     * Returns the yago classes associated with the page
     * @return the yago classes associated with the page
     */
    public ArrayList<DBYagoClass> getYagoClasses() {
        return this.yagoClasses;
    }
}
