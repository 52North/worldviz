package org.n52.v3d.worldviz.featurenet.scene;

/**
 * Concrete X3D scene description of a 3-d connection-map.
 * 
 * @author Adhitya Kamakshidasan
 */
public class WvizConnectionMapSceneX3d 
{
	private boolean x3domMode = false;
	
	/**
	 * exports the X3D description to a file.
	 * 
	 * @param fileName File name (and path)
	 */
	public void writeToFile(String fileName) {
		// TODO
	}

	public boolean isX3domMode() {
		return x3domMode;
	}

	public void setX3domMode(boolean x3dom) {
		this.x3domMode = x3dom;
	}
	
	// TODO: X3DOM (X3D embedded in HTML5 page)
	
	// TODO: export as String or output stream instead of file
}
