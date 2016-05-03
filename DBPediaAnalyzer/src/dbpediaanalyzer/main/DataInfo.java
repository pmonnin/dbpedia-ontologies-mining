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
        System.out.println("=== DATA INFO  ===");
        System.out.println("Please wait... Setting up... Querying hierarchies from server...");
        HierarchiesManager hm = HierarchiesFactory.createHierarchies();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while(!exit) {
            displayMenu();

            switch(scanner.nextInt()) {
                case 1:
                    scannerClear(scanner);
                    displayHierarchyElement(scanner, hm);
                    break;

                case 2:
                    System.out.println("Bye!");
                    exit = true;
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("=== DATA INFO MAIN MENU ===");
        System.out.println("1. Display Hierarchy Element");
        System.out.println("2. Quit");
    }

    private static void scannerClear(Scanner scanner) {
        scanner.nextLine();
    }

    private static void displayHierarchyElement(Scanner scanner, HierarchiesManager hm) {
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
            System.out.println("Children: " + element.getChildren());
            System.out.println("Parents: " + element.getParents());
        }

        else {
            System.out.println("Element " + uri + " wasn't found...");
        }
    }
}
