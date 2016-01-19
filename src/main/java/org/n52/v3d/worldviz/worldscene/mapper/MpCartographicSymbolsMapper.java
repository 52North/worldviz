package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsOnASphereScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

public class MpCartographicSymbolsMapper extends MpAbstractXmlDatasetToGlobeVisualizer {

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

		// parameterizeScene(cartographicSymbolsScene, this.wVizConfigFile);
		//
		// this.colorMapper = initializeColorMapper(this.wVizConfigFile);
		//
		//
		// List<VgAttrFeature> geoObjects = xmlDataset.getFeatures();
		//
		// List<VgAttrFeature> sceneObjects =
		// transformGeoObjectsToSceneObjects(cartographicSymbolsScene,
		// geoObjects);
		//
		// addSceneObjectsToWorldScene(cartographicSymbolsScene, sceneObjects);
		//
		// return cartographicSymbolsScene;
		throw new T3dNotYetImplException();
	}

}
