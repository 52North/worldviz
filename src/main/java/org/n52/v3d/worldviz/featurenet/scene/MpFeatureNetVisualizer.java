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
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.worldviz.featurenet.VgFeatureNet;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dProcMapper;
import org.n52.v3d.worldviz.featurenet.xstream.WvizConfig;

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
