package org.n52.v3d.worldviz.worldscene.helper;

import java.util.Map;

import org.n52.v3d.triturus.vgis.VgAttrFeature;

/**
 * Use this interface to generate a map of all world countries. Key =
 * iso3166-alpha2-code; Value = country as VgAttrFeature
 * 
 * @author Christian Danowski
 *
 */
public interface GeneratorForMapOfAllCountries {

	/**
	 * Use this method to generate a map of all world countries. Key =
	 * iso3166-alpha2-code; Value = country as VgAttrFeature
	 * 
	 * @return a map of all world's countries
	 */
	public Map<String, VgAttrFeature> generateAllCountriesMap();
	
}
