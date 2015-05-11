package org.n52.v3d.worldviz.featurenet;

import java.util.Collection;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * The VgFeatureNet interface allows to operate on sets of interrelated geo-objects ("features" in OGC-jargon).
 * 
 * The kind of relations between geo-objects will depend on your application scenario. Prominent examples are:
 * Flows of trade between regions, flight connections between airports, or the relation "same official language",
 * which would connect Australia and the United States.
 * 
 * From a mathematical perspective, a VgFeatureNet describes a graph structure consisting of vertices and edges.
 * It is left to the concrete application, whether a VgFeatureNet implementation describes directed or undirected
 * graphs and whether weights will be given for the edges.
 * 
 * However, VgFeatureNet structures allow to put a strong focus on the relations between spatial features (instead 
 * of deriving the relations from the features' attribute values).
 * 
 * @author Benno Schmidt
 *
 */
public interface VgFeatureNet {

	/**
	 * TODO
	 * @return
	 */
	public Collection<VgFeature> getFeatures();
	
	/**
	 * TODO
	 * @return
	 */
	public Collection<VgRelation> getRelations();
}
