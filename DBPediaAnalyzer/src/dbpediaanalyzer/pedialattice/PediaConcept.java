package dbpediaanalyzer.pedialattice;

import java.util.ArrayList;
import java.util.HashMap;

import colibri.lib.Concept;
import dbpediaanalyzer.dbpediaobjects.*;

/**
 * Class of PediaConcept object
 * 
 * @author Damien Flament
 * @author Soline Blanc
 * @author Thomas Herbeth
 * @author Pierre Monnin
 * 
 */
@Deprecated
public class PediaConcept {
    private ArrayList<String> objects;
    private ArrayList<String> attributes;
    private ArrayList<DBCategory> categories;
    private ArrayList<DBOntology> ontologies;
    private ArrayList<DBYagoClass> yagoClasses;

    private ArrayList<PediaConcept> parents;
    private ArrayList<PediaConcept> children;

    private int depth;

    public PediaConcept(Concept concept, HashMap<String, Page> dbPages, DBCategoriesManager dbcategories,
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
        
        // Concept intersection of DB Pedia categories, ontologies and yago classes associated (directly or not) to each concept's page
        HashMap<String, Page> conceptPages = new HashMap<>();
        for(String pageUri : this.objects) {
            conceptPages.put(pageUri, dbPages.get(pageUri));
        }
        this.categories = dbcategories.getDataSetCategories(conceptPages);
        this.ontologies = dbontologies.getDataSetOntologies(conceptPages);
        this.yagoClasses = dbyagoclasses.getDataSetYagoClasses(conceptPages);

        this.depth = -1;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        if(depth >= -1) {
            this.depth = depth;
        }
    }

    public ArrayList<DBCategory> getCategories() {
        return this.categories;
    }

    public ArrayList<DBOntology> getOntologies() {
        return this.ontologies;
    }
    
    public ArrayList<DBYagoClass> getYagoClasses() {
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
