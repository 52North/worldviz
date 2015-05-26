package org.n52.v3d.worldviz.featurenet.impl;

import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.VgRelation;

/**
 * A VgFlow describes a directed, weighted relation between two geo-objects ("features").
 * 
 * @author Benno Schmidt
 */
public class WvizFlow implements VgRelation {
	
	private VgFeature from, to;
	private double flowRate;

	public WvizFlow(VgFeature from, VgFeature to, double flowRate) {
		this.from = from;
		this.to = to;
		this.flowRate = flowRate;
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
		return new Double(this.flowRate);
	}
	
	public String toString() {
		return "[" + this.from + " -> " + this.to + ", flow rate = " + this.flowRate + "]";
				
	}
}
