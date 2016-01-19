package org.n52.v3d.worldviz.worldscene.mapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlCountryCodeDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlPointDataset;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsJoinedInlineScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

public class MpXmlDatasetVisualizer extends MpAbstractXmlDatasetToGlobeVisualizer {

	private static final int MINIMUM_NUMER_WORLD_SCENES = 3;

	private static final String RESOURCES_FOLDER = "/resources/";
	private static final String BASE_WORLD_SPHERE_ADDITION = "_worldSphere";
	private static final String DEFORMED_WORLD_SPHERE_ADDITION = "_deformedWorldSphere";
	private static final String COUNTRIES_ADDITION = "_countries";
	private static final String POINT_SYMBOLS_ADDITION = "_pointSymbols";

	private static final String X3D_ENDING = ".x3d";
	private static final String X3DOM_ENDING = ".html";

	private boolean x3dom_mode = false;

	public MpXmlDatasetVisualizer(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);

		String outputFormat = this.wVizConfigFile.getWvizConfig().getGlobeVisualization().getOutputFormat().getFormat();

		/*
		 * x3dom_mode was initialized with false already! So just if x3dom mode
		 * is desired that value has to be changed
		 */
		if (outputFormat.equalsIgnoreCase("X3DOM"))
			this.x3dom_mode = true;
	}

	@Override
	public List<VsAbstractWorldScene> transformToMultipleScenes(XmlDataset xmlDataset, String outputFilePath,
			String fileName) {
		/*
		 * initialize an empty list of scenes, since there is need for multiple
		 * scenes:
		 * 
		 * 1. (optional) deformed globe scene
		 * 
		 * 2. underlying flat/base globe
		 * 
		 * 3. countries or point symbols scene
		 * 
		 * 4. scene that joins both prior scenes
		 */
		List<VsAbstractWorldScene> worldScenes = new ArrayList<VsAbstractWorldScene>(MINIMUM_NUMER_WORLD_SCENES);

		/*
		 * 1. (optional) deformed globe scene
		 */

		String deformGlobeString = this.wVizConfigFile.getWvizConfig().getGlobeVisualization().getGlobe()
				.getDeformation().getDeformGlobe();
		boolean deformGlobe = Boolean.parseBoolean(deformGlobeString);
		if (deformGlobe) {
			MpXmlDatasetToDeformedGlobe deformedGlobeMapper = new MpXmlDatasetToDeformedGlobe(this.wVizConfigFile,
					this.attributeNameForMapping);
			VsAbstractWorldScene deformedGlobeScene = deformedGlobeMapper.transformToSingleScene(xmlDataset);

			// output file
			String outputFilePath_deformedGlobe = outputFilePath.concat("\\" + fileName)
					.concat(DEFORMED_WORLD_SPHERE_ADDITION);
			outputFilePath_deformedGlobe = this.concatFileEnding(outputFilePath_deformedGlobe);
			deformedGlobeScene.setOutputFile(new File(outputFilePath_deformedGlobe));

			worldScenes.add(deformedGlobeScene);
		}

		/*
		 * 2. flat/basic globe scene
		 */
		MpDrapedWorldSphere baseWorldSphereCreator = new MpDrapedWorldSphere(wVizConfigFile, attributeNameForMapping);
		VsAbstractWorldScene baseWorldSphere = baseWorldSphereCreator.transformToSingleScene(xmlDataset);

		// output file
		String outputFilePath_baseWorldSphereScene = outputFilePath.concat(RESOURCES_FOLDER).concat(fileName)
				.concat(BASE_WORLD_SPHERE_ADDITION);
		outputFilePath_baseWorldSphereScene = this.concatFileEnding(outputFilePath_baseWorldSphereScene);
		baseWorldSphere.setOutputFile(new File(outputFilePath_baseWorldSphereScene));

		worldScenes.add(baseWorldSphere);

		/*
		 * 3. countries / point symbols scene
		 */
		if (xmlDataset instanceof XmlCountryCodeDataset) {
			/*
			 * countries
			 */
			MpWorldCountriesMapper countriesSceneMapper = new MpWorldCountriesMapper(this.wVizConfigFile,
					this.attributeNameForMapping);
			VsAbstractWorldScene countriesScene = countriesSceneMapper.transformToSingleScene(xmlDataset);

			// output file
			String outputFilePath_countries = outputFilePath.concat(RESOURCES_FOLDER).concat(fileName)
					.concat(COUNTRIES_ADDITION);
			outputFilePath_countries = this.concatFileEnding(outputFilePath_countries);
			countriesScene.setOutputFile(new File(outputFilePath_countries));

			worldScenes.add(countriesScene);
		}

		/*
		 * point symbols
		 */
		else if (xmlDataset instanceof XmlPointDataset) {
			MpCartographicSymbolsMapper cartographicSymbolsSceneMapper = new MpCartographicSymbolsMapper(
					this.wVizConfigFile, this.attributeNameForMapping);
			VsAbstractWorldScene cartographicSymbolsScene = cartographicSymbolsSceneMapper
					.transformToSingleScene(xmlDataset);

			// output file
			String outputFilePath_symbols = outputFilePath.concat(RESOURCES_FOLDER).concat(fileName)
					.concat(POINT_SYMBOLS_ADDITION);
			outputFilePath_symbols = this.concatFileEnding(outputFilePath_symbols);
			cartographicSymbolsScene.setOutputFile(new File(outputFilePath_symbols));

			worldScenes.add(cartographicSymbolsScene);
		}

		/*
		 * 4. joinedScene
		 */
		// output file
		String outputFilePath_joinedScene = outputFilePath.concat("\\" + fileName);
		outputFilePath_joinedScene = this.concatFileEnding(outputFilePath_joinedScene);

		List<VsAbstractWorldScene> joinableWorldScenes = new ArrayList<VsAbstractWorldScene>();
		// this just copies/links both prior scenes into a new list
		if (worldScenes.size() == 3) {
			joinableWorldScenes.add(worldScenes.get(1));
			joinableWorldScenes.add(worldScenes.get(2));
		} else {
			joinableWorldScenes.add(worldScenes.get(0));
			joinableWorldScenes.add(worldScenes.get(1));
		}

		VsJoinedInlineScene joinedScene = new VsJoinedInlineScene(outputFilePath_joinedScene, joinableWorldScenes,
				this.wVizConfigFile);

		worldScenes.add(joinedScene);

		return worldScenes;

	}

	@Override
	public VsAbstractWorldScene transformToSingleScene(XmlDataset xmlDataset) {
		throw new T3dException(
				"This mapper ist not able to create a single scene as output. Please use method {@link #transformToMultipleScenes(XmlDataset)}");
	}

	private String concatFileEnding(String fileName) {
		if (this.x3dom_mode) {
			fileName = fileName.concat(X3DOM_ENDING);
		} else {
			fileName = fileName.concat(X3D_ENDING);
		}

		return fileName;
	}

}
