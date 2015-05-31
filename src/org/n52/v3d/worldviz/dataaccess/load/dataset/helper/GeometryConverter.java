package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.triturusextensions.GmLinearRing;
import org.n52.v3d.worldviz.triturusextensions.GmMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.GmPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgLinearRing;
import org.n52.v3d.worldviz.triturusextensions.VgMultiPolygon;
import org.n52.v3d.worldviz.triturusextensions.VgPolygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Offers static methods to convert between JTS (Java Topology Suite) geometries
 * and 52�North-Triturus geometries.
 * 
 * @author Christian Danowski
 * 
 */
public class GeometryConverter {

	/**
	 * Converts JTS (Java Topology Suite) <code>MultiPolygon</code> objects into
	 * Triturus <code>VgMultiPolygon</code> objects by calling
	 * {@link GeometryConverter#convertJtsMultiPolygon2TriturusMultiPolygon(MultiPolygon)}
	 * for each JTS <code>MultiPolygon</code>. <br/>
	 * Note: if the geometries of the shape file do not contain any
	 * crs-information, then WGS84 (EPSG:4326) will be set as crs.
	 * 
	 * @param jtsMultiPolygons
	 *            a collection of JTS MultiPolygons
	 * @return a collection of Triturus <code>VgMultiPolygon</code>
	 */
	public static Collection<VgMultiPolygon> convertJtsMultiPolygons2TriturusMultiPolygons(
			Collection<MultiPolygon> jtsMultiPolygons) {
		Collection<VgMultiPolygon> triturusMultiPolygons = new ArrayList<VgMultiPolygon>();

		for (MultiPolygon jtsMultiPolygon : jtsMultiPolygons) {
			triturusMultiPolygons
					.add(convertJtsMultiPolygon2TriturusMultiPolygon(jtsMultiPolygon));
		}

		return triturusMultiPolygons;
	}

	/**
	 * Converts a single JTS (Java Topology Suite) <code>MultiPolygon</code>
	 * into a Triturus {@link VgMultiPolygon}. Iterates through all JTS
	 * <code>MultiPolygon</code> objects and <b>extracts only the outer
	 * boundary</b> of each sub Polygon to convert it into a Triturus
	 * {@link VgPolygon}. Then each {@link VgPolygon} is added to the resulting
	 * {@link VgMultiPolygon} object. <br />
	 * <b><i>Note: Any inner 'hole' of any JTS Polygon is completely lost by
	 * this implementation as only the outer boundary is considered.</i></b><br/>
	 * Note: if the geometries of the shape file do not contain any
	 * crs-information, then WGS84 (EPSG:4326) will be set as crs.
	 * 
	 * @param jtsMultipolygon
	 *            the JTS <code>MultiPolygon</code>
	 * @return the Triturus {@link VgMultiPolygon} representation of the JTS
	 *         <code>MultiPolygon</code>
	 */
	public static VgMultiPolygon convertJtsMultiPolygon2TriturusMultiPolygon(
			MultiPolygon jtsMultipolygon) {

		GmMultiPolygon triturusMultiPolygon = new GmMultiPolygon();

		int numGeometries = jtsMultipolygon.getNumGeometries();

		for (int i = 0; i < numGeometries; i++) {
			Geometry geometryN = jtsMultipolygon.getGeometryN(i);
			if (geometryN instanceof Polygon) {
				Polygon jtsPolygonN = (Polygon) geometryN;

				VgPolygon newTriturusPolygon = convertJtsPolygon2TriturusPolygon(jtsPolygonN);

				triturusMultiPolygon.addPolygon(newTriturusPolygon);
			}
		}

		return triturusMultiPolygon;
	}

	/**
	 * Converts a single JTS (Java Topology Suite) <code>Polygon</code> into a
	 * Triturus {@link GmPolygon}.
	 * 
	 * @param jtsPolygon
	 * @return
	 */
	public static VgPolygon convertJtsPolygon2TriturusPolygon(Polygon jtsPolygon) {

		// crs information
		// if no crs information exists take EPSG:4326
		int sridIDFromJTS = jtsPolygon.getSRID();
		if (sridIDFromJTS == 0) {
			// TODO ?
			sridIDFromJTS = 4326;
		}
		String sridEPSG = "EPSG:" + sridIDFromJTS;

		// get exterior ring and convert it

		VgLinearRing vgExteriorRing = convertJtsLinearRing2TriturusLinearRing(
				jtsPolygon.getExteriorRing(), sridEPSG);

		// get all holes and convert them to a list of holes
		int numInteriorRing = jtsPolygon.getNumInteriorRing();
		List<VgLinearRing> polygonHoles = new ArrayList<VgLinearRing>(
				numInteriorRing);

		for (int i = 0; i < numInteriorRing; i++) {
			LineString jtsInteriorRingN = jtsPolygon.getInteriorRingN(i);

			polygonHoles.add(convertJtsLinearRing2TriturusLinearRing(
					jtsInteriorRingN, sridEPSG));
		}

		GmPolygon newTriturusPolygon = new GmPolygon(vgExteriorRing,
				polygonHoles);

		newTriturusPolygon.setSRS(sridEPSG);

		return newTriturusPolygon;
	}

	/**
	 * 
	 * @param jtsLinearRing
	 *            a java topology suite (JTS) <code>LineString</code>-object,
	 *            which represents either an exterior or interior ring of a JTS
	 *            polygon
	 * @param sridEPSG
	 * @return
	 */
	private static VgLinearRing convertJtsLinearRing2TriturusLinearRing(
			LineString jtsLinearRing, String sridEPSG) {

		Coordinate[] jtsCoordinates = jtsLinearRing.getCoordinates();

		GmLinearRing triturusLinearRing = new GmLinearRing();

		triturusLinearRing.setSRS(sridEPSG);

		for (Coordinate jtsCoordinate : jtsCoordinates) {

			double zCoordinate = jtsCoordinate.z;

			if (Double.isNaN(zCoordinate))
				zCoordinate = 0.;

			GmPoint pPnt = new GmPoint(jtsCoordinate.x, jtsCoordinate.y,
					zCoordinate);
			pPnt.setSRS(sridEPSG);
			triturusLinearRing.addVertex(pPnt);
		}

		return triturusLinearRing;

	}

	/**
	 * Converts a single Triturus {@link VgPolygon} into a JTS (Java Topology
	 * Suite) <code>Polygon</code>.
	 * 
	 * @param triturusPolygon
	 * @return
	 */
	public static Polygon convertTriturusPolygon2JtsPolygon(
			VgPolygon triturusPolygon) {

		// triturus srs looks like "EPSG:xxxx" where xxxx is the integer number
		// of the srs
		String srsWithEPSG = triturusPolygon.getSRS();
		int srs = 0;
		if (srsWithEPSG.startsWith("EPSG:"))
			srs = Integer.parseInt(srsWithEPSG.split(":")[1]);

		LinearRing jtsOuterBoundary = convertTriturusLinearRing2JtsLinearRing(
				triturusPolygon.getOuterBoundary(), srs);

		int numberOfHoles = triturusPolygon.getNumberOfHoles();
		LinearRing[] jtsHoles = new LinearRing[numberOfHoles];
		for (int i = 0; i < numberOfHoles; i++) {
			jtsHoles[i] = convertTriturusLinearRing2JtsLinearRing(
					triturusPolygon.getHole(i), srs);
		}

		Polygon jtsPolygon = new GeometryFactory(new PrecisionModel(), srs)
				.createPolygon(jtsOuterBoundary, jtsHoles);

		return jtsPolygon;

	}

	/**
	 * 
	 * @param triturusLinearRing
	 * @param srid
	 *            only the numbers of the corresponding EPSG-code. e.g. for
	 *            "EPSG:4326" ony use "4326"
	 * @return
	 */
	private static LinearRing convertTriturusLinearRing2JtsLinearRing(
			VgLinearRing triturusLinearRing, int srid) {

		int numberOfVertices = triturusLinearRing.getNumberOfVertices();

		Coordinate[] jtsCoordinates = new Coordinate[numberOfVertices];

		for (int i = 0; i < numberOfVertices; i++) {
			double x = triturusLinearRing.getVertex(i).getX();
			double y = triturusLinearRing.getVertex(i).getY();
			double z = triturusLinearRing.getVertex(i).getZ();

			jtsCoordinates[i] = new Coordinate(x, y, z);
		}

		LinearRing jtsLinearRing = new GeometryFactory(new PrecisionModel(),
				srid).createLinearRing(jtsCoordinates);

		return jtsLinearRing;

	}

	public static VgPoint convertJtsPoint2TriturusPoint(Point jtsPoint) {

		String srs = "EPSG:" + jtsPoint.getSRID();

		GmPoint triturusPoint = new GmPoint();
		triturusPoint.setSRS(srs);
		triturusPoint.setX(jtsPoint.getCoordinate().x);
		triturusPoint.setY(jtsPoint.getCoordinate().y);

		double z = 0;
		if (!Double.isNaN(jtsPoint.getCoordinate().z)) {
			z = jtsPoint.getCoordinate().z;
		}
		triturusPoint.setZ(z);

		return triturusPoint;
	}

}