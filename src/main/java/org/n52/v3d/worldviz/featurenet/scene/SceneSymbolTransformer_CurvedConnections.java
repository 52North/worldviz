package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This class is meant to take two points, that represent a from-to-vector
 * relationship. The class computes the necessary rotation (as angles for x-, y-
 * and z-axis) and translation (as mid-point between the two given points)
 * parameters for a <b>curved connection</b> between the points.<br/>
 * <br/>
 * Please note that any symbol in a Virtual Reality scene description like X3D
 * is directed with respect to the y-axis of the scene coordinate system
 * (meaning that a height-parameter of a symbol like a cylinder will extrude the
 * cylinder in y-direction!).
 * 
 * @author Christian Danowski
 *
 */
public class SceneSymbolTransformer_CurvedConnections extends
		AbstractSceneSymbolTransformer {

	private T3dVector curveDirection = null;

	/**
	 * Constructor that computes all necessary rotation (as angles for x-, y-
	 * and z-axis) and translation (as mid-point between the two given points)
	 * parameters for a <b>curved connection</b> between the points. <br/>
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
	 * @param curveDirectionVector
	 *            the vector pointing to the direction of the curve (e.g. when
	 *            you have points in the XZ-plane and the curved connection
	 *            shall be curved in Y-direction, then provide a vector (0, 1,
	 *            0))
	 */
	public SceneSymbolTransformer_CurvedConnections(VgPoint from, VgPoint to,
			T3dVector curveDirectionVector) {
		super(from, to);

		this.curveDirection = curveDirectionVector;

		computeAngles();

	}

	@Override
	protected void computeAngles() {

		if (this.curveDirection != null) {
			// calculate angles for each coordinate axis

			// angleXZ is the angle between the xAxis and the 2d (!!!)
			// fromToVector
			// consisting only of X and Z coordinate (corresponds to longitude
			// angle
			// in WGS84)
			double angleXZ = calculateAngle(this.xAxis, new T3dVector(
					this.fromToVector.getX(), 0, this.fromToVector.getZ()));

			/*
			 * as angleXZ is always computed as the small inner angle between
			 * two vectors we need to adjust the direction of the rotation by
			 * looking at what quadrant the fromToVector points to! So if it
			 * points to quadrants 3 or 4 (the z-coordinate is negative) we need
			 * to rotate in opposite direction
			 */
			if (this.fromToVector.getZ() < 0)
				angleXZ = -angleXZ;

			/*
			 * diffAngleToCurveDirection describes the angle between the height
			 * axis (here y-axis because of Virtual Reality scene) and the
			 * curveDirection
			 */
			double diffAngleToCurveDirection = calculateAngle(
					symbolDirectionVector, this.curveDirection);

			/*
			 * the '-' before each angle is necessary due to the different
			 * coordinate rotation direction in computer graphic coordinate
			 * systems
			 */
			this.angleX = 0;
			this.angleY = -angleXZ;
			this.angleZ = -diffAngleToCurveDirection;
		}

	}

}
