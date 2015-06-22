package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

// TODO: This class could be migrated to the 52N Triturus core package.

/**
 * Scaling transformation.
 * 
 * @author Benno Schmidt
 */
public class ScaleTransform implements CoordinateTransform
{
	private T3dVector mScale;

	/**
	 * Constructor. This constructor allows to define non-uniform scalings.
	 * 
	 * @param scale Scaling vector
	 */
	public ScaleTransform(T3dVector scale) 
	{
		mScale = scale;
	}

	/**
	 * Constructor. For <tt>sx != sy</tt> and <tt>sx != sz</tt>, a non-uniform 
	 * scaling will be defined.
	 * 
	 * @param scale Scaling vector
	 */
	public ScaleTransform(double sx, double sy, double sz) 
	{
		mScale = new T3dVector(sx, sy, sz);
	}

	/**
	 * Constructor for uniform scalings.
	 * 
	 * @param scale Scaling factor
	 */
	public ScaleTransform(double factor) 
	{
		mScale = new T3dVector(factor, factor, factor);
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		return new T3dVector(
			loc.getX() * mScale.getX(),
			loc.getY() * mScale.getY(), 
			loc.getZ() * mScale.getZ());
	}

	public T3dVector transform(T3dVector pnt) 
	{
		return new T3dVector(
				pnt.getX() * mScale.getX(),
				pnt.getY() * mScale.getY(), 
				pnt.getZ() * mScale.getZ());
	}
}