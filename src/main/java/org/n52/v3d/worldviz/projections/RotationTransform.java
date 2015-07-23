package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

//TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Rotation transformation.
 * 
 * Note: This method has not been implemented yet!
 */
public class RotationTransform implements CoordinateTransform
{
	// TODO This class has not been implemented yet...
	
	public T3dVector transform(VgPoint loc) 
	{
		throw new T3dNotYetImplException(); 
	}

	public T3dVector transform(T3dVector pnt) 
	{
		throw new T3dNotYetImplException(); 
	}

	@Override
	public VgGeomObject transform(VgGeomObject geom) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgPolygon transform(VgPolygon polygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgLinearRing transform(VgLinearRing linearRing) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}
}
