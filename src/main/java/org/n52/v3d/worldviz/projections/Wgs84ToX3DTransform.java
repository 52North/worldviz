package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 *
 * @author Adhitya Kamakshidasan
 */
public class Wgs84ToX3DTransform {
    public ArrayList<VgPoint> transform(ArrayList<VgPoint> geoPos) {
        T3dVector vector;
        AxisSwitchTransform t1 = new AxisSwitchTransform();
        
        for (int i=0; i<geoPos.size(); i++) {
        	VgPoint point = geoPos.get(i);
        	
            point.setSRS(VgPoint.SRSLatLonWgs84);
            vector = t1.transform(point);
            point = new GmPoint(vector.getX(), vector.getY(), vector.getZ());
            
            geoPos.set(i, point);
        }      
        
        NormTransform_Wgs84 t2 = new NormTransform_Wgs84(geoPos);
        
        for(int i =0; i<geoPos.size();i++){
            vector = t2.transform(geoPos.get(i));
            geoPos.set(i, new GmPoint(vector.getX(), vector.getY(), vector.getZ()) );
        }
      
        return geoPos;
    }
    
    public ArrayList <VgPoint> transformVertices(ArrayList <VgFeature> vgFeatures){
        ArrayList <VgPoint> vertices =  new ArrayList<VgPoint>();
        for(VgFeature feature:vgFeatures){
            vertices.add((VgPoint) (feature.getGeometry()));
        }
        return vertices;
    }
    
}
