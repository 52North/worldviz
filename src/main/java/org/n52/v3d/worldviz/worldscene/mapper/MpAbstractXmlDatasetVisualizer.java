package org.n52.v3d.worldviz.worldscene.mapper;

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
public abstract class MpAbstractXmlDatasetVisualizer {

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
	public MpAbstractXmlDatasetVisualizer(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
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
	 * to visualization objects.
	 * 
	 * @param xmlDataset
	 * @return
	 */
	public abstract VsAbstractWorldScene transform(XmlDataset xmlDataset);

}
