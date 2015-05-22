package org.n52.v3d.worldviz.projections.dymaxion;

/**
 * Implementation helper class
 * 
 * @author Benno Schmidt
 *
 */
public class PointLatLon 
{
	double lat;
	double lon;
	
	public PointLatLon(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public String toString() {
		return "[latlon (" + lat + ", " + lon + ")]";
	}
}
