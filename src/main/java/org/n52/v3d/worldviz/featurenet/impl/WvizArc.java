package org.n52.v3d.worldviz.featurenet.impl;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * A <tt>WvizArc</tt> describes an directed relation between two geo-objects ("features").
 * Note that such an "arc" might consist of a nominally scaled edge-value.
 * 
 * @author Benno Schmidt
 */
public class WvizArc implements VgRelation {
	
	private VgFeature from, to;
	private Object value = null;

	/**
	 * Constructor for an connection (without edge-value).
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 */
	public WvizArc(VgFeature from, VgFeature to) {
		this.from = from;
		this.to = to;
		this.value = null;
	}

	/**
	 * Constructor for a connection with edge-value.
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 * @param value Edge value
	 */
	public WvizArc(VgFeature from, VgFeature to, Object value) {
		this.from = from;
		this.to = to;
		this.value = value;
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
		return true;
	}
		 
	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
		if (this.value == null)
			return "[" + this.from + " -> " + this.to + "]";
		else
			return "[" + this.from + " -> " + this.to + ", weight = " + this.value.toString() + "]";				
	}
}
