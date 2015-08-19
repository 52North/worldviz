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
package org.n52.v3d.worldviz.extensions.mappers;

import org.n52.v3d.triturus.t3dutil.MpValue2Symbol;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This mapper is meant to map any numerical (attribute) value to a scale
 * parameter. A typical use case of this mapper may be the following example: in
 * the 3D-scene-decription-language X3D you might wish to set the size of an
 * object to scale the visualization object in a certain coordinate direction.
 * For instance, you want to visualize pillars as cartographic objects of the
 * world's countries whose height is dependent to the value of the attribute
 * CO2-Emission. Then you might use this mapper to set the scale of the
 * pillar-object in height-direction. <br/>
 * <br/>
 * By calling the {@link #setPalette(double[], T3dColor[])}-method you define,
 * how any attribute value shall be mapped to a scale-parameter. It is advisable
 * to call this method before calling any <i>transform</i>-method to define a
 * meaningful mapping. <br/>
 * <br/>
 * <b>Note:</b>If you use features of type {@link VgAttrFeature} and want to
 * create a {@link T3dAttrSymbolInstance} you may consider to use the
 * {@link MpAttrFeature2AttrSymbol}-mapper in the first place. With that you can
 * transform the feature into a symbol like a cube or a sphere and afterwards
 * use this mapper to set the size(=scale) of the symbol depending on an
 * attribute.
 * 
 * @author Christian Danowski
 * 
 */
public class MpValue2ScaledSymbol extends MpValue2Symbol {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected MpValue2NumericExtent scaleMapper;

	/**
	 * Constructor. Note that you should also call
	 * {@link #setPalette(double[], T3dColor[])} to specify the colorMapping.
	 * 
	 * @param symbol
	 *            the symbolInstance whose color shall be mapped to a value.
	 */
	public MpValue2ScaledSymbol() {
		this.scaleMapper = new MpValue2NumericExtent();
	}

	/**
	 * sets the mapping of inputValues to output factors. Here, the value
	 * <i>inputValues[i]</i> will be mapped to the factor
	 * <i>outputFactors[i]</i>. Any other value will be interpolated using the
	 * {@link MpValue2NumericExtent}-mapper.
	 * 
	 * @param inputValues
	 * @param outputFactors
	 * 
	 * @see MpValue2NumericExtent#setPalette(double[], double[], boolean)
	 */
	public void setPalette(double[] inputValues, double[] outputFactors) {
		this.scaleMapper.setPalette(inputValues, outputFactors, true);
	}

	/**
	 * This methods takes an already existing {@link T3dAttrSymbolInstance} and
	 * an {@link AttributeValuePair} and sets the scale of the object in
	 * x-direction (along coordinate axis x). In addition, the
	 * attribute-value-combination is added to the attributes of the
	 * attrSymbolInstance. The attribute's value has to be either a double value
	 * or a String that can be parsed into a double value!<br/>
	 * <b>Note that in virtual worlds the y-axis is used for the height of an
	 * object, while the ground level is defined by the xz-plane</b>
	 * 
	 * @param attrValuePair
	 *            a combination of an attribute's name and it's value. The
	 *            attribute's value has to be either a double value or a String
	 *            that can be parsed into a double value
	 * @return a scaled version of the symbol (scale in x-direction). The
	 *         scale-factor is interpolated by using the
	 *         {@link MpValue2NumericExtent} -mapper and double value of the
	 *         attribute
	 */
	public T3dAttrSymbolInstance scaleX(T3dAttrSymbolInstance symbol,
			AttributeValuePair attrValuePair) {
		Object attributeValue = attrValuePair.getAttributeValue();

		double doubleValue = parseDoubleValue(attrValuePair, attributeValue);

		symbol.addAttributeValuePair(attrValuePair);

		double scaleFactor = this.scaleMapper.transform(doubleValue);
		symbol.setxScale(scaleFactor);

		if (logger.isDebugEnabled())
			logger.debug(
					"The symbol {} will be scaled along the X-axis. Mapped the value '{}' of the attribute '{}'to the scaleFactor '{}'.",
					symbol, attributeValue, attrValuePair.getAttributeName(),
					scaleFactor);

		return symbol;

	}

	/**
	 * This methods takes an already existing {@link T3dAttrSymbolInstance} and
	 * an {@link AttributeValuePair} and sets the scale of the object in
	 * y-direction (along coordinate axis y). In addition, the
	 * attribute-value-combination is added to the attributes of the
	 * attrSymbolInstance. The attribute's value has to be either a double value
	 * or a String that can be parsed into a double value!<br/>
	 * <b>Note that in virtual worlds the y-axis is used for the height of an
	 * object, while the ground level is defined by the xz-plane</b>
	 * 
	 * @param attrValuePair
	 *            a combination of an attribute's name and it's value. The
	 *            attribute's value has to be either a double value or a String
	 *            that can be parsed into a double value
	 * @return a scaled version of the symbol (scale in y-direction). The
	 *         scale-factor is interpolated by using the
	 *         {@link MpValue2NumericExtent} -mapper and double value of the
	 *         attribute
	 */
	public T3dAttrSymbolInstance scaleY(T3dAttrSymbolInstance symbol,
			AttributeValuePair attrValuePair) {
		Object attributeValue = attrValuePair.getAttributeValue();

		double doubleValue = parseDoubleValue(attrValuePair, attributeValue);

		symbol.addAttributeValuePair(attrValuePair);

		double scaleFactor = this.scaleMapper.transform(doubleValue);
		symbol.setyScale(scaleFactor);

		if (logger.isDebugEnabled())
			logger.debug(
					"The symbol {} will be scaled along the Y-axis. Mapped the value '{}' of the attribute '{}'to the scaleFactor '{}'.",
					symbol, attributeValue, attrValuePair.getAttributeName(),
					scaleFactor);

		return symbol;

	}

	/**
	 * This methods takes an already existing {@link T3dAttrSymbolInstance} and
	 * an {@link AttributeValuePair} and sets the scale of the object in
	 * z-direction (along coordinate axis z). In addition, the
	 * attribute-value-combination is added to the attributes of the
	 * attrSymbolInstance. The attribute's value has to be either a double value
	 * or a String that can be parsed into a double value!<br/>
	 * <b>Note that in virtual worlds the y-axis is used for the height of an
	 * object, while the ground level is defined by the xz-plane</b>
	 * 
	 * @param attrValuePair
	 *            a combination of an attribute's name and it's value. The
	 *            attribute's value has to be either a double value or a String
	 *            that can be parsed into a double value
	 * @return a scaled version of the symbol (scale in z-direction). The
	 *         scale-factor is interpolated by using the
	 *         {@link MpValue2NumericExtent} -mapper and double value of the
	 *         attribute
	 */
	public T3dAttrSymbolInstance scaleZ(T3dAttrSymbolInstance symbol,
			AttributeValuePair attrValuePair) {
		Object attributeValue = attrValuePair.getAttributeValue();

		double doubleValue = parseDoubleValue(attrValuePair, attributeValue);

		symbol.addAttributeValuePair(attrValuePair);

		double scaleFactor = this.scaleMapper.transform(doubleValue);
		symbol.setxScale(scaleFactor);

		if (logger.isDebugEnabled())
			logger.debug(
					"The symbol {} will be scaled along the Z-axis. Mapped the value '{}' of the attribute '{}'to the scaleFactor '{}'.",
					symbol, attributeValue, attrValuePair.getAttributeName(),
					scaleFactor);

		return symbol;

	}

	/**
	 * This methods takes an already existing {@link T3dAttrSymbolInstance} and
	 * an {@link AttributeValuePair} and sets the overall/total scale of the
	 * object (same scale value for all coordinate axes). In addition, the
	 * attribute-value-combination is added to the attributes of the
	 * attrSymbolInstance. The attribute's value has to be either a double value
	 * or a String that can be parsed into a double value!
	 * 
	 * @param attrValuePair
	 *            a combination of an attribute's name and it's value. The
	 *            attribute's value has to be either a double value or a String
	 *            that can be parsed into a double value
	 * @return a scaled version of the symbol (same scale for all coordinate
	 *         axes). The scale-factor is interpolated by using the
	 *         {@link MpValue2NumericExtent}-mapper and double value of the
	 *         attribute
	 */
	public T3dAttrSymbolInstance scaleTotal(T3dAttrSymbolInstance symbol,
			AttributeValuePair attrValuePair) {
		Object attributeValue = attrValuePair.getAttributeValue();

		double doubleValue = parseDoubleValue(attrValuePair, attributeValue);

		symbol.addAttributeValuePair(attrValuePair);

		double scaleFactor = this.scaleMapper.transform(doubleValue);
		symbol.setScale(scaleFactor);

		if (logger.isDebugEnabled())
			logger.debug(
					"The symbol {} will be scaled along all axes equally. Mapped the value '{}' of the attribute '{}'to the scaleFactor '{}'.",
					symbol, attributeValue, attrValuePair.getAttributeName(),
					scaleFactor);

		return symbol;

	}

	private double parseDoubleValue(AttributeValuePair attrValuePair,
			Object attributeValue) {
		double doubleValue = 0;
		if (attributeValue instanceof String) {
			String attributeValueString = (String) attributeValue;
			try {
				doubleValue = Double.parseDouble(attributeValueString);
			} catch (Exception e) {

				if (logger.isWarnEnabled())
					logger.warn(
							"The attributeValue '{}' of the attribute '{}' cannot be parsed as a double-value! Thus the defaultDoubleValue '{}' will be used!",
							attributeValue, attrValuePair.getAttributeName(),
							doubleValue);
			}
		}

		else if (attributeValue instanceof Double
				|| attributeValue instanceof Float)
			doubleValue = (Double) attributeValue;

		return doubleValue;
	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
