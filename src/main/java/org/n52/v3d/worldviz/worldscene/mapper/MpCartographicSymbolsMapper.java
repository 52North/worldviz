package org.n52.v3d.worldviz.worldscene.mapper;

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsOnASphereScene;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

public class MpCartographicSymbolsMapper extends MpAbstractXmlDatasetVisualizer {

	public MpCartographicSymbolsMapper(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);
	}

	@Override
	public VsAbstractWorldScene transform(XmlDataset xmlDataset) {
		VsCartographicSymbolsOnASphereScene cartographicSymbolsScene = new VsCartographicSymbolsOnASphereScene();

		throw new T3dNotYetImplException();

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
	}

}
