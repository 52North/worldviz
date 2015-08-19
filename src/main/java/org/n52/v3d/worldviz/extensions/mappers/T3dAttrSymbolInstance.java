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
package org.n52.v3d.worldviz.extensions.mappers;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.t3dutil.T3dSymbolDef;
import org.n52.v3d.triturus.t3dutil.T3dSymbolInstance;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This is an extension of {@link T3dSymbolInstance} that contains an additional
 * list of attributeValuePairs. This is useful if you want to map different
 * attributes of an {@link VgAttrFeature} to different visual components of a
 * symbol in a scene description. You can then add all the used attributes names
 * and values to the symbol instance.<br/>
 * <br/>
 * Example: You want to map the population of a country to the radius of a
 * sphere and in addition want to map the CO2-emission of that country to the
 * color of the sphere. In the final scene you may need to link the visual
 * components (radius and color) to the attribute data (population and
 * CO2-emission). In that case, you may use this class to attach the attribute
 * data to the symbolInstance for later access.<br/>
 * <br/>
 * In Addition, three scale parameter are defined. For each coordinate axes of
 * the scene there is one scale parameter. So if you only change the
 * corresponding scale of the x-axis, then the symbol only gets scaled in that
 * direction. This way, it is possible to have more control of the scale of a
 * symbol in the final scene and for instance map another attribute to the
 * height(y-axis in the scene) of the symbol. Note that
 * {@link #setScale(double)} of the parent class {@link T3dSymbolInstance} sets
 * an overall scale parameter that affects all three coordinate axes in the same
 * extent.
 * 
 * @author Christian Danowski
 * 
 */
public class T3dAttrSymbolInstance extends T3dSymbolInstance {

	private List<AttributeValuePair> attributeValuePairs;

	private double xScale = 1;
	private double yScale = 1;
	private double zScale = 1;

	public T3dAttrSymbolInstance(T3dSymbolDef pSymbol, VgPoint pPos) {
		super(pSymbol, pPos);
		attributeValuePairs = new ArrayList<AttributeValuePair>();
	}

	/**
	 * The xScale-parameter scales the symbol only in x-direction of the scene.
	 * The default value is 1 (which is equal to no scaling).
	 * 
	 * @return
	 */
	public double getxScale() {
		return xScale;
	}

	/**
	 * The xScale-parameter scales the symbol only in x-direction of the scene.
	 * The default value is 1 (which is equal to no scaling).
	 * 
	 * @param xScale
	 */
	public void setxScale(double xScale) {
		this.xScale = xScale;
	}

	/**
	 * The yScale-parameter scales the symbol only in y-direction (= the height)
	 * of the scene. The default value is 1 (which is equal to no scaling).
	 * 
	 * @return
	 */
	public double getyScale() {
		return yScale;
	}

	/**
	 * The yScale-parameter scales the symbol only in y-direction (= the height)
	 * of the scene. The default value is 1 (which is equal to no scaling).
	 * 
	 * @param yScale
	 */
	public void setyScale(double yScale) {
		this.yScale = yScale;
	}

	/**
	 * The zScale-parameter scales the symbol only in z-direction of the scene.
	 * The default value is 1 (which is equal to no scaling).
	 * 
	 * @return
	 */
	public double getzScale() {
		return zScale;
	}

	/**
	 * The zScale-parameter scales the symbol only in z-direction of the scene.
	 * The default value is 1 (which is equal to no scaling).
	 * 
	 * @param zScale
	 */
	public void setzScale(double zScale) {
		this.zScale = zScale;
	}

	public List<AttributeValuePair> getAttributeValuePairs() {
		return attributeValuePairs;
	}

	/**
	 * Simply adds an {@link AttributeValuePair}. Note, that no check of
	 * duplicates is done.
	 * 
	 * @param attrValuePair
	 */
	public void addAttributeValuePair(AttributeValuePair attrValuePair) {
		this.attributeValuePairs.add(attrValuePair);
	}

	/**
	 * Simply adds a combination of attribute and value as an
	 * {@link AttributeValuePair}. Note, that no check of duplicates is done.
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void addAttributeValuePair(String attrName, Object attrValue) {
		this.attributeValuePairs
				.add(new AttributeValuePair(attrName, attrValue));
	}

	@Override
	public String toString() {
		return "T3dAttrSymbolInstance [type="
				+ this.getSymbol().getClass().getSimpleName() + ", position="
				+ this.getPosition() + ", attributeValuePairs="
				+ attributeValuePairs + "]";
	}

}
