package org.n52.v3d.worldviz.demoapplications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.t3dutil.MpSimpleHypsometricColor;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2NumericExtent;
import org.n52.v3d.worldviz.extensions.mappers.NamesForAttributes;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.featurenet.impl.PajekReader;
import org.n52.v3d.worldviz.featurenet.impl.Parse.PajekException;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlow;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.helper.RelativePaths;

/**
 * Simple demo that shows how to use color mapper and sizeMapper.
 *
 * @author Christian Danowski
 *
 */
public class MappersDemo {

    private final String ATTRIBUTE_NAME = "NumberOfRelations";

    public static void main(String[] args) {
        MappersDemo app = new MappersDemo();

        WvizUniversalFeatureNet featureNet = app.generateFeatureNet();

        // extract relations
        Collection<VgRelation> relations = featureNet.getRelations();

        // transform features to attributed features
        List<VgAttrFeature> attrFeatures = app.generateAttrFeatures(featureNet);

        System.out.println("\n=========================Coloring Attributed Features=============================\n");
        app.colorAttrFeatures(attrFeatures);

        System.out.println("\n=========================Coloring Relations=============================\n");
        app.colorRelations(relations);

        System.out.println("\n=========================Computing Width for Relations=============================\n");
        app.computeWidthMapping(relations);
        
    }

    private void computeWidthMapping(Collection<VgRelation> relations) {
        /*
         * Initiate simple Color Mapper that maps an input value to a color:
         */
        MpValue2NumericExtent widthMapper = new MpValue2NumericExtent();

        /*
         * define the input and output arrays. The must have the same length.
         */
        double[] inputWeights = new double[]{0, 20, 50, 100, 500, 3000};
        double[] outputWidths = new double[]{0.1, 0.2, 0.3, 0.5, 0.7, 1};

        // true sets linear interpolation to true!
        widthMapper.setPalette(inputWeights, outputWidths, true);

        for (VgRelation vgRelation : relations) {
            double weight = (Double) vgRelation.getValue();

            double width = widthMapper.transform(weight);

            System.out.println("Weight '" + weight + "' was mapped to width '" + width + "'.");
        }

    }

    private void colorRelations(Collection<VgRelation> relations) {
        /*
         * Initiate simple Color Mapper that maps an input value to a color:
         */
        MpSimpleHypsometricColor simpleColorMapper = new MpSimpleHypsometricColor();

        /*
         * define the input and output arrays. The must have the same length.
         */
        double[] inputWeights = new double[]{0, 20, 50, 100, 500, 3000};
        T3dColor[] outputColors = new T3dColor[]{
            new T3dColor(0, 1, 0),
            new T3dColor(0.25f, 0.75f, 0),
            new T3dColor(0.5f, 0.5f, 0),
            new T3dColor(0.75f, 0.25f, 0),
            new T3dColor(1, 0, 0),
            new T3dColor(0, 0, 1)
        };

        // true sets linear interpolation to true!
        simpleColorMapper.setPalette(inputWeights, outputColors, true);

        for (VgRelation vgRelation : relations) {
            double weight = (Double) vgRelation.getValue();

            T3dColor color = simpleColorMapper.transform(weight);

            float red = color.getRed();
            float green = color.getGreen();
            float blue = color.getBlue();

            System.out.println("Weight '" + weight
                    + "' was mapped to color '[RED:" + red + ", GREEN:" + green + ", BLUE:" + blue + "]'.");
        }

    }

    private void colorAttrFeatures(List<VgAttrFeature> attrFeatures) {
        /*
         * Initiate simple Color Mapper that maps an input value to a color:
         */
        MpValue2ColoredAttrFeature colorMapper4AttrFeatures = new MpValue2ColoredAttrFeature();

        /*
         * define the input and output arrays. The must have the same length.
         */
        double[] inputNumberOfEdges = new double[]{0, 1, 2, 3, 4, 5};
        T3dColor[] outputColors = new T3dColor[]{
            new T3dColor(0, 1, 0),
            new T3dColor(0.25f, 0.75f, 0),
            new T3dColor(0.5f, 0.5f, 0),
            new T3dColor(0.75f, 0.25f, 0),
            new T3dColor(1, 0, 0),
            new T3dColor(0, 0, 1)
        };

        // true sets linear interpolation to true!
        colorMapper4AttrFeatures.setPalette(inputNumberOfEdges, outputColors, true);

        for (VgAttrFeature attrFeature : attrFeatures) {
            Object numberOfRelations = attrFeature.getAttributeValue(this.ATTRIBUTE_NAME);

            /*
             * this will save the color in an additional attribute
             */
            VgAttrFeature coloredAttrFeature = colorMapper4AttrFeatures.transform(this.ATTRIBUTE_NAME, attrFeature);

            String colorAttributeName = NamesForAttributes.attributeNameForColor;

            // get the color from the attributed feature
            T3dColor color = (T3dColor) coloredAttrFeature.getAttributeValue(colorAttributeName);

            float red = color.getRed();
            float green = color.getGreen();
            float blue = color.getBlue();

            System.out.println("Number of Relations '" + numberOfRelations
                    + "' was mapped to color '[RED:" + red + ", GREEN:" + green + ", BLUE:" + blue + "]'.");
        }
    }

    private List<VgAttrFeature> generateAttrFeatures(WvizUniversalFeatureNet featureNet) {
        // extract nodes
        Collection<VgFeature> features = featureNet.getFeatures();

        List<VgAttrFeature> attrFeatures = new ArrayList<VgAttrFeature>(features.size());

        for (VgFeature vgFeature : features) {
            GmAttrFeature newAttrFeature = new GmAttrFeature();
            newAttrFeature.setGeometry(vgFeature.getGeometry());

            /*
             * add the number of inflows and outflows as an attribute to the
             * feature
             */
            WvizFlow[] inFlows = featureNet.getInFlows(vgFeature);
            WvizFlow[] outFlows = featureNet.getOutFlows(vgFeature);

            String numberOfEdges = "" + (inFlows.length + outFlows.length);
            newAttrFeature.addAttribute(this.ATTRIBUTE_NAME, Integer.class.toString(), numberOfEdges);

            attrFeatures.add(newAttrFeature);
        }

        return attrFeatures;
    }

    private WvizUniversalFeatureNet generateFeatureNet() {
        
        WvizUniversalFeatureNet wvizUniversalFeatureNet = null;
        PajekReader pajekReader = new PajekReader();
        
        try {
            wvizUniversalFeatureNet = pajekReader.readFromFile(RelativePaths.PAJEK_FLOWS_OF_TRADE_NET);
        }
        catch (PajekException exception) {
            System.err.println(exception);
        }
        return wvizUniversalFeatureNet;
    }

}
