package org.n52.v3d.worldviz.projections.dymaxion;

/**
 * Implementation helper class
 * 
 * @author Benno Schmidt
 *
 */
public class PointPolar 
{
	double theta;
	double phi;
	
	public PointPolar(double theta, double phi) {
		this.theta = theta;
		this.phi = phi;
	}
	
	public String toString() {
		return "[polar (" + theta + ", " + phi + ")]";
	}
}
