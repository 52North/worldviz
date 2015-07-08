package org.n52.v3d.worldviz.featurenet.scene;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * Abstract base class for 3D 'Connection Map' scene descriptions. The class holds
 * common attributes and methods.
 * 
 * @author Christian Danowski, Adhitya Kamakshidasan
 *
 */
public abstract class WvizConcreteConnectionMapScene {

	protected WvizVirtualConnectionMapScene scene;

	protected String symbolType;

	// List of Style Parameters that were defined in the XML sheet - More will
	// be added!
	protected String symbolColor, symbolSize;

	protected static final String defaultSymbolSize = "0.15";

	protected String propertyName;

	protected Map<VgFeature, String> labels = new HashMap<VgFeature, String>();

	// Cannot be viable once there are more properties
	protected Map<String, String> svgMap = new HashMap<String, String>();

	protected double displacementX, displacementY,displacementZ;

	// Currently, this file supports only Cylinders, when more parameters are
	// used, this parameter should be used and more if statements should be
	// added.
	public String geometryType;

	protected BufferedWriter document;

	public WvizConcreteConnectionMapScene(WvizVirtualConnectionMapScene scene) {
		this.scene = scene;

		// Check in XML file, if there is a default configuration or not
		getDefaultConfiguration(scene);
	}

	private void getDefaultConfiguration(WvizVirtualConnectionMapScene scene) {
		// We will use 0, for default configuration

		// For specific configuration, we should change our current XML schema
		WvizConfig wvizConfig = scene.getStyle();
		ConnectionNet connectionNet = (ConnectionNet) wvizConfig
				.getConnectionNet().get(0);
		Mapper mapper = (Mapper) connectionNet.getMapper().get(0);
		Features features = (Features) mapper.getFeatures().get(0);
		PointVisualizer pointVisualizer = (PointVisualizer) features
				.getPointVisualizer().get(0);
		T3dSymbol t3dSymbol = (T3dSymbol) pointVisualizer.getT3dSymbol().get(0);
		symbolType = t3dSymbol.getType();
		symbolSize = t3dSymbol.getSize();
		symbolColor = t3dSymbol.getColor();
		TextVisualizer textVisualizer = (TextVisualizer) features
				.getTextVisualizer().get(0);
		Label label = (Label) textVisualizer.getLabel().get(0);
		propertyName = label.getPropertyName();

		Font font = (Font) textVisualizer.getFont().get(0);
		List svgParameter = font.getSvgParameter();

		for (Object object : svgParameter) {
			String name = ((SvgParameter) object).getName();
			String value = ((SvgParameter) object).getValue();
			svgMap.put(name, value);
		}

		LabelPlacement labelPlacement = (LabelPlacement) textVisualizer
				.getLabelPlacement().get(0);
		PointPlacement pointPlacement = (PointPlacement) labelPlacement
				.getPointPlacement().get(0);
		Displacement displacement = (Displacement) pointPlacement
				.getDisplacement().get(0);

		displacementX = displacement.getDisplacementX();
		displacementY = displacement.getDisplacementY();
                displacementZ = displacement.getDisplacementZ();

		Fill fill = (Fill) textVisualizer.getFill().get(0);
		svgParameter = fill.getSvgParameter();

		for (Object object : svgParameter) {
			String name = ((SvgParameter) object).getName();
			String value = ((SvgParameter) object).getValue();
			svgMap.put(name, value);
		}

		Relations relations = (Relations) mapper.getRelations().get(0);
		LineVisualizer lineVisualizer = (LineVisualizer) relations
				.getLineVisualizer().get(0);
		Geometry geometry = (Geometry) lineVisualizer.getGeometry().get(0);
		geometryType = geometry.getType();

		Stroke stroke = (Stroke) lineVisualizer.getStroke().get(0);

		svgParameter = stroke.getSvgParameter();

		for (Object object : svgParameter) {
			String name = ((SvgParameter) object).getName();
			String value = ((SvgParameter) object).getValue();
			svgMap.put(name, value);
		}

		for (VgFeature feature : scene.getVertices()) {
			labels.put(feature, (String) ((GmAttrFeature) feature).getAttributeValue(propertyName));
		}

	}

	/**
	 * 
	 * exports the scene description to a file.
	 *
	 * @param fileName
	 *            File name (and path)
	 */
	public abstract void writeToFile(String fileName);

	protected void writeLine(String pLine) {
		try {
			document.write(pLine);
			document.newLine();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	protected void writeLine() {
		try {
			document.newLine();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
