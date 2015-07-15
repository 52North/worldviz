package org.n52.v3d.worldviz.triangulation;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.GeometryConverter;
import org.n52.v3d.worldviz.extensions.VgPolygon;

import org.n52.v3d.triturus.vgis.VgPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public class InnerPointsForPolygonClass {

	/**
	 * This method takes the polygon and uses a "raster overlay" to check which
	 * points of the specified raster lay inside of the polygon. Thus each
	 * raster point is checked and if it lies inside the polygon it will be
	 * added to the returned list of points. <br />
	 * The raster is defined by the parameters <code>xSpacing</code> and
	 * <code>ySpacing</code>, that define the width and height of a single
	 * raster cell. Using the bounding box of the polygon, the raster begins at
	 * minX,minY and ends at maxX, maxY. <br />
	 * <br />
	 * 
	 * <b>Note: The coordinate reference system of <code>xSpacing</code> and
	 * <code>ySpacing</code> needs to be conform to the coordinate reference
	 * system of the polygon!!!</b>
	 * 
	 * @param triturusPolygon
	 * @param xSpacing
	 *            the spatial extent of a single raster cell in positive
	 *            x-direction. In other words, the positive distance between two
	 *            neighbor-points of the raster cell in x-direction.
	 * @param ySpacing
	 *            the spatial extent of a single raster cell in positive
	 *            y-direction. In other words, the positive distance between two
	 *            neighbor-points of the raster cell in y-direction.
	 * @return a list of all points of the specified raster that lie inside of
	 *         the polygon.
	 */
	public static List<VgPoint> getInnerPointsForPolygonUsingRaster(
			VgPolygon triturusPolygon, double xSpacing, double ySpacing) {

		List<VgPoint> innerPoints = new ArrayList<VgPoint>();
		// convert to Java Topology Suite polygon and use JTS to determine which
		// points are inside of the polygon

		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;

		Polygon jtsPolygon = GeometryConverter
				.convertTriturusPolygon2JtsPolygon(triturusPolygon);

		int srid = jtsPolygon.getSRID();

		/*
		 * copy of JTSJavadoc: Returns this Geometrys bounding box. If this
		 * Geometry is the empty geometry, returns an empty Point. If the
		 * Geometry is a point, returns a non-empty Point. Otherwise, returns a
		 * Polygon whose points are (minx, miny), (maxx, miny), (maxx, maxy),
		 * (minx, maxy), (minx, miny).
		 */
		Geometry envelope = jtsPolygon.getEnvelope();

		if (envelope instanceof Polygon) {
			Polygon bbox = (Polygon) envelope;
			Coordinate[] bboxCoordinates = bbox.getCoordinates();

			// the comment obove shows why indexes 0 and 2 are used!
			minX = bboxCoordinates[0].x;
			minY = bboxCoordinates[0].y;
			maxX = bboxCoordinates[2].x;
			maxY = bboxCoordinates[2].y;
		}

		// iterate through raster and check each point
		for (double x = (minX + xSpacing); x < maxX; x += xSpacing) {
			for (double y = (minY + ySpacing); y < maxY; y += ySpacing) {

				Point jtsRasterPoint = (new GeometryFactory(new PrecisionModel(), srid))
						.createPoint(new Coordinate(x, y));
				jtsRasterPoint.setSRID(srid);

				if (jtsPolygon.contains(jtsRasterPoint)) {
					innerPoints.add(GeometryConverter
							.convertJtsPoint2TriturusPoint(jtsRasterPoint));
				}
			}
		}

		return innerPoints;
	}
}
