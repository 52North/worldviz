package org.n52.v3d.worldviz.featurenet.shapes;

import java.util.ArrayList;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;

/**
 *
 * @author Adhitya Kamakshidasan
 */
public class Ribbon {
    public ArrayList<T3dVector> ribbonPoints = new ArrayList<T3dVector>();
    
    public double step = 0.01;
    
    public double getStep(){
        return step;
    }
    
    public void setStep(double step){
        this.step = step;
    }
   
    public ArrayList<T3dVector> generateRibbon(double radius, double distance, int theta){
        Circle circle = new Circle();
        AxisSwitchTransform t = new AxisSwitchTransform();
        VgPoint point;
        T3dVector vector;
        double angle;
        
        double step = getStep();
       
        long turns = Math.round(distance/step)+1;
        double y = -(distance/2);
        
        for(long i=0; i<turns; i++, y+=step){
            angle = (i * Math.PI)/theta;
            point = circle.generatePoint(radius,angle);
            vector = t.transform(point);
            vector.setY(y);
            ribbonPoints.add(vector);
        }
        return ribbonPoints;
    }
    
}
