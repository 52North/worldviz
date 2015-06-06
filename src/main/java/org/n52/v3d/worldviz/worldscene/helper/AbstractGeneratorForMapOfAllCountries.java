package org.n52.v3d.worldviz.worldscene.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;

import org.n52.v3d.triturus.vgis.VgAttrFeature;


/**
 * Use this class to generate a map of all world countries. Key =
 * iso3166-alpha2-code; Value = country as VgAttrFeature
 * 
 * @author Christian Danowski
 * 
 */
public abstract class AbstractGeneratorForMapOfAllCountries implements GeneratorForMapOfAllCountries{

	protected String pathToEneDatasetWithAllWorldCountries = RelativePaths.COUNTRY_CODES_XML;
	private String attributeName_iso3166_alpha2_code = "ISO 3166-2 code";

	public AbstractGeneratorForMapOfAllCountries() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Use this method to generate a map of all world countries. Key =
	 * iso3166-alpha2-code; Value = country as VgAttrFeature
	 * @param worldBordersLOD 
	 * 
	 * @return a map of all world's countries
	 */
	public Map<String, VgAttrFeature> generateAllCountriesMap() {

		XmlDataset countryCodes = null;

		DatasetLoader countryCodesLoader = setupDatasetLoader();

		try {
			countryCodes = countryCodesLoader.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = countryCodes.getFeatures();

		Map<String, VgAttrFeature> countriesMap = new HashMap<String, VgAttrFeature>(
				features.size());

		for (VgAttrFeature vgAttrFeature : features) {
			countriesMap.put((String) vgAttrFeature
					.getAttributeValue(attributeName_iso3166_alpha2_code),
					vgAttrFeature);
		}

		return countriesMap;

	}

	protected abstract DatasetLoader setupDatasetLoader();

}
