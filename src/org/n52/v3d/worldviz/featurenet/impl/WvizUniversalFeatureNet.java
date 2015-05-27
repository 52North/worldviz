package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.VgRelation;

/**
 * Feature net implementation to hold nets consisting of geo-objects and arbitrary connections in between.
 * From the mathematical perspective, the edges inside a <tt>WvizUniversalFeatureNet</tt> might be directed or undirected,
 * with or without (quantitative or nominally scaled) weights.
 * 
 * @author Benno Schmidt
 */
public class WvizUniversalFeatureNet extends VgFeatureNet {
		
	protected ArrayList<VgFeature> features;
	protected ArrayList<VgRelation> relations;

	/**
	 * Constructor.
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (graph edges)
	 */
	public WvizUniversalFeatureNet(
			Collection<VgFeature> features, Collection<VgRelation> relations) 
	{
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			this.relations.add(r);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (graph edges)
	 */
	public WvizUniversalFeatureNet(
			VgFeature[] features, VgRelation[] relations) 
	{
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			this.relations.add(r);
		}
	}
	
	protected WvizUniversalFeatureNet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the net's geo-objects.
	 * 
	 * @return Set of geo-objects (graph nodes)
	 */
	public Collection<VgFeature> getFeatures() {
		return this.features;
	}
	
	/**
	 * Gets the relations between the geo-objects that are held in the net.
	 * 
	 * @return Set of geo-object relations (graph edges)
	 */
	public Collection<VgRelation> getRelations() {
		return this.relations;
	}

	/*
	public int numberOfFeatures() {
		if (this.features == null)
			return 0;
		else
			return this.features.size();
	}
	*/

	/**
	 * gets the outflows for a given feature, i.e. the graph edges directing away from a node.
	 *
	 * TODO: Note that this method is not algorithmically efficient. Later, for big numbers of 
	 * nodes and edges, we could add an extended class WvizFlowNetTopo or sth similar. 
	 * 
	 * @param feature Net node
	 * @return Array holding outflows
	 */
	public WvizFlow[] getOutFlows(VgFeature feature) {
		return this.getFlows(feature, 'o');
	}

	/**
	 * gets the influxes for a given feature, i.e. the graph edges directing to a node.
	 * 
	 * TODO: Note that this method is not algorithmically efficient. Later, for big numbers of 
	 * nodes and edges, we could add an extended class WvizFlowNetTopo or sth similar. 
	 * 
	 * @param feature Net node
	 * @return Array holding influxes
	 */
	public WvizFlow[] getInFlows(VgFeature feature) {
		return this.getFlows(feature, 'i');
	}
	
	private WvizFlow[] getFlows(VgFeature feature, char inOrOut) {
		ArrayList<WvizFlow> res = new ArrayList<WvizFlow>();
		for (VgRelation r : this.relations) {
			if (r instanceof WvizFlow) {
				if (inOrOut == 'o') {
					if (r.getFrom() == feature) {
						res.add((WvizFlow) r);
					}
				}
				else {
					if (r.getTo() == feature) {
						res.add((WvizFlow) r);
					}				
				}
			}
		}
		WvizFlow[] resArr = new WvizFlow[res.size()];
		int i = 0;
		for (WvizFlow fl : res) {
			resArr[i] = fl;
			i++;
		}
		return resArr;
	}	
}
