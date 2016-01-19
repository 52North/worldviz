package org.n52.v3d.worldviz.worldscene.mapper;

import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

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
