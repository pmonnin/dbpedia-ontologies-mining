package dbpediaanalyzer.lattice;

import dbpediaanalyzer.dbpediaobject.Page;

import java.util.ArrayList;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    private ArrayList<Page> objects;
    private ArrayList<String> attributes;
    private ArrayList<Concept> parents;
    private ArrayList<Concept> children;

    public Concept() {
        this.objects = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    void addAttributes(String attribute) {
        if(!this.attributes.contains(attribute)) {
            this.attributes.add(attribute);
        }
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
}
