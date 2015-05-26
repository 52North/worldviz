package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.VgRelation;

/**
 * Special feature net implementation to hold nets consisting of geo-objects and flows in between.
 * From the mathematical perspective, a <tt>WvizFlowNet</tt> corresponds to an edge-weighted directed graph structure.
 * 
 * @author Benno Schmidt
 */
public class WvizFlowNet extends WvizUniversalFeatureNet {
		
	/**
	 * Constructor.
	 * Note that relations that do not specify flows (undirected, unweighted relations) will be ignored. 
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (directed, weighted graph edges)
	 */
	public WvizFlowNet(Collection<VgFeature> features, Collection<VgRelation> relations) {
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			if (r instanceof WvizFlow)
				this.relations.add(r);
		}
	}

	/**
	 * Constructor.
	 * Note that relations that do not specify flows (undirected, unweighted relations) will be ignored. 
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (directed, weighted graph edges)
	 */
	public WvizFlowNet(VgFeature[] features, VgRelation[] relations) {
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			if (r instanceof WvizFlow)
				this.relations.add(r);
		}
	}
}
