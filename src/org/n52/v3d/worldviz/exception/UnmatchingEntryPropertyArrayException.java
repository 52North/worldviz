package org.n52.v3d.worldviz.exception;

/**
 * In an XmlDataset-XML-file, when the amount of declared properties in the
 * 'Table-Structure'-element does not match the amount of properties of each
 * 'Entry'-element, this Exception is thrown.
 * 
 * @author Christian Danowski
 * 
 */
public class UnmatchingEntryPropertyArrayException extends Exception {

	private static final long serialVersionUID = 5237837487732502316L;

	public UnmatchingEntryPropertyArrayException() {
	}

	public UnmatchingEntryPropertyArrayException(String message) {
		super(message);
	}

}
