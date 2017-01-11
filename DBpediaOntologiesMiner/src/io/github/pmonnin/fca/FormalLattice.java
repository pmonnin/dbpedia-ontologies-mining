package io.github.pmonnin.fca;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent the formal lattice
 * Store the actual String for objects and attributes (FormalConcept class only store indexes)
 * @author Pierre Monnin
 */
public class FormalLattice {
    private FormalConcept[] concepts;
    private Map<Integer, String> objects;
    private Map<Integer, String> attributes;
    private Map<Integer, String> annotationsObjects;
    private Map<String, Integer> rAnnotationsObjects;
    private int indexTop;
    private int indexBottom;

    public FormalLattice(int numberOfConcepts, int indexTop, int indexBottom) {
        concepts = new FormalConcept[numberOfConcepts];
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
        this.objects = new HashMap<>();
        this.attributes = new HashMap<>();
        this.annotationsObjects = new HashMap<>();
        this.rAnnotationsObjects = new HashMap<>();
    }

    public void addConcept(int index, FormalConcept concept) {
        if (index >= 0 && index < concepts.length)
            concepts[index] = concept;
    }

    public void addArc(int indexSub, int indexSuper) {
        if (indexSub >= 0 && indexSub < concepts.length && indexSub >= 0 && indexSub < concepts.length) {
            concepts[indexSub].addParent(concepts[indexSuper]);
            concepts[indexSuper].addChild(concepts[indexSub]);
        }
    }

    public void addObject(int index, String object) {
        objects.put(index, object);
    }

    public void addAttribute(int index, String attribute) {
        attributes.put(index, attribute);
    }

    public FormalConcept getTop() {
        return concepts[indexTop];
    }

    public FormalConcept getBottom() {
        return concepts[indexBottom];
    }

    public int getObjectsNumber() {
        return this.objects.size();
    }

    public int getAttributesNumber() {
        return this.attributes.size();
    }

    public String getObject(int index) {
        if (index >= 0 && index < objects.size())
            return objects.get(index);

        return "";
    }

    public String getAttribute(int index) {
        if (index >= 0 && index < attributes.size())
            return attributes.get(index);

        return "";
    }

    public int getConceptsNumber() {
        return concepts.length;
    }

    public FormalConcept getConcept(int index) {
        if (index >= 0 && index < concepts.length)
            return concepts[index];

        return null;
    }

    public int getAnnotationObjectIndex(String annotationObject) {
        if (!rAnnotationsObjects.containsKey(annotationObject)) {
            annotationsObjects.put(annotationsObjects.size(), annotationObject);
            rAnnotationsObjects.put(annotationObject, rAnnotationsObjects.size());
        }

        return rAnnotationsObjects.get(annotationObject);
    }

    public String getAnnotationObject(int index) {
        return annotationsObjects.get(index);
    }
}
