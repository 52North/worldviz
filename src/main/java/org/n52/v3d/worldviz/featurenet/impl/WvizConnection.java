package org.n52.v3d.worldviz.featurenet.impl;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * A <tt>WvizConnection</tt> describes an undirected relation between two geo-objects ("features").
 * Note that such a connection might consist of an edge-weight.
 * TODO: So far, only scalar-valued edge-weights have been implemented.
 * 
 * @author Benno Schmidt
 */
public class WvizConnection implements VgRelation {
	
	private VgFeature from, to;
	private boolean hasWeight = false;
	private double weight;

	/**
	 * Constructor for an unweighted connection.
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 */
	public WvizConnection(VgFeature from, VgFeature to) {
		this.from = from;
		this.to = to;
		this.hasWeight = false;
	}

	/**
	 * Constructor for a weighted connection.
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 * @param weight Scalar value giving the edge weight
	 */
	public WvizConnection(VgFeature from, VgFeature to, double weight) {
		this.from = from;
		this.to = to;
		this.hasWeight = true;
		this.weight = weight;
	}

	public void setFrom(VgFeature from) {
		this.from = from;
	}

	public void setTo(VgFeature to) {
		this.to = to;
	}

	public VgFeature getFrom() {
		return from;
	}

	public VgFeature getTo() {
		return to;
	}

	public boolean isDirected() {
		return false;
	}
		 
	public Object getValue() {
		if (! this.hasWeight)
			return null;
		else
			return new Double(this.weight);
	}
	
	public String toString() {
		if (! this.hasWeight)
			return "[" + this.from + " <-> " + this.to + "]";
		else
			return "[" + this.from + " <-> " + this.to + ", weight = " + this.weight + "]";				
	}
}
