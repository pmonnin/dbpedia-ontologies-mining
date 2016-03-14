package dbpediaanalyzer.dbpediaobject;

import dbpediaanalyzer.main.HierarchiesStatistics;
import dbpediaanalyzer.statistic.HierarchyStatistics;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesManager {
    private HashMap<String, Category> categories;
    private HashMap<String, OntologyClass> ontologyClasses;
    private HashMap<String, YagoClass> yagoClasses;

    public HierarchiesManager(HashMap<String, Category> categories, HashMap<String, OntologyClass> ontologyClasses,
                              HashMap<String, YagoClass> yagoClasses) {
        this.categories = categories;
        this.ontologyClasses = ontologyClasses;
        this.yagoClasses = yagoClasses;
    }

    public Category getCategoryFromUri(String uri) {
        return this.categories.get(uri);
    }

    public OntologyClass getOntologyClassFromUri(String uri) {
        return this.ontologyClasses.get(uri);
    }

    public YagoClass getYagoClassFromUri(String uri) {
        return this.yagoClasses.get(uri);
    }

    public ArrayList<Category> getAccessibleCategories(Collection<Category> fromCategoriesSubset) {
        ArrayList<Category> retVal = new ArrayList<>();
        Collection<HierarchyElement> accessible = getAccessibleElements(fromCategoriesSubset);

        for(HierarchyElement he : accessible) {
            retVal.add((Category) he);
        }

        return retVal;
    }

    public ArrayList<OntologyClass> getAccessibleOntologyClasses(Collection<OntologyClass> fromOntologyClassesSubset) {
        ArrayList<OntologyClass> retVal = new ArrayList<>();
        Collection<HierarchyElement> accessible = getAccessibleElements(fromOntologyClassesSubset);

        for(HierarchyElement he : accessible) {
            retVal.add((OntologyClass) he);
        }

        return retVal;
    }

    public ArrayList<YagoClass> getAccessibleYagoClasses(Collection<YagoClass> fromYagoClassesSubset) {
        ArrayList<YagoClass> retVal = new ArrayList<>();
        Collection<HierarchyElement> accessible = getAccessibleElements(fromYagoClassesSubset);

        for(HierarchyElement he : accessible) {
            retVal.add((YagoClass) he);
        }

        return retVal;
    }

    private Collection<HierarchyElement> getAccessibleElements(Collection<? extends HierarchyElement> fromSubset) {
        HashMap<String, HierarchyElement> accessible = new HashMap<>();
        for(HierarchyElement he : fromSubset) {
            accessible.put(he.getUri(), he);
        }

        Queue<HierarchyElement> queue = new LinkedList<>();
        queue.addAll(fromSubset);

        while(!queue.isEmpty()) {
            HierarchyElement he = queue.poll();

            for(HierarchyElement parent : he.getParents()) {
                if(!accessible.containsKey(parent.getUri())) {
                    accessible.put(parent.getUri(), parent);
                    queue.add(parent);
                }
            }
        }

        return accessible.values();
    }

    public HierarchyStatistics getCategoriesStatistics() {
        return new HierarchyStatistics(this.categories);
    }

    public HierarchyStatistics getOntologyClassesStatistics() {
        return new HierarchyStatistics(this.ontologyClasses);
    }

    public HierarchyStatistics getYagoClassesStatistics() {
        return new HierarchyStatistics(this.yagoClasses);
    }

}
