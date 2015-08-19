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
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.featurenet.impl;
import java.util.ArrayList;
import java.util.Arrays;

public class Vector extends Parse {
    private double dX,dY,dZ;
    private double angleX,angleY,angleZ;

    public void getDirection(Edge edge, ArrayList<Vertex> vertexArrayList){
        Vertex firstVertex = vertexArrayList.get(edge.firstVertex-1); //ArrayList starts from 1!
        Vertex secondVertex = vertexArrayList.get(edge.secondVertex-1);
        vectorDifference(firstVertex,secondVertex);
        normalizeVector(firstVertex,secondVertex);
        angleX = Math.acos(dX);
        angleY = Math.acos(dY);
        angleZ = Math.acos(dZ);
        System.out.println(edge.firstVertex+"-"+edge.secondVertex+": ");
        System.out.println("firstVertex: "+ Arrays.toString(firstVertex.getCoordinates()));
        System.out.println("secondVertex: "+Arrays.toString(secondVertex.getCoordinates()));
        System.out.println("Angle of edge: "+angleX+", "+angleY+", "+angleZ);
        System.out.println("Length of edge: "+length(firstVertex,secondVertex));
        System.out.println("");
    }

    public void vectorDifference(Vertex v1, Vertex v2) {
        dX = v1.getX() - v2.getX();
        dY = v1.getY() - v2.getY();
        dZ = v1.getZ() - v2.getZ();
    }

    public double length(Vertex firstVertex , Vertex secondVertex) {
        vectorDifference(firstVertex,secondVertex);
        return Math.sqrt(dX*dX + dY*dY + dZ*dZ);
    }

    public boolean normalizeVector(Vertex firstVector, Vertex secondVector) {
        double length = this.length(firstVector,secondVector);
        if (length == 0.0){
            return false;
        }
        else{
            dX /= length;
            dY /= length;
            dZ /= length;
            return true;
        }
    }

}
