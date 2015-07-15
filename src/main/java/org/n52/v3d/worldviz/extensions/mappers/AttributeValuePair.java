package org.n52.v3d.worldviz.extensions.mappers;

/**
 * Stores a combination of an attribute name and the corresponding attribute
 * value.
 * 
 * @author Christian Danowski
 * 
 */
public class AttributeValuePair {

	String attributeName;
	Object attributeValue;

	public AttributeValuePair(String attributeName, Object attributeValue) {
		super();
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public Object getAttributeValue() {
		return attributeValue;
	}

	@Override
	public String toString() {
		return "AttributeValuePair [attributeName=" + attributeName
				+ ", attributeValue=" + attributeValue + "]";
	}

}
