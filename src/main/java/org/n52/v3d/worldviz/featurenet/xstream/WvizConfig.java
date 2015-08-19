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
package org.n52.v3d.worldviz.featurenet.xstream;

/**
 * A <tt>WvizConfig</tt> object holds the complete description of presentation
 * parameters needed to set-up WorldViz scenes.
 *
 * @author Adhitya Kamakshidasan, Benno Schmidt
 *
 * @see VgFeatureNet
 * @see WvizVirtualConnectionMapScene
 */
/*Almost all elements of the XML file have been represented as List objects in Java
 In case, we would like to extend the same XML file in future, for more than one Feature or Relation,
 most of the implementation would still remain the same, and very little would have to be changed.
 */
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.io.File;
import java.util.List;

@XStreamAlias("WvizConfig")
public class WvizConfig {

    private WvizConfig wvizConfig;

    @XStreamImplicit
    protected List<Background> background;
    
    @XStreamImplicit
    protected List<Viewpoint> viewpoint;
    
    @XStreamImplicit
    protected List<ConnectionNet> connectionNet;

    public List getBackground() {
        return background;
    }
    
    public List getViewpoint() {
        return viewpoint;
    }
    
    public List getConnectionNet() {
        return connectionNet;
    }

    public WvizConfig(String xml) {
        File file = new File(xml);
        XStream xStream = new XStream();
        xStream.processAnnotations(WvizConfig.class);
        wvizConfig = (WvizConfig) xStream.fromXML(file);
    }

    public WvizConfig getConfiguration() {
        return wvizConfig;
    }
}
