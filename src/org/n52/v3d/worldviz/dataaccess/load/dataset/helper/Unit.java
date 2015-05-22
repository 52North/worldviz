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
