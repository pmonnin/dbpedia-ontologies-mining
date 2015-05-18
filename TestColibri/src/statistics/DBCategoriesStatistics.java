package statistics;

import java.util.ArrayList;
import java.util.HashMap;
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
		// Children relationship creation
		Set<String> keys = this.categories.keySet();
		for(String key : keys) {
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory parentCat = this.categories.get(parent);
				
				if(parentCat != null)
					parentCat.addChild(key);
			}
		}
		
		// Categories number
		this.categoriesNumber = this.categories.size();
		
		// Orphans number, direct subsumptions number,
		ArrayList<String> orphans = new ArrayList<>(); 
		for(String key : keys) {			
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
		}
		
		// Compute depth
		System.out.println("Computing depth");
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				computeDepth();
			}
		});
		t1.start();
		
		// Compute inferred subsumptions
		System.out.println("Computing inferred subsumptions");
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				computeInferredSubsumptions();
			}
		});
		t.start();
	}
	
	public void displayStatistics() {
		System.out.println("== DBPedia categories statistics ==");
		System.out.println("Classes number: " + this.categoriesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth() {
		Set<String> keys = this.categories.keySet();
		boolean done = false;
		
		// Algorithm initialization
		for(String key : keys) {
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.categories.get(key).setDepth(0);
			}
		}
		
		// Algorithm computation
		int i = 0;
		while(!done) {
			done = true;
			
			for(String key : keys) {
				DBCategory cat = this.categories.get(key);
				
				for(String parent : cat.getParents()) {
					DBCategory parentCat = this.categories.get(parent);
							
					if(parentCat != null) {
						if(parentCat.getDepth() + 1 > cat.getDepth()) {
							cat.setDepth(parentCat.getDepth() + 1);
							done = false;
							
							if(cat.getDepth() > this.depth)
								this.depth = cat.getDepth();
						}
					}
				}
			}
			
			i++;
			
			if(i % 50 == 0)
				System.out.println("i: " + i + " depth: " + this.depth);
		}
		
		// Getting depth
		this.depth = 0;
		for(String key : keys) {
			if(this.categories.get(key).getDepth() > this.depth)
				this.depth = this.categories.get(key).getDepth();
		}
		
	}
	
	private void computeInferredSubsumptions() {
		Set<String> keys = this.categories.keySet();
		
		// Algorithm for each category
		int i = 0;
		for(String cat : keys) {
			System.out.println("Inferred subsumptions : " + i + " / " + keys.size());
			
			// Algorithm initialization
			for(String key : keys) {
				this.categories.get(key).setInferredSubsumptionsToken(false);
			}
			
			boolean done = false;
			// We put the token to true on every reachable category 
			while(!done) {
				done = true;
				
				for(String key : keys) {
					if(this.categories.get(key).getInferredSubsumptions()) {
						for(String parent : this.categories.get(key).getParents()) {
							DBCategory parentCat = this.categories.get(parent);
							
							if(parentCat != null) {
								if(!parentCat.getInferredSubsumptions()) {
									parentCat.setInferredSubsumptionsToken(true);
									done = false;
								}
							}
						}
					}
				}
			}
			
			// Algorithm disarming
			for(String key : keys) {
				if(this.categories.get(key).getInferredSubsumptions())
					this.inferredSubsumptions++;
			}
			this.inferredSubsumptions -= this.categories.get(cat).getParentsNumber();
			i++;
		}
	}
}
