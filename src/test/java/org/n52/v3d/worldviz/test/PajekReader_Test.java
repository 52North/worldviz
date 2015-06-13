package org.n52.v3d.worldviz.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.featurenet.impl.PajekReader;
import org.n52.v3d.worldviz.featurenet.impl.WvizConnection;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlow;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;

public class PajekReader_Test {

    public PajekReader reader;
    public WvizUniversalFeatureNet wvizuniversalfeaturenet;

    //Adhitya: This should be later moved to the RelativePaths Class
    public static final String filePath = "data\\graph.net";

    @Before
    public void before() {
        reader = new PajekReader();
        try {
            wvizuniversalfeaturenet = reader.readFromFile(filePath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        try {
            assertTrue(wvizuniversalfeaturenet != null);

            List<VgFeature> features = (List<VgFeature>) (wvizuniversalfeaturenet.getFeatures());
            assertTrue(features != null);

            VgFeature firstFeature = features.get(0);
            assertTrue(firstFeature instanceof GmAttrFeature);
            VgGeomObject geometry = firstFeature.getGeometry();
            assertTrue(geometry != null);

            List<VgRelation> relations = (List<VgRelation>) (wvizuniversalfeaturenet.getRelations());
            assertTrue(relations != null);

            VgRelation firstRelation = relations.get(0);
            //This could be possibly changed in the future
            assertTrue((firstRelation instanceof WvizConnection) || (firstRelation instanceof WvizFlow));

            VgFeature firstVertex = firstRelation.getFrom();
            assertTrue(firstVertex instanceof GmAttrFeature);
            geometry = firstVertex.getGeometry();
            assertTrue(geometry != null);

            VgFeature secondVertex = firstRelation.getTo();
            assertTrue(secondVertex instanceof GmAttrFeature);
            geometry = secondVertex.getGeometry();
            assertTrue(geometry != null);

            //EdgeWeight can be null in nature - We are not testing it!
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

    }

}
