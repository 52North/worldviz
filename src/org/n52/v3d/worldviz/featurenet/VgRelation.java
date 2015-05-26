package org.n52.v3d.worldviz.featurenet;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * A VgRelation holds a relation between two geo-objects ("features" in OGC-jargon).
 * 
 * The kind of relations between geo-objects will depend on your application scenario. Prominent examples are:
 * Flows of trade between regions, flight connections between airports, or the relation "same official language",
 * which would connect Australia and the United States.
 * 
 * <table border='1'>
 *   <tr><td>edge-values:</td><td>not present</td><td>nominally scaled value</td><td>quantitative values</td></tr>
 *   <tr><td>undirected</td><td><tt>WvizConnection</tt></td><td>not yet implemented</td><td><tt>WvizConnection</tt></td></tr>
 *   <tr><td>directed</td><td><tt>WvizArc</tt></td><td><tt>WvizArc</tt></td><td><tt>WvizFlow</tt></td></tr>
 * </table>
 * 
 * @author Benno Schmidt
 */
public interface VgRelation 
{
	/**
	 * gets the first of the two related geo-objects. 
	 * For directed relations (<tt>isDirected() == true</tt>), this object gives the from-node inside the 
	 * corresponding feature graph.
	 *  
	 * @return Geo-object
	 */
	 public VgFeature getFrom();

	/**
	 * gets the second of the two related geo-objects.
	 * For directed relations (<tt>isDirected() == true</tt>), this object gives the to-node inside the 
	 * corresponding feature graph.
	 *  
	 * @return Geo-object
	 */
	 public VgFeature getTo();
	 
	 /**
	  * gets the information whether the relation is directed.
	  * @return <i>true</i> if the relation directs from the first to the second geo-object njode, else <i>false</i>
	  */
	 public boolean isDirected();
	 
	 /**
	  * TODO e.g., a weight-value
	  * TODO: I don't really like this method signature and name yet...
	  * @return
	  */
	 public Object getValue();
}
