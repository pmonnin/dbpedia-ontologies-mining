package dbpediaanalyzer.lattice;

import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    // Basic concept data
    private List<String> objects;
    private List<String> attributes;

    // Relationships
    private List<Concept> parents;
    private List<Concept> children;

    // Annotations
    private List<Category> categories;
    private List<OntologyClass> ontologyClasses;
    private List<YagoClass> yagoClasses;

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

    public Concept(List<String> objects, List<String> attributes, List<Category> categories,
                   List<OntologyClass> ontologyClasses, List<YagoClass> yagoClasses) {

        this.objects = new ArrayList<>(objects);
        this.attributes = new ArrayList<>(attributes);

        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();

        this.categories = new ArrayList<>(categories);
        this.ontologyClasses = new ArrayList<>(ontologyClasses);
        this.yagoClasses = new ArrayList<>(yagoClasses);

    }

    public List<String> getObjects() {
        return new ArrayList<>(this.objects);
    }

    public List<String> getAttributes() {
        return new ArrayList<>(this.attributes);
    }

    public void addParent(Concept parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public List<Concept> getParents() {
        return new ArrayList<>(this.parents);
    }

    public void addChild(Concept child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public List<Concept> getChildren() {
        return new ArrayList<>(this.children);
    }

    public List<Concept> getDescendants() {
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

    public void setCategories(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    public void removeCategories(List<Category> categories) {
        this.categories.removeAll(categories);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(this.categories);
    }

    public void setOntologyClasses(List<OntologyClass> ontologyClasses) {
        this.ontologyClasses = new ArrayList<>(ontologyClasses);
    }

    public void removeOntologyClasses(List<OntologyClass> ontologyClasses) {
        this.ontologyClasses.removeAll(ontologyClasses);
    }

    public List<OntologyClass> getOntologyClasses() {
        return new ArrayList<>(this.ontologyClasses);
    }

    public void setYagoClasses(List<YagoClass> yagoClasses) {
        this.yagoClasses = new ArrayList<>(yagoClasses);
    }

    public void removeYagoClasses(List<YagoClass> yagoClasses) {
        this.yagoClasses.removeAll(yagoClasses);
    }

    public List<YagoClass> getYagoClasses() {
        return new ArrayList<>(this.yagoClasses);
    }
}
