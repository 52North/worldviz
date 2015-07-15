package org.n52.v3d.worldviz.demoapplications;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.impl.CsvReaderForConnectionMap;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.demoapplications.FeatureNetTest;
import org.n52.v3d.worldviz.featurenet.scene.MpFeatureNetVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.MpFeatureNetVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.MprConnectionMapGenerator;
import org.n52.v3d.worldviz.featurenet.scene.MprConnectionMapGenerator;
import org.n52.v3d.worldviz.featurenet.xstream.WvizConfig;
import org.n52.v3d.worldviz.featurenet.scene.WvizConnectionMapSceneX3d;
import org.n52.v3d.worldviz.featurenet.scene.WvizConnectionMapSceneX3d;
import org.n52.v3d.worldviz.featurenet.scene.WvizVirtualConnectionMapScene;
import org.n52.v3d.worldviz.featurenet.scene.WvizVirtualConnectionMapScene;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple demonstrator illustrating how to construct a feature-net.
 *
 * @author Benno Schmidt, Adhitya Kamakshidasan
 */


public class FeatureNetTest_CSV {

    public String csvFile,configurationFile;
    public String outputFile = RelativePaths.TEST_FOLDER;
    public boolean X3DOMMode = false;
    
    final static Logger logger = LoggerFactory.getLogger(FeatureNetTest.class);
    
    public static void main(String[] args){
        
        String csvFile = RelativePaths.IMPORTS_PARTNER_CSV;
        String configurationFile = RelativePaths.STYLE_CONFIGURATION_XML;
        
        FeatureNetTest_CSV app = new FeatureNetTest_CSV();
        app.setConfig(csvFile,configurationFile, false);
        app.run();
    }
    
    public void setConfig(String csvFile, String configurationFile, boolean X3DOMMode){
        this.configurationFile = configurationFile;
        this.csvFile = csvFile;
        this.X3DOMMode = X3DOMMode;
        Path path = Paths.get(csvFile);
        String fileName = path.getFileName().toString();
        fileName = fileName.split("\\.")[0];
        if(X3DOMMode){
            fileName = fileName + ".html";            
        }
        else{
            fileName = fileName+ ".x3d";
        }
        outputFile = new File(outputFile, fileName).toString();
    }
    
    public void run(){
        CsvReaderForConnectionMap csvReaderForConnectionMap = new CsvReaderForConnectionMap();
        WvizUniversalFeatureNet featureNet = csvReaderForConnectionMap.readFromFile(csvFile, ';');
        
        WvizConnectionMapSceneX3d result = this.generateX3dScene(featureNet);
        result.setX3domMode(X3DOMMode);
        result.writeToFile(outputFile);
        logger.info("Result written to file! "+ outputFile);
    }
    

    private WvizConnectionMapSceneX3d generateX3dScene(VgFeatureNet net) {
        
        // Construct virtual connection-map scene
        MpFeatureNetVisualizer t1 = new MpFeatureNetVisualizer();
        WvizConfig style = new WvizConfig(configurationFile);
        style = style.getConfiguration();
        t1.setStyle(style);
        WvizVirtualConnectionMapScene s = t1.transform(net);

        // Export abstract scene to concrete scene descriptions:
        MprConnectionMapGenerator t2 = new MprConnectionMapGenerator();

        // Then generate an X3D file:
        t2.setTargetFormat(MprConnectionMapGenerator.TargetFormats.X3D);
        Object result = t2.transform(s);

        if (result instanceof WvizConnectionMapSceneX3d) {
            WvizConnectionMapSceneX3d wvizConnectionMapSceneX3d = (WvizConnectionMapSceneX3d) result;
            return wvizConnectionMapSceneX3d;
        }
        else {
            return null;
        }
    }
}
