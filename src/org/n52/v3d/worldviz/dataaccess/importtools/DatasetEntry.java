package org.n52.v3d.worldviz.dataaccess.importtools;

import java.util.ArrayList;
import java.util.List;

/**
 * A Java-representation of an ENE-Dataset.xsd-Entry that consists of a set of
 * attributes and corresponding values
 * 
 * @author Christian Danowski
 * 
 */
public class DatasetEntry {

	private List<KeyValuePair> attributeValuePairs;
	private String keyAttributeValue;

	/**
	 * Constructor for an Entry. <b>Note:</b> the first value will be
	 * interpreted as the key-value with which Entries will be sorted.
	 * 
	 * @param names
	 *            an array of attribute names
	 * @param values
	 *            an array of values. <b>Note:</b> values[i] corresponds to
	 *            names[i].
	 */
	public DatasetEntry(String[] names, String[] values) {

		if (names.length == values.length) {
			this.attributeValuePairs = new ArrayList<KeyValuePair>();
			for (int i = 0; i < names.length; i++) {

				addAttributeValuePair(names[i], values[i]);

				if (i == 0) {
					this.keyAttributeValue = values[i];
				}
			}
		}

	}

	/**
	 * A key attribute value is the first value of the values[] array. It is
	 * needed to sort Entries with this key value.
	 * 
	 * @return
	 */
	public String getKeyAttributeValue() {
		return keyAttributeValue;
	}

	protected void setKeyAttributeValue(String keyAttributeValue) {
		this.keyAttributeValue = keyAttributeValue;
	}

	public List<KeyValuePair> getAttributeValuePairs() {
		return attributeValuePairs;
	}

	protected void setAttributeValuePairs(List<KeyValuePair> kvpList) {
		this.attributeValuePairs = kvpList;
	}

	public void addAttributeValuePair(String name, String value) {
		this.attributeValuePairs.add(new KeyValuePair(name, value));
	}

	/**
	 * Returns only the values of each attributeValuePair
	 * @return the values
	 */
	public String[] getValues() {
		String[] values = new String[this.attributeValuePairs.size()];

		for (int i = 0; i < this.attributeValuePairs.size(); i++) {
			values[i] = this.attributeValuePairs.get(i).getValue();
		}
		return values;
	}
	
	/**
	 * Returns only the attributes of each attributeValuePair
	 * @return
	 */
	public String[] getAttributes(){
		String[] attributes = new String[this.attributeValuePairs.size()];

		for (int i = 0; i < this.attributeValuePairs.size(); i++) {
			attributes[i] = this.attributeValuePairs.get(i).getKey();
		}
		return attributes;
	}

	@Override
	public String toString() {
		return "DatasetEntry [attributeValuePairs=" + attributeValuePairs
				+ ", keyAttributeValue=" + keyAttributeValue + "]";
	}
}
