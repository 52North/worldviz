package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.gisimplm.GmSimpleElevationGrid;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgIndexedTIN;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2NumericExtent;
import org.n52.v3d.worldviz.triangulation.InnerPointsForPolygonClass;
import org.n52.v3d.worldviz.triangulation.PolygonTriangulator;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsDrapedWorldSphereScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.Globe.Deformation.DeformationMapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.Globe.Deformation.DeformationMapper.DeformationPalette;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.Globe.Deformation.DeformationMapper.DeformationPalette.DeformationEntry;

public class MpXmlDatasetToDeformedGlobe extends MpDrapedWorldSphere {

	private static final double WGS84_TO_ELEV_GRID_ADDITION_X = 180;
	private static final double WGS84_TO_ELEV_GRID_ADDITION_Y = 90;

	private MpValue2NumericExtent deformationMapper = null;

	public MpXmlDatasetToDeformedGlobe(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);
	}

	@Override
	public VsAbstractWorldScene transformToSingleScene(XmlDataset xmlDataset) {

		VsDrapedWorldSphereScene deformedGlobeScene = new VsDrapedWorldSphereScene();

		this.parameterizeScene(deformedGlobeScene, this.wVizConfigFile);

		this.deformationMapper = initializeDeformationMapper(this.wVizConfigFile);

		List<VgAttrFeature> geoObjects = xmlDataset.getFeatures();

		GmSimpleElevationGrid earthElevGrid = createFlatEarthElevationGrid();

		deformElevationGrid(geoObjects, earthElevGrid);

		deformedGlobeScene.setEarthElevGrid(earthElevGrid);

		return deformedGlobeScene;
	}

	private MpValue2NumericExtent initializeDeformationMapper(WvizConfigDocument wVizConfigFile) {
		MpValue2NumericExtent deformationMapper = new MpValue2NumericExtent();

		DeformationMapper deformationMapperConfig = this.wVizConfigFile.getWvizConfig().getGlobeVisualization()
				.getGlobe().getDeformation().getDeformationMapper();

		boolean linearInterpolation = true;
		String interpolationType = deformationMapperConfig.getInterpolationMode().getType();
		if (!interpolationType.equalsIgnoreCase("linear"))
			linearInterpolation = false;

		DeformationPalette deformationPalette = deformationMapperConfig.getDeformationPalette();
		DeformationEntry[] deformationEntryArray = deformationPalette.getDeformationEntryArray();

		int numberOfDeformationEntries = deformationEntryArray.length;

		double[] attributeValues = new double[numberOfDeformationEntries];
		double[] outputDeformationValues = new double[numberOfDeformationEntries];

		for (int i = 0; i < deformationEntryArray.length; i++) {
			DeformationEntry deformationEntry = deformationEntryArray[i];

			attributeValues[i] = deformationEntry.getInputValue();

			outputDeformationValues[i] = deformationEntry.getOutputDeformation();
		}

		deformationMapper.setPalette(attributeValues, outputDeformationValues, linearInterpolation);

		return deformationMapper;
	}

	private void deformElevationGrid(List<VgAttrFeature> geoObjects, GmSimpleElevationGrid earthElevGrid) {
		for (VgAttrFeature feature : geoObjects) {
			 
			double deformationValue = calculateDeformationValue(feature);

			applyDeformationToElevGrid(earthElevGrid, deformationValue, feature);
		}
	}

	private void applyDeformationToElevGrid(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			VgAttrFeature feature) {
		VgGeomObject geometry = feature.getGeometry();

		String srs = geometry.getSRS();
		if (!srs.equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84))
			throw new T3dException(
					"Globe deformation cannot be done since the feature used a different coordinate reference system than WGS84!"
							+ "It used '" + srs + "'");

		/*
		 * depending on the geometry type of the feature, single or multiple
		 * cell points of the elevation grid must be addressed
		 */

		if (geometry instanceof VgPoint)
			applyWgs84PointDeformation(earthElevGrid, deformationValue, (VgPoint) geometry);

		else if (geometry instanceof VgMultiPolygon)
			applyWgs84MultiPolygonDeformation(earthElevGrid, deformationValue, (VgMultiPolygon) geometry);

		else if (geometry instanceof VgPolygon)
			applyWgs84PolygonDeformation(earthElevGrid, deformationValue, (VgPolygon) geometry);

		else
			throw new T3dNotYetImplException();
	}

	private void applyWgs84PointDeformation(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			VgPoint point_wgs84) {

		/*
		 * actually, the following if-statements are irrelevant. However, while
		 * testing I (Christian) discovered a problem with Antarctica ("AQ"). It
		 * contains WGS84 coordinates which are not inside {(-180, -90), (180,
		 * 90)}. Such coordinates will simply be reset on the border!
		 */
		if (point_wgs84.getX() > 180)
			point_wgs84.setX(180);
		else if (point_wgs84.getX() < -180)
			point_wgs84.setX(-180);

		if (point_wgs84.getY() < -90)
			point_wgs84.setY(-90);
		else if (point_wgs84.getY() > 90)
			point_wgs84.setY(90);

		// transform wgs84 to elevationGrid-indices and apply deformationValue
		// to that point
		VgPoint elevGridIndices = transformWGS84ToElevGrid(point_wgs84);
		int indexRow = (int) elevGridIndices.getY();
		int indexColumn = (int) elevGridIndices.getX();

		earthElevGrid.setValue(indexRow, indexColumn, deformationValue);
	}

	private void applyWgs84MultiPolygonDeformation(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			VgMultiPolygon multiPolygon_wgs84) {

		int numberOfPolygons = multiPolygon_wgs84.getNumberOfGeometries();

		for (int i = 0; i < numberOfPolygons; i++) {
			VgPolygon polygon = (VgPolygon) multiPolygon_wgs84.getGeometry(i);

			applyWgs84PolygonDeformation(earthElevGrid, deformationValue, polygon);
		}

	}

	/**
	 * Calculates a triangulation of the polygon and applies the
	 * deformationValue to each triangulated point.
	 * 
	 * @param earthElevGrid
	 * @param deformationValue
	 * @param polygon_wgs84
	 */
	private void applyWgs84PolygonDeformation(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			VgPolygon polygon_wgs84) {

		List<VgPoint> innerPoints = InnerPointsForPolygonClass.getInnerPointsForPolygonUsingRaster(polygon_wgs84,
				ELEV_GRID_DELTA_X, ELEV_GRID_DELTA_Y);

		VgIndexedTIN vgTIN_wgs84 = PolygonTriangulator.triangulatePolygon(polygon_wgs84, innerPoints);

		// applyEachTIN to elevationGrid
		applyWgs84TINDeformation(earthElevGrid, deformationValue, vgTIN_wgs84);

	}

	private void applyWgs84TINsDeformation(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			List<VgIndexedTIN> vgTINs_wgs84) {

		for (VgIndexedTIN vgTIN_wgs84 : vgTINs_wgs84) {
			applyWgs84TINDeformation(earthElevGrid, deformationValue, vgTIN_wgs84);
		}

	}

	private void applyWgs84TINDeformation(GmSimpleElevationGrid earthElevGrid, double deformationValue,
			VgIndexedTIN vgTIN_wgs84) {

		int numberOfPoints = vgTIN_wgs84.numberOfPoints();

		for (int i = 0; i < numberOfPoints; i++) {
			VgPoint tinPoint_wgs84 = vgTIN_wgs84.getPoint(i);
			applyWgs84PointDeformation(earthElevGrid, deformationValue, tinPoint_wgs84);
		}
	}

	/**
	 * The elevation grid has it's origin in the lower left corner with index
	 * (0,0) that represents WGS84 coordinate (-180,-90). Thus to locate the
	 * correct elevation grid indices a constant vector (180, 90) must be added
	 * to each WGS84 coordinate.
	 * 
	 * @param point_wgs84
	 * @return
	 */
	private VgPoint transformWGS84ToElevGrid(VgPoint point_wgs84) {
		/*
		 * since geocoordinates refer to a different location inside the
		 * elevationGrid for the globe, those coordinates must be adjusted!
		 * 
		 * E.g. point (0,0) refers to top-left corner of elevationGrid but point
		 * (0,0) in WGS84 refers to the mid-point of the elevation grid
		 * 
		 * 
		 */

		double x_wgs84 = point_wgs84.getX();
		double y_wgs84 = point_wgs84.getY();

		double x_elevGrid = x_wgs84 + WGS84_TO_ELEV_GRID_ADDITION_X;
		double y_elevGrid = y_wgs84 + WGS84_TO_ELEV_GRID_ADDITION_Y;
		double z_elevGrid = 0;

		return new GmPoint(x_elevGrid, y_elevGrid, z_elevGrid);
	}

	private double calculateDeformationValue(VgAttrFeature feature) {
		Object attributeObject = feature.getAttributeValue(this.attributeNameForMapping);
		double attributeDouble = parseAsDouble(attributeObject);
		double deformationValue = this.deformationMapper.transform(attributeDouble);

		return deformationValue;
	}

	private double parseAsDouble(Object attributeValue) {
		double doubleValue = 0;

		if (attributeValue instanceof String) {
			String attributeValueString = (String) attributeValue;
			try {
				doubleValue = Double.parseDouble(attributeValueString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if (attributeValue instanceof Double || attributeValue instanceof Float)
			doubleValue = (Double) attributeValue;

		return doubleValue;
	}

}
