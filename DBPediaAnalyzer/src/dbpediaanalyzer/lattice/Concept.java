package dbpediaanalyzer.lattice;

import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.dbpediaobject.YagoClass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    // Basic concept data
    private ArrayList<Page> objects;
    private ArrayList<String> attributes;

    // Relationships
    private ArrayList<Concept> parents;
    private ArrayList<Concept> children;

    // Annotations
    private ArrayList<Category> categories;
    private ArrayList<OntologyClass> ontologyClasses;
    private ArrayList<YagoClass> yagoClasses;

    public Concept(colibri.lib.Concept colibriConcept, HashMap<String, Page> dataSet) {
        this.objects = new ArrayList<>();
        this.attributes = new ArrayList<>();

        for(Object o : colibriConcept.getObjects()) {
            this.objects.add(dataSet.get(o.toString()));
        }

        for(Object a : colibriConcept.getAttributes()) {
            this.attributes.add((String) a);
        }

        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();

        this.categories = new ArrayList<>();
        this.ontologyClasses = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();
    }

    public ArrayList<Page> getObjects() {
        return this.objects;
    }

    void addParent(Concept parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public ArrayList<Concept> getParents() {
        return new ArrayList<>(this.parents);
    }

    void addChild(Concept child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public ArrayList<Concept> getChildren() {
        return new ArrayList<>(this.children);
    }

    void setCategories(ArrayList<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    void setOntologyClasses(ArrayList<OntologyClass> ontologyClasses) {
        this.ontologyClasses = new ArrayList<>(ontologyClasses);
    }

    void setYagoClasses(ArrayList<YagoClass> yagoClasses) {
        this.yagoClasses = new ArrayList<>(yagoClasses);
    }
}
