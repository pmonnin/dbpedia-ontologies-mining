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
                    scannerClear(scanner);
                    displayLCAOfTwoHelements(scanner, hm);
                    break;

                case 4:
                    scannerClear(scanner);
                    displayDistanceFromClosestTopLevelClass(scanner, hm);
                    break;

                case 5:
                    System.out.println("Bye!");
                    exit = true;
                    break;

                default:
                    System.out.println("Choice not available");
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("=== DATA INFO MAIN MENU ===");
        System.out.println("1. Display hierarchy element info");
        System.out.println("2. Find path between two hierarchy elements");
        System.out.println("3. Display Lower Common Ancestor of two hierarchy elements");
        System.out.println("4. Display distance from closest top level class of a hierarchy element");
        System.out.println("5. Quit");
    }

    private static void scannerClear(Scanner scanner) {
        scanner.nextLine();
    }

    private static void displayHierarchyElementInfo(Scanner scanner, HierarchiesManager hm) {
        System.out.println("--- Display hierarchy element info ---");
        System.out.println("Enter the hierarchy element URI");

        String uri = scanner.nextLine();
        HierarchyElement element = fromURIToHierarchyElement(uri, hm);

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
        System.out.println("--- Find path between two hierarchy elements ---");
        System.out.println("Enter the bottom hierarchy element URI");
        String uriBottom = scanner.nextLine();
        System.out.println("Enter the top hierarchy element URI");
        String uriTop = scanner.nextLine();

        HierarchyElement top = fromURIToHierarchyElement(uriTop, hm), bottom = fromURIToHierarchyElement(uriBottom, hm);

        if(!checkElementsNotNullAndSameType(top, bottom)) {
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

    private static void displayLCAOfTwoHelements(Scanner scanner, HierarchiesManager hm) {
        System.out.println("--- Display LCA of two hierarchy elements ---");
        System.out.println("Enter first hierarchy element URI");
        String uri1 = scanner.nextLine();
        System.out.println("Enter second hierarchy element URI");
        String uri2 = scanner.nextLine();

        HierarchyElement element1 = fromURIToHierarchyElement(uri1, hm);
        HierarchyElement element2 = fromURIToHierarchyElement(uri2, hm);

        if(checkElementsNotNullAndSameType(element1, element2)) {
            HierarchyElement lca = element1.getLowestCommonAncestor(element2);

            if(lca != null) {
                System.out.println("LCA: " + lca.getUri());
            }

            else {
                System.out.println("LCA: owl:Thing");
            }
        }

        else {
            System.out.println("Elements weren't found or weren't from the same hierarchy");
        }
    }

    private static void displayDistanceFromClosestTopLevelClass(Scanner scanner, HierarchiesManager hm) {
        System.out.println("--- Display distance from closest top level class ---");
        System.out.println("Enter the origin hierarchy element URI");
        String elementURI = scanner.nextLine();

        HierarchyElement element = fromURIToHierarchyElement(elementURI, hm);

        if(element != null) {
            System.out.println("Distance from closest top level class: " + element.getDistanceFromClosestTopLevelClass());
        }

        else {
            System.out.println("Element " + elementURI + " wasn't found...");
        }
    }

    private static HierarchyElement fromURIToHierarchyElement(String elementURI, HierarchiesManager hm) {
        if(hm.getCategoryFromUri(elementURI) != null) {
            return hm.getCategoryFromUri(elementURI);
        }

        if(hm.getOntologyClassFromUri(elementURI) != null) {
            return hm.getOntologyClassFromUri(elementURI);
        }

        if(hm.getYagoClassFromUri(elementURI) != null) {
            return hm.getYagoClassFromUri(elementURI);
        }

        return null;
    }

    private static boolean checkElementsNotNullAndSameType(HierarchyElement element1, HierarchyElement element2) {
        if(element1 == null || element2 == null) {
            return false;
        }

        if(element1 instanceof Category && element2 instanceof Category) {
            return true;
        }

        if(element1 instanceof OntologyClass && element2 instanceof OntologyClass) {
            return true;
        }

        if(element1 instanceof YagoClass && element2 instanceof YagoClass) {
            return true;
        }

        return false;
    }
}
