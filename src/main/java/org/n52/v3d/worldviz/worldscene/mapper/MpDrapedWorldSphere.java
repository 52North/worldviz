package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.gisimplm.GmSimpleElevationGrid;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.StringParser;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsDrapedWorldSphereScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.Globe;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.Globe.GlobeTexture.AdditionalTextures;

public class MpDrapedWorldSphere extends MpAbstractXmlDatasetToGlobeVisualizer {

	private static final double INITIAL_ELEVATION = 0.0;
	protected static final double ELEV_GRID_DELTA_Y = 1.;
	protected static final double ELEV_GRID_DELTA_X = 1.;
	private static final int ELEV_GRID_NUMBER_ROWS = 181;
	private static final int ELEV_GRID_NUMBER_COLUMNS = 361;
	private static final GmPoint ORIGIN_ELEV_GRID = new GmPoint(-180, -90, 0);

	public MpDrapedWorldSphere(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);
	}

	@Override
	public VsAbstractWorldScene transformToSingleScene(XmlDataset xmlDataset) {

		VsDrapedWorldSphereScene deformedGlobeScene = new VsDrapedWorldSphereScene();

		this.parameterizeScene(deformedGlobeScene, this.wVizConfigFile);

		GmSimpleElevationGrid earthElevGrid = createFlatEarthElevationGrid();

		deformedGlobeScene.setEarthElevGrid(earthElevGrid);

		return deformedGlobeScene;
	}

	@Override
	protected void parameterizeScene(VsAbstractWorldScene scene, WvizConfigDocument wVizConfigFile) {

		super.parameterizeScene(scene, wVizConfigFile);

		VsDrapedWorldSphereScene deformedGlobeScene = (VsDrapedWorldSphereScene) scene;

		GlobeVisualization globeVisualization = wVizConfigFile.getWvizConfig().getGlobeVisualization();

		Globe globe = globeVisualization.getGlobe();

		// GLOBE COLOR
		T3dColor globeColor = StringParser.parseStringAsRgbColor(globe.getGlobeColor().getStringValue());
		deformedGlobeScene.setSphereColor(globeColor);

		// GLOBE RADIUS
		deformedGlobeScene.setDefaultRadiusForSphere(globe.getGlobeRadius());

		// GLOBE TEXTURE
		deformedGlobeScene.setPathToMainSphereTexture(globe.getGlobeTexture().getMainTexture().getTexturePath());

		// GLOBE ADDITIONAL TEXTURES
		AdditionalTextures additionalTextures = globe.getGlobeTexture().getAdditionalTextures();
		String textureMode = additionalTextures.getMode();
		String texturePath = additionalTextures.getAdditionalTexture().getTexturePath();
		deformedGlobeScene.setAdditionalTexturePaths(new String[] { texturePath });

	}

	protected GmSimpleElevationGrid createFlatEarthElevationGrid() {
		GmSimpleElevationGrid earthElevGrid = new GmSimpleElevationGrid(ELEV_GRID_NUMBER_COLUMNS, ELEV_GRID_NUMBER_ROWS,
				ORIGIN_ELEV_GRID, ELEV_GRID_DELTA_X, ELEV_GRID_DELTA_Y);

		for (int i = 0; i < earthElevGrid.numberOfColumns(); i++) {
			for (int j = 0; j < earthElevGrid.numberOfRows(); j++) {
				earthElevGrid.setValue(j, i, INITIAL_ELEVATION);
			}
		}
		return earthElevGrid;
	}

	@Override
	public List<VsAbstractWorldScene> transformToMultipleScenes(XmlDataset xmlDataset, String outputFilePath,
			String fileName) {
		throw new T3dException(
				"This mapper ist not able to create multiple scenes as output. Please use method {@link #transformToSingleScene(XmlDataset)}");
	}

}
