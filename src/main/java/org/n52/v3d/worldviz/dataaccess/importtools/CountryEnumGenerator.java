package org.n52.v3d.worldviz.dataaccess.importtools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TODO: Documentation
 *  
 * @author Christian Danowski
 */
public class CountryEnumGenerator {

	public static Map<String, Country> createCountryMap() {
		// ISO3166-ALPHA-2 codes
		String[] countryCodes = Locale.getISOCountries(); 

		// Key = CountryCode
		// Value = Country
		Map<String, Country> countries = new HashMap<String, Country>(countryCodes.length); 

		

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

		return countries;

	}
}
