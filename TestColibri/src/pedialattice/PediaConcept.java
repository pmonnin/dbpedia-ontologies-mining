package pedialattice;

import java.util.ArrayList;
import java.util.HashMap;

import colibri.lib.Concept;
import com.sun.org.apache.xalan.internal.xsltc.dom.ArrayNodeListIterator;
import dbpediaobjects.DBCategory;
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
        this.objects = new ArrayList<>();
        for(Object o : concept.getObjects()) {
        	this.objects.add((String) o);
        }
        
        this.attributes = new ArrayList<>();
        for(Object a : concept.getAttributes()) {
        	this.attributes.add((String) a);
        }
        
        // Concept relationship within lattice
        this.parents = new ArrayList<>();
        
        // Concept intersection of DB Pedia categories, ontologies and yago classes associated to each concept's page
        this.categories = new ArrayList<>();
        this.ontologies = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();

        if(this.objects.size() > 0) {
            for(String o : this.objects) {
                DBPage page = dbPages.get(o);

                if(page != null) {
                    for(String cat : page.getCategories()) {
                        if(!this.categories.contains(cat))  {
                            boolean toAdd = true;

                            for(String o2 : this.objects) {
                                DBPage p = dbPages.get(o2);

                                if(p != null && !p.getCategories().contains(cat)) {
                                    toAdd = false;
                                }
                            }

                            if(toAdd) {
                                this.categories.add(cat);
                            }
                        }
                    }

                    for(String onto : page.getOntologies()) {
                        if(!this.ontologies.contains(onto)) {
                            boolean toAdd = true;

                            for(String o2 : this.objects) {
                                DBPage p = dbPages.get(o2);

                                if(p != null && !p.getOntologies().contains(onto)) {
                                    toAdd = false;
                                }
                            }

                            if(toAdd) {
                                this.ontologies.add(onto);
                            }
                        }
                    }

                    for(String yagoClass : page.getYagoClasses()) {
                        if(!this.yagoClasses.contains(yagoClass)) {
                            boolean toAdd = true;

                            for(String o2 : this.objects) {
                                DBPage p = dbPages.get(o2);

                                if(p != null && !p.getYagoClasses().contains(yagoClass)) {
                                    toAdd = false;
                                }
                            }

                            if(toAdd) {
                                this.yagoClasses.add(yagoClass);
                            }
                        }
                    }
                }
            }
        }
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

    public ArrayList<PediaConcept> getParents() {
        return this.parents;
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