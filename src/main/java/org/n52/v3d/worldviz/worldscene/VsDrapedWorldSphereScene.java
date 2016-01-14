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
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.worldscene;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.gisimplm.GmSimpleElevationGrid;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgElevationGrid;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.projections.Wgs84ToSphereCoordsTransform;

/**
 * Class to create a scene description that consists of a sphere-object and an
 * image-texture as drape. In combination they create a world sphere to
 * visualize world (earth) content on a sphere.
 * 
 * @author Christian Danowski
 * 
 */
public class VsDrapedWorldSphereScene extends VsAbstractWorldScene {

	private AxisSwitchTransform axisSwitchTransform = new AxisSwitchTransform();
	
	private String pathToMainSphereTexture;
	private String[] additionalTexturePaths;
	private double defaultRadiusForSphere = 1.f;

	// the backupColor if the imageTexture cannot be loaded
	private T3dColor sphereColor = new T3dColor(0.2f, 0.2f, 0.2f);

	// GeoElevationGrid and IndexedFaceSet are two specific geometry-types in
	// X3D
	private boolean useGeoElevationGridInsteadOfIndexedFaceSet = false;

	/*
	 * helper parameters for GeoElevationGrid
	 * 
	 * geoGridOrigin='-90 -180 0' solid='false' xDimension='361' xSpacing='1.0'
	 * yScale='1.0' zDimension='181' zSpacing='1.0' height='
	 */
	private T3dVector geoGridOrigin = new T3dVector(-90, -180, 0);
	private String solid = "false";
	// dimension means the total count of fields in each direction
	private int xDimension = 361;
	private int zDimension = 181;
	// spacing means how "large" each field is in each direction
	private float xSpacing = 1.0f;
	private float zSpacing = 1.0f;
	// yScale means how each height-value of each field will be scaled
	private float yScale = 1.0f;
	// height is the height parameter for each field. For a simple world globe
	// without any height differences, we just use o
	private float defaultHeight = 0;
	private VgElevationGrid earthElevGrid;

	/**
	 * Standard constructor. Note that all parameters like textures or
	 * outputFile path must be set manually when using this constructor.
	 */
	public VsDrapedWorldSphereScene() {

	}

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            a path to the file that will be created by the
	 *            <i>generateScene()</i>-method
	 * @param imagePath
	 *            a path to the image file, that will be used as single texture
	 */
	public VsDrapedWorldSphereScene(String filePath, String imagePath) {
		super(filePath);

		this.pathToMainSphereTexture = imagePath;
		this.additionalTexturePaths = new String[] {};
	}

	/**
	 * Constructor with all parameters
	 * 
	 * @param filePath
	 *            a path to the file that will be created by the
	 *            <i>generateScene()</i>-method
	 * @param mainImagePath
	 *            a path to the image file, that will be used as the main single
	 *            texture
	 * @param furtherImages
	 *            one or more paths to additional images (e.g. for use as a bump
	 *            map) that will be used in combination with the main texture.
	 */
	public VsDrapedWorldSphereScene(String filePath, String mainImagePath, String[] furtherImages) {
		super(filePath);

		this.pathToMainSphereTexture = mainImagePath;
		this.additionalTexturePaths = furtherImages;
	}

	public String getPathToMainSphereTexture() {
		return pathToMainSphereTexture;
	}

	/**
	 * This is the main texture of the world sphere. Note: if you set the
	 * texture path as a relative path, then consider, that you need to set the
	 * path relative to the folder where the output scene is written to!
	 * 
	 * @param pathToSphereTexture
	 */
	public void setPathToMainSphereTexture(String pathToSphereTexture) {
		this.pathToMainSphereTexture = pathToSphereTexture;
	}

	public String[] getAdditionalTexturePaths() {
		return additionalTexturePaths;
	}

	/**
	 * These are additional and optional textures of the world sphere, that will
	 * be combined with the main texture (e.g. you may use a different texture
	 * as a black-and-white bump map). <br/>
	 * Note: if you set the texture path as a relative path, then consider, that
	 * you need to set the path relative to the folder where the output scene is
	 * written to!
	 * 
	 * @param additionalTexturePaths
	 */
	public void setAdditionalTexturePaths(String[] additionalTexturePaths) {
		this.additionalTexturePaths = additionalTexturePaths;
	}

	public double getDefaultRadiusForSphere() {
		return defaultRadiusForSphere;
	}

	public void setDefaultRadiusForSphere(double defaultRadiusForSphere) {
		this.defaultRadiusForSphere = defaultRadiusForSphere;
	}

	/**
	 * This parameter is only relevant when the output format is set to 'X3D'
	 * and indicates whether the world sphere shall be created as a
	 * 'indexedFaceSet'-element or as a 'GeoElevationGrid'-element in X3D.
	 * However, only certain X3D-Viewers like 'BsContact Geo' (as of August
	 * 2014) do support the 'GeoElevationGrid'-element correctly. Thus you may
	 * set the parameter to <code>false</code>, to display the sphere as
	 * IndexedFaceSet.
	 * 
	 * @return true, if GeoElevationGrid shall be used
	 */
	public boolean isUseGeoElevationGridInsteadOfIndexedFaceSet() {
		return useGeoElevationGridInsteadOfIndexedFaceSet;
	}

	/**
	 * This parameter is only relevant when the output format is set to 'X3D'
	 * and indicates whether the world sphere shall be created as a
	 * 'indexedFaceSet'-element or as a 'GeoElevationGrid'-element in X3D.
	 * However, only certain X3D-Viewers like 'BsContact Geo' (as of August
	 * 2014) do support the 'GeoElevationGrid'-element correctly. Thus you may
	 * set the parameter to <code>false</code>, to display the sphere as
	 * IndexedFaceSet.
	 * 
	 * @param useGeoElevationGridInsteadOfSphere
	 */
	public void setUseGeoElevationGridInsteadOfSphere(boolean useGeoElevationGridInsteadOfSphere) {
		this.useGeoElevationGridInsteadOfIndexedFaceSet = useGeoElevationGridInsteadOfSphere;
	}

	public VgElevationGrid getEarthElevGrid() {
		return earthElevGrid;
	}

	public void setEarthElevGrid(VgElevationGrid earthElevGrid) {
		this.earthElevGrid = earthElevGrid;
	}

	public T3dColor getSphereColor() {
		return sphereColor;
	}

	public void setSphereColor(T3dColor sphereColor) {
		this.sphereColor = sphereColor;
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

		if (useGeoElevationGridInsteadOfIndexedFaceSet)
			writeAsGeoElevationGrid(asX3DOM);
		else {
			writeAsIndexedFaceSet(asX3DOM);
		}

	}

	private void writeAsIndexedFaceSet(boolean asX3DOM) {

		if (this.earthElevGrid == null) {
			VgElevationGrid flatElevationGrid = new GmSimpleElevationGrid(361, 181, new GmPoint(-180, -90, 0), 1., 1.);

			for (int i = 0; i < flatElevationGrid.numberOfColumns(); i++) {
				for (int j = 0; j < flatElevationGrid.numberOfRows(); j++) {
					flatElevationGrid.setValue(j, i, 0.0);
				}
			}

			this.earthElevGrid = flatElevationGrid;
		}

		try {
			writeToX3dSphere(this.earthElevGrid, asX3DOM);

		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("An error occured: " + e.getMessage());
		}

	}

	private void writeAsGeoElevationGrid(boolean asX3DOM) {

		wl("<Transform scale='" + defaultRadiusForSphere + " " + defaultRadiusForSphere + " " + defaultRadiusForSphere
				+ "'>");
		wl("	<Shape>");
		wl("		<Appearance>");
		// set a backup color that is rendered if the imageTexture cannot be
		// loaded.
		wl("			<Material diffuseColor='" + sphereColor.getRed() + " "
				+ sphereColor.getGreen() + " " + sphereColor.getBlue() + "'/>");
		wl("			<ImageTexture url='" + pathToMainSphereTexture + "'/>");
		// the texture has to be translated in order to place vector overlays
		// properly on top of the countries.
		wl("			<TextureTransform translation='-.25 .0'/>");
		wl("		</Appearance>");

		// GeoElevationGrid
		w("		<GeoElevationGrid ");
		// write all attributes of GeoElevationGrid
		w("geoGridOrigin='" + geoGridOrigin.getX() + " " + geoGridOrigin.getY() + " " + geoGridOrigin.getZ() + "'"
				+ " ");
		w("solid='" + solid + "'" + " ");
		w("xDimension='" + xDimension + "'" + " ");
		w("zDimension='" + zDimension + "'" + " ");
		w("xSpacing='" + xSpacing + "'" + " ");
		w("zSpacing='" + zSpacing + "'" + " ");
		w("yScale='" + yScale + "'" + " ");
		w("height='");

		// write all height parameters
		for (int i = 0; i < xDimension; i++) {
			for (int j = 0; j < zDimension; j++) {
				w("" + defaultHeight + " ");
			}
		}

		wl("'/>");
		wl("	</Shape>");
		wl("</Transform>");
	}

	private void writeToX3dSphere(VgElevationGrid elevationGrid, boolean asX3DOM) {

		// rotation is necessary, if other scenes like
		// VsColoredWoorldCountriesOnASphereScene act as an overlay of
		// the
		// sphere; otherwise the texture will not fit the countries.
		wl("    <Transform>");
		wl("      <Shape>");
		wl("        <Appearance>");
		wl("          <Material></Material>");

		// check if there are multiple textures

		// However, if the output format of this scene (see
		// VsAbstractWorldScene#setOutputFormat(OutputFormatEnum)) is set to
		// 'X3DOM', then there is no working method of supporting multiple
		// textures yet (03.2015)
		// then we simply ignore this and only display the main texture!
		if (this.additionalTexturePaths == null || this.additionalTexturePaths.length == 0
				|| this.getOutputFormat().equals(OutputFormatEnum.X3DOM)) {
			// here there are no additional textures defined. Thus only one
			// texture needs to be written.
			wl("          <ImageTexture url='" + this.pathToMainSphereTexture + "'></ImageTexture>");

		} else {
			// here there are additional textures defined. Thus we need
			// MultiTexture-element!

			wl("          <MultiTexture mode='BLENDDIFFUSEALPHA'>");

			// main texture
			wl("            <ImageTexture url='" + this.pathToMainSphereTexture + "'/>");

			// additional textures
			for (String additionalTexture : this.additionalTexturePaths) {
				wl("        <ImageTexture url='" + additionalTexture + "'/>");
			}

			wl("          </MultiTexture>");
		}

		wl("        </Appearance>");

		w("         <IndexedFaceSet solid='false' creaseAngle='0.5' coordIndex='");
		for (int i = 0; i < elevationGrid.numberOfRows() - 1; i++) {
			for (int j = 0; j < elevationGrid.numberOfColumns() - 1; j++) {

				long idxLL = (i) * elevationGrid.numberOfColumns() + (j);
				long idxLR = (i) * elevationGrid.numberOfColumns() + (j + 1);
				long idxUL = (i + 1) * elevationGrid.numberOfColumns() + (j);
				long idxUR = (i + 1) * elevationGrid.numberOfColumns() + (j + 1);

				w("" + idxLL + ", " + idxLR + ", " + idxUR + ", " + idxUL + " -1");

				if (i != (elevationGrid.numberOfRows() - 2) || j != (elevationGrid.numberOfColumns() - 2)) {
					// for all elements, except the final one, a comma is set
					// and the next elements starts in a new line
					w(",");
					wl();
					w("                  ");
				}

			}
		}

		wl("'");

		w("             texCoordIndex='");
		for (int i = 0; i < elevationGrid.numberOfRows() - 1; i++) {
			for (int j = 0; j < elevationGrid.numberOfColumns() - 1; j++) {
				long idxLL = (i) * elevationGrid.numberOfColumns() + (j),
						idxLR = (i) * elevationGrid.numberOfColumns() + (j + 1),
						idxUL = (i + 1) * elevationGrid.numberOfColumns() + (j),
						idxUR = (i + 1) * elevationGrid.numberOfColumns() + (j + 1);

				w("" + idxLL + ", " + idxLR + ", " + idxUR + ", " + idxUL + ", -1");

				if (i != (elevationGrid.numberOfRows() - 2) || j != (elevationGrid.numberOfColumns() - 2)) {
					// for all elements, except the final one, a comma is set
					// and the next elements starts in a new line
					w(",");
					wl();
					w("                  ");
				}

			}
		}

		wl("'>");
		wl();

		w("            <Coordinate point='");

		for (int i = 0; i < elevationGrid.numberOfRows(); i++) {
			for (int j = 0; j < elevationGrid.numberOfColumns(); j++) {

				VgPoint cellPoint = ((GmSimpleElevationGrid) elevationGrid).getPoint(i, j);
				cellPoint.setZ(elevationGrid.getValue(i, j));

				cellPoint.setSRS(VgGeomObject.SRSLatLonWgs84);

				VgPoint sphereCoordinates = Wgs84ToSphereCoordsTransform.wgs84ToSphere(cellPoint,
						defaultRadiusForSphere);

				T3dVector sceneCoordinates = axisSwitchTransform.transform(sphereCoordinates);

				String sceneCoordinateString = "" + this.decimalFormatter.format(sceneCoordinates.getX()) + " "
						+ this.decimalFormatter.format(sceneCoordinates.getY()) + " "
						+ this.decimalFormatter.format(sceneCoordinates.getZ());

				w(sceneCoordinateString);

				if (i != (elevationGrid.numberOfRows() - 1) || j != (elevationGrid.numberOfColumns() - 1)) {
					// for all elements, except the final one, a comma is set
					// and the next elements starts in a new line
					w(",");
					wl();
					w("                  ");
				}

			}
		}
		wl("'></Coordinate>");

		w("            <TextureCoordinate point='");
		for (int i = 0; i < elevationGrid.numberOfRows(); i++) {
			for (int j = 0; j < elevationGrid.numberOfColumns(); j++) {
				// von 0,1 nach 1,0 in 360/180 schritten
				
				w("" + (double) j / (elevationGrid.numberOfColumns() - 1)
						+ " " + (double) i / (elevationGrid.numberOfRows() - 1) + " ");

				if (i != (elevationGrid.numberOfRows() - 1) || j != (elevationGrid.numberOfColumns() - 1)) {
					// for all elements, except the final one, a comma is set
					// and the next elements starts in a new line
					w(",");
					wl();
					w("                  ");
				}

			}
		}
		wl("'></TextureCoordinate>");

		wl("      </IndexedFaceSet>");
		wl("    </Shape>");
		wl("  </Transform>");
	}
}
