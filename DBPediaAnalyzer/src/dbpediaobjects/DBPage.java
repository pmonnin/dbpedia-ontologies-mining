package dbpediaobjects;

import java.util.ArrayList;

/**
 * Represents a page of DBPedia with its URI, relationships, categories, ontologies and yago classes
 * 
 * @author Pierre Monnin
 *
 */
@Deprecated
public class DBPage {

    private String uri;
    private ArrayList<String> relationships;
    private ArrayList<DBOntology> ontologies;
    private ArrayList<DBCategory> categories;
    private ArrayList<DBYagoClass> yagoClasses;

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

    public void addCategory(DBCategory category) {
        if(!this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public ArrayList<DBCategory> getCategories() {
        return this.categories;
    }

    public void addOntology(DBOntology ontology) {
        if(!this.ontologies.contains(ontology)) {
            this.ontologies.add(ontology);
        }
    }

    public ArrayList<DBOntology> getOntologies() {
        return this.ontologies;
    }

    public void addYagoClass(DBYagoClass yagoClass) {
        if(!this.yagoClasses.contains(yagoClass)) {
            this.yagoClasses.add(yagoClass);
        }
    }
    
    public ArrayList<DBYagoClass> getYagoClasses() {
        return this.yagoClasses;
    }

    public String getURI() {
        return this.uri;
    }
}
