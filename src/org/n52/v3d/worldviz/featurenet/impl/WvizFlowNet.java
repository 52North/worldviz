package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.VgFlow;
import org.n52.v3d.worldviz.featurenet.VgRelation;

/**
 * Special feature net implementation to hold nets consisting of geo-objects and flows in between.
 * From the mathematical perspective, a <tt>WvizFlowNet</tt> corresponds to an edge-weighted directed graph structure.
 * 
 * @author Benno Schmidt
 */
public class WvizFlowNet extends VgFeatureNet {
		
	private ArrayList<VgFeature> features;
	private ArrayList<VgRelation> relations;

	/**
	 * Constructor.
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (graph edges)
	 */
	public WvizFlowNet(
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
	public WvizFlowNet(
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
	
	/**
	 * Gets the flow net's geo-objects.
	 * 
	 * @return Set of geo-objects (graph nodes)
	 */
	public Collection<VgFeature> getFeatures() {
		return this.features;
	}
	
	/**
	 * Gets the flows between the geo-objects that are held in the flow net.
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
	 * @param feature Flow net node
	 * @return
	 */
	public VgFlow[] getOutFlows(VgFeature feature) {
		return this.getFlows(feature, 'o');
	}

	/**
	 * gets the influxes for a given feature, i.e. the graph edges directing to a node.
	 * 
	 * TODO: Note that this method is not algorithmically efficient. Later, for big numbers of 
	 * nodes and edges, we could add an extended class WvizFlowNetTopo or sth similar. 
	 * 
	 * @param feature Flow net node
	 * @return
	 */
	public VgFlow[] getInFlows(VgFeature feature) {
		return this.getFlows(feature, 'i');
	}
	
	private VgFlow[] getFlows(VgFeature feature, char inOrOut) {
		ArrayList<VgFlow> res = new ArrayList<VgFlow>();
		for (VgRelation r : this.relations) {
			if (inOrOut == 'o') {
				if (r.getFrom() == feature) {
					res.add((VgFlow) r);
				}
			}
			else {
				if (r.getTo() == feature) {
					res.add((VgFlow) r);
				}				
			}
		}
		VgFlow[] resArr = new VgFlow[res.size()];
		int i = 0;
		for (VgFlow fl : res) {
			resArr[i] = fl;
			i++;
		}
		return resArr;
	}	
}
