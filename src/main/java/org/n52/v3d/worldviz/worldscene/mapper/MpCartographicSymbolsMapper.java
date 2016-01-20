package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.extensions.mappers.AttributeValuePair;
import org.n52.v3d.worldviz.extensions.mappers.MpAttrFeature2AttrSymbol;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ColoredSymbol;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ScaledSymbol;
import org.n52.v3d.worldviz.extensions.mappers.T3dAttrSymbolInstance;
import org.n52.v3d.worldviz.helper.StringParser;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsOnASphereScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ColorMapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ColorMapper.ColorPalette;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ColorMapper.ColorPalette.ColorEntry;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ScaleMapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ScaleMapper.ScalePalette;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.ScaleMapper.ScalePalette.ScaleEntry;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.CartographicObjects.SymbolVisualizer.SymbolMapper.T3DSymbol;

public class MpCartographicSymbolsMapper extends MpAbstractXmlDatasetToGlobeVisualizer {

	private MpAttrFeature2AttrSymbol symbolMapper;
	private MpValue2ColoredSymbol symbolColorMapper;
	private MpValue2ScaledSymbol symbolScaleMapper;

	private AxisSwitchTransform axisSwitch = new AxisSwitchTransform();

	public MpCartographicSymbolsMapper(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);
	}

	@Override
	public List<VsAbstractWorldScene> transformToMultipleScenes(XmlDataset xmlDataset, String outputFilePath,
			String fileName) {
		throw new T3dException(
				"This mapper ist not able to create multiple scenes as output. Please use method {@link #transformToSingleScene(XmlDataset)}");
	}

	@Override
	public VsAbstractWorldScene transformToSingleScene(XmlDataset xmlDataset) {
		VsCartographicSymbolsOnASphereScene cartographicSymbolsScene = new VsCartographicSymbolsOnASphereScene();

		this.parameterizeScene(cartographicSymbolsScene, this.wVizConfigFile);

		this.symbolMapper = initializeSymbolMapper(this.wVizConfigFile);

		this.symbolColorMapper = initializeColorMapper(this.wVizConfigFile);

		this.symbolScaleMapper = initializeScaleMapper(this.wVizConfigFile);

		List<VgAttrFeature> geoObjects = xmlDataset.getFeatures();

		List<T3dAttrSymbolInstance> sceneObjects = transformGeoObjectsToSceneObjects(cartographicSymbolsScene,
				geoObjects);

		addSceneObjectsToWorldScene(cartographicSymbolsScene, sceneObjects);

		return cartographicSymbolsScene;
	}

	private MpAttrFeature2AttrSymbol initializeSymbolMapper(WvizConfigDocument wVizConfigFile) {
		return new MpAttrFeature2AttrSymbol();
	}

	private MpValue2ColoredSymbol initializeColorMapper(WvizConfigDocument wVizConfigFile) {
		MpValue2ColoredSymbol symbolColorMapper = new MpValue2ColoredSymbol();

		ColorMapper colorMapperConfig = wVizConfigFile.getWvizConfig().getGlobeVisualization().getCartographicObjects()
				.getSymbolVisualizer().getSymbolMapper().getColorMapper();

		// interpolation mode
		boolean linearInterpolation = true;
		String interpolationType = colorMapperConfig.getInterpolationMode().getType();
		if (!interpolationType.equalsIgnoreCase("linear"))
			linearInterpolation = false;

		// color palette
		ColorPalette colorPalette = colorMapperConfig.getColorPalette();
		ColorEntry[] colorEntryArray = colorPalette.getColorEntryArray();

		int numberOfColorEntries = colorEntryArray.length;

		double[] attributeValues = new double[numberOfColorEntries];
		T3dColor[] colors = new T3dColor[numberOfColorEntries];

		for (int i = 0; i < colorEntryArray.length; i++) {
			ColorEntry colorEntry = colorEntryArray[i];

			attributeValues[i] = colorEntry.getInputValue();

			String outputColorString = colorEntry.getOutputColor().getStringValue();

			colors[i] = StringParser.parseStringAsRgbColor(outputColorString);
		}

		symbolColorMapper.setPalette(attributeValues, colors, linearInterpolation);

		return symbolColorMapper;
	}

	private MpValue2ScaledSymbol initializeScaleMapper(WvizConfigDocument wVizConfigFile) {
		MpValue2ScaledSymbol symbolScaleMapper = new MpValue2ScaledSymbol();

		// set palette
		ScaleMapper scaleMapperConfig = wVizConfigFile.getWvizConfig().getGlobeVisualization().getCartographicObjects()
				.getSymbolVisualizer().getSymbolMapper().getScaleMapper();

		// interpolation mode
		boolean linearInterpolation = true;
		String interpolationType = scaleMapperConfig.getInterpolationMode().getType();
		if (!interpolationType.equalsIgnoreCase("linear"))
			linearInterpolation = false;

		// color palette
		ScalePalette scalePalette = scaleMapperConfig.getScalePalette();
		ScaleEntry[] scaleEntryArray = scalePalette.getScaleEntryArray();

		int numberOfScaleEntries = scaleEntryArray.length;

		double[] attributeValues = new double[numberOfScaleEntries];
		double[] scaleFactors = new double[numberOfScaleEntries];

		for (int i = 0; i < scaleEntryArray.length; i++) {
			ScaleEntry scaleEntry = scaleEntryArray[i];

			attributeValues[i] = scaleEntry.getInputValue();

			scaleFactors[i] = scaleEntry.getOutputScale();
		}

		symbolScaleMapper.setPalette(attributeValues, scaleFactors, linearInterpolation);

		return symbolScaleMapper;
	}

	private List<T3dAttrSymbolInstance> transformGeoObjectsToSceneObjects(
			VsCartographicSymbolsOnASphereScene cartographicSymbolsScene, List<VgAttrFeature> geoObjects) {

		List<T3dAttrSymbolInstance> symbols = new ArrayList<T3dAttrSymbolInstance>(geoObjects.size());

		for (VgAttrFeature vgAttrFeature : geoObjects) {
			// the geometry in THIS case is a point, thus we may cast it
			// normally an instanceOf-check must be done
			VgPoint geometry = (VgPoint) vgAttrFeature.getGeometry();

			// point in virtual world (axes need to be switched.)
			T3dVector virtualPoint = axisSwitch.transform(geometry);

			T3dAttrSymbolInstance symbol = createSymbol(vgAttrFeature, virtualPoint);

			Object attributeValue = vgAttrFeature.getAttributeValue(this.attributeNameForMapping);

			AttributeValuePair attrValuePair = new AttributeValuePair(this.attributeNameForMapping, attributeValue);

			T3dAttrSymbolInstance scaledSymbol = symbolScaleMapper.scaleTotal(symbol, attrValuePair);

			T3dAttrSymbolInstance scaledColoredSymbol = symbolColorMapper.transform(scaledSymbol, attrValuePair);

			symbols.add(scaledColoredSymbol);
		}

		return symbols;
	}

	private T3dAttrSymbolInstance createSymbol(VgAttrFeature vgAttrFeature, T3dVector virtualPoint) {
		T3DSymbol t3dSymbol = wVizConfigFile.getWvizConfig().getGlobeVisualization().getCartographicObjects()
				.getSymbolVisualizer().getSymbolMapper().getT3DSymbol();
		String type = t3dSymbol.getType();

		T3dAttrSymbolInstance symbol;

		if (type.equalsIgnoreCase("Box")) {
			symbol = this.symbolMapper.createBoxSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ()));
		} else if (type.equalsIgnoreCase("Cylinder"))
			symbol = this.symbolMapper.createCylinderSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ()));

		else if (type.equalsIgnoreCase("Cone"))
			symbol = this.symbolMapper.createConeSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ()));

		else if (type.equalsIgnoreCase("Cube"))
			symbol = this.symbolMapper.createCubeSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ()));

		else
			symbol = this.symbolMapper.createSphereSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ()));

		// apply initial size/scale
		symbol.setScale(t3dSymbol.getSize());

		return symbol;
	}

	private void addSceneObjectsToWorldScene(VsCartographicSymbolsOnASphereScene cartographicSymbolsScene,
			List<T3dAttrSymbolInstance> sceneObjects) {

		for (T3dAttrSymbolInstance symbol : sceneObjects) {
			cartographicSymbolsScene.addCartographicSymbol(symbol);
		}

	}

	@Override
	protected void parameterizeScene(VsAbstractWorldScene scene, WvizConfigDocument wVizConfigFile) {

		super.parameterizeScene(scene, wVizConfigFile);

		VsCartographicSymbolsOnASphereScene symbolsScene = (VsCartographicSymbolsOnASphereScene) scene;

		/*
		 * DEFAULT COLOR
		 */
		String defaultColorString = wVizConfigFile.getWvizConfig().getGlobeVisualization().getCartographicObjects()
				.getSymbolVisualizer().getSymbolMapper().getT3DSymbol().getDefaultColor();
		T3dColor defaultColor = StringParser.parseStringAsRgbColor(defaultColorString);
		symbolsScene.setDefaultColor(defaultColor);
	}
}
