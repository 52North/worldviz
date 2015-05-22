package org.n52.v3d.worldviz.projections.dymaxion;

import java.text.*;

/**
 * Implementation helper class
 * 
 * @author Benno Schmidt
 *
 */
public class Point2D 
{
	public double x;
	public double y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public String sx() {
		return new DecimalFormat("#.###").format(x);
	}

	public String sy() {
		return new DecimalFormat("#.###").format(y);
	}
}
