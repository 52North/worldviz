package org.n52.v3d.worldviz.exception;

/**
 * While extracting specific geometries(features) for a specific
 * attribute-value-combination from a shape file this exception is thrown when
 * no feature could be found for the specified attribute-value-combination.
 * 
 * @author Christian Danowski
 * 
 */
public class ShapeGeometryNotFoundException extends Exception {

	private static final long serialVersionUID = 7870942023006853712L;

	public ShapeGeometryNotFoundException() {
		super(
				"No geometry could be determined for the attribute-value-combination!");
	}

	public ShapeGeometryNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
