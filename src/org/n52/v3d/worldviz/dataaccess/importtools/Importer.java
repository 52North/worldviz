package org.n52.v3d.worldviz.dataaccess.importtools;

import java.io.FileNotFoundException;

/**
 * Interface for filling the 'Entries'-part of an xml-file that is valid to the
 * 'Dataset.xsd'-schema-file.
 * 
 * @author Christian Danowski
 * 
 */
public interface Importer {
	/**
	 * Generates new 'Entries' inside of an existing XML-Dataset. Note: any old
	 * values will be lost, as the whole 'Entries'-Part of the file will be
	 * renewed.
	 * 
	 * @throws FileNotFoundException
	 */
	public void fillDatasetEntries() throws FileNotFoundException;
}
