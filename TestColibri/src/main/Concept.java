package main;

import java.util.ArrayList;


public class Concept {
    private ArrayList<String> listeObjets, listeAttributs;
    
    public Concept(ArrayList<String> objets, ArrayList<String> attributs){
        listeObjets = objets;
        listeAttributs = attributs;
    }

    public ArrayList<String> getListeObjets() {
        return listeObjets;
    }

    public void setListeObjets(ArrayList<String> listeObjets) {
        this.listeObjets = listeObjets;
    }

    public ArrayList<String> getListeAttributs() {
        return listeAttributs;
    }

    public void setListeAttributs(ArrayList<String> listeAttributs) {
        this.listeAttributs = listeAttributs;
    }
    
    
}
