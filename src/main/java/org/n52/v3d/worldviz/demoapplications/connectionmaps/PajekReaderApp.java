/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 * - Apache License, version 2.0 - Apache Software License, version 1.0 - GNU
 * Lesser General Public License, version 3 - Mozilla Public License, versions
 * 1.0, 1.1 and 2.0 - Common Development and Distribution License (CDDL),
 * version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders if
 * the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package org.n52.v3d.worldviz.demoapplications.connectionmaps;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.n52.v3d.worldviz.featurenet.impl.Edge;
import org.n52.v3d.worldviz.featurenet.impl.FileIO;
import org.n52.v3d.worldviz.featurenet.impl.Vector;
import org.n52.v3d.worldviz.featurenet.impl.X3DScene;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class PajekReaderApp extends FileIO {

    public String pajekFile;
    public String outputPath = RelativePaths.TEST_FOLDER;

    public void setPajekFile(String pajekFile) {
        this.pajekFile = pajekFile;
    }

    public void setOutputFile(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputFile() {
        return this.outputPath;
    }

    public String getFileName(String pajekFile) {
        Path path = Paths.get(pajekFile);
        String fileName = path.getFileName().toString();
        fileName = fileName.split("\\.")[0];
        fileName += ".html";
        outputPath = new File(outputPath, fileName).toString();
        return outputPath;
    }

    public void run() throws PajekException {
        PajekReaderApp pajekReaderApp = new PajekReaderApp();
        pajekReaderApp.init(pajekFile);
        getFileName(pajekFile);
        if (pajekReaderApp.readFile(pajekReaderApp.getReader())) {
            ArrayList<Edge> edgeArrayList = pajekReaderApp.getEdges();
            for (Edge edge : edgeArrayList) {
                new Vector().getDirection(edge, pajekReaderApp.getVertices());
            }
            X3DScene x3DScene = new X3DScene(pajekReaderApp.getVertices(), pajekReaderApp.getEdges());
            x3DScene.writeToX3dFile(getOutputFile());
        }
    }

    public PajekReaderApp() {
        super();
    }

}
