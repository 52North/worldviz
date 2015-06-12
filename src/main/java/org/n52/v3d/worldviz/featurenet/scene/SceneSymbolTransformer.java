package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This class is meant to take two points, that represent a from-to-vector
 * relationship. The class computes the necessary rotation (as angles for x-, y-
 * and z-axis) and translation (as mid-point between the two given points)
 * parameters.<br/>
 * <br/>
 * Please note that any symbol in a Virtual Reality scene description like X3D
 * is directed with respect to the y-axis of the scene coordinate system
 * (meaning that a height-parameter of a symbol like a cylinder will extrude the
 * cylinder in y-direction!).
 * 
 * @author Christian Danowski
 *
 */
public class SceneSymbolTransformer {

	private final T3dVector xAxis = new T3dVector(1, 0, 0);
	private final T3dVector yAxis = new T3dVector(0, 1, 0);
	private final T3dVector zAxis = new T3dVector(0, 0, 1);

	// in Virtual Reality the y-axis is the height axis!!!
	private final T3dVector symbolDirectionVector = yAxis;

	private T3dVector fromToVector;
	private double lengthFromTo;

	private double angleX;
	private double angleY;
	private double angleZ;

	private VgPoint midPoint;

	/**
	 * Constructor that computes all necessary rotation (as angles for x-, y-
	 * and z-axis) and translation (as mid-point between the two given points)
	 * parameters. <br/>
	 * <br/>
	 * A symbol can be rotated and translated according to the vector specified
	 * by the two points 'from' and 'to'. <br/>
	 * <br/>
	 * Please note that any symbol in a Virtual Reality scene description like
	 * X3D is directed with respect to the y-axis of the scene coordinate system
	 * (meaning that a height-parameter of a symbol like a cylinder will extrude
	 * the cylinder in y-direction!). Thus each angle is computed according to
	 * this definition!
	 * 
	 * @param from
	 * @param to
	 */
	public SceneSymbolTransformer(VgPoint from, VgPoint to) {

		this.fromToVector = new T3dVector();

		// assign "to - from" to the vector
		this.fromToVector.assignDiff(to, from);

		this.lengthFromTo = this.fromToVector.length();

		// calculate angles for each coordinate axis

		// angleXZ is the angle between the xAxis and the 2d (!!!) fromToVector
		// consisting only of X and Z coordinate (corresponds to longitude angle
		// in WGS84)
		double angleXZ = calculateAngle(this.xAxis, new T3dVector(
				this.fromToVector.getX(), 0, this.fromToVector.getZ()));

		// diffAngleToHeightAxis describes the angle between the height axis
		// (here y-axis because of Virtual Reality scene) and the from-to-vector
		double diffAngleToHeightAxis = calculateAngle(symbolDirectionVector,
				fromToVector);

		this.angleX = 0;
		this.angleY = angleXZ;
		this.angleZ = -diffAngleToHeightAxis;

		// caclulate mid-point
		T3dVector midPointVec = new T3dVector();
		midPointVec.assignSum(from, to);
		double x = midPointVec.getX() / 2;
		double y = midPointVec.getY() / 2;
		double z = midPointVec.getZ() / 2;
		this.midPoint = new GmPoint(x, y, z);
	}

	/**
	 * Returns the length of the vector between the points 'from' and 'to'
	 * 
	 * @return
	 */
	public double getLengthFromTo() {
		return lengthFromTo;
	}

	/**
	 * Returns the rotation of a symbol with respect to the x-axis (pointing to
	 * the right) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleX() {
		return angleX;
	}

	/**
	 * Returns the rotation of a symbol with respect to the y-axis (pointing
	 * upwards; equivalent to the height axis) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleY() {
		return angleY;
	}

	/**
	 * Returns the rotation of a symbol with respect to the z-axis (pointing
	 * towards the user outside of the screen) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleZ() {
		return angleZ;
	}

	/**
	 * Returns the mid-point between the points 'from' and 'to' defined in the
	 * constructor.
	 * 
	 * @return
	 */
	public VgPoint getMidPoint() {
		return midPoint;
	}

	/**
	 * Computes the angle in radiant between two vectors using the scalar product and the
	 * lengths of both vectors.
	 * 
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private double calculateAngle(T3dVector vec1, T3dVector vec2) {

		double scalarProd = vec1.scalarProd(vec2);

		double lengthVec1 = vec1.length();
		double lengthVec2 = vec2.length();
		if (lengthVec1 == 0. || lengthVec2 == 0.)
			return 0.;

		double cosPhi = scalarProd / (lengthVec1 * lengthVec2);
		return Math.acos(cosPhi);
	}

}
