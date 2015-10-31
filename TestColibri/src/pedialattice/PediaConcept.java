package pedialattice;

import java.util.ArrayList;
import java.util.HashMap;

import colibri.lib.Concept;
import dbpediaobjects.DBCategoriesManager;
import dbpediaobjects.DBOntologiesManager;
import dbpediaobjects.DBPage;
import dbpediaobjects.DBYagoClassesManager;

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
    private ArrayList<PediaConcept> children;

    private int depth;

    public PediaConcept(Concept concept, HashMap<String, DBPage> dbPages, DBCategoriesManager dbcategories,
                        DBOntologiesManager dbontologies, DBYagoClassesManager dbyagoclasses) {
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
        this.children = new ArrayList<>();
        
        // Concept intersection of DB Pedia categories, ontologies and yago classes associated to each concept's page
        this.categories = new ArrayList<>();
        this.ontologies = new ArrayList<>();
        this.yagoClasses = new ArrayList<>();

        boolean initialized = false;
        for(String o : this.objects) {
            DBPage page = dbPages.get(o);

            if(page != null) {
                if(!initialized) {
                    initializeHierarchiesFromPage(page, dbcategories, dbontologies, dbyagoclasses);
                    initialized = true;
                }

                else {
                    hierarchiesIntersectionWithPage(page, dbcategories, dbontologies, dbyagoclasses);
                }
            }
        }

        this.depth = -1;
    }

    private void initializeHierarchiesFromPage(DBPage page, DBCategoriesManager dbcategories, DBOntologiesManager dbontologies,
                                               DBYagoClassesManager dbyagoclasses) {

        for(int i = 0 ; i < page.getCategories().size() ; i++) {
            if(i == 0) {
                this.categories.addAll(dbcategories.getSelfAndAncestors(page.getCategories().get(0)));
            }

            else {
                ArrayList<String> selfAndAncestors = dbcategories.getSelfAndAncestors(page.getCategories().get(i));

                for(String cat : selfAndAncestors) {
                    if(!this.categories.contains(cat)) {
                        this.categories.add(cat);
                    }
                }
            }
        }

        for(int i = 0 ; i < page.getOntologies().size() ; i++) {
            if(i == 0) {
                this.ontologies.addAll(dbontologies.getSelfAndAncestors(page.getOntologies().get(0)));
            }

            else {
                ArrayList<String> selfAndAncestors = dbontologies.getSelfAndAncestors(page.getOntologies().get(i));

                for(String ontology : selfAndAncestors) {
                    if(!this.ontologies.contains(ontology)) {
                        this.ontologies.add(ontology);
                    }
                }
            }
        }

        for(int i = 0 ; i < page.getYagoClasses().size() ; i++) {
            if(i == 0) {
                this.yagoClasses.addAll(dbyagoclasses.getSelfAndAncestors(page.getYagoClasses().get(0)));
            }

            else {
                ArrayList<String> selfAndAncestors = dbyagoclasses.getSelfAndAncestors(page.getYagoClasses().get(i));

                for(String yagoClass : selfAndAncestors) {
                    if (!this.yagoClasses.contains(yagoClass)) {
                        this.yagoClasses.add(yagoClass);
                    }
                }
            }
        }
    }

    private void hierarchiesIntersectionWithPage(DBPage page, DBCategoriesManager dbCategoriesManager, DBOntologiesManager dbontologies,
                                                 DBYagoClassesManager dbyagoclasses) {

    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        if(depth >= -1) {
            this.depth = depth;
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

    public ArrayList<PediaConcept> getChildren() {
        return this.children;
    }

    public void addParent(PediaConcept parent) {
        this.parents.add(parent);
    }

    public void addChild(PediaConcept child) {
        this.children.add(child);
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
