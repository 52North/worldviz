package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

//TODO: This class could be migrated to the 52N Triturus core package.

/**
 * Translation transformation ("coordinate offset").
 * 
 * @author Benno Schmidt
 */
public class TranslationTransform implements CoordinateTransform
{
	private T3dVector mOffset;
	
	public TranslationTransform(T3dVector offset) 
	{
		mOffset = offset;
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		return new T3dVector(
			loc.getX() + mOffset.getX(),
			loc.getY() + mOffset.getY(), 
			loc.getZ() + mOffset.getZ());
	}

	public T3dVector transform(T3dVector pnt) 
	{
		return new T3dVector(
				pnt.getX() + mOffset.getX(),
				pnt.getY() + mOffset.getY(), 
				pnt.getZ() + mOffset.getZ());
	}
}
