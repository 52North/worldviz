package org.n52.v3d.worldviz.dataaccess.importtools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is meant to produce a java map that maps a {@link Country} to it's
 * ISO 3166 country code.<br/>
 * Each map entry consists of:<br/>
 * 
 * <table border=1>
 * <tr>
 * <td>key:</td>
 * <td>ISO 3166 country code</td>
 * </tr>
 * <tr>
 * <td>value:</td>
 * <td>the corresponding country as {@link Country}-instance</td>
 * </table>
 * 
 * 
 * @author Christian Danowski
 */
public class CountryIsoCodeMapGenerator {

	private static Logger logger = LoggerFactory
			.getLogger(CountryIsoCodeMapGenerator.class);

	/**
	 * Produces a java map that maps a {@link Country} to it's ISO 3166 country
	 * code.<br/>
	 * Each map entry consists of:<br/>
	 * 
	 * <table border=1>
	 * <tr>
	 * <td>key:</td>
	 * <td>ISO 3166 country code</td>
	 * </tr>
	 * <tr>
	 * <td>value:</td>
	 * <td>the corresponding country as {@link Country}-instance</td>
	 * </table>
	 * 
	 * @return
	 */
	public static Map<String, Country> createCountryMap() {
		// ISO3166-ALPHA-2 codes
		String[] countryCodes = Locale.getISOCountries();

		// Key = CountryCode
		// Value = Country
		Map<String, Country> countries = new HashMap<String, Country>(
				countryCodes.length);

		for (String cc : countryCodes) {

			Locale.setDefault(Locale.US);
			String countryNameEnglish = new Locale(Locale.US.getLanguage(), cc)
					.getDisplayCountry();

			Locale.setDefault(Locale.GERMANY);
			String countryNameGerman = new Locale(Locale.GERMANY.getLanguage(),
					cc).getDisplayCountry();

			Country country = new Country(cc.toUpperCase(), countryNameEnglish,
					countryNameGerman);

			countries.put(cc, country);

		}

		if (logger.isInfoEnabled()) {
			logger.info(
					"Created a country-iso3166Code Map<String, Country> with {} entries.",
					countries.size());
		}

		return countries;

	}
}