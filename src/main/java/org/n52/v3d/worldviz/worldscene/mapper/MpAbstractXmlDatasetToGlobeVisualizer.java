package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.List;

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.triturus.vscene.VsCamera;
import org.n52.v3d.triturus.vscene.VsViewpoint;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.StringParser;
import org.n52.v3d.worldviz.worldscene.OutputFormatEnum;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.Viewpoint;

/**
 * Abstract mapper to map/transform the geo-objects inside an XmlDataset-object
 * to visualization objects.
 * 
 * @author Christian Danowski
 *
 */
public abstract class MpAbstractXmlDatasetToGlobeVisualizer {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected WvizConfigDocument wVizConfigFile;

	protected String attributeNameForMapping;

	protected CountryBordersLODEnum countryBorderLOD = CountryBordersLODEnum.SIMPLIFIED_50m;

	/**
	 * Constructor using a configuration file
	 * 
	 * @param wVizConfigFile
	 * @param attributeNameForMapping
	 *            name of the attribute of the features that is used to color
	 *            and extrude each feature.
	 */
	public MpAbstractXmlDatasetToGlobeVisualizer(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		this.wVizConfigFile = wVizConfigFile;
		this.attributeNameForMapping = attributeNameForMapping;
	}

	/**
	 * Sets the configuration file
	 * 
	 * @param wVizConfigFile
	 */
	public void setStyle(WvizConfigDocument wVizConfigFile) {
		this.wVizConfigFile = wVizConfigFile;
	}

	/**
	 * This parameter is only relevant when country code datasets are mapped (
	 * {@link MpWorldCountriesMapper} in the mapper's hierarchy).
	 * 
	 * @param countryBorderLOD
	 *            declares the level of detail for the world borders
	 */
	public void setCountryBorderLOD(CountryBordersLODEnum countryBorderLOD) {
		this.countryBorderLOD = countryBorderLOD;
	}

	/**
	 * Sets standard parameters, that are valid for each scene, independent from
	 * it's concrete type. <br/>
	 * Each subtype shall override this method and add scene-type-specific
	 * parameters.
	 * 
	 * @param scene
	 * @param wVizConfigFile
	 */
	protected void parameterizeScene(VsAbstractWorldScene scene, WvizConfigDocument wVizConfigFile) {
		// TODO Viewpoint
		/*
		 * Viewpoint is given in the following format:
		 * 
		 * <Viewpoint position='0 3 30' orientation='1 0 0 -0.1'/>
		 * 
		 */
		Viewpoint viewpoint = wVizConfigFile.getWvizConfig().getViewpoint();
		String position = viewpoint.getPosition();
		scene.setX3dViewpointPosition(position);
		String orientation = viewpoint.getOrientation();
		scene.setX3dViewpointOrientation(orientation);

		VsCamera camera = new VsCamera();
		VsViewpoint cameraViewpoint = new VsViewpoint();

		// CAMERA POSITION / LOOK FROM
		String positionAsCommaSepString = position.replace(' ', ',');
		VgPoint cameraPosition = new GmPoint(positionAsCommaSepString);
		cameraViewpoint.setLookFrom(cameraPosition);

		// CAMERA ORIENTATION / LOOK AT
		VgPoint lookAt = calculateLookAtPoint(cameraPosition, orientation);
		cameraViewpoint.setLookAt(lookAt);

		camera.addViewpoint(cameraViewpoint);
		scene.addCamera(camera);

		GlobeVisualization globeVisualization = wVizConfigFile.getWvizConfig().getGlobeVisualization();

		// OUTPUT FORMAT
		String outputFormat = globeVisualization.getOutputFormat().getFormat();

		if (outputFormat.equalsIgnoreCase(OutputFormatEnum.X3D.toString()))
			scene.setOutputFormat(OutputFormatEnum.X3D);
		else if (outputFormat.equalsIgnoreCase(OutputFormatEnum.X3DOM.toString()))
			scene.setOutputFormat(OutputFormatEnum.X3DOM);
		else
			throw new T3dNotYetImplException();

		// BACKGROUND COLOR
		String skyColorString = wVizConfigFile.getWvizConfig().getBackground().getSkyColor();
		T3dColor backgroundColorRGB = StringParser.parseStringAsRgbColor(skyColorString);
		scene.setBackgroundColor(backgroundColorRGB);

	}

	private VgPoint calculateLookAtPoint(VgPoint cameraPosition, String orientation) {
		/*
		 * <Viewpoint position='0 3 30' orientation='1 0 0 -0.1'/>
		 * 
		 * Thus orientationString looks like "1 0 0 -0.1".
		 * 
		 * 1. axis of rotation (X-axis in example)
		 * 
		 * 2. angle in radiant
		 * 
		 * --------- standard looakAt vektor in virtual reality --> (0, 0,
		 * -1)!!!
		 */
		T3dVector standardLookAtInVR = new T3dVector(0, 0, -1);

		String[] orientationComponents = orientation.split(" ");
		String axisAsCommaSepString = orientationComponents[0] + "," + orientationComponents[1] + ","
				+ orientationComponents[2];
		VgPoint axis = new GmPoint(axisAsCommaSepString);
		T3dVector axisOfRotation = new T3dVector(axis);

		double angleInRadians = Double.parseDouble(orientationComponents[3]);

		T3dVector lookAtVector = standardLookAtInVR;
		if (axisOfRotation.getX() != 0)
			lookAtVector = rotateYZ(standardLookAtInVR, angleInRadians);
		else if (axisOfRotation.getY() != 0)
			lookAtVector = rotateXZ(standardLookAtInVR, angleInRadians);
		else
			lookAtVector = rotateXY(standardLookAtInVR, angleInRadians);

		// apply lookAtVector to cameraPosition
		T3dVector lookFrom = new T3dVector(cameraPosition);
		T3dVector lookAtPointCoords = new T3dVector();
		lookAtPointCoords.assignSum(lookFrom, lookAtVector);

		return new GmPoint(lookAtPointCoords.getX(), lookAtPointCoords.getY(), lookAtPointCoords.getZ());

	}

	private T3dVector rotateXY(T3dVector standardLookAtInVR, double angleInRadians) {
		double x_rotated = Math.cos(angleInRadians) * standardLookAtInVR.getX()
				- Math.sin(angleInRadians) * standardLookAtInVR.getY();
		double y_rotated = Math.sin(angleInRadians) * standardLookAtInVR.getX()
				+ Math.cos(angleInRadians) * standardLookAtInVR.getY();
		return new T3dVector(x_rotated, y_rotated, standardLookAtInVR.getZ());
	}

	private T3dVector rotateXZ(T3dVector standardLookAtInVR, double angleInRadians) {
		double x_rotated = Math.cos(angleInRadians) * standardLookAtInVR.getX()
				+ Math.sin(angleInRadians) * standardLookAtInVR.getZ();
		double z_rotated = -Math.sin(angleInRadians) * standardLookAtInVR.getX()
				+ Math.sin(angleInRadians) * standardLookAtInVR.getZ();
		return new T3dVector(x_rotated, standardLookAtInVR.getY(), z_rotated);
	}

	private T3dVector rotateYZ(T3dVector standardLookAtInVR, double angleInRadians) {
		double y_rotated = Math.cos(angleInRadians) * standardLookAtInVR.getY()
				- Math.sin(angleInRadians) * standardLookAtInVR.getZ();
		double z_rotated = Math.sin(angleInRadians) * standardLookAtInVR.getY()
				+ Math.cos(angleInRadians) * standardLookAtInVR.getZ();
		return new T3dVector(standardLookAtInVR.getX(), y_rotated, z_rotated);
	}

	/**
	 * Transforms the content of the xmlDataset-object (these are GEO-objects)
	 * to a scene description.
	 * 
	 * @param xmlDataset
	 * @return
	 */
	public abstract VsAbstractWorldScene transformToSingleScene(XmlDataset xmlDataset);

	/**
	 * Transforms the content of the xmlDataset-object (these are GEO-objects)
	 * to multiple scene descriptions. <br/>
	 * <br/>
	 * 
	 * The returned list of scenes has the following order of elements: <list>
	 * <li>(optional) 1. deformed globe</li>
	 * <li>2. Basic (flat) globe</li>
	 * <li>3. either countries scene or point symbols scene (which acts as an
	 * overlay on top of the base globe)</li>
	 * <li>4. joined scene that combines both prior scenes to a single scene
	 * </li> </list>
	 * 
	 * @param xmlDataset
	 * @return
	 */
	public abstract List<VsAbstractWorldScene> transformToMultipleScenes(XmlDataset xmlDataset, String outputFilePath,
			String fileName);

}
