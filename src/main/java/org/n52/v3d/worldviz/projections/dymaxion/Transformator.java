package org.n52.v3d.worldviz.projections.dymaxion;

/**
 * Dymaxion transformation implementation. The basic code has been taken from the dymax.js code included with Protovis,
 * see http://mbostock.github.io/protovis/ex/dymax.js. Note that only the forward transformation (from lat/lon to 
 * Dymaxion coordinates) has been implemented. 
 * 
 * @author Benno Schmidt
 *
 */
public class Transformator 
{
	// Note: In Java, array indexing starts with zero (0). Here, array indexing starts with 1, 
	// so all arrays are defined one element longer than they need to be.

	final static private double SQRT_3  = Math.sqrt(3);
	final static private double SQRT_5  = Math.sqrt(5);
	final static private double SQRT_8  = Math.sqrt(8);
	final static private double SQRT_10 = Math.sqrt(10);
	final static private double SQRT_15 = Math.sqrt(15);

	private Point3D[] v = new Point3D[13];
	private Point3D[] center = new Point3D[21];
	private double garc, gt, gdve, gel;

	private double lastLon = 100000;
	private double lastLat = 100000;
	private Point2D lastPoint; 
	
	public Transformator() {
		this.init_stuff();
	}

	// TODO Methode fuer VgPoint ergaenzen mit CRS-in =(!) "EPSG:4326" und CRS-out = "DYMAX",
	// "DYMAX"-Werte evtl. x *= 150. und y *= 150.
	
	public Point2D s2pCached(PointLatLon ll) throws Exception
	{
	  if (lastLat == ll.lat && lastLon == ll.lon) {
	    return lastPoint;
	  } 
	  else {
	    lastLon = ll.lon;
	    lastLat = ll.lat;
	    lastPoint = this.s2p(ll);
	    return lastPoint;
	  }
	}
	
	/**
	 * Convert the given (lon, lat) coordinate into Fuller x-y coordinates (radius, theta, phi) with radius = 1. 
	 * Angles are given in radians (not in degrees).
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public Point2D s2p(PointLatLon ll) throws Exception
	{
		// This is the main control procedure.
		
		PointPolar sc = this.ll2sc(ll);

		// Convert the spherical polar coordinates into Cartesian (x, y, z) coordinates:
		Point3D c = this.s2c(sc);

		// Determine which of the 20 spherical icosahedron triangles the given point is in and the LCD triangle:
		InfoObj info = this.s_tri_info(c);

		// Determine the corresponding Fuller map plane(x, y) point:
		return this.dymax_point(info.tri, info.hlcd, c);
	}


	private PointPolar ll2sc(PointLatLon ll) {
		// convert(lat, lon) point into spherical polar coordinates with radius = 1. Angles are given in radians.
		double h_theta = 90. - ll.lat;
		double h_phi = ll.lon;
		if (ll.lon < 0.) {
			h_phi = ll.lon + 360.;
		}
		return new PointPolar(deg2rad(h_theta), deg2rad(h_phi));
	} 

	private double deg2rad(double degrees) {
	    // convert angles given in degrees into angles give in radians
	    return(Math.PI * degrees / 180.);
	} 

	private void init_stuff() 
	{
		// initializes the global variables which includes the vertex coordinates and mid-face coordinates.

		// Cartesian coordinates for the 12 vertices of icosahedron:
		v[1]  = new Point3D( 0.420152426708710003,  0.078145249402782959,  0.904082550615019298);
		v[2]  = new Point3D( 0.995009439436241649, -0.091347795276427931,  0.040147175877166645);
		v[3]  = new Point3D( 0.518836730327364437,  0.835420380378235850,  0.181331837557262454);
		v[4]  = new Point3D(-0.414682225320335218,  0.655962405434800777,  0.630675807891475371);
		v[5]  = new Point3D(-0.515455959944041808, -0.381716898287133011,  0.767200992517747538);
		v[6]  = new Point3D( 0.355781402532944713, -0.843580002466178147,  0.402234226602925571);
		v[7]  = new Point3D( 0.414682225320335218, -0.655962405434800777, -0.630675807891475371);
		v[8]  = new Point3D( 0.515455959944041808,  0.381716898287133011, -0.767200992517747538);
		v[9]  = new Point3D(-0.355781402532944713,  0.843580002466178147, -0.402234226602925571);
		v[10] = new Point3D(-0.995009439436241649,  0.091347795276427931, -0.040147175877166645);
		v[11] = new Point3D(-0.518836730327364437, -0.835420380378235850, -0.181331837557262454);
		v[12] = new Point3D(-0.420152426708710003, -0.078145249402782959, -0.904082550615019298);

		// now calculate mid face coordinates:
		
		Point3D hold = new Point3D(42.,42.,42.);
		double magn;

		for (int i = 1; i < 21; i++) {
			switch (i) {
				case 1: hold = this.midFace(v[1], v[2], v[3]); break;
				case 2: hold = this.midFace(v[1], v[3], v[4]); break;
				case 3: hold = this.midFace(v[1], v[4], v[5]); break;
				case 4: hold = this.midFace(v[1], v[5], v[6]); break;
				case 5: hold = this.midFace(v[1], v[2], v[6]); break;
				case 6: hold = this.midFace(v[2], v[3], v[8]); break;
				case 7: hold = this.midFace(v[8], v[3], v[9]); break;
				case 8: hold = this.midFace(v[9], v[3], v[4]); break;
				case 9: hold = this.midFace(v[10], v[9], v[4]); break;
				case 10: hold = this.midFace(v[5], v[10], v[4]); break;
				case 11: hold = this.midFace(v[5], v[11], v[10]); break;
				case 12: hold = this.midFace(v[5], v[6], v[11]); break;
				case 13: hold = this.midFace(v[11], v[6], v[7]); break;
				case 14: hold = this.midFace(v[7], v[6], v[2]); break;
				case 15: hold = this.midFace(v[8], v[7], v[2]); break;
				case 16: hold = this.midFace(v[12], v[9], v[8]); break;
				case 17: hold = this.midFace(v[12], v[9], v[10]); break;
				case 18: hold = this.midFace(v[12], v[11], v[10]); break;
				case 19: hold = this.midFace(v[12], v[11], v[7]); break;
				case 20: hold = this.midFace(v[12], v[8], v[7]); break;
			}
			magn = hold.abs();
			center[i] = hold.getScaled(1./magn);
		}

		garc = 2. * Math.asin(Math.sqrt(5. - SQRT_5) / SQRT_10);
		gt = garc / 2.;

		gdve = Math.sqrt(3. + SQRT_5) / Math.sqrt(5. + SQRT_5);
		gel = SQRT_8 / Math.sqrt(5. + SQRT_5);
	} 

	private Point3D midFace(Point3D v1, Point3D v2, Point3D v3) {
		return new Point3D(
				(v1.x + v2.x + v3.x) / 3.,
				(v1.y + v2.y + v3.y) / 3.,
				(v1.z + v2.z + v3.z) / 3.);
	}
	
	private Point3D s2c(PointPolar p) {
		// Convert spherical polar coordinates to Cartesian coordinates. 
		// The angles are given in radians.
		return new Point3D(
				Math.sin(p.theta) * Math.cos(p.phi),
				Math.sin(p.theta) * Math.sin(p.phi),
				Math.cos(p.theta));
	} 

	private PointLatLon c2s(Point3D c) {
		// convert Cartesian coordinates into spherical polar coordinates. 
		// The angles are given in radians.                                
		PointLatLon s = new PointLatLon(42., 42.);
	    double a = 0.; // wird a stets gesetzt im Folgenden? 

	    if (c.x > 0. && c.y > 0.) {
	    	a = deg2rad(0.);
	    }
	    if (c.x < 0. && c.y > 0.) {
	    	a = deg2rad(180.);
	    }
	    if (c.x < 0. && c.y < 0.) {
	    	a = deg2rad(180.);
	    }
	    if (c.x > 0. && c.y < 0.) {
	    	a = deg2rad(360.);
	    }
	    s.lat = Math.acos(c.z);
	    if (c.x == 0. && c.y > 0.) {
	    	s.lon = deg2rad(90.);
	    }
	    if (c.x == 0. && c.y < 0.) {
	    	s.lon = deg2rad(270.);
	    }
	    if (c.x > 0. && c.y == 0.) {
	    	s.lon = deg2rad(0.);
	    }
	    if (c.x < 0. && c.y == 0.) {
	    	s.lon = deg2rad(180.);
	    }
	    if (c.x != 0. && c.y != 0.){
	    	s.lon = Math.atan(c.y / c.x) + a;
	    }
	    return s;
	} 

	/**
	 * Determine which triangle and LCD triangle the point is in.
	 *  
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public InfoObj s_tri_info(Point3D c) throws Exception
	{
		double h_dist1 = 9999., h_dist2, h_dist3; 
		int h_lcd = -1; 
		int v1 = -1, v2 = -1, v3 = -1;       
	  
		InfoObj info = new InfoObj();

		int h_tri = 0;

		// Which triangle face center is the closest to the given point is the triangle in which the given point is in.

		Point3D h; 
		for (int i = 1; i <= 20; i++) {
			h = new Point3D(
					center[i].x - c.x,
					center[i].y - c.y,
					center[i].z - c.z);
			h_dist2 = h.abs();
			if (h_dist2 < h_dist1) {
				h_tri = i;
				h_dist1 = h_dist2;
			} 
		}

		info.tri = h_tri;

		// Now the LCD triangle is determined. 

		switch (h_tri)
		{
	    	case 1:  v1 =  1; v2 =  3; v3 =  2; break;
	    	case 2:  v1 =  1; v2 =  4; v3 =  3; break;
	    	case 3:  v1 =  1; v2 =  5; v3 =  4; break;
	    	case 4:  v1 =  1; v2 =  6; v3 =  5; break;
	    	case 5:  v1 =  1; v2 =  2; v3 =  6; break;
	    	case 6:  v1 =  2; v2 =  3; v3 =  8; break;
	    	case 7:  v1 =  3; v2 =  9; v3 =  8; break;
	    	case 8:  v1 =  3; v2 =  4; v3 =  9; break;
	    	case 9:  v1 =  4; v2 = 10; v3 =  9; break;
	    	case 10: v1 =  4; v2 =  5; v3 = 10; break;
	    	case 11: v1 =  5; v2 = 11; v3 = 10; break;
	    	case 12: v1 =  5; v2 =  6; v3 = 11; break;
	    	case 13: v1 =  6; v2 =  7; v3 = 11; break;
	    	case 14: v1 =  2; v2 =  7; v3 =  6; break;
	    	case 15: v1 =  2; v2 =  8; v3 =  7; break;
	    	case 16: v1 =  8; v2 =  9; v3 = 12; break;
	    	case 17: v1 =  9; v2 = 10; v3 = 12; break;
	    	case 18: v1 = 10; v2 = 11; v3 = 12; break;
	    	case 19: v1 = 11; v2 =  7; v3 = 12; break;
	    	case 20: v1 =  8; v2 = 12; v3 =  7; break;
	   } 

	   if (v1 < 0 || v2 < 0 || v3 < 0) {
		   throw new Exception("v-variables have not been set properly!");
	   }
	   
	   h_dist1 = new Point3D(c.x - v[v1].x, c.y - v[v1].y, c.z - v[v1].z).abs();
	   h_dist2 = new Point3D(c.x - v[v2].x, c.y - v[v2].y, c.z - v[v2].z).abs();
	   h_dist3 = new Point3D(c.x - v[v3].x, c.y - v[v3].y, c.z - v[v3].z).abs();

	   if (h_dist1 <= h_dist2 && h_dist2 <= h_dist3) {
		   h_lcd = 1; 
	   }
	   if (h_dist1 <= h_dist3 && h_dist3 <= h_dist2) {
		   h_lcd = 6; 
	   }
	   if (h_dist2 <= h_dist1 && h_dist1 <= h_dist3) {
		   h_lcd = 2; 
	   }
	   if (h_dist2 <= h_dist3 && h_dist3 <= h_dist1) {
		   h_lcd = 3; 
	   }
	   if (h_dist3 <= h_dist1 && h_dist1 <= h_dist2) {
		   h_lcd = 5; 
	   }
	   if (h_dist3 <= h_dist2 && h_dist2 <= h_dist1) {
		   h_lcd = 4; 
	   }

	   if (h_lcd < 0) {
		   throw new Exception("h_lcd have not been set properly!");
	   }  
	   info.hlcd = h_lcd;

	   return info;
	} 

	private Point2D dymax_point(int tri, double lcd, Point3D c) throws Exception
	{
		int axis, v1 = -1; 

		// In order to rotate the given point into the template spherical triangle, we need the 
		// spherical polar coordinates of the center of the face and one of the face vertices. 
		// So set up which vertex to use.
	  
		switch (tri)
		{
	    	case 1:  v1 =  1;  break;
	    	case 2:  v1 =  1;  break;
	    	case 3:  v1 =  1;  break;
	    	case 4:  v1 =  1;  break;
	    	case 5:  v1 =  1;  break;
	    	case 6:  v1 =  2;  break;
	    	case 7:  v1 =  3;  break;
	    	case 8:  v1 =  3;  break;
	    	case 9:  v1 =  4;  break;
	    	case 10: v1 =  4;  break;
	    	case 11: v1 =  5;  break;
	    	case 12: v1 =  5;  break;
	    	case 13: v1 =  6;  break;
	    	case 14: v1 =  2;  break;
	    	case 15: v1 =  2;  break;
	    	case 16: v1 =  8;  break;
	    	case 17: v1 =  9;  break;
	    	case 18: v1 = 10;  break;
	    	case 19: v1 = 11;  break;
	    	case 20: v1 =  8;  break;
		} 

		Point3D h0 = new Point3D(c);

		if (v1 < 0) {
			throw new Exception("v-variable has not been set properly!");
		}

		Point3D h1 = new Point3D(v[v1]);

		PointLatLon h = this.c2s(center[tri]);

		axis = 3;
		h0 = this.rotate3d(axis, h.lon, h0);
		h1 = this.rotate3d(axis, h.lon, h1);

		axis = 2;
		h0 = this.rotate3d(axis, h.lat, h0);
		h1 = this.rotate3d(axis, h.lat, h1);

		h = this.c2s(h1);
		h.lon = h.lon - deg2rad(90.);

		axis = 3;
		h0 = this.rotate3d(axis, h.lon, h0);

		double gx, gy, gz;

		// exact transformation equations 

		gz = Math.sqrt(1. - h0.x * h0.x - h0.y * h0.y);
		double gs = Math.sqrt(5. + 2. * SQRT_5) / (gz * SQRT_15);

		double gxp = h0.x * gs;
		double gyp = h0.y * gs;

		double ga1p = 2. * gyp/SQRT_3 + (gel/3.);
		double ga2p = gxp - (gyp/SQRT_3) +  (gel/3.);
		double ga3p = (gel/3.) - gxp - (gyp/SQRT_3);

		double ga1 = gt + Math.atan((ga1p - 0.5 * gel) / gdve);
		double ga2 = gt + Math.atan((ga2p - 0.5 * gel) / gdve);
		double ga3 = gt + Math.atan((ga3p - 0.5 * gel) / gdve);

		gx = 0.5 * (ga2 - ga3);
		gy = (1./(2. * SQRT_3) ) * (2. * ga1 - ga2 - ga3);

		// Re-scale so plane triangle edge length is 1.
		Point2D pt = new Point2D(gx / garc, gy / garc);

		// Rotate and translate to correct position          
		Point2D q = null;
	  
		switch (tri) {
			case 1: 
				pt = this.rotate2d(240., pt);
				q = new Point2D(pt.x + 2., pt.y + 7./(2. * SQRT_3)); 
				break;
			case 2: 
				pt = this.rotate2d(300., pt); 
				q = new Point2D(pt.x + 2., pt.y + 5./(2. * SQRT_3)); 
	            break;
			case 3: 
	    	 	pt = this.rotate2d(0., pt);
	            q = new Point2D(pt.x + 2.5, pt.y + 2./SQRT_3); 
	            break;
			case 4: 
				pt = this.rotate2d(60., pt);
	            q = new Point2D(pt.x + 3., pt.y + 5./(2. * SQRT_3)); 
	            break;
			case 5: 
				pt = this.rotate2d(180., pt);
				q = new Point2D(pt.x + 2.5, pt.y + 4. * SQRT_3 / 3.); 
				break;
			case 6: 
				pt = this.rotate2d(300., pt);
	            q = new Point2D(pt.x + 1.5, pt.y + 4. * SQRT_3 / 3.); 
	            break;
			case 7: 
				pt = this.rotate2d(300., pt);
	            q = new Point2D(pt.x + 1., pt.y + 5. / (2.*SQRT_3)); 
	            break;
			case 8: 
				pt = this.rotate2d(0., pt);
				q = new Point2D(pt.x + 1.5, pt.y + 2./SQRT_3); 
				break;
			case 9: 
				if (lcd > 2) {
					pt = this.rotate2d(300., pt);
					q = new Point2D(pt.x + 1.5, pt.y + 1./SQRT_3);
				}
				else {
					pt = this.rotate2d(0., pt);
					q = new Point2D(pt.x + 2., pt.y + 1./(2. * SQRT_3));
				}
				break;
			case 10: 
				pt = this.rotate2d(60., pt);
				q = new Point2D(pt.x + 2.5, pt.y + 1./SQRT_3); 
				break;
			case 11: 
				pt = this.rotate2d(60., pt);
				q = new Point2D(pt.x + 3.5, pt.y + 1./SQRT_3); 
				break;
			case 12: 
				pt = this.rotate2d(120., pt);
				q = new Point2D(pt.x + 3.5, pt.y + 2./SQRT_3); 
				break;
			case 13: 
				pt = this.rotate2d(60., pt);
				q = new Point2D(pt.x + 4., pt.y + 5. / (2.*SQRT_3)); 
				break;
			case 14: 
				pt = this.rotate2d(0., pt);
				q = new Point2D(pt.x + 4., pt.y + 7. / (2.*SQRT_3)); 
				break;
			case 15: 
				pt = this.rotate2d(0., pt);
				q = new Point2D(pt.x + 5., pt.y + 7. / (2.*SQRT_3)); 
				break;
			case 16: 
	    	 	if (lcd < 4) {
	    	 		pt = this.rotate2d(60., pt);
	    	 		q = new Point2D(pt.x + 0.5, pt.y + 1./SQRT_3);
	    	 	}
	    	 	else {
	    	 		pt = this.rotate2d(0., pt);
	    	 		q = new Point2D(pt.x + 5.5, pt.y + 2./SQRT_3);
	    	 	}
	    	 	break;
			case 17: 
				pt = this.rotate2d(0., pt);
				q = new Point2D(pt.x + 1., pt.y + 1./(2. * SQRT_3)); 
				break;
			case 18: 
				pt = this.rotate2d(120., pt);
				q = new Point2D(pt.x + 4., pt.y + 1./(2. * SQRT_3)); 
				break;
			case 19: 
				pt = this.rotate2d(120., pt);
				q = new Point2D(pt.x + 4.5, pt.y + 2./SQRT_3); 
				break;
			case 20: 
				pt = this.rotate2d(300., pt);
				q = new Point2D(pt.x + 5., pt.y + 5./(2. * SQRT_3)); 
				break;
	   } 
		
	   return q;
	} 

	private Point2D rotate2d(double angle, Point2D p) {
	  // Rotate the point to correct orientation in XY-plane.
	  double a = deg2rad(angle);
	  return new Point2D(
			  p.x * Math.cos(a) - p.y * Math.sin(a),
			  p.x * Math.sin(a) + p.y * Math.cos(a));
	}

	private Point3D rotate3d(int axis, double alpha, Point3D p) {
		// Rotate a 3D point about the specified axis.
		Point3D q = new Point3D(p);
		switch (axis) {
	  		case 1:
	  			q.y = p.y * Math.cos(alpha) + p.z * Math.sin(alpha);
	  			q.z = p.z * Math.cos(alpha) - p.y * Math.sin(alpha);
	  			break;
	  		case 2:
	  			q.x = p.x * Math.cos(alpha) - p.z * Math.sin(alpha);
	  			q.z = p.x * Math.sin(alpha) + p.z * Math.cos(alpha);
	  			break;
	  		case 3:	
	  			q.x = p.x * Math.cos(alpha) + p.y * Math.sin(alpha);
	  			q.y = p.y * Math.cos(alpha) - p.x * Math.sin(alpha);
	  			break;
		}
		return q;
	}
}
