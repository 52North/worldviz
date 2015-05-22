package org.n52.v3d.worldviz.dataaccess.importtools;

/**
 * KeyValuePair is used to store key-value combinations. For most cases the key
 * will be an attribute name.
 * 
 * @author Christian Danowski
 * 
 */
public class KeyValuePair implements Comparable<KeyValuePair> {

	private String key;
	private String value;

	/**
	 * Constructs a key-value-combination
	 * 
	 * @param key
	 *            the key, e.g. an attribute name
	 * @param value
	 *            the corresponding value
	 */
	public KeyValuePair(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	protected String getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key = key;
	}

	protected String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(KeyValuePair o) {
		return this.key.compareTo(o.getKey());

	}

	@Override
	public String toString() {
		return "KeyValuePair [key=" + key + ", value=" + value + "]";
	}

}
