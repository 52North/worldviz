package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.io.IOException;

public class CustomShapeReader extends AbstractShapeReader {

	public CustomShapeReader(String shapefileLocation) throws IOException {
		super();
		
		this.shapeFileLocation = shapefileLocation;
	}

}
