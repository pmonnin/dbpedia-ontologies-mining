package dbpediaanalyzer.lattice;

import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.dbpediaobject.YagoClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    // Basic concept data
    private ArrayList<String> objects;
    private ArrayList<String> attributes;

    // Relationships
    private ArrayList<Concept> parents;
    private ArrayList<Concept> children;

    // Annotations
    private ArrayList<Category> categories;
    private ArrayList<OntologyClass> ontologyClasses;
    private ArrayList<YagoClass> yagoClasses;

    public Concept(colibri.lib.Concept colibriConcept) {
        this.objects = new ArrayList<>();
        this.attributes = new ArrayList<>();

        for(Object o : colibriConcept.getObjects()) {
            this.objects.add(o.toString());
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

    public Concept(ArrayList<String> objects, ArrayList<String> attributes, ArrayList<Category> categories,
                   ArrayList<OntologyClass> ontologyClasses, ArrayList<YagoClass> yagoClasses) {

        this.objects = new ArrayList<>(objects);
        this.attributes = new ArrayList<>(attributes);
        this.categories = new ArrayList<>(categories);
        this.ontologyClasses = new ArrayList<>(ontologyClasses);
        this.yagoClasses = new ArrayList<>(yagoClasses);

    }

    public ArrayList<String> getObjects() {
        return new ArrayList<>(this.objects);
    }

    public ArrayList<String> getAttributes() {
        return new ArrayList<>(this.attributes);
    }

    public void addParent(Concept parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public ArrayList<Concept> getParents() {
        return new ArrayList<>(this.parents);
    }

    public void addChild(Concept child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public ArrayList<Concept> getChildren() {
        return new ArrayList<>(this.children);
    }

    public ArrayList<Concept> getDescendants() {
        ArrayList<Concept> descendants = new ArrayList<>();

        Queue<Concept> queue = new LinkedList<>();
        queue.addAll(this.children);
        descendants.addAll(this.children);

        while(!queue.isEmpty()) {
            Concept concept = queue.poll();

            for(Concept child : concept.getChildren()) {
                if(!descendants.contains(child)) {
                    descendants.add(child);
                    queue.add(child);
                }
            }
        }

        return descendants;
    }

    void setCategories(ArrayList<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    void removeCategories(ArrayList<Category> categories) {
        this.categories.removeAll(categories);
    }

    public ArrayList<Category> getCategories() {
        return new ArrayList<>(this.categories);
    }

    void setOntologyClasses(ArrayList<OntologyClass> ontologyClasses) {
        this.ontologyClasses = new ArrayList<>(ontologyClasses);
    }

    void removeOntologyClasses(ArrayList<OntologyClass> ontologyClasses) {
        this.ontologyClasses.removeAll(ontologyClasses);
    }

    public ArrayList<OntologyClass> getOntologyClasses() {
        return new ArrayList<>(this.ontologyClasses);
    }

    void setYagoClasses(ArrayList<YagoClass> yagoClasses) {
        this.yagoClasses = new ArrayList<>(yagoClasses);
    }

    void removeYagoClasses(ArrayList<YagoClass> yagoClasses) {
        this.yagoClasses.removeAll(yagoClasses);
    }

    public ArrayList<YagoClass> getYagoClasses() {
        return new ArrayList<>(this.yagoClasses);
    }
}
