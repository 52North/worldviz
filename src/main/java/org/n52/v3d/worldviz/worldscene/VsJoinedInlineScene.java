package org.n52.v3d.worldviz.worldscene;

import java.util.List;

import org.n52.v3d.triturus.core.T3dNotYetImplException;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.Viewpoint;

public class VsJoinedInlineScene extends VsAbstractWorldScene {

	private List<VsAbstractWorldScene> combinableWorldScenes;
	private WvizConfigDocument configFile;

	public VsJoinedInlineScene(String filePath, List<VsAbstractWorldScene> combinableWorldScenes,
			WvizConfigDocument configFile) {
		super(filePath);
		this.combinableWorldScenes = combinableWorldScenes;
		this.configFile = configFile;
	}

	@Override
	protected void generateSceneContentKML() {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException("This method is not yet implemented! Please use format 'X3D' instead!");
	}

	@Override
	protected void generateSceneContentVRML() {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException("This method is not yet implemented! Please use format 'X3D' instead!");
	}

	@Override
	protected void generateSceneContentX3D(boolean asX3DOM) {
		/*
		 * Viewpoint
		 */
		Viewpoint viewpoint = this.configFile.getWvizConfig().getViewpoint();
		String position = viewpoint.getPosition();
		String orientation = viewpoint.getOrientation();

		wl("<Viewpoint position=\"" + position + "\" orientation=\"" + orientation + "\"/> ");

		/*
		 * Inline each scene
		 * 
		 * TODO relative paths are better, then one could move all scene to a
		 * new place and it would still work
		 */
		for (VsAbstractWorldScene wScene : combinableWorldScenes) {
			String absolutePath = wScene.getOutputFile().getAbsolutePath();

			wl("<Inline url=\"" + absolutePath + "\"/>");
		}
	}

}
