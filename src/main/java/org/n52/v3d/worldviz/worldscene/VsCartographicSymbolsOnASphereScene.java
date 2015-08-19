/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.worldscene;

import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.projections.Wgs84ToSphereCoordsTransform;
import org.n52.v3d.worldviz.extensions.mappers.T3dAttrSymbolInstance;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dSymbolDef;
import org.n52.v3d.triturus.t3dutil.T3dVector;
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
 * reference system of the input data is EPSG:4326 (WGS84), <b>but referring to
 * virtual coordinate axes (Y-Axis = height axis, Z-axis points towards the
 * user)</b>. So any WGS84-point (longitude, latitude, altitude) must be prior
 * transformed to scene coordinates (longitude, altitude, -latitude) via
 * {@link AxisSwitchTransform}. <br/>
 * <br/>
 * The transformation (translation and rotation of each symbol) is done during
 * the scene generation (you only need to specify the ground level position
 * (altitude=0) in WGS84-coordinates). This includes that any geometry will
 * additionally be offsetted in height direction for half of it's extent (E.g.
 * if you want to place a box with height=24 on top of the sphere then the box
 * will have an offset of 12). For further information please refer to
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

	private AxisSwitchTransform axisSwitch = new AxisSwitchTransform();

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

			// switching back the axes from scene coordinates of real world
			// coordinates in WGS84-CRS
			T3dVector wgs84Vector = axisSwitch.retransform(attrSymbol
					.getPosition());
			VgPoint wgs84position = new GmPoint(wgs84Vector.getX(),
					wgs84Vector.getY(), wgs84Vector.getZ());
			wgs84position.setSRS(VgPoint.SRSLatLonWgs84);

			// transform into Sphere-coordinates
			VgPoint spherePosition = computeSphereCoordinates(attrSymbol,
					wgs84position);

			// switching back to scene coordinates
			T3dVector scenePosition = axisSwitch.transform(spherePosition);
			// set sphere scene position
			attrSymbol.setPosition(new GmPoint(scenePosition.getX(),
					scenePosition.getY(), scenePosition.getZ()));

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

	private VgPoint computeSphereCoordinates(T3dAttrSymbolInstance attrSymbol,
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
		return spherePosition;
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
