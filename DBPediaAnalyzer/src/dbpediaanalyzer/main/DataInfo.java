package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.dbpediaobject.HierarchyElement;
import dbpediaanalyzer.factory.HierarchiesFactory;

import java.util.Scanner;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataInfo {


    public static void main(String[] args) {
        System.out.println("=== DATA INFO ===");
        System.out.println("Please wait... Setting up... Querying hierarchies from server...");
        HierarchiesManager hm = HierarchiesFactory.createHierarchies();
        System.out.println("Loaded " + hm.getCategoriesNumber() + " DBPedia categories");
        System.out.println("Loaded " + hm.getOntologyClassesNumber() + " DBPedia ontology classes");
        System.out.println("Loaded " + hm.getYagoClassesNumber() + " DBPedia yago classes");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while(!exit) {
            displayMenu();

            switch(scanner.nextInt()) {
                case 1:
                    scannerClear(scanner);
                    displayHierarchyElementInfo(scanner, hm);
                    break;

                case 2:
                    scannerClear(scanner);
                    findPathBetweenTwoElements();
                    break;

                case 3:
                    System.out.println("Bye!");
                    exit = true;
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("=== DATA INFO MAIN MENU ===");
        System.out.println("1. Display hierarchy element info");
        System.out.println("2. Find path between two hierarchy elements");
        System.out.println("3. Quit");
    }

    private static void scannerClear(Scanner scanner) {
        scanner.nextLine();
    }

    private static void displayHierarchyElementInfo(Scanner scanner, HierarchiesManager hm) {
        System.out.println("--- Display Hierarchy Element ---");
        System.out.println("Enter the hierarchy element URI");

        String uri = scanner.nextLine();
        HierarchyElement element = null;

        if(hm.getCategoryFromUri(uri) != null) {
            element = hm.getCategoryFromUri(uri);
        }

        else if(hm.getOntologyClassFromUri(uri) != null) {
            element = hm.getOntologyClassFromUri(uri);
        }

        else if(hm.getYagoClassFromUri(uri) != null) {
            element = hm.getYagoClassFromUri(uri);
        }

        if(element != null) {
            System.out.println("Result: ");
            System.out.println("URI: " + element.getUri());

            System.out.println("Children: ");
            for(HierarchyElement child : element.getChildren()) {
                System.out.println(child.getUri());
            }

            System.out.println("Parents: ");
            for(HierarchyElement parent : element.getParents()) {
                System.out.println(parent.getUri());
            }
        }

        else {
            System.out.println("Element " + uri + " wasn't found...");
        }
    }

    private static void findPathBetweenTwoElements() {

    }
}
