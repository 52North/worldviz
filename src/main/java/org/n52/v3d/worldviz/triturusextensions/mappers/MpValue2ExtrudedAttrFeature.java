package org.n52.v3d.worldviz.triturusextensions.mappers;

import java.util.List;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dProcMapper;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapper that maps a numeric extent to an attributed feature. The numeric is
 * interpolated by using the {@link MpValue2NumericExtent}-mapper.
 * 
 * <br/>
 * By calling the {@link #setPalette(double[], T3dColor[])}-method you define,
 * how any attribute value shall be mapped to a color. It is advisable to call
 * this method before calling any <i>transform</i>-method to define a meaningful
 * mapping.
 *
 */
public class MpValue2ExtrudedAttrFeature extends T3dProcMapper {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private MpValue2NumericExtent numericExtentMapper;

	private double neutralExtrusionHeight = 0.0;

	/**
	 * Constructor. Note that you should also call
	 * {@link #setPalette(double[], double[], boolean))} to specify the mapping.
	 */
	public MpValue2ExtrudedAttrFeature() {
		this.numericExtentMapper = new MpValue2NumericExtent();
	}

	/**
	 * sets the numericExtent palette. Here, the value <i>pValues[i]</i> will be
	 * mapped to the extent <i>pExtents[i]</i>. Any other value will be linearly
	 * interpolated if the 'interpolate'-parameter is set to TRUE.
	 * 
	 * @param pValues
	 * @param pExtents
	 * @param interpolate
	 *            if set to TRUE, then any value will be linearly interpolated
	 * 
	 * @see MpValue2NumericExtent#setPalette(double[], double[], boolean)
	 */
	public void setPalette(double[] pValues, double[] pExtents,
			boolean interpolate) {
		this.numericExtentMapper.setPalette(pValues, pExtents, interpolate);
	}

	/**
	 * This is the default extrusion height for any attribute's value, which
	 * cannot be parsed as a double value. This might happen, when the
	 * attribute's value simply is no real double value and is thus symbolized
	 * through any String like '-'. The neutral extrusion height is set to 0.0
	 * 
	 * @return
	 */
	public double getNeutralExtrusionHeight() {
		return neutralExtrusionHeight;
	}

	/**
	 * Sets the default extrusion height. This is the default extrusion height
	 * for any attribute's value, which cannot be parsed as a double value. This
	 * might happen, when the attribute's value simply is no real double value
	 * and is thus symbolized through any String like '-'. The neutral extrusion
	 * height is set to 0.0
	 * 
	 * @param neutralExtrusionHeight
	 */
	public void setNeutralExtrusionHeight(double neutralExtrusionHeight) {
		this.neutralExtrusionHeight = neutralExtrusionHeight;
	}

	/**
	 * Creates an extruded version of the attributed feature by interpolating an
	 * extrusion height based on the <i>value</i>-parameter. The extrusion
	 * height value is stored in an additional attribute of the
	 * {@link VgAttrFeature}-object: <br/>
	 * <br/>
	 * <b>Attribute name = the value stored at
	 * {@link NamesForAttributes#attributeNameForExtrusion} <br/>
	 * Attribute value = a double value representing the height of the
	 * object</b>
	 * 
	 * @param value
	 *            the value of the attribute which shall be used to extrude the
	 *            feature
	 * @param feature
	 *            the feature
	 * @return the feature-instance that holds the extrusion height for the
	 *         feature in a new attribute (see information above)
	 */
	public VgAttrFeature transform(double value, VgAttrFeature feature) {
		double extrusionHeight = this.numericExtentMapper.transform(value);

		addExtrusionAttribute(feature, extrusionHeight);

		return feature;

	}

	/**
	 * Adds the extrusion height attribute to the feature. The extrusion height
	 * value is stored in an additional attribute of the {@link VgAttrFeature}
	 * -object: <br/>
	 * <br/>
	 * <b>Attribute name = the value stored at
	 * {@link NamesForAttributes#attributeNameForExtrusion} <br/>
	 * Attribute value = a double value representing the height of the
	 * object</b>
	 * 
	 * @param feature
	 * @param extrusionHeight
	 */
	private void addExtrusionAttribute(VgAttrFeature feature,
			double extrusionHeight) {
		feature.addAttribute(NamesForAttributes.attributeNameForExtrusion,
				Double.class.toString());
		feature.setAttributeValue(NamesForAttributes.attributeNameForExtrusion,
				extrusionHeight);
	}

	/**
	 * Creates an extruded version of the attributed feature by interpolating an
	 * extrusion height based on the double-value of the
	 * <i>attrName</i>-parameter. Note that the attrName must correspond to one
	 * of the features attributes and it's value must be a double-value
	 * (floating-point number). <br/>
	 * <br/>
	 ** The extrusion height value is stored in an additional attribute of the
	 * {@link VgAttrFeature}-object: <br/>
	 * <br/>
	 * <b>Attribute name = the value stored at
	 * {@link NamesForAttributes#attributeNameForExtrusion} <br/>
	 * Attribute value = a double value representing the height of the
	 * object</b>
	 * 
	 * @param attrName
	 *            the name of the attribute which shall be used to extrude the
	 *            feature
	 * @param feature
	 *            the feature
	 * @return the feature-instance that holds the extrusion height for the
	 *         feature in a new attribute (see information above)
	 */
	public VgAttrFeature transform(String attrName, VgAttrFeature feature) {

		// check if that object is either a double or a String value!
		Object attributeValue = feature.getAttributeValue(attrName);
		double doubleValue = 0;

		if (attributeValue == null)
			throw new T3dException("The feature " + feature
					+ " does not contain any attribute like '" + attrName
					+ "'!");

		if (attributeValue instanceof String) {
			String attributeValueString = (String) attributeValue;
			try {
				doubleValue = Double.parseDouble(attributeValueString);
			} catch (Exception e) {

				if (logger.isWarnEnabled())
					logger.warn(
							"The attributeValue '{}' of the attribute '{}' cannot be parsed as a double-value! Thus the extrusion height will be set to the default extrusion height for non double values!! ({})",
							attributeValue, attrName,
							this.neutralExtrusionHeight);

				addExtrusionAttribute(feature, this.neutralExtrusionHeight);

				return feature;
			}

		}

		else if (attributeValue instanceof Double
				|| attributeValue instanceof Float)
			doubleValue = (Double) attributeValue;

		return transform(doubleValue, feature);
	}

	/**
	 * Creates extruded versions of all attributed features by interpolating a
	 * height value based on the value of the <i>attrName</i>-parameter. Note
	 * that the attrName must correspond to one of each features attributes it's
	 * value must be a double-value (floating-point number).<br/>
	 * <br/>
	 * 
	 * The extrusion height value is stored in an additional attribute of the
	 * {@link VgAttrFeature}-object: <br/>
	 * <br/>
	 * <b>Attribute name = the value stored at
	 * {@link NamesForAttributes#attributeNameForExtrusion} <br/>
	 * Attribute value = a double value representing the height of the
	 * object</b>
	 * 
	 * @param attrName
	 *            the name of the attribute which shall be used to extrude each
	 *            feature
	 * @param features
	 *            the features
	 * @return the list of feature-instances that hold the extrusion height for
	 *         the feature in a new attribute (see information above)
	 */
	public List<VgAttrFeature> transform(String attrName,
			List<VgAttrFeature> features) {

		for (VgAttrFeature feature : features) {
			feature = transform(attrName, feature);
		}

		return features;
	}

	public VgAttrFeature extrudeWithNeutralHeight(VgAttrFeature feature) {

		addExtrusionAttribute(feature, neutralExtrusionHeight);

		return feature;

	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
