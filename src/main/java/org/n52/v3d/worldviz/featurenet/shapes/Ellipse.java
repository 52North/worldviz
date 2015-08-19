/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
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
