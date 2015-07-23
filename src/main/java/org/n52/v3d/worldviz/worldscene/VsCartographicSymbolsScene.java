package org.n52.v3d.worldviz.worldscene;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dSymbolDef;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dBox;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCone;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCube;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCylinder;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dSphere;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.mappers.T3dAttrSymbolInstance;

/**
 * Class to create a scene description which contains cartographic visualization
 * objects. The style (size, color) of each object may depend on different
 * values of an attribute (by using various mapper-classes like
 * {@link MpValue2Sphere} beforehand).
 * 
 * @author Christian Danowski
 * 
 */
public class VsCartographicSymbolsScene extends VsAbstractWorldScene {

	// TODO Metadaten ueber X3DMetadataObject (MetadataSet)?!?

	private static final T3dVector HEIGHT_AXIS_IN_VIRTUAL_WORLD = new T3dVector(
			0, 1, 0);

	// und man braucht eigentlich auch noch eine Legende... die einem die
	// Thematiken anzeigt.
	protected List<T3dAttrSymbolInstance> cartographicSymbols;

	T3dColor defaultColor = new T3dColor(1, 1, 1);

	/**
	 * transparency of the symbols
	 */
	private float transparency = 0.25f;

	public VsCartographicSymbolsScene(String filePath) {
		super(filePath);

		cartographicSymbols = new ArrayList<T3dAttrSymbolInstance>();
	}

	/**
	 * The default color is used to color any {@link T3dAttrSymbolInstance}
	 * -objects of the scene that do NOT HAVE a valid color description of their
	 * own. This is the case if {@link T3dAttrSymbolInstance#getColor()} returns
	 * <Code>null</code>. The default color is set to white ((1, 1, 1) in
	 * RGB-model).
	 * 
	 * @return
	 */
	public T3dColor getDefaultColor() {
		return defaultColor;
	}

	/**
	 * The default color is used to color any {@link T3dAttrSymbolInstance}
	 * -objects of the scene that do NOT HAVE a valid color description of their
	 * own. This is the case if {@link T3dAttrSymbolInstance#getColor()} returns
	 * <Code>null</code>. The default color is set to white ((1, 1, 1) in
	 * RGB-model).
	 * 
	 * @param defaultColor
	 */
	public void setDefaultColor(T3dColor defaultColor) {
		this.defaultColor = defaultColor;
	}

	/**
	 * Simply adds a cartographic symbol to the scene. Note, that you are
	 * responsible for the consistency of the scene. This method does not check
	 * any duplicates. The coordinates of the position of the symbol are
	 * expected in scene coordinates (Y-Axis = height axis, Z-axis points
	 * towards the user)
	 * 
	 * @param symbol
	 *            a cartographic symbol
	 */
	public void addCartographicSymbol(T3dAttrSymbolInstance symbol) {
		cartographicSymbols.add(symbol);

	}

	/**
	 * Sets the transparency of the symbols.
	 * 
	 * @return the transparency. The default value is 0.25.
	 */
	public float getTransparency() {
		return transparency;
	}

	/**
	 * Sets the transparency of the symbols. The default value is 0.25.
	 * 
	 * @param transparency
	 *            The transparency value must obey the rule '0 <= transparency
	 *            <= 1'.
	 */
	public void setTransparency(float transparency) {
		this.transparency = transparency;
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

		int amountOfFeatures = cartographicSymbols.size();

		for (int i = 0; i < amountOfFeatures; i++) {
			if (i % 30 == 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Processed {} / {} features.", i,
							amountOfFeatures);
				}
			}

			T3dAttrSymbolInstance attrSymbol = cartographicSymbols.get(i);

			writeT3dAttrSymbolInstance(attrSymbol);
		}

	}

	private void writeT3dAttrSymbolInstance(T3dAttrSymbolInstance attrSymbol) {

		/*
		 * position
		 */
		VgPoint position = attrSymbol.getPosition();

		/*
		 * angles
		 */
		// in VR the angleXY of the real world object will be seen as angleXZ;
		// both times this angle affects the height-axis (which is the z-axis in
		// real world, but in VR it's the y-axis.)
		// angles are given in radiant
		double symbolAngleXY = attrSymbol.getAngleXY();
		double symbolAngleZ = attrSymbol.getAngleZ();

		double[] xyzAngles = determineAngles(symbolAngleXY, symbolAngleZ,
				position);
		double angle_xAxis = xyzAngles[0];
		double angle_yAxis = xyzAngles[1];
		double angle_zAxis = xyzAngles[2];

		/*
		 * scale-parameters
		 */
		// scaleTotal = same scale for all three coordinate axes
		double scaleTotal = attrSymbol.getScale();
		double scaleX = attrSymbol.getxScale() * scaleTotal;
		double scaleY = attrSymbol.getyScale() * scaleTotal;
		double scaleZ = attrSymbol.getzScale() * scaleTotal;

		wl("<Transform translation=\""
				+ this.decimalFormatter.format(position.getX()) + " "
				+ this.decimalFormatter.format(position.getY()) + " "
				+ this.decimalFormatter.format(position.getZ()) + "\">");
		wl("	<Transform rotation=\"1 0 0 "
				+ this.decimalFormatter.format(angle_xAxis) + "\">");
		wl("		<Transform rotation=\"0 1 0 "
				+ this.decimalFormatter.format(angle_yAxis) + "\">");
		wl("			<Transform rotation=\"0 0 1 "
				+ this.decimalFormatter.format(angle_zAxis) + "\">");
		wl("				<Transform scale=\"" + this.decimalFormatter.format(scaleX)
				+ " " + this.decimalFormatter.format(scaleY) + " "
				+ this.decimalFormatter.format(scaleZ) + "\">");

		/*
		 * Shape
		 */
		T3dColor color = attrSymbol.getColor();
		if (color == null)
			color = defaultColor;

		wl("					<Shape>");
		wl("						<Appearance>");
		wl("							<Material diffuseColor=\"" + color.getRed() + " "
				+ color.getGreen() + " " + color.getBlue()
				+ "\" transparency=\"" + transparency + "\"/>");

		/*
		 * texture
		 */
		String texture = attrSymbol.getSymbol().getTexture();
		if ((texture != null) && (!texture.equals(""))) {
			wl("						<ImageTexture url=\"" + texture + "\"");
		}

		wl("						</Appearance>");

		/*
		 * symbol geometry (cone, sphere, box, cylinder)
		 */
		T3dSymbolDef symbol = attrSymbol.getSymbol();
		writeSymbolGeometry(symbol);

		wl("					</Shape>");
		wl("				</Transform>");
		wl("			</Transform>");
		wl("		</Transform>");
		wl("	</Transform>");
		wl("</Transform>");

	}

	/**
	 * 
	 * @param angleXY
	 *            angle in radiant
	 * @param angleZ
	 *            angle in radiant
	 * @param position
	 * @return
	 */
	private double[] determineAngles(double angleXY, double angleZ,
			VgPoint position) {
		double[] angles = new double[] { 0, 0, 0 };

		// double longitude = Math.toDegrees(angleXY);
		// double latitude = Math.toDegrees(angleZ);
		//
		// VgPoint latLonPoint = new GmPoint(longitude, latitude, 0);
		// latLonPoint.setSRS(VgGeomObject.SRSLatLonWgs84);

		if (getClass().equals(VsCartographicSymbolsScene.class)) {
			double angleXaxis = 0;
			double angleYaxis = 0;
			double angleZaxis = 0;

			angles = new double[] { angleXaxis, angleYaxis, angleZaxis };
		}

		else {
			T3dVector normalVector = new T3dVector(position);

			double diffAngle = Math.acos(normalVector
					.scalarProd(HEIGHT_AXIS_IN_VIRTUAL_WORLD)
					/ (normalVector.length() * HEIGHT_AXIS_IN_VIRTUAL_WORLD
							.length()));

			double angleXaxis = 0;
			double angleYaxis = angleXY;
			double angleZaxis = -diffAngle;

			angles = new double[] { angleXaxis, angleYaxis, angleZaxis };
		}

		return angles;
	}

	private void writeSymbolGeometry(T3dSymbolDef symbol) {
		if (symbol instanceof T3dSphere)
			writeSphere(symbol);
		else if (symbol instanceof T3dBox)
			writeBox(symbol);
		else if (symbol instanceof T3dCube)
			writeCube(symbol);
		else if (symbol instanceof T3dCone)
			writeCone(symbol);
		else if (symbol instanceof T3dCylinder)
			writeCylinder(symbol);
		else
			throw new T3dNotYetImplException("The symbol geometry of type '"
					+ symbol.getClass() + "' is not yet supported!");
	}

	private void writeCylinder(T3dSymbolDef symbol) {
		wl("					<Cylinder height=\"" + ((T3dCylinder) symbol).getHeight()
				+ "\" radius=\"" + ((T3dCylinder) symbol).getRadius() + "\"/>");
	}

	private void writeCone(T3dSymbolDef symbol) {
		wl("					<Cone height=\"" + ((T3dCone) symbol).getHeight()
				+ "\" bottomRadius=\"" + ((T3dCone) symbol).getRadius()
				+ "\"/>");
	}

	private void writeCube(T3dSymbolDef symbol) {
		wl("					<Box size=\"" + ((T3dCube) symbol).getSize() + " "
				+ ((T3dCube) symbol).getSize() + " "
				+ ((T3dCube) symbol).getSize() + "\"/>");
	}

	private void writeBox(T3dSymbolDef symbol) {
		wl("					<Box size=\"" + ((T3dBox) symbol).getSizeX() + " "
				+ ((T3dBox) symbol).getSizeY() + " "
				+ ((T3dBox) symbol).getSizeZ() + "\"/>");
	}

	private void writeSphere(T3dSymbolDef symbol) {
		wl("					<Sphere radius=\"" + ((T3dSphere) symbol).getRadius() + "\"/>");
	}
}
