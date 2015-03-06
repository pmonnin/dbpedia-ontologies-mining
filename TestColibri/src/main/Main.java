package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import latticecreation.PediaLattice;

import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;

public class Main {

    // Ontology :
    // http://wiki.dbpedia.org/Ontology
    // http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
    // http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
    // http://dbpedia.org/ontology/

    public static void main(String[] args) throws ParseException, IOException {
        Date startDate = new Date();
        
        // Crawling DB categories
        System.out.println("DEBUT PARSAGE CATEGORIES");
        DBCategoriesCrawler dbCategoriesCrawler = new DBCategoriesCrawler();
        dbCategoriesCrawler.computeParents();
        HashMap<String, DBCategory> dbcategories = dbCategoriesCrawler.getDbcategories();

        // Crawling DB ontologies
        System.out.println("DEBUT PARSAGE ONTOLOGIES");
        DBOntologyClassesCrawler dbOntologyClasses = new DBOntologyClassesCrawler();
        dbOntologyClasses.computeParents();
        HashMap<String, DBOntologyClass> dbontologies = dbOntologyClasses.getDbontologies();

        // We create the lattice
        System.out.println("DEBUT CREATION LATTICE");
        PediaLattice lattice = new PediaLattice();
        // lattice.deleteFirstIterationAttributes();
        ArrayList<PediaConcept> lc = lattice.execIterator();

//        for (PediaConcept c : lc) {
//            System.out.println("/***********Concept************/");
//            for (String obj : c.getListeObjets())
//                System.out.println("objet: " + obj);
//            System.out.println("\n");
//
//            for (String cat : c.getCategories())
//                System.out.println("catégorie: " + cat);
//            System.out.println("\n");
//
//            for (PediaConcept par : c.getParents()) {
//                for (String op : par.getListeObjets())
//                    System.out.println("objet du parent: " + op);
//            }
//            System.out.println("/***********Fin concept*********/\n\n");
//        }

        /* Pour chaque concept */
        for (PediaConcept c : lc) {
            /* Récupérer l'union des parents des ontologies */
            ArrayList<String> latticeOntologyParents = c.unionOntologiesParent();
            ArrayList<String> latticeOntologies = c.getOntologies();

            /* Pour chaque ontologie du concept */
            for (String ontoChild : latticeOntologies) {
                for (String ontoParent : latticeOntologyParents) {
                    if (dbontologies.get(ontoChild).hasParent(ontoParent)) {
//                        System.out.println("La relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) a bien été trouvée dans les classes d'ontologie.");
                    } else {
//                        System.out.println("Il manque la relation " + ontoChild + " (enfant) -> " + ontoParent + " (parent) dans les classes d'ontologie.");
                    }
                }
            }

            /* Récupérer l'union des parents des catégories */
            ArrayList<String> latticeCategoryParents = c.unionCategoriesParent();
            ArrayList<String> latticeCategories = c.getCategories();

            /* Pour chaque catégories du concept */
            for (String cateChild : latticeCategories) {
                for (String cateParent : latticeCategoryParents) {
                    if (dbcategories.get(cateChild).hasParent(cateParent)) {
//                        System.out.println("La relation " + cateChild + " (enfant) -> " + cateParent + " (parent) a bien été trouvée dans les catégories.");
                    } else {
//                        System.out.println("Il manque la relation " + cateChild + " (enfant) -> " + cateParent + " (parent) dans les catégories.");
                    }
                }
            }
        }
        
        System.out.println("FIN DU PROGRAMME EN : " + (new Date().getTime() - startDate.getTime()) + " SECONDES.");
    }
}
