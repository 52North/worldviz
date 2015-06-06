package org.n52.v3d.worldviz.worldscene;

import org.n52.v3d.worldviz.projections.Wgs84ToSphereCoordsTransform;
import org.n52.v3d.worldviz.triturusextensions.mappers.T3dAttrSymbolInstance;

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dSymbolDef;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dBox;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCone;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCube;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCylinder;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dSphere;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This class is a specialization of {@link VsCartographicSymbolsScene} that
 * projects the cartographic symbols into their spherical representatives that
 * can be used as an overlay for a world sphere. The expected coordinate
 * reference system of the input data is EPSG:4326 (WGS84). The transformation
 * (translation and rotation of each symbol) is done during the scene generation
 * (you only need to specify the ground level position (altitude=0) in
 * WGS84-coordinates). This includes that any geometry will additionally be
 * offsetted in height direction for half of it's extent (E.g. if you want to
 * place a box with height=24 on top of the sphere then the box will have an
 * offset of 12). For further information please refer to
 * {@link #setAddOffsetInHeightDirection(boolean)}.<br/>
 * Note that you should inspect all setter-methods before you generate a scene.
 * 
 * @author Christian Danowski
 * 
 */
public class VsCartographicSymbolsOnASphereScene extends
		VsCartographicSymbolsScene {

	private boolean addOffsetInHeightDirection = true;

	private double radius = 10;

	public VsCartographicSymbolsOnASphereScene(String filePath) {
		super(filePath);
	}

	/**
	 * Gets the radius of the reference sphere, that is used to project the
	 * WGS84-geometries into spherical geometries. The default value is 10.
	 * 
	 * @return
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius. This radius is important when transforming the
	 * WGS84-geometries into spherical geometries.It is the radius of the
	 * reference sphere on which the geometries shall be overlaid. The default
	 * value is 10.
	 * 
	 * @param radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Indicated whether all cartographic scene-objects shall be offsetted in
	 * height direction for half of their height-extent.
	 * 
	 * Example: The position of any {@link T3dAttrSymbolInstance} (method
	 * {@link T3dAttrSymbolInstance#getPosition()}) usually refers to the ground
	 * level of the world sphere (altitude of position = 0). In a 3D-scene
	 * description language this will cause to draw half of any object inside of
	 * the sphere as the position refers to the midpoint of that geometry. So to
	 * place it correctly the object has to be offsetted in height-direction.
	 * 
	 * @return true, if the offset shall be added
	 */
	public boolean isAddOffsetInHeightDirection() {
		return addOffsetInHeightDirection;
	}

	/**
	 * Sets whether all cartographic scene-objects shall be offsetted in height
	 * direction for half of their height-extent.
	 * 
	 * Example: The position of any {@link T3dAttrSymbolInstance} (method
	 * {@link T3dAttrSymbolInstance#getPosition()}) usually refers to the ground
	 * level of the world sphere (altitude of position = 0). In a 3D-scene
	 * description language this will cause to draw half of any object inside of
	 * the sphere as the position refers to the midpoint of that geometry. So to
	 * place it correctly the object has to be offsetted in height-direction.
	 * 
	 * @param addOffsetInHeightDirection
	 *            true, if the offset shall be added
	 */
	public void setAddOffsetInHeightDirection(boolean addOffsetInHeightDirection) {
		this.addOffsetInHeightDirection = addOffsetInHeightDirection;
	}

	@Override
	protected void generateSceneContentKML() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateSceneContentVRML() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateSceneContentX3D(boolean asX3DOM) {
		// set position and angles

		for (T3dAttrSymbolInstance attrSymbol : cartographicSymbols) {

			VgPoint wgs84position = attrSymbol.getPosition();

			// transform into Sphere-coordinates
			transformIntoSpherePosition(attrSymbol, wgs84position);

			// set angles
			setAngles(wgs84position, attrSymbol);
		}

		// call super method
		super.generateSceneContentX3D(asX3DOM);
	}

	private void setAngles(VgPoint wgs84position,
			T3dAttrSymbolInstance attrSymbol) {
		double longitude = wgs84position.getX();
		double latitude = wgs84position.getY();

		// longitude --> angle of the height axis
		attrSymbol.setAngleXY(Math.toRadians(longitude));
		attrSymbol.setAngleZ(Math.toRadians(latitude));
	}

	private void transformIntoSpherePosition(T3dAttrSymbolInstance attrSymbol,
			VgPoint wgs84position) {
		// before: add offset to the altitude
		if (addOffsetInHeightDirection) {
			// determine the offset
			double offset = determineOffset(attrSymbol);

			wgs84position.setZ(wgs84position.getZ() + offset);
			attrSymbol.setPosition(wgs84position);
		}

		VgPoint spherePosition = Wgs84ToSphereCoordsTransform.wgs84ToSphere(
				wgs84position, this.radius);
		// set sphere position
		attrSymbol.setPosition(spherePosition);
	}

	private double determineOffset(T3dAttrSymbolInstance attrSymbol) {
		double heightScale = attrSymbol.getyScale();
		double totalScale = attrSymbol.getScale();
		T3dSymbolDef symbol = attrSymbol.getSymbol();

		if (symbol instanceof T3dSphere)
			return determineOffsetForSphere(symbol, heightScale, totalScale);
		else if (symbol instanceof T3dBox)
			return determineOffsetForBox(symbol, heightScale, totalScale);
		else if (symbol instanceof T3dCube)
			return determineOffsetForCube(symbol, heightScale, totalScale);
		else if (symbol instanceof T3dCone)
			return determineOffsetForCone(symbol, heightScale, totalScale);
		else if (symbol instanceof T3dCylinder)
			return determineOffsetForCylinder(symbol, heightScale, totalScale);
		else
			throw new T3dNotYetImplException("The symbol geometry of type '"
					+ symbol.getClass() + "' is not yet supported!");
	}

	private double determineOffsetForCylinder(T3dSymbolDef symbol,
			double heightScale, double totalScale) {

		// half of the geometry's height serves as the offset
		double cylinderHeight = ((T3dCylinder) symbol).getHeight();

		return (cylinderHeight * heightScale * totalScale) / 2;

	}

	private double determineOffsetForCone(T3dSymbolDef symbol,
			double heightScale, double totalScale) {

		// half of the geometry's height serves as the offset
		double coneHeight = ((T3dCone) symbol).getHeight();

		return (coneHeight * heightScale * totalScale) / 2;

	}

	private double determineOffsetForCube(T3dSymbolDef symbol,
			double heightScale, double totalScale) {
		// half of the geometry's height serves as the offset
		double cubeHeight = ((T3dCube) symbol).getSize();

		return (cubeHeight * heightScale * totalScale) / 2;

	}

	private double determineOffsetForBox(T3dSymbolDef symbol,
			double heightScale, double totalScale) {
		// half of the geometry's height serves as the offset
		double boxHeight = ((T3dBox) symbol).getSizeY();

		return (boxHeight * heightScale * totalScale) / 2;

	}

	private double determineOffsetForSphere(T3dSymbolDef symbol,
			double heightScale, double totalScale) {
		// half of the geometry's height serves as the offset
		double cylinderHeight = ((T3dSphere) symbol).getRadius() * 2;

		return (cylinderHeight * heightScale * totalScale) / 2;

	}
}
