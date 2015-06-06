package org.n52.v3d.worldviz.demoapplications.ene.earth;

import org.n52.v3d.worldviz.worldscene.VsDrapedWorldSphereScene;

public class DrapedWorldSphere {

	private static String outputFile = "test/DrapedWorldSphere_new.x3d";
	private static String pathToMainTexture = "../data/color_etopo1_ice_low.jpg";
	private static String[] additionalTextures = new String[] { "../data/color_etopo1_ice_low_BumpMap.jpg" };

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		VsDrapedWorldSphereScene scene = new VsDrapedWorldSphereScene(
				outputFile, pathToMainTexture, additionalTextures);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
				+ "s");
	}

}
