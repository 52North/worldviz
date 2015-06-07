package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.worldviz.featurenet.VgFeatureNet;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dProcMapper;

/**
 * Mapper to perform the transformation of a feature-nets to an abstract 3-d
 * connection-map scene.
 *
 * @author Adhitya Kamakshidasan
 */
public class MpFeatureNetVisualizer extends T3dProcMapper {

    private WvizConfig style; // Cartographic presentation parameters
    private String vcs = "LON_ELEV_LAT"; // TODO: will this work? / later this could be "SPHERE" or "DYMAXION" 

    /**
     * sets the style configuration defining the cartographic presentation
     * parameters to be used for scene generation.
     *
     * @param style Cartographic style configuration
     */
    public void setStyle(WvizConfig style) {
        this.style = style;
    }

    public WvizConfig getStyle() {
        return style;
    }

    /**
     * defines the target coordinate-system. By default, "LON_ELEV_LAT" will be
     * used to set-up a plain lat/lon map in 3-d space. Alternatively, "SPHERE"
     * can be used to generate a globe-like visualization.
     *
     * @param vcs Visualization coordinate system
     */
    public void setTargetCoordinateSystem(String vcs) {
		 // TODO Will this work? / Does this make sense? -> Christian and Benno

		 // TODO More VCS's such as Dymaxion etc.? 
        this.vcs = vcs;
    }

    /**
     * performs the mapping process.
     *
     * @param net
     * @return WvizVirtualConnectionMapScene
     */
    public WvizVirtualConnectionMapScene transform(VgFeatureNet net) throws T3dException {
        return new WvizVirtualConnectionMapScene(net, style);
    }

    @Override
    public String log() {
        return "Visualizing feature-net (visualization coordinate system = \"" + vcs + "\")";
    }
}
