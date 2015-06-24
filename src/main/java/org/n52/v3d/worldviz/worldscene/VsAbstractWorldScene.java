package org.n52.v3d.worldviz.worldscene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vscene.VsScene;

/**
 * Abstract class that contains common parameters for scene descriptions.
 * 
 * @author Christian Danowski
 * 
 */
public abstract class VsAbstractWorldScene extends VsScene {

	// file object where the generated Scene-description will be saved.
	private File outputFile;

	private OutputFormatEnum outputFormat = OutputFormatEnum.X3D;

	private T3dColor backgroundColor = new T3dColor(1.f, 1.f, 1.f);

	private BufferedWriter docWriter;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            a path to the file that will be created by the
	 *            <i>generateScene()</i>-method
	 */
	public VsAbstractWorldScene(String filePath) {

		this.outputFile = new File(filePath);
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

	@Override
	public final Object generateScene() {

		startSceneDescription();

		addSceneObjects();

		endSceneDescription();

		return outputFile;

	}

	/**
	 * Used to start a concrete scene-description (e.g. an X3D- or X3DOM-file)
	 * and add common contents. By calling the method {@link #addSceneObjects()}
	 * the concrete objects of the scene will be written to the file.
	 */
	private void startSceneDescription() {

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
			throw new T3dException(
					"The output format '"
							+ outputFormat.toString()
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
			throw new T3dException(
					"The output format '"
							+ outputFormat.toString()
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
			throw new T3dException(
					"The output format '"
							+ outputFormat.toString()
							+ "' is not yet supported! Please consider to use format 'X3D' instead.");
		}

	}

	private void endKmlDescription() {

		// TODO implement
		throw new T3dNotYetImplException(
				"Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void endVrmlDescription() {

		// TODO implement
		throw new T3dNotYetImplException(
				"Use outputFormat.X3D or outputFormat.X3DOM instead!");

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
		throw new T3dNotYetImplException(
				"Use outputFormat.X3D or outputFormat.X3DOM instead!");

	}

	private void startVrmlDescription() {
		// TODO implement
		throw new T3dNotYetImplException(
				"Use outputFormat.X3D or outputFormat.X3DOM instead!");

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
			wl("    <WorldInfo info='Scene generated by 52N Triturus' title='"
					+ getClass().getName() + "'></WorldInfo>");
			wl("    <NavigationInfo type='\"EXAMINE\",\"ANY\"'></NavigationInfo>");
			wl("    <Background skyColor='" + backgroundColor.getRed() + " "
					+ backgroundColor.getGreen() + " "
					+ backgroundColor.getBlue() + "'></Background>");

		} catch (FileNotFoundException e) {
			throw new T3dException("Could not access file '" + outputFile
					+ "'.");
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
