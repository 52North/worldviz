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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vscene.VsScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that contains common parameters for scene descriptions.
 * 
 * @author Christian Danowski
 * 
 */
public abstract class VsAbstractWorldScene extends VsScene {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// file object where the generated Scene-description will be saved.
	private File outputFile;

	private OutputFormatEnum outputFormat = OutputFormatEnum.X3D;

	private T3dColor backgroundColor = new T3dColor(1.f, 1.f, 1.f);

	private String x3dViewpointPosition;
	private String x3dViewpointOrientation;

	private BufferedWriter docWriter;

	protected DecimalFormat decimalFormatter;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            a path to the file that will be created by the
	 *            <i>generateScene()</i>-method
	 */
	public VsAbstractWorldScene(String filePath) {

		this.outputFile = new File(filePath);

		initializeDecimalFormatter();

	}

	/**
	 * Constructor
	 * 
	 */
	public VsAbstractWorldScene() {

		initializeDecimalFormatter();

	}

	private void initializeDecimalFormatter() {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		formatSymbols.setDecimalSeparator('.');

		this.decimalFormatter = new DecimalFormat("#0.000", formatSymbols);
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public OutputFormatEnum getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(OutputFormatEnum outputFormat) {
		this.outputFormat = outputFormat;
	}

	public T3dColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(T3dColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getX3dViewpointPosition() {
		return x3dViewpointPosition;
	}

	public void setX3dViewpointPosition(String x3dViewpointPosition) {
		this.x3dViewpointPosition = x3dViewpointPosition;
	}

	public String getX3dViewpointOrientation() {
		return x3dViewpointOrientation;
	}

	public void setX3dViewpointOrientation(String x3dViewpointOrientation) {
		this.x3dViewpointOrientation = x3dViewpointOrientation;
	}

	/**
	 * <b>When using this method make sure that the output file parameter is
	 * set, e.g. by using the {@link #setOutputFile(File)} method or by calling
	 * {@link #writeToFile(String)}!</b><br/>
	 * <br/>
	 * {@inheritDoc}
	 */
	@Override
	public final Object generateScene() {

		if (logger.isInfoEnabled())
			logger.info("Starting to generate scene! Output format = '{}', output destination = '{}'",
					this.outputFormat, this.outputFile);

		startSceneDescription();

		addSceneObjects();

		endSceneDescription();

		if (logger.isInfoEnabled())
			logger.info("Finished to generate scene! output file = '{}'", this.outputFile);

		return outputFile;

	}

	/**
	 * Sets the parameter as output file and then calls {@link #generateScene()}
	 * 
	 * @param outputFilePath
	 */
	public void writeToFile(String outputFilePath) {

		this.outputFile = new File(outputFilePath);

		this.generateScene();
	}

	/**
	 * Used to start a concrete scene-description (e.g. an X3D- or X3DOM-file)
	 * and add common contents. By calling the method {@link #addSceneObjects()}
	 * the concrete objects of the scene will be written to the file.
	 */
	private void startSceneDescription() {

		if (!this.outputFile.exists()) {
			this.outputFile.getParentFile().mkdirs();
		}

		switch (outputFormat) {
		case X3D:
			startX3dDescription(false);
			break;
		case X3DOM:
			startX3dDescription(true);
			break;
		case VRML:
			startVrmlDescription();
			break;
		case KML:
			startKmlDescription();
			break;
		default:
			throw new T3dException("The output format '" + outputFormat.toString()
					+ "' is not yet supported! Please consider to use format 'X3D' instead.");
		}

	}

	/**
	 * Used to end a concrete scene-description (e.g. an X3D- or X3DOM-file).
	 */
	private void endSceneDescription() {

		switch (outputFormat) {
		case X3D:
			endX3dDescription(false);
			break;
		case X3DOM:
			endX3dDescription(true);
			break;
		case VRML:
			endVrmlDescription();
			break;
		case KML:
			endKmlDescription();
			break;
		default:
			throw new T3dException("The output format '" + outputFormat.toString()
					+ "' is not yet supported! Please consider to use format 'X3D' instead.");
		}

	}

	/**
	 * Used to add Scene-specific objects to the scene-description (e.g. writing
	 * into an X3D-file)!
	 */
	protected void addSceneObjects() {

		switch (outputFormat) {
		case X3D:
			generateSceneContentX3D(false);
			break;
		case X3DOM:
			generateSceneContentX3D(true);
			break;
		case VRML:
			generateSceneContentVRML();
			break;
		case KML:
			generateSceneContentKML();
			break;
		default:
			throw new T3dException("The output format '" + outputFormat.toString()
					+ "' is not yet supported! Please consider to use format 'X3D' instead.");
		}

	}

	private void endKmlDescription() {

		// TODO implement
		throw new T3dNotYetImplException("Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void endVrmlDescription() {

		// TODO implement
		throw new T3dNotYetImplException("Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void endX3dDescription(boolean asX3DOM) {

		try {
			wl("  </Scene>");
			wl("</X3D>");

			if (asX3DOM) {
				wl();
				wl("  </body>");
				wl("</html>");
			}

			docWriter.close();
		} catch (IOException e) {
			throw new T3dException(e.getMessage());
		} catch (T3dException e) {
			throw new T3dException(e.getMessage());
		}

	}

	private void startKmlDescription() {
		// TODO implement
		throw new T3dNotYetImplException("Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void startVrmlDescription() {
		// TODO implement
		throw new T3dNotYetImplException("Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void startX3dDescription(boolean asX3DOM) {

		try {
			docWriter = new BufferedWriter(new FileWriter(outputFile));

			if (asX3DOM) {
				String lTitle = "52N Triturus XHTML/X3DOM document";
				wl("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>");
				wl("<html xmlns='http://www.w3.org/1999/xhtml' style='padding: 0px; margin: 0px; border: 0px; width:100%; height:100%;'>");
				wl("  <head>");
				wl("    <meta http-equiv='X-UA-Compatible' content='chrome=1' />");
				wl("    <meta http-equiv='Content-Type' content='text/html;charset=utf-8' />");
				wl("    <title>" + lTitle + "</title>");
				wl("    <link rel='stylesheet' type='text/css' href='http://www.x3dom.org/x3dom/release/x3dom.css'></link>");
				wl("    <script type='text/javascript' src='http://www.x3dom.org/x3dom/release/x3dom-full.js'></script>");
				wl("  </head>");
				wl("  <body style='padding: 0px; margin: 0px; border: 0px; width:100%; height:100%;'>");
				wl();
				wl("<x3d id='x3dScene' xmlns:xsd='http://www.w3.org/2001/XMLSchema-instance' xsd:noNamespaceSchemaLocation='http://www.web3d.org/specifications/x3d-3.2.xsd'");
				wl(" showStat='true' showLog='true' style='width:100%; height:100%; border:0px;'>");
			} else {
				wl("<?xml version='1.0' encoding='UTF-8'?>");
				wl("<!DOCTYPE X3D PUBLIC 'ISO//Web3D//DTD X3D 3.3//EN' 'http://www.web3d.org/specifications/x3d-3.3.dtd'>");
				wl("<X3D version='3.3' profile='Immersive' xmlns:xsd='http://www.w3.org/2001/XMLSchema-instance' xsd:noNamespaceSchemaLocation='http://www.web3d.org/specifications/x3d-3.3.xsd'>");
			}

			wl("  <Scene>");
			wl("    <WorldInfo info='Scene generated by 52N Triturus' title='" + getClass().getName()
					+ "'></WorldInfo>");
			wl("    <NavigationInfo type='\"EXAMINE\",\"ANY\"'></NavigationInfo>");
			wl("    <Background skyColor='" + backgroundColor.getRed() + " " + backgroundColor.getGreen() + " "
					+ backgroundColor.getBlue() + "'></Background>");

			// camera/viewpoint
			if (this.x3dViewpointPosition != null) {
				w("    <Viewpoint position='" + this.x3dViewpointPosition + "'");
				if (this.x3dViewpointOrientation != null)
					w(" orientation='" + this.x3dViewpointOrientation + "'");
				wl("></Viewpoint>");
			}

		} catch (FileNotFoundException e) {
			throw new T3dException("Could not access file '" + outputFile + "'.");
		} catch (IOException e) {
			throw new T3dException(e.getMessage());
		} catch (T3dException e) {
			throw new T3dException(e.getMessage());
		}

	}

	protected abstract void generateSceneContentKML();

	protected abstract void generateSceneContentVRML();

	protected abstract void generateSceneContentX3D(boolean asX3DOM);

	protected void w(String pLine) {
		try {
			docWriter.write(pLine);
		} catch (IOException e) {
			throw new T3dException(e.getMessage());
		}
	}

	protected void wl(String pLine) {
		try {
			docWriter.write(pLine);
			docWriter.newLine();
		} catch (IOException e) {
			throw new T3dException(e.getMessage());
		}
	}

	protected void wl() {
		try {
			docWriter.newLine();
		} catch (IOException e) {
			throw new T3dException(e.getMessage());
		}
	}

}
