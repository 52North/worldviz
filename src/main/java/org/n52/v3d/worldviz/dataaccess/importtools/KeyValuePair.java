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

	public int compareTo(KeyValuePair o) {
		return this.key.compareTo(o.getKey());

	}

	@Override
	public String toString() {
		return "KeyValuePair [key=" + key + ", value=" + value + "]";
	}

}
