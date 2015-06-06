package org.n52.v3d.worldviz.demoapplications.ene.earth;

import org.n52.v3d.worldviz.worldscene.OutputFormatEnum;
import org.n52.v3d.worldviz.worldscene.VsDrapedWorldSphereScene;

public class DrapedWorldSphere_withVgElevationGrid {

	private static String outputFile = "test/DrapedWorldSphere_withVgELevationGrid.x3d";
	private static String pathToMainTexture = "../data/earth/color_etopo1_ice_low.jpg";

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		VsDrapedWorldSphereScene scene = new VsDrapedWorldSphereScene(
				outputFile, pathToMainTexture);
		
		scene.setOutputFormat(OutputFormatEnum.X3D);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
				+ "s");
	}

}
