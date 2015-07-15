package org.n52.v3d.worldviz.featurenet.shapes;

import java.util.ArrayList;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 *
 * @author Adhitya Kamakshidasan
 */

public class Circle {
    
    public ArrayList<T3dVector> circlePoints = new ArrayList<T3dVector>();
    
    public VgPoint generatePoint(double radius, double angle){
        //Angle to be specified in radians
        double x = radius * Math.cos(angle);
        double y = radius * Math.sin(angle);
        double z = 0.0;
        VgPoint point = new GmPoint(x, y, z);
        return point;
    }
    
    public T3dVector generateVector(VgPoint point){
        double x,y,z;
        x = point.getX();
        y = point.getY(); 
        z = point.getZ();
        return new T3dVector(x, y, z);
    }
    
    
    
    public ArrayList<T3dVector> generateCircle(double radius, int theta){
        
        VgPoint point;
        T3dVector vector;
        double angle;
        
        for(long i=0; i<=2*theta; i++){
            angle = (i * Math.PI)/theta;
            point = generatePoint(radius,angle);
            point.setZ(0.0);
            vector = generateVector(point);
            circlePoints.add(vector);
        }
        
        return circlePoints;
    }
}
