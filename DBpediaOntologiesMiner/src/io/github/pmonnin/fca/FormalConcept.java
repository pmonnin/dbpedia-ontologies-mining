package io.github.pmonnin.fca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent a formal concept
 * Store index of objects and attributes. Actual String can be obtained from FormalLattice class
 * @author Pierre Monnin
 */
public class FormalConcept {
    private List<Integer> extent;
    private List<Integer> intent;
    private List<FormalConcept> parents;
    private List<FormalConcept> children;
    private Map<String, List<Integer>> annotations;

    public FormalConcept() {
        this.extent = new ArrayList<>();
        this.intent = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.annotations = new HashMap<>();
    }

    public void addParent(FormalConcept parent) {
        if (!parents.contains(parent))
            parents.add(parent);
    }

    public void addChild(FormalConcept child) {
        if (!children.contains(child))
            children.add(child);
    }

    public void addExtendObject(int index) {
        if (!extent.contains(index))
            extent.add(index);
    }

    public void addIntentAttribute(int index) {
        if (!intent.contains(index))
            intent.add(index);
    }

    public int getParentsNumber() {
        return parents.size();
    }

    public List<FormalConcept> getParents() {
        return new ArrayList<>(parents);
    }

    public int getChildrenNumber() {
        return children.size();
    }

    public List<FormalConcept> getChildren() {
        return new ArrayList<>(children);
    }

    public int getExtentSize() {
        return extent.size();
    }

    public int getIntentSize() {
        return intent.size();
    }

    public int getExtentObject(int index) {
        if (index >= 0 && index < extent.size())
            return extent.get(0);

        return -1;
    }

    public void addAnnotation(String annotationName) {
        if (!annotations.containsKey(annotationName))
            annotations.put(annotationName, new ArrayList<Integer>());
    }

    public void addAnnotationObject(String annotationName, int annotationObject) {
        if (!annotations.containsKey(annotationName)) {
            annotations.put(annotationName, new ArrayList<Integer>());
        }

        if (!annotations.get(annotationName).contains(annotationObject)) {
            annotations.get(annotationName).add(annotationObject);
        }
    }

    public List<Integer> getAnnotation(String annotation) {
        return new ArrayList<>(annotations.get(annotation));
    }

}
