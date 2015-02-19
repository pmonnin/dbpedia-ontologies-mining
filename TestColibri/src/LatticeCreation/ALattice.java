package LatticeCreation;

import java.util.Iterator;

import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.LatticeImpl;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;

public class ALattice {
	
	private Lattice lattice;

	public ALattice()
	{
		Relation rel = new TreeRelation();
		
		// Object 1
		AnObject object1 = new AnObject("Object1");
		object1.addAttribute("att1");
		object1.addAttribute("att12");
		object1.addToRelation(rel);
		
		// Object 2
		AnObject object2 = new AnObject("Object2");
		object2.addAttribute("att2");
		object2.addAttribute("att12");
		object2.addToRelation(rel);
		
		lattice = new HybridLattice(rel);
	}


	public void execIterator()
	{
		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);
	
		while(it.hasNext())
		{
		    Concept c = it.next();
		    
		    // While we have 2 objects in 1 concept, we display it
	        if (c.getObjects().size() >= 2)
	        {
	        	System.out.println(c);
	        }
	        // When we have less than 2 objects, we stop it
	        else
	        {
	                break;
	        }
		}
	}
	
}