package colibri.io.lattice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import colibri.lib.Edge;
import colibri.lib.Lattice;
import colibri.lib.Traversal;


/**
 * Class for exporting a lattice to a .dot file. The current implementation
 * is suitable for small lattices only.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class LatticeWriterDot {
	public void write(Lattice lattice, File file, String setSeparator, String elementSeparator) throws IOException {
		write(lattice, file, setSeparator, elementSeparator, Traversal.TOP_ATTR);
	}
	
	
	public void write(Lattice lattice, File file, String setSeparator, String elementSeparator, Traversal traversal) throws IOException {
		FileWriter writer = new FileWriter(file);
		
		try {
			Iterator<Edge> edgeIterator = lattice.edgeIterator(traversal);
			
			writer.write("digraph lattice {\n");
			
			while(edgeIterator.hasNext()) {
				Edge edge = edgeIterator.next();
				StringBuffer buffer = new StringBuffer();
				
				buffer.append("\"");
				
				Iterator upperObjectIterator = edge.getUpper().getObjects().iterator();
				
				while (upperObjectIterator.hasNext()) {
					buffer.append(upperObjectIterator.next().toString());
					
					if (upperObjectIterator.hasNext()) {
						buffer.append(elementSeparator);
					}
				}
				
				buffer.append(setSeparator);
				
				Iterator upperAttributeIterator = edge.getUpper().getAttributes().iterator();
				
				while (upperAttributeIterator.hasNext()) {
					buffer.append(upperAttributeIterator.next().toString());
					
					if (upperAttributeIterator.hasNext()) {
						buffer.append(elementSeparator);
					}
				}
				
				buffer.append("\"->\"");
				
				Iterator lowerObjectIterator = edge.getLower().getObjects().iterator();
				
				while (lowerObjectIterator.hasNext()) {
					buffer.append(lowerObjectIterator.next().toString());
					
					if (lowerObjectIterator.hasNext()) {
						buffer.append(elementSeparator);
					}
				}
				
				buffer.append(setSeparator);
				
				Iterator lowerAttributeIterator = edge.getLower().getAttributes().iterator();
				
				while (lowerAttributeIterator.hasNext()) {
					buffer.append(lowerAttributeIterator.next().toString());
					
					if (lowerAttributeIterator.hasNext()) {
						buffer.append(elementSeparator);
					}
				}
				
				buffer.append("\"\n");
				writer.write(buffer.toString());
			}
			
			
			writer.write("}\n");
		} finally {
			writer.close();			
		}
	}
}
