package colibri.io.lattice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import colibri.lib.Edge;
import colibri.lib.Lattice;
import colibri.lib.Traversal;


/**
 * Class for exporting the edges of a concept lattice to an .xml file.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class LatticeWriterXmlEdges {
	public void write (Lattice lattice, File file) throws IOException {
		write(lattice, file, Traversal.TOP_ATTR);
	}
	
	
	public void write (Lattice lattice, File file, Traversal traversal) throws IOException {
		FileWriter writer = new FileWriter(file);
		
		try {
			Iterator<Edge> edgeIterator = lattice.edgeIterator(traversal);
			
			writer.write("<?xml version=\"1.0\" ?>\n<lattice type=\"edges_only\">\n");
			
			while(edgeIterator.hasNext()) {
				Edge edge = edgeIterator.next();
				
				writer.write("<edge>\n<upper_concept>\n");
				
				Iterator<Comparable> it;
				it = edge.getUpper().getObjects().iterator();
				while (it.hasNext()) {
					writer.write("<object name=\"" + it.next() + "\"></object>\n");
				}
				
				it = edge.getUpper().getAttributes().iterator();
				while (it.hasNext()) {
					writer.write("<attribute name=\"" + it.next() + "\"></attribute>\n");
				}
				
				writer.write("</upper_concept>\n<lower_concept>\n");
				
				it = edge.getLower().getObjects().iterator();
				while (it.hasNext()) {
					writer.write("<object name=\"" + it.next() + "\"></object>\n");
				}
				
				it = edge.getLower().getAttributes().iterator();
				while (it.hasNext()) {
					writer.write("<attribute name=\"" + it.next() + "\"></attribute>\n");
				}
				
				writer.write("</lower_concept>\n</edge>\n");
			}
			
			writer.write("</lattice>");
		} finally {
			writer.close();			
		}
	}
}
