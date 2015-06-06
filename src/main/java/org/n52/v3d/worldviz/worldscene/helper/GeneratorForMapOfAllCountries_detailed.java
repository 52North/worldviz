package org.n52.v3d.worldviz.worldscene.helper;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class GeneratorForMapOfAllCountries_detailed extends
		AbstractGeneratorForMapOfAllCountries {

	public GeneratorForMapOfAllCountries_detailed() {
		super();
		
		this.pathToEneDatasetWithAllWorldCountries = RelativePaths.COUNTRY_CODES_XML;
	}

	@Override
	protected DatasetLoader setupDatasetLoader() {
		DatasetLoader countryCodes = new DatasetLoader(
				this.pathToEneDatasetWithAllWorldCountries);
		countryCodes.setCountryBordersLOD(CountryBordersLODEnum.DETAILED);
		
		return countryCodes;
	}

}
