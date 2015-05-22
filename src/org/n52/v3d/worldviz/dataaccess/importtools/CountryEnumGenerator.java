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
		String[] countryCodes = Locale.getISOCountries(); // ISO3166-ALPHA-2
															// codes
//		List<Country> countryList = new ArrayList<Country>(countryCodes.length);

		Map<String, Country> countries = new HashMap<String, Country>(countryCodes.length); // Key=CountryCode
																				// Value
																				// =
																				// Country

		

		for (String cc : countryCodes) {

			Locale.setDefault(Locale.US);
			String countryNameEnglish = new Locale(Locale.US.getLanguage(), cc)
					.getDisplayCountry();

			Locale.setDefault(Locale.GERMANY);
			String countryNameGerman = new Locale(Locale.GERMANY.getLanguage(),
					cc).getDisplayCountry();

			Country country = new Country(cc.toUpperCase(), countryNameEnglish,
					countryNameGerman);

//			countryList.add(country);

			countries.put(cc, country);

		}

//		Collections.sort(countryList);
//
//		for (Country c : countryList) {
//			System.out.println("/**" + c.getNameEnglish() + "*/");
//			System.out.println(c.getIsoCode3166_2() + "(\"" + c.getNameGerman()
//					+ "\"),");
//		}

		return countries;

	}
}
