package org.n52.v3d.worldviz.worldscene.helper;

/**
 * This enumeration is used to distinguish between different level of detail for
 * the borders of the world's countries.<br/>
 * 
 * There are three different levels: <b>DETAILED</b> (1:10m scale),
 * <b>SIMPLIFIED_50m</b> (1:50m scale) and <b>SIMPLIFIED_110m</b> (1:110m scale)
 * 
 * 
 * 
 * @author Christian Danowski
 * 
 */
public enum CountryBordersLODEnum {

	/**
	 * detailed border lines of each country. 1:10m scale
	 */
	DETAILED, 
	
	/**
	 * countries's borders are generalized. 1:50m scale
	 */
	SIMPLIFIED_50m, 
	
	/**
	 * countries's borders are generalized. 1:110m scale
	 */
	SIMPLIFIED_110m

}
