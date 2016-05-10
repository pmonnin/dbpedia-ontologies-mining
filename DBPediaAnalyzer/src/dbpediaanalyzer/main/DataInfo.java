package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.*;
import dbpediaanalyzer.factory.HierarchiesFactory;

import java.util.List;
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
        System.out.println("Please wait... Setting up...");
        System.out.println("\t Querying and parsing DBPedia hierarchies...");
        HierarchiesManager hm = HierarchiesFactory.createHierarchies();
        System.out.println("\t Loaded " + hm.getCategoriesNumber() + " DBPedia categories");
        System.out.println("\t Loaded " + hm.getOntologyClassesNumber() + " DBPedia ontology classes");
        System.out.println("\t Loaded " + hm.getYagoClassesNumber() + " DBPedia yago classes");

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
                    findPathBetweenTwoElements(scanner, hm);
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
        System.out.println("--- Display hierarchy element info ---");
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

    private static void findPathBetweenTwoElements(Scanner scanner, HierarchiesManager hm) {
        System.out.println("--- Find path between two hierarchy element ---");
        System.out.println("Enter the bottom hierarchy element URI");
        String uriBottom = scanner.nextLine();
        System.out.println("Enter the top hierarchy element URI");
        String uriTop = scanner.nextLine();

        HierarchyElement top = null, bottom = null;

        if(hm.getCategoryFromUri(uriBottom) != null && hm.getCategoryFromUri(uriTop) != null) {
            bottom = hm.getCategoryFromUri(uriBottom);
            top = hm.getCategoryFromUri(uriTop);
        }

        else if(hm.getOntologyClassFromUri(uriBottom) != null && hm.getOntologyClassFromUri(uriTop) != null) {
            bottom = hm.getOntologyClassFromUri(uriBottom);
            top = hm.getOntologyClassFromUri(uriTop);
        }

        else if(hm.getYagoClassFromUri(uriBottom) != null && hm.getYagoClassFromUri(uriTop) != null) {
            bottom = hm.getYagoClassFromUri(uriBottom);
            top = hm.getYagoClassFromUri(uriTop);
        }

        if(top == null || bottom == null) {
            System.out.println("Elements weren't found or weren't from the same hierarchy");
        }

        else {
            List<HierarchyElement> path = bottom.findPathTo(top);

            if(path.isEmpty()) {
                System.out.println("Path couldn't be found. Top element isn't an ancestor of bottom element.");
            }

            else {
                System.out.println("Path:");
                for(int i = 0 ; i < path.size() ; i++) {
                    System.out.print(path.get(i).getUri());

                    if(i < path.size() - 1) {
                        System.out.print(" -> ");
                    }
                }

                System.out.print("\n");
            }
        }
    }
}
