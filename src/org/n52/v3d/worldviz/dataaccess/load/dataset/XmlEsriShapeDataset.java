package org.n52.v3d.worldviz.dataaccess.load.dataset;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPolygon;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.BoundingBoxExtractor;

/**
 * Specialization of XmlDataset that is used for shape-datasets (dataset that
 * represent a shape-file that stores external geometries). Such datasets
 * contain a georeference that represents the bounding box of the shape file.</br>
 * <b>Note:</b> the feature's geometry is just the BBOX of all geometries of the shape-file.
 * 
 * @author Christian Danowski
 * 
 */
public class XmlEsriShapeDataset extends AbstractXmlDataset {

	public XmlEsriShapeDataset(DatasetDocument doc)
			throws Exception {
		super(doc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) throws Exception {

		String crs = property.getGeoReference().getCRS().toString();

		VgPolygon polygon = BoundingBoxExtractor.createPolygonFromBoundingBox(
				entryValue, crs);

		((GmAttrFeature) newFeature).setGeometry(polygon);

	}

}
