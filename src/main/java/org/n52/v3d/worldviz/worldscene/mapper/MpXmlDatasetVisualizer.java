package org.n52.v3d.worldviz.worldscene.mapper;

import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlCountryCodeDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlPointDataset;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;

import de.hsbo.fbg.worldviz.WvizConfigDocument;

public class MpXmlDatasetVisualizer extends MpAbstractXmlDatasetVisualizer {

	public MpXmlDatasetVisualizer(WvizConfigDocument wVizConfigFile, String attributeNameForMapping) {
		super(wVizConfigFile, attributeNameForMapping);
	}

	@Override
	public VsAbstractWorldScene transform(XmlDataset xmlDataset) {
		MpAbstractXmlDatasetVisualizer mapper = null;

		if (xmlDataset instanceof XmlCountryCodeDataset)
			mapper = new MpWorldCountriesMapper(this.wVizConfigFile, this.attributeNameForMapping);

		else if (xmlDataset instanceof XmlPointDataset)
			mapper = new MpCartographicSymbolsMapper(this.wVizConfigFile, this.attributeNameForMapping);

		return mapper.transform(xmlDataset);

	}

}
