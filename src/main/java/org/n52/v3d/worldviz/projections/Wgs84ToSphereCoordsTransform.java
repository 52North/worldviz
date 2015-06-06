package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.worldviz.triturusextensions.GmLinearRing;
import org.n52.v3d.worldviz.triturusextensions.GmMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.GmPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgLinearRing;
import org.n52.v3d.worldviz.triturusextensions.VgMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgPolygon;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.gisimplm.GmSimpleTINGeometry;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgIndexedTIN;
import org.n52.v3d.triturus.vgis.VgPoint;

public class Wgs84ToSphereCoordsTransform {

	/**
	 * WGS84 coordinates in degrees!
	 * 
	 * @param coordsWGS84
	 * @param radius
	 *            must be the same metric system as the points altitude!
	 * @return
	 */
	public static VgPoint wgs84ToSphere(VgPoint coordsWGS84, double radius) {

		if (!coordsWGS84.getSRS().equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {

			System.out
					.println("WARNING: The geo reference system of the point is not 'EPSG:4326' (WGS84)! "
							+ coordsWGS84);

			// throw new T3dException(
			// "The geo reference system of the point is not 'EPSG:4326' (WGS84)! "
			// + coordsWGS84);
		}

		double longitude = coordsWGS84.getX();
		double latitude = coordsWGS84.getY();
		double altitude = coordsWGS84.getZ();

		double longitudeInRadians = Math.toRadians(longitude);
		double latitudeInRadians = Math.toRadians(latitude);

		// sphere coords
		double x = (radius + altitude) * Math.cos(latitudeInRadians)
				* Math.cos(longitudeInRadians);
		double y = (radius + altitude) * Math.cos(latitudeInRadians)
				* Math.sin(longitudeInRadians);
		double z = (radius + altitude) * Math.sin(latitudeInRadians);

		GmPoint spherePoint = new GmPoint(x, y, z);

		return spherePoint;
	}

	public static VgPolygon wgs84ToSphere(VgPolygon polygonWGS84, double radius) {

		if (!polygonWGS84.getSRS()
				.equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		}

		VgLinearRing outerBoundaryUntransformed = polygonWGS84
				.getOuterBoundary();

		VgLinearRing outerBoundaryTransformed = wgs84ToSphere(
				outerBoundaryUntransformed, radius);

		// process holes
		int numberOfHoles = polygonWGS84.getNumberOfHoles();
		List<VgLinearRing> holesTransformed = new ArrayList<VgLinearRing>(
				numberOfHoles);

		for (int i = 0; i < numberOfHoles; i++) {
			holesTransformed
					.add(wgs84ToSphere(polygonWGS84.getHole(i), radius));
		}

		GmPolygon spherePolygon = new GmPolygon(outerBoundaryTransformed,
				holesTransformed);

		return spherePolygon;

	}

	private static VgLinearRing wgs84ToSphere(VgLinearRing linearRing,
			double radius) {

		int numberOfVertices = linearRing.getNumberOfVertices();

		GmLinearRing linearRingTransformed = new GmLinearRing();

		for (int i = 0; i < numberOfVertices; i++) {
			linearRingTransformed.addVertex(wgs84ToSphere(
					linearRing.getVertex(i), radius));
		}

		return linearRingTransformed;
	}

	public static VgMultiPolygon wgs84ToSphere(VgMultiPolygon mPolygonWGS84,
			double radius) {

		if (!mPolygonWGS84.getSRS().equalsIgnoreCase(
				VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		}

		int numberOfGeometries = mPolygonWGS84.getNumberOfGeometries();

		GmMultiPolygon sphereMultiPolygon = new GmMultiPolygon();

		for (int i = 0; i < numberOfGeometries; i++) {
			VgPolygon vgPolygon = (VgPolygon) mPolygonWGS84.getGeometry(i);
			sphereMultiPolygon.addPolygon(wgs84ToSphere(vgPolygon, radius));
		}

		return sphereMultiPolygon;

	}

	public static List<VgIndexedTIN> wgs84ToSphere(List<VgIndexedTIN> vgTINs,
			double radius) {

		List<VgIndexedTIN> vgTinsSphere = new ArrayList<VgIndexedTIN>(
				vgTINs.size());

		for (VgIndexedTIN vgIndexedTIN : vgTINs) {

			vgTinsSphere.add(wgs84ToSphere(vgIndexedTIN, radius));

		}

		return vgTinsSphere;
	}

	/**
	 * Manipulates the given TIN-object by transforming the coordinates of each
	 * TrianglePoint! No new object is created!
	 * 
	 * @param vgIndexedTIN
	 * @param radius
	 * @return
	 */
	public static VgIndexedTIN wgs84ToSphere(VgIndexedTIN vgIndexedTIN,
			double radius) {

		if (!vgIndexedTIN.getSRS()
				.equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		}

		if (vgIndexedTIN instanceof GmSimpleTINGeometry) {

			GmSimpleTINGeometry tin = (GmSimpleTINGeometry) vgIndexedTIN;

			for (int i = 0; i < tin.numberOfPoints(); i++) {
				VgPoint oldPoint = tin.getPoint(i);

				tin.setPoint(i, wgs84ToSphere(oldPoint, radius));

			}
			return tin;
		}

		return vgIndexedTIN;
	}

	/**
	 * Iterates through the list of VgAttributedFeatures and transforms each
	 * geometry of each feature from WGS84 to spherical coordinates. Note: The
	 * geometry of the input features will be replaced and overwritten!
	 * 
	 * @param attrFeaturesWGS84
	 * @param radius
	 * @return
	 */
	public static List<VgAttrFeature> wgs84ToSphereForAttrFeatures(
			List<VgAttrFeature> attrFeaturesWGS84, double radius) {

		// List<VgAttrFeature> sphericalFeatures = new
		// ArrayList<VgAttrFeature>();

		for (VgAttrFeature vgAttrFeature : attrFeaturesWGS84) {

			VgGeomObject geometry = vgAttrFeature.getGeometry();

			VgGeomObject transformedGeometry = wgs84ToSphere(geometry, radius);

			// replace geometry
			GmAttrFeature feat = (GmAttrFeature) vgAttrFeature;
			feat.setGeometry(transformedGeometry);

			// sphericalFeatures.add(feat);
		}

		return attrFeaturesWGS84;
		// return sphericalFeatures;

	}

	public static VgGeomObject wgs84ToSphere(VgGeomObject geometry,
			double radius) {

		if (!geometry.getSRS().equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the geometry is not 'EPSG:4326' (WGS84)! "
							+ geometry);
		}

		if (geometry instanceof VgPoint)
			return wgs84ToSphere((VgPoint) geometry, radius);
		else if (geometry instanceof VgPolygon)
			return wgs84ToSphere((VgPolygon) geometry, radius);
		else if (geometry instanceof VgMultiPolygon)
			return wgs84ToSphere((VgMultiPolygon) geometry, radius);
		else if (geometry instanceof VgIndexedTIN)
			return wgs84ToSphere((VgIndexedTIN) geometry, radius);
		else
			throw new T3dNotYetImplException(
					"The support for the geometry of type '"
							+ geometry.getClass() + "' is not yet implemented!");

	}

	public static VgPoint sphereToWgs84(VgPoint coordsSphere, double radius){
		
		// x, y, z from sphere coordinates
		double x = coordsSphere.getX();
		double y = coordsSphere.getY();
		double z = coordsSphere.getZ();
		
		// transform to latitude and longitude
		
		double lat = Math.atan2(z, (Math.sqrt(x*x + y*y))) * (180/Math.PI);
		double lon = Math.atan2(y , x) * 180 / Math.PI;
		double alt = Math.sqrt(x*x + y*y + z*z) - radius;
		
		GmPoint pointWgs84 = new GmPoint(lon, lat, alt);
		pointWgs84.setSRS(VgGeomObject.SRSLatLonWgs84);
		
		return pointWgs84;
	}
}
