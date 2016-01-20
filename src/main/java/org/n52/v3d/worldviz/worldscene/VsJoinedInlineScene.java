package org.n52.v3d.worldviz.worldscene;

import java.util.List;

import org.n52.v3d.triturus.core.T3dNotYetImplException;

public class VsJoinedInlineScene extends VsAbstractWorldScene {

	private List<VsAbstractWorldScene> combinableWorldScenes;

	public VsJoinedInlineScene(String filePath, List<VsAbstractWorldScene> combinableWorldScenes) {
		super(filePath);
		this.combinableWorldScenes = combinableWorldScenes;
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
