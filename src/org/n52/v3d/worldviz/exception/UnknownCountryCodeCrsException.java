package org.n52.v3d.worldviz.exception;

/**
 * This exception is thrown during the instantiation of an
 * {@link XmlCountryCodeDataset} if the specified coordinate reference system of
 * the corresponding XML-document cannot be interpreted by the processing logic.
 * 
 * @author Christian Danowski
 * 
 */
public class UnknownCountryCodeCrsException extends Exception {

	private static final long serialVersionUID = 6926367501468084314L;

	public UnknownCountryCodeCrsException() {
		super(
				"The GeoReference-element of the ENE-CountryCode-dataset contains a coordinate reference system that is unknown and cannot be interpreted!");
	}

	public UnknownCountryCodeCrsException(String message) {
		super(message);
	}

}
