package org.n52.v3d.worldviz.dataaccess.load.dataset;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPolygon;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.BoundingBoxExtractor;

/**
 * Specialization of XmlDataset that is used for image-datasets (datasets that
 * represent an image-file that can be used as drape for any object in X3D).
 * Such datasets contain a georeference that represents the bounding box of the
 * image.
 * 
 * @author Christian Danowski
 * 
 */
public class XmlBitmapDataset extends AbstractXmlDataset {

	public XmlBitmapDataset(DatasetDocument doc)
			throws Exception {
		super(doc);
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) throws Exception {

		// As this concerns images, the geographical bounding box shall be
		// expressed as a polygon.

		String crs = property.getGeoReference().getCRS().toString();

		VgPolygon polygon = BoundingBoxExtractor.createPolygonFromBoundingBox(
				entryValue, crs);

		((GmAttrFeature) newFeature).setGeometry(polygon);

	}

}
