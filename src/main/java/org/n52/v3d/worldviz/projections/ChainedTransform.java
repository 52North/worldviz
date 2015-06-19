package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

//TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Implementation of coordinate-transformation chains. 
 * <p />
 * This implementation allows to give two coordinate-transformations <tt>t1</tt> 
 * and <tt>t2</tt>. When calling the <tt>transform</tt> method, these two transformations 
 * will be applied to a given point <tt>p</tt> consecutively, i.e. the result will 
 * be <tt>t2.transform(t1.transform(p))</tt>.  
 * 
 * @author Benno Schmidt
 */
public class ChainedTransform 
{
	private CoordinateTransform t1;
	private CoordinateTransform t2;

	/**
	 * Constructor.
	 * 
	 * @param t1 First coordinate-transformation to be applied
	 * @param t2 Second coordinate-transformation to be applied
	 */
	public ChainedTransform(CoordinateTransform t1, CoordinateTransform t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public T3dVector transform(T3dVector pnt) 
	{
		T3dVector tmp = t1.transform(pnt);
		return t2.transform(tmp);
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		T3dVector tmp = t1.transform(loc);
		return t2.transform(tmp);		
	}
}
