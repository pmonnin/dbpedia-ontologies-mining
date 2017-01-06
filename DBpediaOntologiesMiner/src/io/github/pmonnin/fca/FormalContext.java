package io.github.pmonnin.fca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent a formal context
 * @author Pierre Monnin
 */
public class FormalContext {
    private Map<String, Integer> objects;
    private List<String> objectsNames;
    private Map<String, Integer> attributes;
    private List<String> attributesNames;
    private Map<Integer, List<Integer>> incidence;

    public FormalContext() {
        objects = new HashMap<>();
        objectsNames = new ArrayList<>();
        attributes = new HashMap<>();
        attributesNames = new ArrayList<>();
        incidence = new HashMap<>();
    }

    public void add(String object, String attribute) {
        if (!objects.containsKey(object)) {
            objects.put(object, objects.size());
            objectsNames.add(object);
            incidence.put(objects.get(object), new ArrayList<Integer>());
        }

        if (!attributes.containsKey(attribute)) {
            attributes.put(attribute, attributes.size());
            attributesNames.add(attribute);
        }

        incidence.get(objects.get(object)).add(attributes.get(attribute));
    }

    public int getObjectsNumber() {
        return objects.size();
    }

    public String getObject(int index) {
        return objectsNames.get(index);
    }

    public int getAttributesNumber() {
        return attributes.size();
    }

    public String getAttribute(int index) {
        return attributesNames.get(index);
    }

    public List<Integer> getObjectIncidence(int index) {
        return new ArrayList<>(incidence.get(index));
    }
}
