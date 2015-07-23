package org.n52.v3d.worldviz.worldscene;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgIndexedTIN;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;
import org.n52.v3d.worldviz.projections.Wgs84ToSphereCoordsTransform;
import org.n52.v3d.worldviz.triangulation.InnerPointsForPolygonClass;
import org.n52.v3d.worldviz.triangulation.PolygonTriangulator;

/**
 * This class is a specialization of {@link VsWorldCountriesScene} that projects
 * the polygonal geometries into spherical geometries that can be used as an
 * overlay for a world sphere. The expected coordinate reference system of the
 * input data is EPSG:4326 (WGS84). The transformation into spherical
 * coordinates is done during the scene generation.<br/>
 * Note that you should inspect all setter-methods before you generate a scene.
 * 
 * @author Christian Danowski
 * 
 */
public class VsWorldCountriesOnASphereScene extends VsWorldCountriesScene {

	// raster width is used to create additional points inside a polygon!
	private boolean generateAdditionalInnerPolygonPoints = true;

	// 0.5 is quite small and thus takes a long time to triangulate the polygon;
	// but the result is very good. If one sets a higher
	// value (e.g. 1.0), then large polygons may have visible triangles in the
	// final rendering process of the scene (e.g. the polygon of russia)
	// (phenomenon observed in X3D).
	private double xRasterWidth = 0.5;
	private double yRasterWidth = 0.5;
	private double radius = 10;
	private double radiusForBorder = radius + this.offsetForBorders;

	public VsWorldCountriesOnASphereScene(String filePath) {
		super(filePath);
	}

	/**
	 * Gets the radius of the reference sphere, that is used to project the
	 * WGS84-geometries into spherical geometries. The default value is 10.
	 * 
	 * @return
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius. This radius is important when transforming the
	 * WGS84-geometries into spherical geometries.It is the radius of the
	 * reference sphere on which the geometries shall be overlaid. The default
	 * value is 10.
	 * 
	 * @param radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Any polygon of the attributed feature must be triangulated in order to
	 * express the polygon's surface in a scene description language like X3D.
	 * The raster width is used internally to create additional points inside of
	 * the feature's polygon(s). This way, the triangulation will create
	 * additional triangles inside the polygon(s) and the resulting triangle
	 * mesh will be finer. <br />
	 * <br />
	 * However this is only useful under certain circumstances: If all feature
	 * geometries are drawn in a single plane, then there is no need to create
	 * additional triangles. <br />
	 * But let's say you want to draw the features around a world globe (meaning
	 * the geometries are drawn on top of a sphere), then you MUST create
	 * additional inner triangles and create a finer mesh. Otherwise large
	 * geometries will have plane triangles that cut through the spherical
	 * geometry of the world globe.
	 * 
	 * You can choose to create additional inner points/triangles by calling
	 * {@link #setGenerateAdditionalInnerPolygonPoints(boolean)}.
	 * 
	 * Also see {@link InnerPointsForPolygonClass}.
	 * 
	 * @return the raster width in y-direction of the raster in which new points
	 *         are added to the feature's polygonal geometry. Note that the
	 *         value must correspond to the coordinate reference system of the
	 *         geometry of the feature. Default value is 0.5. 0.5 is quite small
	 *         and thus takes a long time to triangulate the polygon; but the
	 *         result is very good. If one sets a higher value (e.g. 1.0), then
	 *         large polygons may have visible triangles in the final rendering
	 *         process of the scene (e.g. the polygon of russia) (phenomenon
	 *         observed in X3D)
	 * @see InnerPointsForPolygonClass
	 */
	public double getyRasterWidth() {
		return yRasterWidth;
	}

	/**
	 * First of all, please read the comments for {@link #getyRasterWidth()} to
	 * get to know this parameter.
	 * 
	 * Sets the raster width of the raster which is used to add additional inner
	 * points inside of the feature's polygonal geometry. Thus a finer triangle
	 * mesh is created during the triangulation of any polygon. <br/>
	 * <br/>
	 * This parameter will only be used when
	 * {@link #isGenerateAdditionalInnerPolygonPoints()} is set to true!
	 * 
	 * @param yRasterWidth
	 *            the raster width in y-direction. Note that the value must
	 *            correspond to the coordinate reference system of the geometry
	 *            of the feature. In general: A smaller value causes a finer
	 *            mesh and thus more triangles; Default value is 0.5. 0.5 is
	 *            quite small and thus takes a long time to triangulate the
	 *            polygon; but the result is very good. If one sets a higher
	 *            value (e.g. 1.0), then large polygons may have visible
	 *            triangles in the final rendering process of the scene (e.g.
	 *            the polygon of russia) (phenomenon observed in X3D)
	 */
	public void setyRasterWidth(float yRasterWidth) {
		this.yRasterWidth = yRasterWidth;
	}

	/**
	 * Any polygon of the attributed feature must be triangulated in order to
	 * express the polygon's surface in a scene description language like X3D.
	 * The raster width is used internally to create additional points inside of
	 * the feature's polygon(s). This way, the triangulation will create
	 * additional triangles inside the polygon(s) and the resulting triangle
	 * mesh will be finer. <br />
	 * <br />
	 * However this is only useful under certain circumstances: If all feature
	 * geometries are drawn in a single plane, then there is no need to create
	 * additional triangles. <br />
	 * But let's say you want to draw the features around a world globe (meaning
	 * the geometries are drawn on top of a sphere), then you MUST create
	 * additional inner triangles and create a finer mesh. Otherwise large
	 * geometries will have plane triangles that cut through the spherical
	 * geometry of the world globe.
	 * 
	 * You can coose to create additional inner points/triangles by calling
	 * {@link #setGenerateAdditionalInnerPolygonPoints(boolean)}.
	 * 
	 * Also see {@link InnerPointsForPolygonClass}.
	 * 
	 * @return the raster width in x-direction of the raster in which new points
	 *         are added to the feature's polygonal geometry. Note that the
	 *         value must correspond to the coordinate reference system of the
	 *         geometry of the feature. Default value is 0.5. 0.5 is quite small
	 *         and thus takes a long time to triangulate the polygon; but the
	 *         result is very good. If one sets a higher value (e.g. 1.0), then
	 *         large polygons may have visible triangles in the final rendering
	 *         process of the scene (e.g. the polygon of russia) (phenomenon
	 *         observed in X3D)
	 * @see InnerPointsForPolygonClass
	 */
	public double getxRasterWidth() {
		return xRasterWidth;
	}

	/**
	 * First of all, please read the comments for {@link #getxRasterWidth()} to
	 * get to know this parameter.
	 * 
	 * Sets the raster width of the raster which is used to add additional inner
	 * points inside of the feature's polygonal geometry. Thus a finer triangle
	 * mesh is created during the triangulation of any polygon. <br/>
	 * <br/>
	 * This parameter will only be used when
	 * {@link #isGenerateAdditionalInnerPolygonPoints()} is set to true!
	 * 
	 * @param xRasterWidth
	 *            the raster width in x-direction. Note that the value must
	 *            correspond to the coordinate reference system of the geometry
	 *            of the feature. In general: A smaller value causes a finer
	 *            mesh and thus more triangles; Default value is 0.5. 0.5 is
	 *            quite small and thus takes a long time to triangulate the
	 *            polygon; but the result is very good. If one sets a higher
	 *            value (e.g. 1.0), then large polygons may have visible
	 *            triangles in the final rendering process of the scene (e.g.
	 *            the polygon of russia) (phenomenon observed in X3D)
	 */
	public void setxRasterWidth(float xRasterWidth) {
		this.xRasterWidth = xRasterWidth;
	}

	/**
	 * Any polygon of the attributed feature must be triangulated in order to
	 * express the polygon's surface in a scene description language like X3D.
	 * The raster width is used internally to create additional points inside of
	 * the feature's polygon(s). This way, the triangulation will create
	 * additional triangles inside the polygon(s) and the resulting triangle
	 * mesh will be finer. <br />
	 * <br />
	 * However this is only useful under certain circumstances: If all feature
	 * geometries are drawn in a single plane, then there is no need to create
	 * additional triangles. <br />
	 * But let's say you want to draw the features around a world globe (meaning
	 * the geometries are drawn on top of a sphere), then you MUST create
	 * additional inner triangles and create a finer mesh. Otherwise large
	 * geometries will have plane triangles that cut through the spherical
	 * geometry of the world globe.
	 * 
	 * @return
	 */
	public boolean isGenerateAdditionalInnerPolygonPoints() {
		return generateAdditionalInnerPolygonPoints;
	}

	/**
	 * Any polygon of the attributed feature must be triangulated in order to
	 * express the polygon's surface in a scene description language like X3D.
	 * The raster width is used internally to create additional points inside of
	 * the feature's polygon(s). This way, the triangulation will create
	 * additional triangles inside the polygon(s) and the resulting triangle
	 * mesh will be finer. <br />
	 * <br />
	 * However this is only useful under certain circumstances: If all feature
	 * geometries are drawn in a single plane, then there is no need to create
	 * additional triangles. <br />
	 * But let's say you want to draw the features around a world globe (meaning
	 * the geometries are drawn on top of a sphere), then you MUST create
	 * additional inner triangles and create a finer mesh. Otherwise large
	 * geometries will have plane triangles that cut through the spherical
	 * geometry of the world globe. <br />
	 * You should also check {@link #setxRasterWidth(float)} and
	 * {@link #setyRasterWidth(float)}
	 * 
	 * @param generateAdditionalInnerPolygonPoints
	 *            true if you want to generate additional inner points for the
	 *            feature's geometry.
	 * 
	 */
	public void setGenerateAdditionalInnerPolygonPoints(
			boolean generateAdditionalInnerPolygonPoints) {
		this.generateAdditionalInnerPolygonPoints = generateAdditionalInnerPolygonPoints;
	}

	/**
	 * {@inheritDoc} Additionally only geometries with coordinate reference
	 * system 'EPSG:4326' (WGS84) are allowed.
	 */
	@Override
	public void addWorldCountry(VgAttrFeature coloredWorldFeature) {

		// check CRS

		VgGeomObject geometry = coloredWorldFeature.getGeometry();
		String srs = geometry.getSRS();
//		if (!srs.equals(VgGeomObject.SRSLatLonWgs84))
//			throw new T3dException(
//					"A geometry with coordinate reference system (CRS) 'EPSG:4326' (WGS84) "
//							+ "is expected. The CRS of the geometry was: "
//							+ srs + "'.");

		super.addWorldCountry(coloredWorldFeature);
	}

	/**
	 * {@inheritDoc} Additionally each resulting TIN is transformed into a
	 * spherical TIN by using {@link Wgs84ToSphereCoordsTransform}.
	 */
	@Override
	protected List<VgIndexedTIN> calculateTriangulation(
			VgMultiPolygon multiPolygon) {

		int numberOfGeometries = multiPolygon.getNumberOfGeometries();
		List<VgIndexedTIN> vgTINs = new ArrayList<VgIndexedTIN>(
				numberOfGeometries);
		for (int i = 0; i < numberOfGeometries; i++) {

			VgPolygon vgPolygon = (VgPolygon) multiPolygon.getGeometry(i);

			addTINsForPolygon(vgTINs, vgPolygon);
		}

		// transform all WGS84 TINs into spherical TINs.
		vgTINs = Wgs84ToSphereCoordsTransform
				.wgs84ToSphere(vgTINs, this.radius);

		return vgTINs;
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * As the resulting TINs shall be displayed on a sphere, there might be the
	 * need to add additional "Steiner Points" inside the polygon to create
	 * smaller triangles before the triangulation. This is done if
	 * {@link VsWorldCountriesOnASphereScene#isGenerateAdditionalInnerPolygonPoints()}
	 * return true.
	 * </p>
	 */
	@Override
	protected void addTINsForPolygon(List<VgIndexedTIN> vgTINs,
			VgPolygon vgPolygon) {
		if (isGenerateAdditionalInnerPolygonPoints()) {
			List<VgPoint> innerPoints = InnerPointsForPolygonClass
					.getInnerPointsForPolygonUsingRaster(vgPolygon,
							this.xRasterWidth, this.yRasterWidth);

			vgTINs.add(PolygonTriangulator.triangulatePolygon(vgPolygon,
					innerPoints));
		} else {
			vgTINs.add(PolygonTriangulator.triangulatePolygon(vgPolygon));
		}

	}

	/**
	 * The offset of the border is simply added to the radius of the sphere on
	 * which the geometries will be displayed. The radius is stored in the
	 * class-attribute {@link VsWorldCountriesOnASphereScene#radiusForBorder}
	 */
	@Override
	protected VgPoint calculateOffsetBorderPoint(VgPolygon polygon, int i,
			double extrusionHeight) {

		// here we can use the given radius for the transformation instead of
		// calculating the border point with use of the normal-vector
		// so we only increase the radius slightly, so that the border is drawn
		// on top of the polygons
		VgPoint vertex = polygon.getOuterBoundary().getVertex(i);
		return Wgs84ToSphereCoordsTransform.wgs84ToSphere(vertex,
				radiusForBorder + extrusionHeight);

	}

	@Override
	protected VgPoint calculateOffsetBorderPoint(VgLinearRing linearRing,
			int i, double extrusionHeight) {

		VgPoint vertex = linearRing.getVertex(i);
		return Wgs84ToSphereCoordsTransform.wgs84ToSphere(vertex,
				radiusForBorder + extrusionHeight);

	}

	@Override
	protected VgPoint extrudePoint(VgPoint pointSphere, T3dVector normalVector,
			double extrusionHeight) {

		T3dVector pointSphereVector = new T3dVector(pointSphere);
		pointSphereVector = pointSphereVector.norm();

		VgPoint pointSphereExtruded = addOffsetInNormalDirection(
				pointSphereVector, pointSphere, 0, extrusionHeight);

		return pointSphereExtruded;
	}
}
