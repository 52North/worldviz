package org.n52.v3d.worldviz.helper;

import org.n52.v3d.triturus.t3dutil.T3dColor;

public class StringParser {
	
	/**
	 * Expects a String like '1.0 0.0 0.0' to parse it into RGB-color!
	 * @param rgbColorAsString a String like '1.0 0.0 0.0' of RGB values
	 * @return
	 */
	public static T3dColor parseStringAsRgbColor(String rgbColorAsString) {
		float[] borderColorRGB = new float[3];
		String[] borderColorRGBString = rgbColorAsString.split(" ");
		for (int i = 0; i < 3; i++) {
			borderColorRGB[i] = Float.parseFloat(borderColorRGBString[i]);
		}

		T3dColor borderColorT3d = new T3dColor(borderColorRGB[0], borderColorRGB[1], borderColorRGB[2]);
		return borderColorT3d;
	}

}
