package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.triturusextensions.GmLinearRing;
import org.n52.v3d.worldviz.triturusextensions.GmMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.GmPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgLinearRing;
import org.n52.v3d.worldviz.triturusextensions.VgMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgPolygon;

public class Wgs84ToPetersTransform {

	public static VgMultiPolygon transformToPeters(
			VgMultiPolygon multiPolygonWGS84) throws Exception {
		if (!multiPolygonWGS84.getSRS().equalsIgnoreCase(
				VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		}

		int numberOfGeometries = multiPolygonWGS84.getNumberOfGeometries();

		GmMultiPolygon buckmFullerMultiPolygon = new GmMultiPolygon();

		for (int i = 0; i < numberOfGeometries; i++) {
			VgPolygon vgPolygon = (VgPolygon) multiPolygonWGS84.getGeometry(i);
			buckmFullerMultiPolygon.addPolygon(transformToPeters(vgPolygon));
		}

		return buckmFullerMultiPolygon;
	}

	public static VgPolygon transformToPeters(VgPolygon polygonWGS84)
			throws Exception {
		// if (!polygonWGS84.getSRS()
		// .equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
		// throw new T3dException(
		// "The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		// }

		VgLinearRing outerBoundaryUntransformed = polygonWGS84
				.getOuterBoundary();

		VgLinearRing outerBoundaryTransformed = transformToPeters(outerBoundaryUntransformed);

		// process inner holes
		int numberOfHoles = polygonWGS84.getNumberOfHoles();
		List<VgLinearRing> holesTransformed = new ArrayList<VgLinearRing>(
				numberOfHoles);

		for (int i = 0; i < numberOfHoles; i++) {
			holesTransformed.add(transformToPeters((polygonWGS84.getHole(i))));
		}

		GmPolygon buckmFullerPolygon = new GmPolygon(outerBoundaryTransformed,
				holesTransformed);

		return buckmFullerPolygon;
	}

	public static VgLinearRing transformToPeters(VgLinearRing linearRing)
			throws T3dException, Exception {

		int numberOfVertices = linearRing.getNumberOfVertices();

		GmLinearRing linearRingTransformed = new GmLinearRing();

		for (int i = 0; i < numberOfVertices; i++) {
			linearRingTransformed.addVertex(transformToPeters(linearRing
					.getVertex(i)));
		}

		return linearRing;
	}

	public static VgPoint transformToPeters(VgPoint pointWGS84)
			throws Exception {

		VgPoint point2D = latLon2GP(pointWGS84);

		GmPoint newPointBuckmFuller = new GmPoint(point2D.getX(),
				point2D.getY(), 0);

		return newPointBuckmFuller;
	}

	private static VgPoint latLon2GP(VgPoint pIn) {
		VgPoint pOut = new GmPoint();
		double mSqrt2 = Math.sqrt(2.0);

		if (!pIn.getSRS().equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {

			// throw new
			// T3dSRSException("Tried to process illegal point coordinate.");

		}

		double lat = pIn.getY();

		double lon = pIn.getX();

		double R = 6371.;

		pOut.setX(R * Math.PI * lon / (180. * mSqrt2));

		pOut.setY(R * mSqrt2 * Math.sin(lat * Math.PI / 180.));

		pOut.setZ(pIn.getZ());

		pOut.setSRS(VgGeomObject.SRSNone); // provisorische Setzung

		return pOut;
	}

}
