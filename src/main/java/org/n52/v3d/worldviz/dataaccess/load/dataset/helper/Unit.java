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
package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.util.Arrays;

/**
 * Unit is a java-representation of the Unit-element of the ENE-dataset that
 * consists of one ore more titles and a single code to describe the unit of a
 * measurement.
 * 
 * @author Christian Danowski
 * 
 */
public class Unit {

	private String[] titles;
	private String code;

	/**
	 * Constructor
	 * 
	 * @param titles
	 *            the array of titles from the ENE-dataset. At least one title
	 *            should be present (e.g.: gigagrams per year)
	 * @param code
	 *            the code of the unit (e.g.: Gg/a)
	 */
	public Unit(String[] titles, String code) {
		super();
		this.titles = titles;
		this.code = code;
	}

	public String[] getTitles() {
		return titles;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Unit [titles=" + Arrays.toString(titles) + ", code=" + code
				+ "]";
	}

}
