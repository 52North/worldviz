package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

//TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Transformation that simply switches the coordinate-axes in the following manner:
 * <p />
 * <code>
 * x -&gt; X<br />
 * y -&gt; -Z<br />
 * z -&gt; Y<br />
 * </code>
 * <p />
 * Hence, this transformation maps typical real-world coordinate-axes 
 * <tt>(x, y, z)</tt> to those "classical" coordinate-systems used in the field of 
 * Computer Graphics <tt>(X, Y, Z)</tt>.
 * <p />
 * Note: Mostly, a scaling and translation transformation will be performed, too,
 * e.g. by applying a <tt>NormTransform</tt>. 
 * 
 * @author Benno Schmidt
 *
 * @see TranslationTransform
 * @see ScaleTransform
 * @see NormTransform
 */
public class AxisSwitchTransform implements CoordinateTransform
{
	public T3dVector transform(VgPoint loc) 
	{
		double 
			x = loc.getX(),
			y = -loc.getZ(), 
			z = loc.getY();
		return new T3dVector(x, y, z);
	}

	public T3dVector transform(T3dVector pnt) 
	{
		double 
			x = pnt.getX(),
			y = -pnt.getZ(), 
			z = pnt.getY();
		return new T3dVector(x, y, z);
	}
}
