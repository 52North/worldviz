package org.n52.v3d.worldviz.extensions;

import org.n52.v3d.triturus.vgis.VgGeomObject2d;

/**
 * A simple Extension of {@link VgGeomObject2d} for collections of 2-dimensional
 * geometries.
 * 
 * @author Christian Danowski
 * 
 */
public abstract class VgCollection2D extends VgGeomObject2d {

	/**
	 * Gets the i-th geometry of the collection.<br />
	 * Note: The following condition must always be ensured: <b>0 &lt;= i &lt;
	 * {@link VgCollection2D#getNumberOfGeometries()}</b>.
	 * 
	 * @param i
	 *            geometry index
	 * @return the i-th geometry of the collection
	 */
	public abstract VgGeomObject2d getGeometry(int i);

	/**
	 * Gets the number of geometries that are part of the collection.
	 * 
	 * @return the number of geometries
	 */
	public abstract int getNumberOfGeometries();

}
