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
package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.v3d.worldviz.featurenet.VgFeatureNet;
import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * Feature net implementation to hold nets consisting of geo-objects and
 * arbitrary connections in between. From the mathematical perspective, the
 * edges inside a <tt>WvizUniversalFeatureNet</tt> might be directed or
 * undirected, with or without (quantitative or nominally scaled) weights.
 *
 * @author Benno Schmidt
 */
public class WvizUniversalFeatureNet extends VgFeatureNet {

    protected ArrayList<VgFeature> features;
    protected ArrayList<VgRelation> relations;

    /**
     * Constructor.
     *
     * @param features Set of geo-objects (graph nodes)
     * @param relations Set of relations between geo-objects (graph edges)
     */
    public WvizUniversalFeatureNet(Collection<VgFeature> features, Collection<VgRelation> relations) {
        this.features = new ArrayList<VgFeature>();
        for (VgFeature f : features) {
            this.features.add(f);
        }
        this.relations = new ArrayList<VgRelation>();
        for (VgRelation r : relations) {
            this.relations.add(r);
        }
    }

    /**
     * Constructor.
     *
     * @param features Set of geo-objects (graph nodes)
     * @param relations Set of relations between geo-objects (graph edges)
     */
    public WvizUniversalFeatureNet(VgFeature[] features, VgRelation[] relations) {
        this.features = new ArrayList<VgFeature>();
        for (VgFeature f : features) {
            this.features.add(f);
        }
        this.relations = new ArrayList<VgRelation>();
        for (VgRelation r : relations) {
            this.relations.add(r);
        }
    }

    protected WvizUniversalFeatureNet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the net's geo-objects.
     *
     * @return Set of geo-objects (graph nodes)
     */
    public Collection<VgFeature> getFeatures() {
        return this.features;
    }

    /**
     * Gets the relations between the geo-objects that are held in the net.
     *
     * @return Set of geo-object relations (graph edges)
     */
    public Collection<VgRelation> getRelations() {
        return this.relations;
    }

    /*
     public int numberOfFeatures() {
     if (this.features == null)
     return 0;
     else
     return this.features.size();
     }
     */
    /**
     * gets the outflows for a given feature, i.e. the graph edges directing
     * away from a node.
     *
     * TODO: Note that this method is not algorithmically efficient. Later, for
     * big numbers of nodes and edges, we could add an extended class
     * WvizFlowNetTopo or sth similar.
     *
     * @param feature Net node
     * @return Array holding outflows
     */
    public WvizFlow[] getOutFlows(VgFeature feature) {
        return this.getFlows(feature, 'o');
    }

    /**
     * gets the influxes for a given feature, i.e. the graph edges directing to
     * a node.
     *
     * TODO: Note that this method is not algorithmically efficient. Later, for
     * big numbers of nodes and edges, we could add an extended class
     * WvizFlowNetTopo or sth similar.
     *
     * @param feature Net node
     * @return Array holding influxes
     */
    public WvizFlow[] getInFlows(VgFeature feature) {
        return this.getFlows(feature, 'i');
    }

    private WvizFlow[] getFlows(VgFeature feature, char inOrOut) {
        ArrayList<WvizFlow> res = new ArrayList<WvizFlow>();
        for (VgRelation r : this.relations) {
            if (r instanceof WvizFlow) {
                if (inOrOut == 'o') {
                    if (r.getFrom() == feature) {
                        res.add((WvizFlow) r);
                    }
                } else {
                    if (r.getTo() == feature) {
                        res.add((WvizFlow) r);
                    }
                }
            }
        }
        WvizFlow[] resArr = new WvizFlow[res.size()];
        int i = 0;
        for (WvizFlow fl : res) {
            resArr[i] = fl;
            i++;
        }
        return resArr;
    }
}
