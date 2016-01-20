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

import org.n52.v3d.triturus.t3dutil.MpHypsometricColor;
import org.n52.v3d.triturus.t3dutil.MpSimpleHypsometricColor;
import org.n52.v3d.triturus.t3dutil.MpValue2Symbol;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a class to map a numerical value to a colored symbol. The color is
 * interpolated by using the {@link MpHypsometricColor}-mapper of the
 * Triturus-framework.
 * 
 * <br/>
 * By calling the {@link #setPalette(double[], T3dColor[])}-method you define,
 * how any attribute value shall be mapped to a color. It is advisable to call
 * this method before calling any <i>transform</i>-method to define a meaningful
 * mapping. <br/>
 * <br/>
 * <b>Note:</b>If you use features of type {@link VgAttrFeature} and want to
 * create a {@link T3dAttrSymbolInstance} you may consider to use the
 * {@link MpAttrFeature2AttrSymbol}-mapper in the first place. With that you can
 * transform the feature into a symbol like a cube or a sphere and afterwards
 * use this mapper to set the color of the symbol depending on an attribute.
 * 
 * @author Christian Danowski
 * 
 */
public class MpValue2ColoredSymbol extends MpValue2Symbol {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected MpSimpleHypsometricColor colorMapper;

	/**
	 * Constructor. Note that you should also call
	 * {@link #setPalette(double[], T3dColor[])} to specify the colorMapping.
	 * 
	 * @param symbol
	 *            the symbolInstance whose color shall be mapped to a value.
	 */
	public MpValue2ColoredSymbol() {
		colorMapper = new MpSimpleHypsometricColor();
	}

	/**
	 * sets the hypsometric color palette. Here, the value <i>pValues[i]</i>
	 * will be mapped to the color <i>pColors[i]</i>. Any other value will be
	 * interpolated between colors in HSV color space (hue/saturation/value)
	 * 
	 * @param pValues
	 * @param pColors
	 * 
	 * @see MpSimpleHypsometricColor#setPalette(double[], T3dColor[], boolean)
	 */
	public void setPalette(double[] pValues, T3dColor[] pColors) {
		this.colorMapper.setPalette(pValues, pColors, true);
	}

	/**
	 * sets the hypsometric color palette. Here, the value <i>pValues[i]</i>
	 * will be mapped to the color <i>pColors[i]</i>. Any other value will be
	 * interpolated between colors in HSV color space (hue/saturation/value) if
	 * the 'interpolate'-parameter is set to TRUE.
	 * 
	 * @param pValues
	 * @param pColors
	 * @param interpolate
	 *            if set to TRUE, then any value will be linearly interpolated
	 * 
	 * @see MpSimpleHypsometricColor#setPalette(double[], T3dColor[], boolean)
	 */
	public void setPalette(double[] pValues, T3dColor[] pColors, boolean interpolate) {
		this.colorMapper.setPalette(pValues, pColors, interpolate);
	}

	/**
	 * This methods takes an already existing {@link T3dAttrSymbolInstance} and
	 * an {@link AttributeValuePair} and determines the appropriate color for
	 * the symbol. In addition, the attribute-value-combination is added to the
	 * attributes of the attrSymbolInstance. The attribute's value has to be
	 * either a double value or a String that can be parsed into a double value!
	 * 
	 * @param attrValuePair
	 *            a combination of an attribute's name and it's value. The
	 *            attribute's value has to be either a double value or a String
	 *            that can be parsed into a double value
	 * @return a colored version of the symbol. The color is interpolated by
	 *         using the {@link MpHypsometricColor}-mapper of the
	 *         Triturus-framework and double value of the attribute
	 */
	public T3dAttrSymbolInstance transform(T3dAttrSymbolInstance symbol, AttributeValuePair attrValuePair) {
		Object attributeValue = attrValuePair.getAttributeValue();

		if (logger.isDebugEnabled())
			logger.debug("Mapping the value '{}' of the attribute '{}' to a color.", attributeValue,
					attrValuePair.getAttributeName());

		double doubleValue = parseDoubleValue(attrValuePair, attributeValue);

		symbol.addAttributeValuePair(attrValuePair);

		T3dColor color = this.colorMapper.transform(doubleValue);
		symbol.setColor(color);

		if (logger.isDebugEnabled())
			logger.debug("Attribute value: '{}';    interpolated color: '{}'", attributeValue, color);

		return symbol;

	}

	private double parseDoubleValue(AttributeValuePair attrValuePair, Object attributeValue) {
		double doubleValue = 0;
		if (attributeValue instanceof String) {
			String attributeValueString = (String) attributeValue;
			try {
				doubleValue = Double.parseDouble(attributeValueString);
			} catch (Exception e) {

				if (logger.isWarnEnabled())
					logger.warn(
							"The attributeValue '{}' of the attribute '{}' cannot be parsed as a double-value! Thus the defaultDoubleValue '{}' will be used!!",
							attributeValue, attrValuePair.getAttributeName(), doubleValue);
			}
		}

		else if (attributeValue instanceof Double || attributeValue instanceof Float)
			doubleValue = (Double) attributeValue;

		return doubleValue;
	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
