package org.n52.v3d.worldviz.dataaccess.load.dataset;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * Specialization of XmlDataset that is used for point-datasets (datasets that
 * contain information that refers to a single point as geometry). The
 * coordinates of the point are provided by the ENE-dataset.
 * 
 * @author Christian Danowski
 * 
 */
public class XmlPointDataset extends AbstractXmlDataset {

	public XmlPointDataset(DatasetDocument doc)
			throws Exception {
		super(doc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) {

		String crs = property.getGeoReference().getCRS().toString();

		VgPoint newPoint = createPoint(entryValue, crs);

		((GmAttrFeature) newFeature).setGeometry(newPoint);

	}

	/**
	 * Parses the coordinates from the comma-separated-string and creates a
	 * Triturus-'VgPoint'.
	 * 
	 * @param coordCommaSeparatedList
	 *            the string that looks like 'longitude,latitude'
	 * @param crs
	 *            the coordinate reference system from the ENE-dataset
	 * @return a VgPoint, that represents the feature's point-geometry
	 */
	private VgPoint createPoint(String coordCommaSeparatedList, String crs) {

		// coordCommaSeparatedList looks like:
		// "longitude,latitude"
		// optional: "longitude,latitude,altitude"
		// so: x,y,z

		VgPoint point = new GmPoint(coordCommaSeparatedList);
		point.setSRS(crs);

		return point;
	}

}
