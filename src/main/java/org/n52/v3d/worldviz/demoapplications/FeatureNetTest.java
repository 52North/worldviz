package org.n52.v3d.worldviz.demoapplications;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlow;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlowNet;

import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.impl.PajekReader;
import org.n52.v3d.worldviz.featurenet.impl.Parse.PajekException;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.featurenet.scene.MpFeatureNetVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.MprConnectionMapGenerator;
import org.n52.v3d.worldviz.featurenet.scene.WvizConnectionMapSceneX3d;
import org.n52.v3d.worldviz.featurenet.scene.WvizVirtualConnectionMapScene;
import org.n52.v3d.worldviz.featurenet.xstream.WvizConfig;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple demonstrator illustrating how to construct a feature-net.
 *
 * @author Benno Schmidt, Adhitya Kamakshidasan
 */


public class FeatureNetTest {
    
    public String pajekFile,configurationFile;
    public String outputFile = RelativePaths.TEST_FOLDER;
    public boolean X3DOMMode = false;
    

    final static Logger logger = LoggerFactory.getLogger(FeatureNetTest.class);
    
    public static void main(String[] args) throws PajekException {
        
        String pajekFile = RelativePaths.PAJEK_FLOWS_OF_TRADE_NET;
        String configurationFile = RelativePaths.STYLE_CONFIGURATION_XML;
        
        FeatureNetTest app = new FeatureNetTest();
        app.setConfig(pajekFile,configurationFile, true);
        app.run();
    }
    
    public void setConfig(String pajekFile, String configurationFile, boolean X3DOMMode){
        this.configurationFile = configurationFile;
        this.pajekFile = pajekFile;
        this.X3DOMMode = X3DOMMode;
        Path path = Paths.get(pajekFile);
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
    
    
    public void setOutputFile(String outputFile){
        this.outputFile = outputFile;
    }
    
    public String getOutputFile(){
        return this.outputFile;
    }

    public void run() throws PajekException {
        VgFeatureNet net = this.generateFeatureNet();
        
        //this.print(net); // Test output
        
        WvizConnectionMapSceneX3d result = this.generateX3dScene(net);
        result.setX3domMode(X3DOMMode);
        result.writeToFile(outputFile);
        logger.info("Result written to file! "+ outputFile);
    }

    private VgFeatureNet generateFeatureNet() throws PajekException {
        PajekReader pajekReader = new PajekReader();
        WvizUniversalFeatureNet wvizUniversalFeatureNet = pajekReader.readFromFile(pajekFile);
        /*
         VgFeature[] nodes = new VgFeature[3];

         nodes[0] = new GmAttrFeature();
         ((GmAttrFeature) nodes[0]).setGeometry(new GmPoint(1., 3., 0.));
         ((GmAttrFeature) nodes[0]).addAttribute("name", "String", "A");

         nodes[1] = new GmAttrFeature();
         ((GmAttrFeature) nodes[1]).setGeometry(new GmPoint(3., 6., 0.));
         ((GmAttrFeature) nodes[1]).addAttribute("name", "String", "B");

         nodes[2] = new GmAttrFeature();
         ((GmAttrFeature) nodes[2]).setGeometry(new GmPoint(5., 3., 0.));
         ((GmAttrFeature) nodes[2]).addAttribute("name", "String", "C");
         //((GmAttrFeature) nodes[2]).setAttributeValue("name", "C"); TODO: Bug inside Triturus 1.0 -> Benno

         VgRelation[] edges = new VgRelation[3];

         edges[0] = new WvizFlow(nodes[0], nodes[1], 5.);
         edges[1] = new WvizFlow(nodes[1], nodes[2], 6.);
         edges[2] = new WvizFlow(nodes[2], nodes[1], 7.);

         VgFeatureNet net = new WvizFlowNet(nodes, edges);
         */
        return wvizUniversalFeatureNet;
    }

    private void print(VgFeatureNet net) {
        System.out.println(net);

        for (VgFeature f : net.getFeatures()) {
            System.out.println(f);
            WvizFlow[] out = (WvizFlow[]) ((WvizFlowNet) net).getOutFlows(f);
            if (out != null && out.length > 0) {
                System.out.println("  Out flows:");
                for (WvizFlow fl : out) {
                    System.out.println("    " + fl);
                }
            }
            WvizFlow[] in = (WvizFlow[]) ((WvizFlowNet) net).getInFlows(f);
            if (in != null && in.length > 0) {
                System.out.println("  In flows:");
                for (WvizFlow fl : in) {
                    System.out.println("    " + fl);
                }
            }
        }
    }

    private WvizConnectionMapSceneX3d generateX3dScene(VgFeatureNet net) {
        // Construct virtual connection-map scene:

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
