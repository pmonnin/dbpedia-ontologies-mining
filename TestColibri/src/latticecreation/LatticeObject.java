package latticecreation;

import java.util.ArrayList;

import colibri.lib.Relation;

public class LatticeObject {

    private String name;
    private ArrayList<String> attributes;
    private ArrayList<String> ontologies;
    private ArrayList<String> categories;
    private ArrayList<String> yagoClasses;

    public LatticeObject(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.ontologies = new ArrayList<String>();
        this.categories = new ArrayList<String>();
        this.yagoClasses = new ArrayList<String>();
    }

    public void addAttribute(String att) {
        this.attributes.add(att);
    }

    public void addToRelation(Relation rel) {
        for (int i = 0; i < this.attributes.size(); i++) {
            rel.add(this.name, this.attributes.get(i));
        }
    }

    public void deleteAttribute(String att) {
        this.attributes.remove(att);
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

    public String getName() {
        return name;
    }
}
