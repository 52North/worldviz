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
package org.n52.v3d.worldviz.demoapplications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.xmlbeans.XmlException;
import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.impl.CsvReaderForConnectionMap;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.featurenet.scene.MpFeatureNetVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.MprConnectionMapGenerator;
import org.n52.v3d.worldviz.featurenet.scene.WvizConnectionMapSceneX3d;
import org.n52.v3d.worldviz.featurenet.scene.WvizVirtualConnectionMapScene;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

/**
 * Simple demonstrator illustrating how to construct a feature-net.
 *
 * @author Benno Schmidt, Adhitya Kamakshidasan
 */
public class FeatureNetTest_CSV implements FeatureNetInterface{

    public String csvFile, configurationFile;
    public String outputFile = RelativePaths.TEST_FOLDER;
    public boolean X3DOMMode = false;

    final static Logger logger = LoggerFactory.getLogger(FeatureNetTest.class);

    public static void main(String[] args) {

        String csvFile = RelativePaths.EXPORTS_PARTNER_CSV;
        String configurationFile = RelativePaths.STYLE_CONFIGURATION_XML;

        FeatureNetTest_CSV app = new FeatureNetTest_CSV();
        app.setConfig(csvFile, configurationFile, true);
        app.run();
    }

    public void setConfig(String csvFile, String configurationFile, boolean X3DOMMode) {
        this.configurationFile = configurationFile;
        this.csvFile = csvFile;
        this.X3DOMMode = X3DOMMode;
        Path path = Paths.get(csvFile);
        String fileName = path.getFileName().toString();
        fileName = fileName.split("\\.")[0];
        if (X3DOMMode) {
            fileName = fileName + ".html";
        } else {
            fileName = fileName + ".x3d";
        }
        outputFile = new File(outputFile, fileName).toString();
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return this.outputFile;
    }

    public void run() {
        CsvReaderForConnectionMap csvReaderForConnectionMap = new CsvReaderForConnectionMap();
        WvizUniversalFeatureNet featureNet = csvReaderForConnectionMap.readFromFile(csvFile, ';');

        WvizConnectionMapSceneX3d result = this.generateX3dScene(featureNet);
        result.setX3domMode(X3DOMMode);
        result.writeToFile(outputFile);
        logger.info("Result written to file! " + outputFile);
    }

    private WvizConnectionMapSceneX3d generateX3dScene(VgFeatureNet net) {

        // Construct virtual connection-map scene
        MpFeatureNetVisualizer t1 = new MpFeatureNetVisualizer();
        
        WvizConfigDocument wVisConfigDoc;
        de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig configFile;
        
        try {
			wVisConfigDoc = WvizConfigDocument.Factory.parse(new File(configurationFile));
			
			configFile = wVisConfigDoc.getWvizConfig();
		
        } catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new T3dException("Error while parsing the config file!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new T3dException("Error while parsing the config file!");
		}
		
        t1.setStyle(configFile);
        WvizVirtualConnectionMapScene s = t1.transform(net);

        // Export abstract scene to concrete scene descriptions:
        MprConnectionMapGenerator t2 = new MprConnectionMapGenerator();

        // Then generate an X3D file:
        t2.setTargetFormat(MprConnectionMapGenerator.TargetFormats.X3D);
        Object result = t2.transform(s);

        if (result instanceof WvizConnectionMapSceneX3d) {
            WvizConnectionMapSceneX3d wvizConnectionMapSceneX3d = (WvizConnectionMapSceneX3d) result;
            return wvizConnectionMapSceneX3d;
        } else {
            return null;
        }
    }
}
