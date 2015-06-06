package org.n52.v3d.worldviz.helper;

import java.io.File;
import java.io.FileNotFoundException;

public class FileHelper {
	
	public static boolean filesExist(File... files) throws FileNotFoundException{
		for (File file : files) {
			if (!fileExists(file))
				throw new FileNotFoundException(
						"The file could not be found! Supposed Location: "
								+ file.getAbsolutePath());
		}
		
		return true;
		
	}

	private static boolean fileExists(File file) {
		return file.exists();
	}

}
