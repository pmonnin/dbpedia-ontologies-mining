package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import dbpediaobjects.DBCategory;

/**
 * Class computing statistics over the DB Categories hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBCategoriesStatistics {
	private HashMap<String, DBCategory> categories;
	private int categoriesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBCategoriesStatistics(HashMap<String, DBCategory> categories) {
		this.categories = categories;
		this.categoriesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Categories number
		this.categoriesNumber = this.categories.size();
		
		// Orphans number, direct subsumptions number,
		Set<String> keys = this.categories.keySet();
		ArrayList<String> orphans = new ArrayList<String>();
		for(String key : keys) {
			// Children relationship creation
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory parentCat = this.categories.get(parent);
				
				if(parentCat != null)
					parentCat.addChild(key);
			}
			
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
		}
		
		System.out.println("Compute depth");
		computeDepth(orphans);
		System.out.println("End compute depth");
	}
	
	public void displayStatistics() {
		System.out.println("== DBPedia categories statistics ==");
		System.out.println("Classes number: " + this.categoriesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth(ArrayList<String> orphans) {
		for(String key : orphans) {
			int currentDepth = 0;
			LinkedList<String> stack = new LinkedList<String>();
			
			// Algorithm initialization
			for(String category : this.categories.keySet()) {
				this.categories.get(category).setDepth(-1);
			}
			this.categories.get(key).setDepth(0);
			stack.push(key);
			
			// Algorithm computation
			while(!stack.isEmpty()) {
				String cat = stack.pollFirst();
				DBCategory dbCat = this.categories.get(cat);
				
				if(dbCat != null) {
					boolean childrenModified = false;
					for(String child : dbCat.getChildren()) {
						DBCategory childClass = this.categories.get(child);
						
						if(childClass != null && childClass.getDepth() == -1) {
							childClass.setDepth(dbCat.getDepth() + 1);
							stack.add(child);
							childrenModified = true;
						}
					}
					
					if(!childrenModified && dbCat.getDepth() > currentDepth) {
						currentDepth = dbCat.getDepth();
					}
					
				}
			}
			
			if(currentDepth > this.depth) {
				this.depth = currentDepth;
				System.out.println(this.depth);
			}
		}
	}
}
