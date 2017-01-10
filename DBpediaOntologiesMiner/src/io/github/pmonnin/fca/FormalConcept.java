package io.github.pmonnin.fca;

import java.util.ArrayList;
import java.util.List;

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

    public FormalConcept() {
        this.extent = new ArrayList<>();
        this.intent = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
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

    public int getChildrenNumber() {
        return children.size();
    }

}
