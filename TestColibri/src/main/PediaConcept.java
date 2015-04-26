package main;

import java.util.ArrayList;
import java.util.HashMap;

import latticecreation.LatticeObject;

public class PediaConcept {

    private ArrayList<String> listeObjets, listeAttributs, categories, ontologies, yagoClasses;
    private ArrayList<PediaConcept> parents;

    public PediaConcept(ArrayList<String> objets, ArrayList<String> attributs, HashMap<String, LatticeObject> objects) {
        listeObjets = objets;
        listeAttributs = attributs;
        parents = new ArrayList<>();
        categories = new ArrayList<>();
        ontologies = new ArrayList<>();
        yagoClasses = new ArrayList<>();

        if (this.listeObjets.size() > 0) {
            // On remplit les catégories
            categories = intersectCategories(objects);

            // On remplit les ontologies
            ontologies = intersectOntologies(objects);
            
            // On remplit les classes yago
            yagoClasses = intersectYagoClasses(objects);
        }

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

    public ArrayList<String> getOntologies() {
        return ontologies;
    }
    
    public ArrayList<String> getYagoClasses() {
        return yagoClasses;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void setOntologies(ArrayList<String> ontologies) {
        this.ontologies = ontologies;
    }

    public ArrayList<PediaConcept> getParents() {
        return parents;
    }

    public int getParentsNumber() {
        return parents.size();
    }

    public void addParentPediaConcept(PediaConcept parent) {
        removeDoublonsCategories(parent.getCategories());
        removeDoublonsOntologies(parent.getOntologies());
        parents.add(parent);
    }

    public void addCategoriesPediaConcept(ArrayList<String> cats) {
        categories = cats;
    }

    public void addOntologiesPediaConcept(ArrayList<String> ontos) {
        ontologies = ontos;
    }

    public void removeDoublonsCategories(ArrayList<String> catP) {
        ArrayList<String> categoriesASupprimees = new ArrayList<>();

        for (String cat : this.getCategories()) {
            if (catP.contains(cat))
                categoriesASupprimees.add(cat);
        }

        for (String cat : categoriesASupprimees)
            this.getCategories().remove(cat);
    }

    public void removeDoublonsOntologies(ArrayList<String> ontP) {
        ArrayList<String> ontologiesASupprimer = new ArrayList<>();

        for (String ont : this.getOntologies()) {
            if (ontP.contains(ont))
                ontologiesASupprimer.add(ont);
        }

        for (String ont : ontologiesASupprimer)
            this.getOntologies().remove(ont);
    }

    public ArrayList<String> intersectOntologies(HashMap<String, LatticeObject> objects) {
        // On initialise la liste à retourner aux ontologies du premier objet
        ArrayList<String> returnOntologies = objects.get(listeObjets.get(0)).getOntologies();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.listeObjets.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnOntologies = new ArrayList<String>();

            // On récupère le LatticeObject correspondant
            LatticeObject currentObject = objects.get(listeObjets.get(i));
            // on récupère ses ontologies
            ArrayList<String> currentOntologies = currentObject.getOntologies();

            // On compare les ontologies à la liste déjà présente dans returnOntologies
            for (int j = 0; j < currentOntologies.size(); j++) {
                // Si l'ontologie a été rencontrée au préalable
                if (returnOntologies.contains(currentOntologies.get(j))) {
                    // on l'ajoute à celle qui sera la nouvelle liste à retourner
                    newReturnOntologies.add(currentOntologies.get(j));
                }
            }

            // On remplace returnOntologies par newReturnOntologies
            returnOntologies = newReturnOntologies;
        }

        return returnOntologies;
    }
    
    private ArrayList<String> intersectYagoClasses(HashMap<String, LatticeObject> objects) {
        // On initialise la liste à retourner aux classes yago du premier objet
        ArrayList<String> returnYagoClasses = objects.get(listeObjets.get(0)).getYagoClasses();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.listeObjets.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnYagoClasses = new ArrayList<String>();

            // On récupère le LatticeObject correspondant
            LatticeObject currentObject = objects.get(listeObjets.get(i));
            // on récupère ses ontologies
            ArrayList<String> currentYagoClasses = currentObject.getYagoClasses();

            // On compare les ontologies à la liste déjà présente dans returnOntologies
            for (int j = 0; j < currentYagoClasses.size(); j++) {
                // Si l'ontologie a été rencontrée au préalable
                if (returnYagoClasses.contains(currentYagoClasses.get(j))) {
                    // on l'ajoute à celle qui sera la nouvelle liste à retourner
                    newReturnYagoClasses.add(currentYagoClasses.get(j));
                }
            }

            // On remplace returnOntologies par newReturnOntologies
            returnYagoClasses = newReturnYagoClasses;
        }

        return returnYagoClasses;
    }

    public ArrayList<String> intersectCategories(HashMap<String, LatticeObject> objects) {
        // On initialise la liste � retourner aux categories du premier objet
        ArrayList<String> returnCategories = objects.get(listeObjets.get(0)).getCategories();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.listeObjets.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnCategories = new ArrayList<String>();

            // On r�cup�re le LatticeObject correspondant
            LatticeObject currentObject = objects.get(listeObjets.get(i));
            // on r�cup�re ses categories
            ArrayList<String> currentCategories = currentObject.getCategories();

            // On compare les cat�gories � la liste d�j� pr�sente dans returnCategories
            for (int j = 0; j < currentCategories.size(); j++) {
                // Si la categorie a �t� rencontr�e au pr�alable
                if (returnCategories.contains(currentCategories.get(j))) {
                    // on l'ajoute � celle qui sera la nouvelle liste � retourner
                    newReturnCategories.add(currentCategories.get(j));
                }
            }

            // On remplace returnCategories par newReturnCategories
            returnCategories = newReturnCategories;
        }

        return returnCategories;
    }

    public String makeRequestOntology() {
        // Begin of the request
        String request = "SELECT DISTINCT ?onto";
        request += " WHERE {";

        // For each object, we link it to the categ
        for (int i = 0; i < listeObjets.size(); i++) {
            request += "<" + listeObjets.get(i) + "> a ?onto";

            // if (i < listeObjets.size() - 1) {
            request += ".";
            // }
        }

        // End of the request
        request += "FILTER (REGEX(STR(?onto), \"http://dbpedia.org/ontology\", \"i\"))";
        request += "}";

        return request;
    }

    public String makeRequestCategory() {
        // Begin of the request

        String request = "SELECT DISTINCT ?categ";
        request += " WHERE {";

        // For each object, we link it to the categ
        for (int i = 0; i < listeObjets.size(); i++) {
            request += "<" + listeObjets.get(i) + "> <http://purl.org/dc/terms/subject> ?categ";

            if (i < listeObjets.size() - 1) {
                request += ".";
            }
        }

        // End of the request
        request += "}";

        return request;
    }

    public boolean isEquivalentTo(PediaConcept pc) {
        ArrayList<String> objets = pc.getListeObjets();
        ArrayList<String> atts = pc.getListeAttributs();

        if (objets.size() != this.listeObjets.size())
            return false;
        if (atts.size() != this.listeAttributs.size())
            return false;

        for (int i = 0; i < listeObjets.size(); i++) {
            if (!objets.contains(listeObjets.get(i)))
                return false;
        }

        for (int i = 0; i < listeAttributs.size(); i++) {
            if (!atts.contains(listeAttributs.get(i)))
                return false;
        }

        return true;
    }

    public ArrayList<String> unionCategoriesParent() {
        ArrayList<String> res = new ArrayList<>();

        for (PediaConcept p : this.getParents()) {
            for (String c : p.getCategories())
                res.add(c);
        }
        return res;
    }

    public ArrayList<String> unionOntologiesParent() {
        ArrayList<String> res = new ArrayList<>();

        for (PediaConcept p : this.getParents()) {
            for (String o : p.getOntologies())
                res.add(o);
        }
        return res;
    }

    public ArrayList<String> unionYagoClassesParent() {
        ArrayList<String> res = new ArrayList<>();

        for (PediaConcept p : this.getParents()) {
            for (String o : p.getYagoClasses())
                res.add(o);
        }
        return res;
    }
}
