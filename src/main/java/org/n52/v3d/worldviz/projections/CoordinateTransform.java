package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

//TODO: This class could be migrated to the 52N Triturus core package in the future.

/**
 * Interface for coordinate-transformations. Implementations of this class provide
 * transformations of geo-referenced locations (given by <tt>VgPoint</tt>-objects)
 * or positions referring to visualization coordinate-systems  (given by <tt>T3dVector 
 * objects</tt>) to visualization coordinates (given by <tt>T3dVector objects</tt>).
 * <p />
 * Thus, <tt>CoordinateTransform</tt> objects typically will be used when mapping
 * geo-objects (i.e. <tt>VgFeature</tt> geometries) to (abstract) scene descriptions.
 * <p />
 * Note that <tt>CoordinateTransform</tt> could be composed to coordinate-transformation 
 * chains (<tt>ChainedTransform</tt>).
 *  
 * @see VgPoint
 * @see T3dVector
 * @see ChainedTransform
 * @see CoordinateTransformDemo
 * 
 * @author Benno Schmidt
 */
public interface CoordinateTransform 
{
	/**
	 * transforms a geo-referenced location to a visualization coordinate. 
	 *  
	 * @param loc Geo-referenced location (source coordinate-system)
	 * @return Coordinates referring to the target coordinate-system
	 */
	public T3dVector transform(VgPoint loc); 

	/**
	 * transforms visualization coordinates.
	 *  
	 * @param pnt Coordinates referring to the source coordinate-system
	 * @return Coordinates referring to the target coordinate-system
	 */
	public T3dVector transform(T3dVector pnt); 
}
