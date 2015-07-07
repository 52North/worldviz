package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This is a subclass of {@link NormTransform} that is only applicable to
 * coordinates in coordinate reference system WGS84 (Longitude, Latitude,
 * Altitude).<br/>
 * <br/>
 * 
 * When transforming WGS84 coordinates via this class, the normalization of
 * coordinates will be executed within a fixed spatial extent. This fixed
 * spatial extent refers to the bounding box of possible WGS84-coordinates. <br/>
 * <br/>
 * The bounding box is given by the points (X,Y,Z = Longitude, Latitude,
 * Altitude): <code>LowerLeft(-180, -90, 0), TopRight(180, 90, 0)</code>.
 * 
 * @author Christian Danowski
 *
 */
public class NormTransform_Wgs84 extends NormTransform {

	public NormTransform_Wgs84(VgPoint[] geoPos) {
		super(geoPos);
	}

	public NormTransform_Wgs84(ArrayList<VgPoint> geoPos) {
		super(geoPos);
	}

	/**
	 * This method adds the bounding box coordinates of WGS84 coordinate
	 * reference system to the collection of georeferenced points. Thus this
	 * specialized subclass of {@link NormTransform} works with a fixed spatial
	 * extent. </br></br>
	 * 
	 * The bounding box is given by the points (X,Y,Z = Longitude, Latitude,
	 * Altitude): <code>LowerLeft(-180, -90, 0), TopRight(180, 90, 0)</code>.
	 * 
	 * @param geoPos
	 *            to this collection of points the two points
	 *            <code>(-180, -90, 0), (180, 90, 0)</code> are added
	 */
	@Override
	protected void addSpecialCoordinates(VgPoint[] geoPos) {
		VgPoint[] bbox_wgs84 = createBboxPoints();

		List<VgPoint> geoPosList = Arrays.asList(geoPos);

		geoPosList.add(bbox_wgs84[0]);
		geoPosList.add(bbox_wgs84[1]);

		VgPoint[] newGeoPos = new VgPoint[geoPosList.size()];

		newGeoPos = geoPosList.toArray(newGeoPos);

		geoPos = newGeoPos;
	}

	/**
	 * This method adds the bounding box coordinates of WGS84 coordinate
	 * reference system to the collection of georeferenced points. Thus this
	 * specialized subclass of {@link NormTransform} works with a fixed spatial
	 * extent. </br></br>
	 * 
	 * The bounding box is given by the points (X,Y,Z = Longitude, Latitude,
	 * Altitude): <code>LowerLeft(-180, -90, 0), TopRight(180, 90, 0)</code>.
	 * 
	 * @param geoPos
	 *            to this collection of points the two points
	 *            <code>(-180, -90, 0), (180, 90, 0)</code> are added
	 */
	@Override
	protected void addSpecialCoordinates(List<VgPoint> geoPos) {
		VgPoint[] bbox_wgs84 = createBboxPoints();

		geoPos.add(bbox_wgs84[0]);
		geoPos.add(bbox_wgs84[1]);
	}

	private VgPoint[] createBboxPoints() {
		VgPoint bboxMinPoint = new GmPoint(-180, -90, 0);
		bboxMinPoint.setSRS(VgPoint.SRSLatLonWgs84);

		VgPoint bboxMaxPoint = new GmPoint(180, 90, 0);
		bboxMaxPoint.setSRS(VgPoint.SRSLatLonWgs84);

		VgPoint[] bbox_wgs84 = new VgPoint[] { bboxMinPoint, bboxMaxPoint };

		return bbox_wgs84;
	}

}
