package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.io.IOException;

import org.n52.v3d.worldviz.helper.RelativePaths;

public class ShapeReader_Detailed extends AbstractShapeReader {
	
	public ShapeReader_Detailed() throws IOException{
		super();
		
		this.shapeFileLocation = RelativePaths.COUNTRIES_SHAPE_ESRI_SHAPE;
	}

}
