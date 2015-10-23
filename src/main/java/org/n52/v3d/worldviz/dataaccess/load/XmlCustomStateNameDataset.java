package org.n52.v3d.worldviz.dataaccess.load;

import java.util.List;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.worldviz.dataaccess.load.dataset.AbstractXmlDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.GeometryConverter;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.JTSHelper;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.MultiPolygon;

public class XmlCustomStateNameDataset extends AbstractXmlDataset {

	public XmlCustomStateNameDataset(DatasetDocument doc,
			String customShapeFilePath, String customJoinHeader)
			throws Exception {
		super(doc, customShapeFilePath, customJoinHeader);
	}

	public XmlCustomStateNameDataset(DatasetDocument doc,
			CountryBordersLODEnum countryBordersLOD) throws Exception {
		super(doc, countryBordersLOD);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug(
					"Creating a MultiPolygon-geometry from the feature property '{}' with value '{}'.",
					property.getTitle().getStringValue(), entryValue);

		List<SimpleFeature> jtsSimpleFeatures = this.shapeReader
				.getSimpleFeatureCollection();

		MultiPolygon jtsMultiPolygon = JTSHelper.getMultiPolygonForAttribute(
				this.customJoinHeader, entryValue, jtsSimpleFeatures);

		VgMultiPolygon triturusMultiPolygon = null;

		if (jtsMultiPolygon != null) {
			triturusMultiPolygon = GeometryConverter
					.convertJtsMultiPolygon2TriturusMultiPolygon(jtsMultiPolygon);
		}

		// if triturusMultiPolygon is still =null, then we catch that in super
		// class AbstractXmlDataset (methods processProperties() und
		// generateFeatures())
		((GmAttrFeature) newFeature).setGeometry(triturusMultiPolygon);

	}

}
