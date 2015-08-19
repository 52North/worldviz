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
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.extensions;

import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmEnvelope;
import org.n52.v3d.triturus.vgis.VgEnvelope;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgGeomObject2d;

/**
 * Simple extension for MultiPolygons (in short: a collection of
 * {@link VgPolygon}).
 * 
 * @author Christian Danowski
 * 
 */
public abstract class VgMultiPolygon extends VgCollection2D {

	protected List<VgPolygon> polygons;

	@Override
	public VgGeomObject2d getGeometry(int i) {
		if (i < 0 || i >= this.getNumberOfGeometries())
			throw new T3dException("Index out of bounds.");

		return this.polygons.get(i);
	}

	@Override
	public int getNumberOfGeometries() {
		return this.polygons.size();
	}

	@Override
	public double area() {
		double summedUpArea = 0;

		for (int i = 0; i < this.polygons.size(); i++) {
			summedUpArea += polygons.get(i).area();
		}
		return summedUpArea;
	}

	@Override
	public VgEnvelope envelope() {
		if (!(this.polygons.size() > 0))
			return null;

		GmEnvelope envelope = null;

		envelope = createEnvelopeFromFirstPoint(envelope);

		// now the envelope should be instantiated
		if (envelope == null)
			return null;

		for (VgGeomObject2d polygon : this.polygons) {

			enlargeEnvelopeForNextPolygon((VgPolygon) polygon, envelope);

		}

		return envelope;

	}

	private GmEnvelope createEnvelopeFromFirstPoint(GmEnvelope envelope) {
		VgPolygon firstPolygon = (VgPolygon) this.polygons.get(0);
		int numberOfVertices = firstPolygon.getOuterBoundary().getNumberOfVertices();

		if (numberOfVertices > 0)
			envelope = new GmEnvelope(firstPolygon.getOuterBoundary().getVertex(0));
		return envelope;
	}

	private void enlargeEnvelopeForNextPolygon(VgPolygon polygon,
			VgEnvelope envelope) {

		int numberOfVertices = polygon.getOuterBoundary().getNumberOfVertices();
		if (numberOfVertices > 0) {
			for (int i = 0; i < numberOfVertices; i++)
				envelope.letContainPoint(polygon.getOuterBoundary().getVertex(i));
		}

	}

	@Override
	public VgGeomObject footprint() {

		GmMultiPolygon multiPolygon = new GmMultiPolygon();

		for (VgPolygon polygon : this.polygons) {
			VgPolygon polygonFootprint = (VgPolygon) polygon.footprint();
			multiPolygon.addPolygon(polygonFootprint);
		}

		return multiPolygon;

	}

	@Override
	public String toString() {
		return "MultiPolygon [polygons=" + polygons + "]";
	}

}
