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
        
        for (VgPoint point:geoPos) {
            point.setSRS(VgPoint.SRSLatLonWgs84);
            point = Wgs84ToSphereCoordsTransform.wgs84ToSphere(point, 6370.);
            vector = t1.transform(point);
            point = new GmPoint(vector.getX(), vector.getY(), vector.getZ());
        }
        
        
        NormTransform t2 = new NormTransform(geoPos);
        
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
