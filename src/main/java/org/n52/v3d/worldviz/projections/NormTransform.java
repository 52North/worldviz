package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmEnvelope;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgEnvelope;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

// TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.
/**
 * Normalization transformation. This transformation maps a set of given
 * point-coordinates to the the ground-plane's range (-1, -1) - (+1, +1). I.e.,
 * after applying this transformation, all points will lie inside this
 * unit-quad. Additionally, the aspect-ratios with respect to <i>x</i>,
 * <i>y,</i> and <i>z</i> will be preserved. Basically, both a scaling and a
 * translation operation will be performed.
 * <p />
 * This kind of transformation makes sense especially for positions, where all
 * coordinates refer to the same metric dimension, e.g. ground-values <i>x</i>
 * and <i>y</i> in meters and <i>z</i>-values in meters above ground.
 * <p />
 * Example:<br />
 * <tt><br />
 * VgPoint[] loc = new VgPoint[2];<br />
 * loc[0] = new GmPoint(-180., -90., 0.);<br />
 * loc[1] = new GmPoint(+180., +90., 8.848); // Mt Everest elevation in km<br />
 * NormTransform t = new NormTransform(loc);<br />
 * System.out.println(loc[0] + " -> " + t.transform(loc[0]));<br /> 
 * System.out.println(loc[1] + " -> " + t.transform(loc[1]));
 * </tt>
 * <p />
 * The console output for the code above would look like this:<br />
 * <tt><br />
 * (-180.0, -90.0, 0.0 (none)) -> (-1.0, -0.5, 0.0)<br />
 * (180.0, 90.0, 8.848 (none)) -> (1.0, 0.5, 0.04915555555555556)
 * </tt>
 * <p />
 * Note that the aspect ratio of the given points will be maintained! Thus the
 * envelope (bounding-box) of the output point-set must not be (-1, -1, -1) -
 * (1, 1, 1)!
 * 
 * @author Benno Schmidt, Adhitya Kamakshidasan
 */
public class NormTransform implements CoordinateTransform {

	private double mScale; // Scaling factor used for geo-coordinate
							// normalization
	private T3dVector mOffset = new T3dVector(); // Translation vector used for
													// geo-coordinate
													// normalization

	/**
	 * Constructor.
	 *
	 * @param geoPos
	 *            Array holding point coordinates
	 */
	public NormTransform(VgPoint[] geoPos) {
		/*
		 * this method call can be necessary for special subclasses that define
		 * a fixed spatial extent. In this class this has no effect.
		 */
		addSpecialCoordinates(geoPos);

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

	public NormTransform(ArrayList<VgPoint> geoPos) {
		/*
		 * this method call can be necessary for special subclasses that define
		 * a fixed spatial extent. In this class this has no effect.
		 */
		addSpecialCoordinates(geoPos);

		if (geoPos.isEmpty() || geoPos == null) {
			throw new T3dException(
					"Could not determine NormTransform bounding-box!");
		}

		VgEnvelope env = new GmEnvelope(geoPos.get(0));
		for (VgPoint g : geoPos) {
			env.letContainPoint(g);
		}

		this.calculateNormTransformation(env);
	}

	public T3dVector transform(T3dVector pnt) {
		return this.transform(pnt.getX(), pnt.getY(), pnt.getZ());
	}

	public T3dVector transform(VgPoint loc) {
		return this.transform(loc.getX(), loc.getY(), loc.getZ());
	}

	private T3dVector transform(double x, double y, double z) {
		return new T3dVector(x * mScale + mOffset.getX(), y * mScale
				+ mOffset.getY(), z * mScale);
	}

	private void calculateNormTransformation(VgEnvelope env) {
		double xMinGeo = env.getXMin();
		double xMaxGeo = env.getXMax();
		double yMinGeo = env.getYMin();
		double yMaxGeo = env.getYMax();

		double dx = xMaxGeo - xMinGeo;
		double dy = yMaxGeo - yMinGeo;

		if (Math.abs(dx) > Math.abs(dy)) {
			mScale = 2. / dx;
			mOffset.setX(-(xMinGeo + xMaxGeo) / dx);
			mOffset.setY(-(yMinGeo + yMaxGeo) / dx);
		} else {
			mScale = 2. / dy;
			mOffset.setX(-(xMinGeo + xMaxGeo) / dy);
			mOffset.setY(-(yMinGeo + yMaxGeo) / dy);
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

	/**
	 * This method has no content for this class. But subclasses that extend the
	 * class {@link NormTransform} might have a fixed spatial extent. Then this
	 * method will be overridden by this subclass to add bounding box
	 * coordinates to create a fixed spatial extent. An example can be seen in
	 * {@link NormTransform_Wgs84}.
	 * 
	 * @param geoPos
	 */
	protected void addSpecialCoordinates(VgPoint[] geoPos) {
		// here there is no content
		// only specialized classes have content in this method
	}

	/**
	 * This method has no content for this class. But subclasses that extend the
	 * class {@link NormTransform} might have a fixed spatial extent. Then this
	 * method will be overridden by this subclass to add bounding box
	 * coordinates to create a fixed spatial extent. An example can be seen in
	 * {@link NormTransform_Wgs84}.
	 * 
	 * @param geoPos
	 */
	protected void addSpecialCoordinates(List<VgPoint> geoPos) {
		// here there is no content
		// only specialized classes have content in this method
	}

	@Override
	public VgGeomObject transform(VgGeomObject geom) {
		throw new T3dNotYetImplException();
	}

	@Override
	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		throw new T3dNotYetImplException();
	}

	@Override
	public VgPolygon transform(VgPolygon polygon) {
		throw new T3dNotYetImplException();
	}

	@Override
	public VgLinearRing transform(VgLinearRing linearRing) {
		throw new T3dNotYetImplException();
	}
}
