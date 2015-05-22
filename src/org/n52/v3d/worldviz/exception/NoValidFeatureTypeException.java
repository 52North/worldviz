package org.n52.v3d.worldviz.exception;

/**
 * This exception is thrown during the load process of an ENE XML file when the
 * type of the XML document does not match any type of the
 * {@link GeoReferenceFeatureTypeEnum}. For further information please check
 * {@link DatasetLoader#loadDataset()}.
 * 
 * @author Christian Danowski
 * 
 */
public class NoValidFeatureTypeException extends Exception {

	private static final long serialVersionUID = 3528367561477962527L;

	public NoValidFeatureTypeException() {
		super(
				"The featureType-attribute of the GeoReference-element of the ENE-dataset has an invalid value!");
	}

	public NoValidFeatureTypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
