package org.n52.v3d.worldviz.worldscene.helper;

import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgIndexedTIN;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgPolygon;

public class NormalVectorHelper {

	public NormalVectorHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This method uses the three points of the triangle and calculates the
	 * normal-vector of these three points.
	 * 
	 * @param vgIndexedTIN
	 * @return
	 */
	public static T3dVector calculateNormalVectorFromThreePoints(
			VgIndexedTIN vgIndexedTIN) {

		// counterclockwise
		VgPoint p0 = vgIndexedTIN.getPoint(0);
		VgPoint p1 = vgIndexedTIN.getPoint(1);
		VgPoint p2 = vgIndexedTIN.getPoint(2);

		T3dVector normalVector = calculateNormalVectorFromThreePoints(p0, p1,
				p2);

		return normalVector;

	}

	/**
	 * This method uses three neighbor points (indices (i-1), i, (i+1)) and
	 * calculates the normal-vector of these three points.
	 * 
	 * @param linearRing
	 * @param i
	 *            the index position of the vertex of interest
	 * @return
	 */
	public static T3dVector calculateNormalVectorFromThreePoints(
			VgLinearRing linearRing, int i) {
		// actual Point!
		VgPoint actualPoint = linearRing.getVertex(i);
		// nextPoint
		VgPoint nextPoint = getNextPoint(linearRing, i);
		// previousPoint
		VgPoint previousPoint = getPreviousPoint(linearRing, i);

		// determine the two vectors in the actual point through it's
		// neighboring points
		T3dVector normalVector = calculateNormalVectorFromThreePoints(
				actualPoint, nextPoint, previousPoint);

		return normalVector;
	}

	public static T3dVector calculateNormalVectorFromThreePoints(VgPolygon polygon,
			int i) {
		return calculateNormalVectorFromThreePoints(polygon.getOuterBoundary(),
				i);
	}

	/**
	 * To solve this problem this method uses three neighbor points (indices
	 * (i-1), i, (i+1)) and calculates the normal-vector of these three points. <br/>
	 * <br/>
	 * From the point actualPoint two vectors are constructed
	 * (nextPoint-actualPoint) and (previousPoint-actualPoint). <br/>
	 * Then the cross-product of these two vectors results in the normal vector.
	 * See {@link T3dVector#ortho(T3dVector, T3dVector)}.
	 * 
	 * 
	 * @param actualPoint
	 * @param nextPoint
	 * @param previousPoint
	 * @return
	 */
	public static T3dVector calculateNormalVectorFromThreePoints(VgPoint actualPoint,
			VgPoint nextPoint, VgPoint previousPoint) {
		T3dVector vector1 = new T3dVector(
				nextPoint.getX() - actualPoint.getX(), nextPoint.getY()
						- actualPoint.getY(), nextPoint.getZ()
						- actualPoint.getZ());
		T3dVector vector2 = new T3dVector(previousPoint.getX()
				- actualPoint.getX(),
				previousPoint.getY() - actualPoint.getY(), previousPoint.getZ()
						- actualPoint.getZ());
		T3dVector normalVector = new T3dVector();
		normalVector.ortho(vector1, vector2);
		return normalVector;
	}
	
	/**
	 * Determines the previous point for a specific index of a polygon.
	 * 
	 * <p>
	 * If index=0, then the previous point index is (numberOfVertices-1)
	 * </p>
	 * <p>
	 * else the previous point index is (index-1)
	 * </p>
	 * 
	 * @param polygon
	 * @param index
	 * @return
	 */
	private static VgPoint getPreviousPoint(VgLinearRing polygon, int index) {

		int numberOfVertices = polygon.getNumberOfVertices();
		if (index == 0) {
			// we have the first point of the polygonPointList. So now we need
			// the last point
			return polygon.getVertex(numberOfVertices - 1);
		} else
			return polygon.getVertex(index - 1);
	}

	/**
	 * Determines the next point for a specific index of a polygon.
	 * 
	 * <p>
	 * If index=(numberOfVertices-1), then the next point index is (0)
	 * </p>
	 * <p>
	 * else the next point index is (index+1)
	 * </p>
	 * 
	 * @param polygon
	 * @param index
	 * @return
	 */
	private static VgPoint getNextPoint(VgLinearRing polygon, int index) {
		int numberOfVertices = polygon.getNumberOfVertices();
		if (index == numberOfVertices - 1) {
			// we have the last point of the polygonPointList. So now we need
			// the first point
			return polygon.getVertex(0);
		} else
			return polygon.getVertex(index + 1);
	}

}
