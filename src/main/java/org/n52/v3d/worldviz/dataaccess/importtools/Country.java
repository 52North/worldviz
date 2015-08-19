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
 * This class holds the iso3166-alpha2-code and the German and English name
 * about one country.
 * 
 * @author Christian Danowski
 * 
 */
public class Country implements Comparable<Country> {
	private String isoCode3166_2; // ISO code 3166
	private String nameEnglish;
	private String nameGerman;

	/**
	 * Constructor with all important information about one country.
	 * 
	 * @param isoCode3166_2
	 *            the iso3166-alpha2-code
	 * @param nameEnglish
	 *            the english name
	 * @param nameGerman
	 *            the german name
	 */
	public Country(String isoCode3166_2, String nameEnglish, String nameGerman) {
		super();
		this.isoCode3166_2 = isoCode3166_2;
		this.nameEnglish = nameEnglish;
		this.nameGerman = nameGerman;
	}

	public String getIsoCode3166_2() {
		return isoCode3166_2;
	}

	public String getNameEnglish() {
		return nameEnglish;
	}

	public String getNameGerman() {
		return nameGerman;
	}

	public int compareTo(Country o) {
		return this.isoCode3166_2.compareTo(o.isoCode3166_2);
	}

	@Override
	public String toString() {
		return "Country [isoCode3166_2=" + isoCode3166_2 + ", nameEnglish="
				+ nameEnglish + ", nameGerman=" + nameGerman + "]";
	}

}
