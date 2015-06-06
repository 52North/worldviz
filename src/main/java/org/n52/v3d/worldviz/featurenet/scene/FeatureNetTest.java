package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlow;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlowNet;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * Simple demonstrator illustrating how to construct a feature-net.
 *    
 * @author Benno Schmidt
 */
public class FeatureNetTest {

	public static void main(String[] args) 
	{
		FeatureNetTest app = new FeatureNetTest();
		app.run();
	}
	
	private void run() 
	{	
		VgFeatureNet net = this.generateFeatureNet();
		
		this.print(net); // Test output
		
		WvizConnectionMapSceneX3d res = this.generateX3dScene(net);
		res.writeToFile("/tmp/example.x3d");
	}
	
	private VgFeatureNet generateFeatureNet() 
	{	
		VgFeature[] nodes = new VgFeature[3];
		
		nodes[0] = new GmAttrFeature();
		((GmAttrFeature) nodes[0]).setGeometry(new GmPoint(1.,3.,0.));
		((GmAttrFeature) nodes[0]).addAttribute("name", "String", "A");

		nodes[1] = new GmAttrFeature();
		((GmAttrFeature) nodes[1]).setGeometry(new GmPoint(3.,6.,0.));
		((GmAttrFeature) nodes[1]).addAttribute("name", "String", "B");
		
		nodes[2] = new GmAttrFeature();
		((GmAttrFeature) nodes[2]).setGeometry(new GmPoint(5.,3.,0.));
		((GmAttrFeature) nodes[2]).addAttribute("name", "String", "C");
		//((GmAttrFeature) nodes[2]).setAttributeValue("name", "C"); TODO: Bug inside Triturus 1.0 -> Benno
		
		VgRelation[] edges = new VgRelation[3];
		 
		edges[0] = new WvizFlow(nodes[0], nodes[1], 5.);
		edges[1] = new WvizFlow(nodes[1], nodes[2], 6.);
		edges[2] = new WvizFlow(nodes[2], nodes[1], 7.);
		
		VgFeatureNet net = new WvizFlowNet(nodes, edges);

		return net;
	}
	
	private void print(VgFeatureNet net) 
	{
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
	
	private WvizConnectionMapSceneX3d generateX3dScene(VgFeatureNet net) 
	{
		// Construct virtual connection-map scene:
		MpFeatureNetVisualizer t1 = new MpFeatureNetVisualizer();
		WvizConfig style = new WvizConfig();
		t1.setStyle(style);
		WvizVirtualConnectionMapScene s = t1.transform(net);
		
		// Export abstract scene to concrete scene descriptions:
		MprConnectionMapGenerator t2 = new MprConnectionMapGenerator();
		// First, a very simple test:
		t2.setTargetFormat(MprConnectionMapGenerator.TargetFormats.TEST);
		Object result = t2.transform(s);
		// Then generate an X3D file:
		t2.setTargetFormat(MprConnectionMapGenerator.TargetFormats.X3D);
		result = t2.transform(s);
		
		if (result instanceof WvizConnectionMapSceneX3d) {
			return (WvizConnectionMapSceneX3d) result;
		} 
		else {
			return null;
		}
	}
}
