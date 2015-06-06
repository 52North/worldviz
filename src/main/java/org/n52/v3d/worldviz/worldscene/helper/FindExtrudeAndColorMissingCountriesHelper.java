package org.n52.v3d.worldviz.worldscene.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.n52.v3d.worldviz.triturusextensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.triturusextensions.mappers.MpValue2ExtrudedAttrFeature;

import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;

/**
 * Use this class if you have the following use case:<br/>
 * You intend to create a scene in which you want to color/extrude all the
 * world's countries depending on a certain attribute's value (e.g. carbon
 * emissions). However, the dataset does not contain attribute information for
 * EACH AND EVERY country. So to still color/extrude these missing non-thematic
 * countries in a neutral way, you may use the method
 * {@link #findExtrudeAndColorMissingCountries(List)}.
 * 
 * @author Christian Danowski
 * 
 */
public class FindExtrudeAndColorMissingCountriesHelper {

	private Map<String, VgAttrFeature> allCountriesMap;
	private String attributeName_iso3166_alpha2_code = "Country code";

	private CountryBordersLODEnum worldBordersLOD;

	/**
	 * Constructor.
	 * 
	 * @param worldBordersLOD
	 *            this parameter determines how detailed the country borders are
	 *            drawn. For performance reasons it is sufficient to use
	 *            {@link CountryBordersLODEnum#SIMPLIFIED_50m}
	 */
	public FindExtrudeAndColorMissingCountriesHelper(
			CountryBordersLODEnum worldBordersLOD) {

		this.worldBordersLOD = worldBordersLOD;

		GeneratorForMapOfAllCountries generatorForMapOfAllCountries = determineGeneratorForMapOfAllCountries();

		this.allCountriesMap = generatorForMapOfAllCountries
				.generateAllCountriesMap();
	}

	/**
	 * Depending on the {@link #worldBordersLOD}-parameter the appropriate
	 * generator is used to create a map of all countries.
	 * 
	 * @return
	 */
	private GeneratorForMapOfAllCountries determineGeneratorForMapOfAllCountries() {
		GeneratorForMapOfAllCountries generatorForMapOfAllCountries = null;

		switch (this.worldBordersLOD) {
		case DETAILED:
			generatorForMapOfAllCountries = new GeneratorForMapOfAllCountries_detailed();
			break;

		case SIMPLIFIED_50m:
			generatorForMapOfAllCountries = new GeneratorForMapOfAllCountries_simplified_50m();
			break;

		case SIMPLIFIED_110m:
			generatorForMapOfAllCountries = new GeneratorForMapOfAllCountries_simplified_110m();
			break;

		default:
			generatorForMapOfAllCountries = new GeneratorForMapOfAllCountries_detailed();
			break;
		}
		return generatorForMapOfAllCountries;
	}

	/**
	 * Sets the name of the attribute that represents the iso3166-alpha2-code in
	 * the dataset.<br/>
	 * The standard value is 'Country code'.
	 * 
	 * @param attributeName_iso3166_alpha2_code
	 */
	public void setAttributeName_iso3166_alpha2_code(
			String attributeName_iso3166_alpha2_code) {
		this.attributeName_iso3166_alpha2_code = attributeName_iso3166_alpha2_code;
	}

	/**
	 * Use this method to determine any missing countries, extrude and color
	 * these in the neutral extrusionHeight/color using the corresponding
	 * mapper.
	 * 
	 * @param alreadyExtrudedAndColoredCountries
	 *            a list of already colored countries, that need to have the
	 *            attribute 'ISO 3166-2 code'.
	 * @return a list of ALL the world's countries where non-thematic countries
	 *         are colored in the neutral color and extruded in neutral
	 *         extrusionHeight.
	 */
	public List<VgAttrFeature> findExtrudeAndColorMissingCountries(
			List<VgAttrFeature> alreadyExtrudedAndColoredCountries) {

		if (alreadyExtrudedAndColoredCountries.size() == this.allCountriesMap
				.size()) {
			// then it is assumed, that all countries are already colored and
			// extruded
			return alreadyExtrudedAndColoredCountries;
		}

		Map<String, VgAttrFeature> remainingCountriesMap = removeAlreadyExtrudedAndColoredCountriesFromMap(
				this.allCountriesMap, alreadyExtrudedAndColoredCountries);

		List<VgAttrFeature> extrudedColoredRemainingCountries = extrudeAndColorRemainingCountries(remainingCountriesMap);

		// add remaining colored countries and return
		alreadyExtrudedAndColoredCountries
				.addAll(extrudedColoredRemainingCountries);

		return alreadyExtrudedAndColoredCountries;

	}

	/**
	 * Use this method to determine any missing countries, extrude and color
	 * these in the neutral extrusionHeight/color using the corresponding
	 * mapper. Via this method you can set the neutral color/extrusionHeight.
	 * 
	 * @param alreadyExtrudedAndColoredCountries
	 *            a list of already colored countries, that need to have the
	 *            attribute 'ISO 3166-2 code'.
	 * @param neutralColor
	 * @param neutralExtrusionHeight
	 * @return a list of ALL the world's countries where non-thematic countries
	 *         are colored in the neutral color and extruded in neutral
	 *         extrusionHeight.
	 */
	public List<VgAttrFeature> findExtrudeAndColorMissingCountries(
			List<VgAttrFeature> alreadyExtrudedAndColoredCountries,
			T3dColor neutralColor, double neutralExtrusionHeight) {

		if (alreadyExtrudedAndColoredCountries.size() == this.allCountriesMap
				.size()) {
			// then it is assumed, that all countries are already colored and
			// extruded
			return alreadyExtrudedAndColoredCountries;
		}

		Map<String, VgAttrFeature> remainingCountriesMap = removeAlreadyExtrudedAndColoredCountriesFromMap(
				this.allCountriesMap, alreadyExtrudedAndColoredCountries);

		List<VgAttrFeature> extrudedColoredRemainingCountries = extrudeAndColorRemainingCountries(
				remainingCountriesMap, neutralColor, neutralExtrusionHeight);

		// add remaining colored countries and return
		alreadyExtrudedAndColoredCountries
				.addAll(extrudedColoredRemainingCountries);

		return alreadyExtrudedAndColoredCountries;

	}

	private List<VgAttrFeature> extrudeAndColorRemainingCountries(
			Map<String, VgAttrFeature> remainingCountriesMap,
			T3dColor neutralColor, double neutralExtrusionHeight) {

		Set<Entry<String, VgAttrFeature>> remainingCountries = remainingCountriesMap
				.entrySet();

		List<VgAttrFeature> extrudedColoredRemainingCountries = new ArrayList<VgAttrFeature>(
				remainingCountries.size());

		//color mapper to color the remaining countries with neutral color
		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();
		if (neutralColor != null) {
			colorMapper.setNeutralColor(neutralColor);
		}

		//extrusion mapper to extrude the remaining countries with neutral extrusionHeight
		MpValue2ExtrudedAttrFeature extrusionMapper = new MpValue2ExtrudedAttrFeature();
		extrusionMapper.setNeutralExtrusionHeight(neutralExtrusionHeight);

		for (Entry<String, VgAttrFeature> remainingCountry : remainingCountries) {

			VgAttrFeature coloredCountry = colorMapper
					.colorWithNeutralColor(remainingCountry.getValue());

			VgAttrFeature extrudedColoredCountry = extrusionMapper
					.extrudeWithNeutralHeight(coloredCountry);

			extrudedColoredRemainingCountries.add(extrudedColoredCountry);
		}

		return extrudedColoredRemainingCountries;

	}

	private List<VgAttrFeature> extrudeAndColorRemainingCountries(
			Map<String, VgAttrFeature> remainingCountriesMap) {

		Set<Entry<String, VgAttrFeature>> remainingCountries = remainingCountriesMap
				.entrySet();

		List<VgAttrFeature> extrudedColoredRemainingCountries = new ArrayList<VgAttrFeature>(
				remainingCountries.size());

		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();
		MpValue2ExtrudedAttrFeature extrusionMapper = new MpValue2ExtrudedAttrFeature();

		for (Entry<String, VgAttrFeature> remainingCountry : remainingCountries) {

			VgAttrFeature coloredCountry = colorMapper
					.colorWithNeutralColor(remainingCountry.getValue());

			VgAttrFeature extrudedColoredCountry = extrusionMapper
					.extrudeWithNeutralHeight(coloredCountry);

			extrudedColoredRemainingCountries.add(extrudedColoredCountry);
		}

		return extrudedColoredRemainingCountries;
	}

	private Map<String, VgAttrFeature> removeAlreadyExtrudedAndColoredCountriesFromMap(
			Map<String, VgAttrFeature> mapWithAllCountries,
			List<VgAttrFeature> alreadyColoredCountries) {

		for (VgAttrFeature alreadyColoredCountry : alreadyColoredCountries) {
			String iso3166_alpha2_code = (String) alreadyColoredCountry
					.getAttributeValue(this.attributeName_iso3166_alpha2_code);

			mapWithAllCountries.remove(iso3166_alpha2_code);
		}

		// return remaining countries
		return mapWithAllCountries;

	}

}
