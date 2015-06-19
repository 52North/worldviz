package org.n52.v3d.worldviz.projections;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmEnvelope;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgEnvelope;
import org.n52.v3d.triturus.vgis.VgPoint;

// TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Normalization transformation. This transformation maps a set of given 
 * point-coordinates to the unit-box, i.e. to the range (-1, -1, 1) - (1, 1, 1). 
 * Basically, both a scaling and a translation operation will be performed.
 * Note that the aspect ratio of the given points will be maintained.
 *
 * @author Benno Schmidt
 */
public class NormTransform implements CoordinateTransform
{
	private double mScale; // Scaling factor used for geo-coordinate normalization
	private T3dVector mOffset = new T3dVector(); // Translation vector used for geo-coordinate normalization

	/**
	 * Constructor.
	 * 
	 * @param geoPos Array holding point coordinates
	 */
	public NormTransform(VgPoint[] geoPos) 
	{
		if (geoPos == null || geoPos.length < 1) {
			throw new T3dException(
					"Could not determine NormTransform bounding-box!");
		}
		VgEnvelope env = new GmEnvelope(geoPos[0]);
		for (VgPoint g : geoPos) {
			env.letContainPoint(g);
		}

		this.calculateNormTransformation(env);
	}

	public T3dVector transform(T3dVector pnt) 
	{
		return this.transform(pnt.getX(), pnt.getY(), pnt.getZ());
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		return this.transform(loc.getX(), loc.getY(), loc.getZ());
	}

	private T3dVector transform(double x, double y, double z) 
	{
		return new T3dVector(
			    x * mScale + mOffset.getX(),
			    y * mScale + mOffset.getY(),
			    z * mScale);
	}

	private void calculateNormTransformation(VgEnvelope env)
	{
		double xMinGeo = env.getXMin();
		double xMaxGeo = env.getXMax();
		double yMinGeo = env.getYMin();
		double yMaxGeo = env.getYMax();

        double dx = xMaxGeo - xMinGeo;
        double dy = yMaxGeo - yMinGeo;

        if (Math.abs(dx) > Math.abs(dy)) {
        	mScale = 2./dx;
        	mOffset.setX(-(xMinGeo + xMaxGeo)/ dx);
        	mOffset.setY(-(yMinGeo + yMaxGeo)/ dx);
        }
        else {
        	mScale = 2./dy;
        	mOffset.setX(-(xMinGeo + xMaxGeo)/ dy);
        	mOffset.setY(-(yMinGeo + yMaxGeo)/ dy);
        }
	}

	/**
	 * gets the scaling-value.
	 * 
	 * @return scale
	 */
	public double getScale() {
		return mScale;
	}

	/**
	 * gets the translation vector (coordinate-offset resp. shift vector).
	 * 
	 * @return translation
	 */
	public T3dVector getOffset() {
		return mOffset;
	}	
}
