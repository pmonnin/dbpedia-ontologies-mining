package pedialattice;

import java.util.ArrayList;
import java.util.HashMap;

import colibri.lib.Concept;
import dbpediaobjects.DBPage;

/**
 * Class of PediaConcept object
 * 
 * @author Damien Flament
 * @author Soline Blanc
 * @author Thomas Herbeth
 * @author Pierre Monnin
 * 
 */
public class PediaConcept {
    private ArrayList<String> objects;
    private ArrayList<String> attributes;
    private ArrayList<String> categories;
    private ArrayList<String> ontologies;
    private ArrayList<String> yagoClasses;
    private ArrayList<PediaConcept> parents;

    public PediaConcept(Concept concept, HashMap<String, DBPage> dbPages) {
    	// Concept objects (DBPedia pages) & attributes (DBPedia relationship)
        this.objects = new ArrayList<String>();
        concept.getObjects().forEach(o -> this.objects.add((String) o));
        
        this.attributes = new ArrayList<String>();
        concept.getAttributes().forEach(a -> this.attributes.add((String) a));
        
        // Concept relationship within lattice
        this.parents = new ArrayList<>();
        
        // Concept intersection of DB Pedia categories, ontologies and yago classes associated to concept's pages
        this.categories = new ArrayList<>();
        this.ontologies = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();

        if (this.objects.size() > 0) {
            // On remplit les catégories
            this.categories = intersectCategories(dbPages);

            // On remplit les ontologies
            this.ontologies = intersectOntologies(dbPages);
            
            // On remplit les classes yago
            this.yagoClasses = intersectYagoClasses(dbPages);
        }

    }

    public ArrayList<String> getListeObjets() {
        return this.objects;
    }

    public void setListeObjets(ArrayList<String> listeObjets) {
        this.objects = listeObjets;
    }

    public ArrayList<String> getListeAttributs() {
        return this.attributes;
    }

    public void setListeAttributs(ArrayList<String> listeAttributs) {
        this.attributes = listeAttributs;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public ArrayList<String> getOntologies() {
        return this.ontologies;
    }
    
    public ArrayList<String> getYagoClasses() {
        return this.yagoClasses;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void setOntologies(ArrayList<String> ontologies) {
        this.ontologies = ontologies;
    }

    public ArrayList<PediaConcept> getParents() {
        return this.parents;
    }

    /**
     * getParentsNumber
     * @return size of the list of parents
     */
    public int getParentsNumber() {
        return this.parents.size();
    }

    /**
     * Add a parent to the list of parents of the current pedia concept
     * @param parent : parent of the current pedia concept
     */
    public void addParentPediaConcept(PediaConcept parent) {
        removeDoublonsCategories(parent.getCategories());
        removeDoublonsOntologies(parent.getOntologies());
        this.parents.add(parent);
    }

    public void addCategoriesPediaConcept(ArrayList<String> cats) {
        this.categories = cats;
    }

    public void addOntologiesPediaConcept(ArrayList<String> ontos) {
        this.ontologies = ontos;
    }

    /**
     * remove duplicated categories present in the list of categories
     * @param catP : list of categories
     */
    public void removeDoublonsCategories(ArrayList<String> catP) {
        ArrayList<String> categoriesASupprimees = new ArrayList<>();

        for (String cat : this.getCategories()) {
            if (catP.contains(cat))
                categoriesASupprimees.add(cat);
        }

        for (String cat : categoriesASupprimees)
            this.getCategories().remove(cat);
    }

    /**
     * remove duplicated ontologies present in the list of categories
     * @param ontP : list of ontologies
     */
    public void removeDoublonsOntologies(ArrayList<String> ontP) {
        ArrayList<String> ontologiesASupprimer = new ArrayList<>();

        for (String ont : this.getOntologies()) {
            if (ontP.contains(ont))
                ontologiesASupprimer.add(ont);
        }

        for (String ont : ontologiesASupprimer)
            this.getOntologies().remove(ont);
    }

    public ArrayList<String> intersectOntologies(HashMap<String, DBPage> objects) {
        // On initialise la liste à retourner aux ontologies du premier objet
        ArrayList<String> returnOntologies = objects.get(this.objects.get(0)).getOntologies();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.objects.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnOntologies = new ArrayList<>();

            // On récupère le LatticeObject correspondant
            DBPage currentObject = objects.get(this.objects.get(i));
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
    
    private ArrayList<String> intersectYagoClasses(HashMap<String, DBPage> objects) {
        // On initialise la liste à retourner aux classes yago du premier objet
        ArrayList<String> returnYagoClasses = objects.get(this.objects.get(0)).getYagoClasses();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.objects.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnYagoClasses = new ArrayList<>();

            // On récupère le LatticeObject correspondant
            DBPage currentObject = objects.get(this.objects.get(i));
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

    public ArrayList<String> intersectCategories(HashMap<String, DBPage> objects) {
        // On initialise la liste � retourner aux categories du premier objet
        ArrayList<String> returnCategories = objects.get(this.objects.get(0)).getCategories();

        // Pour chaque objet du concept, hormis le premier
        for (int i = 1; i < this.objects.size(); i++) {
            // On initialise celle qui sera la nouvelle liste � retourner
            ArrayList<String> newReturnCategories = new ArrayList<>();

            // On r�cup�re le LatticeObject correspondant
            DBPage currentObject = objects.get(this.objects.get(i));
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
        for (int i = 0; i < this.objects.size(); i++) {
            request += "<" + this.objects.get(i) + "> a ?onto";

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
        for (int i = 0; i < this.objects.size(); i++) {
            request += "<" + this.objects.get(i) + "> <http://purl.org/dc/terms/subject> ?categ";

            if (i < this.objects.size() - 1) {
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

        if (objets.size() != this.objects.size())
            return false;
        if (atts.size() != this.attributes.size())
            return false;

        for (int i = 0; i < this.objects.size(); i++) {
            if (!objets.contains(this.objects.get(i)))
                return false;
        }

        for (int i = 0; i < this.attributes.size(); i++) {
            if (!atts.contains(this.attributes.get(i)))
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

    @Override
    public String toString() {
        return "PediaConcept{" + "listeObjets=" + this.objects + ", listeAttributs=" + this.attributes + ", categories=" + this.categories + ", ontologies=" + this.ontologies + ", yagoClasses=" + this.yagoClasses + ", parents=" + this.parents + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.categories == null) ? 0 : this.categories.hashCode());
        result = prime * result + ((this.attributes == null) ? 0 : this.attributes.hashCode());
        result = prime * result + ((this.objects == null) ? 0 : this.objects.hashCode());
        result = prime * result + ((this.ontologies == null) ? 0 : this.ontologies.hashCode());
        result = prime * result + ((this.parents == null) ? 0 : this.parents.hashCode());
        result = prime * result + ((this.yagoClasses == null) ? 0 : this.yagoClasses.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        PediaConcept other = (PediaConcept) obj;
        
        if (this.categories == null) {
            if (other.categories != null) {
                return false;
            }
        } 
        
        else if (!this.categories.equals(other.categories)) {
            return false;
        }
        
        if (this.attributes == null) {
            if (other.attributes != null) {
                return false;
            }
        } 
        
        else if (!this.attributes.equals(other.attributes)) {
            return false;
        }
        
        if (this.objects == null) {
            if (other.objects != null) {
                return false;
            }
        } 
        
        else if (!this.objects.equals(other.objects)) {
            return false;
        }
        
        if (this.ontologies == null) {
            if (other.ontologies != null) {
                return false;
            }
        } 
        
        else if (!this.ontologies.equals(other.ontologies)) {
            return false;
        }
        
        if (this.parents == null) {
            if (other.parents != null) {
                return false;
            }
        } 
        
        else if (!this.parents.equals(other.parents)) {
            return false;
        }
        
        if (this.yagoClasses == null) {
            if (other.yagoClasses != null) {
                return false;
            }
        } 
        
        else if (!this.yagoClasses.equals(other.yagoClasses)) {
            return false;
        }
        
        return true;
    }
}
