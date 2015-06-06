package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.io.IOException;

import org.n52.v3d.worldviz.helper.RelativePaths;

public class ShapeReader_Simplified_50m extends AbstractShapeReader {
	
	public ShapeReader_Simplified_50m() throws IOException {
		super();
		
		this.shapeFileLocation = RelativePaths.COUNTRIES_SHAPE_SIMPLIFIED_50m_ESRI_SHAPE;
	}

}
