package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgFeature;

public class PajekReader extends FileIO {

    /**
     * reads a feature net given in Pajek NET format from a file.
     *
     * @param fileName File path and name
     * @return Feature-net
     */
    public WvizUniversalFeatureNet readFromFile(String fileName) throws PajekException {
        
        PajekReader pajekReader = new PajekReader();
        pajekReader.init(fileName);
        
        if (pajekReader.readFile(pajekReader.getReader())) {
            ArrayList<Edge> edges = pajekReader.getEdges();
            ArrayList<Vertex> vertices = pajekReader.getVertices();
            ArrayList<Arc> arcs = pajekReader.getArcs();

            ArrayList<VgFeature> features = new ArrayList<VgFeature>();
            ArrayList<VgRelation> relations = new ArrayList<VgRelation>();
            
            for (Vertex vertex : vertices) {
                VgFeature node = new GmAttrFeature();
                ((GmAttrFeature) node).setGeometry(new GmPoint(vertex.getX(), vertex.getY(), vertex.getZ()));
                ((GmAttrFeature) node).addAttribute("name", "String", vertex.getLabel());
                features.add(node);
            }

            for (Edge e : edges) {
                int first = e.getFirstVertex();
                int second = e.getSecondVertex();

                /*This is under the assumption that all nodes that are presetnt in vertexArrayList are present in proper order (1,2,...n) 
                 If this is not the case, then a sorting operation can be performed on the list based on vertexNumber
                 We do vertexNumber-1 in nodes.get() because Pajek is 1-indexed while Java is 0-indexed
                 */
                VgRelation edge = new WvizConnection(features.get(first - 1), features.get(second - 1), e.getWeight());
                relations.add(edge);
            }
            
            for (Arc a : arcs) {
                int first = a.getFirstVertex();
                int second = a.getSecondVertex();

                /*This is under the assumption that all nodes that are presetnt in vertexArrayList are present in proper order (1,2,...n) 
                 In case if this is not the case, then a sorting operation can be performed on the list based on vertexNumber
                 We do vertexNumber-1 in nodes.get() because Pajek is 1-indexed while Java is 0-indexed
                 */
                VgRelation arc = new WvizFlow(features.get(first - 1), features.get(second - 1), a.getWeight());
                relations.add(arc);
            }

            WvizUniversalFeatureNet net = new WvizUniversalFeatureNet(features, relations);
            return net;

        }
        else {
            return null;
        }
    }
    
    public static void main(String args[]) throws PajekException{
        //Let's test our reader!
        PajekReader reader = new PajekReader();
        WvizUniversalFeatureNet wvizuniversalfeaturenet = reader.readFromFile(args[0]);
        for (VgFeature f : wvizuniversalfeaturenet.getFeatures()) {
            System.out.println(f);
        }
        
        for (VgRelation r : wvizuniversalfeaturenet.getRelations()) {
            System.out.println(r);
        }
        
    }
    
}
