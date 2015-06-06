package org.n52.v3d.worldviz.worldscene.helper;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class GeneratorForMapOfAllCountries_simplified_110m extends
		AbstractGeneratorForMapOfAllCountries {

	public GeneratorForMapOfAllCountries_simplified_110m() {
		super();
		
		this.pathToEneDatasetWithAllWorldCountries = RelativePaths.COUNTRY_CODES_SIMPLIFIED_110m_XML;
	}

	@Override
	protected DatasetLoader setupDatasetLoader() {
		DatasetLoader countryCodes = new DatasetLoader(
				this.pathToEneDatasetWithAllWorldCountries);
		countryCodes.setCountryBordersLOD(CountryBordersLODEnum.SIMPLIFIED_110m);
		
		return countryCodes;
	}

}
