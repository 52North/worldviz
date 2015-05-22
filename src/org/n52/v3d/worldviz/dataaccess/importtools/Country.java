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

	@Override
	public int compareTo(Country o) {
		return this.isoCode3166_2.compareTo(o.isoCode3166_2);
	}

	@Override
	public String toString() {
		return "Country [isoCode3166_2=" + isoCode3166_2 + ", nameEnglish="
				+ nameEnglish + ", nameGerman=" + nameGerman + "]";
	}

}
