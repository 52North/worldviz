package org.n52.v3d.worldviz.extensions.mappers;

import org.n52.v3d.triturus.vgis.VgAttrFeature;

/**
 * This class contains String values which represent certain names for
 * attributes that are added to {@link VgAttrFeature}-objects during a mapping
 * step. <br/>
 * <br/>
 * For instance when a color shall be mapped to an {@link VgAttrFeature} -object
 * using the mapper {@link MpValue2ColoredAttrFeature}, then a new attribute
 * must be added to store the color. The name of that attribute is stored in
 * this class.
 * 
 * @author Christian Danowski
 *
 */
public final class NamesForAttributes {

	/**
	 * 
	 */
	public static String attributeNameForColor = "ColorForVisualization";
	public static String attributeNameForExtrusion = "ExtrusionHeightForVisualization";

}
