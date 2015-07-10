package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

public abstract class AbstractSceneSymbolTransformer implements
		SceneSymbolTransformer {

	protected final T3dVector xAxis = new T3dVector(1, 0, 0);
	protected final T3dVector yAxis = new T3dVector(0, 1, 0);
	protected final T3dVector zAxis = new T3dVector(0, 0, 1);

	// in Virtual Reality the y-axis is the height axis!!!
	protected final T3dVector symbolDirectionVector = yAxis;

	protected T3dVector fromToVector;
	protected double lengthFromTo;

	protected double angleX, angleY, angleZ;

	protected VgPoint midPoint;

	/**
	 * Constructor that computes all necessary rotation (as angles for x-, y-
	 * and z-axis) and translation (as mid-point between the two given points)
	 * parameters for a connection between the points. <br/>
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
	public AbstractSceneSymbolTransformer(VgPoint from, VgPoint to) {

		computeFromToVector(from, to);

		computeLengthFromTo();

		computeAngles();

		computeMidPoint(from, to);
	}

	private void computeMidPoint(VgPoint from, VgPoint to) {
		// caclulate mid-point
		T3dVector midPointVec = new T3dVector();
		midPointVec.assignSum(from, to);
		double x = midPointVec.getX() / 2;
		double y = midPointVec.getY() / 2;
		double z = midPointVec.getZ() / 2;
		this.midPoint = new GmPoint(x, y, z);
	}

	protected abstract void computeAngles();

	private void computeLengthFromTo() {
		this.lengthFromTo = this.fromToVector.length();
	}

	private void computeFromToVector(VgPoint from, VgPoint to) {
		this.fromToVector = new T3dVector();

		// assign "to - from" to the vector
		this.fromToVector.assignDiff(to, from);
	}

	@Override
	public double getLengthFromTo() {
		return this.lengthFromTo;
	}

	@Override
	public double getAngleX() {
		return this.angleX;
	}

	@Override
	public double getAngleY() {
		return this.angleY;
	}

	@Override
	public double getAngleZ() {
		return this.angleZ;
	}

	@Override
	public VgPoint getMidPoint() {
		return this.midPoint;
	}

	/**
	 * Computes the angle in radiant between two vectors using the scalar
	 * product and the lengths of both vectors.<br/>
	 * Between two vectors there will always be two angles. A small inner angle
	 * and a large outer angle.<br/>
	 * This method always computes the <b>small inner angle</b>!
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	protected double calculateAngle(T3dVector vector1, T3dVector vector2) {

		double scalarProd = vector1.scalarProd(vector2);

		double lengthVector1 = vector1.length();
		double lengthVector2 = vector2.length();
		if (lengthVector1 == 0. || lengthVector2 == 0.)
			return 0.;

		double cosPhi = scalarProd / (lengthVector1 * lengthVector2);
		return Math.acos(cosPhi);
	}

}
