package io.github.pmonnin.semanticwebobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a class of an ontology
 * @author Pierre Monnin
 */
public class OntologyClass {
    private String name;
    private List<OntologyClass> parents;
    private List<OntologyClass> children;

    public OntologyClass(String name) {
        this.name = name;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addChild(OntologyClass child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public void addParent(OntologyClass parent) {
        if (!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean hasParents() {
        return !this.parents.isEmpty();
    }

    public List<OntologyClass> getParents() {
        return new ArrayList<>(this.parents);
    }

    public int getParentsNumber() {
        return this.parents.size();
    }

    public List<OntologyClass> getChildren() {
        return new ArrayList<>(this.children);
    }

    public int getChildrenNumber() {
        return this.children.size();
    }
}
