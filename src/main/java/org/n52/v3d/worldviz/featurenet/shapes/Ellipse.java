package org.n52.v3d.worldviz.featurenet.shapes;

import java.util.ArrayList;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 *
 * @author Adhitya Kamakshidasan
 */

public class Ellipse {
    
    public ArrayList<T3dVector> ellipsePoints = new ArrayList<T3dVector>();
    
    public VgPoint generatePoint(double a, double b, double angle){
        //Angle to be specified in radians
        double x = a * Math.cos(angle);
        double y = b * Math.sin(angle);
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
    
    
    
    public ArrayList<T3dVector> generateEllipse(double a, double b, int theta){
        
        VgPoint point;
        T3dVector vector;
        double angle;
        
        //We want only half the curve - That is why we have not multiplied it by two
        for(long i=0; i<=theta; i++){
            angle = (i * Math.PI)/theta;
            point = generatePoint(a, b, angle);
            point.setZ(0.0);
            vector = generateVector(point);
            ellipsePoints.add(vector);
        }
        return ellipsePoints;
    }
}
