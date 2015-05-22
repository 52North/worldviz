package org.n52.v3d.worldviz.dataaccess.load.dataset;

import java.util.List;

import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.Unit;

/**
 * This interface is used for the java-representation of ENE-Datasets.
 * 
 * @author Christian Danowski
 * 
 */
public interface XmlDataset {

	/**
	 * Each ENE-dataset contains at least one title.
	 * 
	 * @return all titles
	 */
	public String[] getTitles();

	/**
	 * Each ENE-dataset contains at least one description of it's content.
	 * 
	 * @return all descriptions
	 */
	public String[] getDescriptions();

	/**
	 * The unit-element consists of a <i>title</i> and a <i>code</i> to specify
	 * some unit (e.g. gigagram per year).
	 * 
	 * @return the unit
	 */
	public Unit getUnit();

	/**
	 * The PublicReference-element may store information about a public person
	 * or organization from which the source data of the ENE-XML-file was
	 * gathered.
	 * 
	 * @return a reference to the data source
	 */
	public String getPublicReference();

	/**
	 * The MetadataReference-element stores a reference to the metadata file of
	 * the dataset. Note: It may be empty, if no public reference has been
	 * declared in the dataset!
	 * 
	 * @return a reference to the metadata file of the dataset
	 */
	public String getMetadataReference();

	/**
	 * Each Entry-element of the XML-dataset is interpreted as a feature with a
	 * georeferenced geometry and attributes. So each property becomes an
	 * attribute of the feature; exception: the property that contains a
	 * GeoReference-element is used for the geometry of the feature.
	 * 
	 * @return a list of all features (one feature per Entry)
	 */
	public List<VgAttrFeature> getFeatures();

}
