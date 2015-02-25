package main;

import java.util.ArrayList;

public class PediaConcept {

    private ArrayList<String> listeObjets, listeAttributs, categories;
    private ArrayList<PediaConcept> parents;

    public PediaConcept(ArrayList<String> objets, ArrayList<String> attributs) {
        listeObjets = objets;
        listeAttributs = attributs;
        parents = new ArrayList<>();
    }

    public ArrayList<String> getListeObjets() {
        return listeObjets;
    }

    public void setListeObjets(ArrayList<String> listeObjets) {
        this.listeObjets = listeObjets;
    }

    public ArrayList<String> getListeAttributs() {
        return listeAttributs;
    }

    public void setListeAttributs(ArrayList<String> listeAttributs) {
        this.listeAttributs = listeAttributs;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<PediaConcept> getParents() {
        return parents;
    }

    public void addParentPediaConcept(PediaConcept parent) {
        parents.add(parent);
    }

    public void addCategoriesPediaConcept(ArrayList<String> cats) {
        categories = cats;
    }

    public String makeRequestCategory() {
        // Begin of the request
        String request = "SELECT DISTINCT ?categ";
        request += " WHERE {";

        // For each object, we link it to the categ
        for (int i = 0; i < listeObjets.size(); i++) {
            request += "<" + listeObjets.get(i) + "> a ?categ";

            if (i < listeObjets.size() - 1) {
                request += ".";
            }
        }

        // End of the request
        request += "}";

        return request;
    }
}
