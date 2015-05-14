package org.n52.v3d.worldviz.demoapplications;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * Test class to check, if your Java environment is installed properly.
 *    
 * @author Benno Schmidt, Christian Danowski
 */
public class HelloWorldViz {

	public static void main(String[] args) {
		VgPoint aPoint = new GmPoint(42.,42.,42.);
		
		System.out.println("Hello, Triturus WorldViz!");
		System.out.println("The z-value is: " + aPoint.getZ());
	}
	
}

