package org.n52.v3d.worldviz.demoapplications;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.VgFlow;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlowNet;

/**
 * Simple demonstrator illustrating how to construct a feature-net.
 *    
 * @author Benno Schmidt
 */
public class FeatureNetTest {

	public static void main(String[] args) {

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
		 
		edges[0] = new VgFlow(nodes[0], nodes[1], 5.);
		edges[1] = new VgFlow(nodes[1], nodes[2], 6.);
		edges[2] = new VgFlow(nodes[2], nodes[1], 7.);
		
		VgFeatureNet net = new WvizFlowNet(nodes, edges);

		System.out.println(net);
		
		for (VgFeature f : net.getFeatures()) {
			System.out.println(f);
			VgFlow[] out = (VgFlow[]) ((WvizFlowNet) net).getOutFlows(f);
			if (out != null && out.length > 0) {
				System.out.println("  Out flows:");
				for (VgFlow fl : out) {
					System.out.println("    " + fl);			
				}
			}
			VgFlow[] in = (VgFlow[]) ((WvizFlowNet) net).getInFlows(f);
			if (in != null && in.length > 0) {
				System.out.println("  In flows:");
				for (VgFlow fl : in) {
					System.out.println("    " + fl);			
				}
			}
		}
	}
}
