package org.n52.v3d.worldviz.projections.dymaxion;

/**
 * Implementation helper class
 * 
 * @author Benno Schmidt
 *
 */
public class Point3D 
{
	public double x;
	public double y;
	public double z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public double abs() {
		return Math.sqrt(x * x + y * y + z * z);	
	}
	
	public Point3D getScaled(double factor) {
		return new Point3D(x * factor, y * factor, z * factor);
	}
	
	public String toString() {
		return "(" + x + ", " + y + "," + z + ")";
	}
}
