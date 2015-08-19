/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.dataaccess.load.dataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.GeoReferenceFeatureTypeEnum;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.ShapeReader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.ShapeReader_Detailed;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.ShapeReader_Simplified_110m;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.ShapeReader_Simplified_50m;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.Unit;
import org.n52.v3d.worldviz.exception.UnmatchingEntryPropertyArrayException;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.Entries.Entry;
import noNamespace.DatasetDocument.Dataset.GeneralInformation;
import noNamespace.DatasetDocument.Dataset.GeneralInformation.Theme;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;
import noNamespace.Description;
import noNamespace.Title;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that implements the interface {@link XmlDataset}. It parses
 * any type of XmlDataset and offers methods to give general information like
 * 'tile', 'unit' or 'description' of the dataset. Only the determination of the
 * geometry to display the information is delegated to child-classes.
 * 
 * @author Christian Danowski
 * 
 */
public abstract class AbstractXmlDataset implements XmlDataset {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String[] titles;
	private String[] descriptions;
	private Unit unit;
	private String publicReference;
	private String MetadataReference;
	private DatasetDocument xmlDoc;
	private List<VgAttrFeature> features;

	protected ShapeReader shapeReader;
	protected CountryBordersLODEnum countryBordersLOD = CountryBordersLODEnum.DETAILED;

	/**
	 * Constructor
	 * 
	 * @param doc
	 *            the XmlDataset-document that shall be parsed.
	 * @throws Exception
	 * @see {@link DatasetLoader} the method <code>loadDataset()</code> creates
	 *      an instance of <code>XmlDataset</code>
	 * 
	 * 
	 */
	public AbstractXmlDataset(DatasetDocument doc) throws Exception {

		this.xmlDoc = doc;

		this.features = new ArrayList<VgAttrFeature>(this.xmlDoc.getDataset()
				.getEntries().sizeOfEntryArray());

		this.shapeReader = determineEneShapeReader();

		processGeneralInformation();

		generateFeatures();

	}

	/**
	 * Constructor
	 * 
	 * @param doc
	 *            the XmlDataset-document that shall be parsed.
	 * @param countryBordersLOD
	 *            the level of detail of world borders.
	 * @throws Exception
	 * @see {@link DatasetLoader} the method <code>loadDataset()</code> creates
	 *      an instance of <code>XmlDataset</code>
	 * 
	 * 
	 */
	public AbstractXmlDataset(DatasetDocument doc,
			CountryBordersLODEnum countryBordersLOD) throws Exception {

		this.xmlDoc = doc;

		this.features = new ArrayList<VgAttrFeature>(this.xmlDoc.getDataset()
				.getEntries().sizeOfEntryArray());

		this.countryBordersLOD = countryBordersLOD;

		this.shapeReader = determineEneShapeReader();

		processGeneralInformation();

		generateFeatures();

	}

	public final String[] getTitles() {
		return titles;
	}

	public final String[] getDescriptions() {
		return descriptions;
	}

	public final Unit getUnit() {
		return unit;
	}

	public final String getPublicReference() {
		return publicReference;
	}

	public final String getMetadataReference() {
		return MetadataReference;
	}

	/**
	 * Returns the XmlDataset-XML-Document as JavaBeans-representation of
	 * 'Dataset.xsd'.
	 * 
	 * @return
	 */
	protected DatasetDocument getXmlDoc() {
		return xmlDoc;
	}

	public final List<VgAttrFeature> getFeatures() {
		return features;
	}

	/**
	 * Parses the <code>'GeneralInformation'</code>-element of the dataset which
	 * consists of <code>titles, descriptions, unit, metadata-</code> and
	 * <code>public reference</code>. The methods extracts those values from the
	 * xml-document and sets the corresponding member variables.
	 */
	private void processGeneralInformation() {

		if (logger.isDebugEnabled())
			logger.debug("Parsing general information from XML document.");

		GeneralInformation generalInformation = this.xmlDoc.getDataset()
				.getGeneralInformation();
		processTheme(generalInformation.getTheme());

		processUnit(generalInformation.getUnit());

		this.MetadataReference = generalInformation.getMetadataReference();
		this.publicReference = generalInformation.getPublicReference();

	}

	/**
	 * Parses the <code>Theme</code>-element of the dataset that contains the
	 * <code>titles</code> and <code>descriptions</code>.
	 * 
	 * @param theme
	 */
	private void processTheme(Theme theme) {

		Title[] titleArray = theme.getTitleArray();

		processTitles(titleArray);

		Description[] descriptionArray = theme.getDescriptionArray();

		processDescriptions(descriptionArray);
	}

	/**
	 * Parses the titles of the dataset.
	 * 
	 * @param titleArray
	 */
	private void processTitles(Title[] titleArray) {

		this.titles = new String[titleArray.length];

		for (int i = 0; i < titleArray.length; i++) {
			this.titles[i] = titleArray[i].getStringValue();
		}

	}

	/**
	 * Parses the decriptions of the dataset.
	 * 
	 * @param descriptionArray
	 */
	private void processDescriptions(Description[] descriptionArray) {
		this.descriptions = new String[descriptionArray.length];

		for (int i = 0; i < descriptionArray.length; i++) {
			this.descriptions[i] = descriptionArray[i].getStringValue();
		}

	}

	/**
	 * Parses the unit of the dataset.
	 * 
	 * @param xmlUnit
	 */
	private void processUnit(
			noNamespace.DatasetDocument.Dataset.GeneralInformation.Unit xmlUnit) {

		// titles
		Title[] titleArray = xmlUnit.getTitleArray();

		String[] unitTitles = new String[titleArray.length];

		for (int i = 0; i < titleArray.length; i++) {
			unitTitles[i] = titleArray[i].getStringValue();
		}

		this.unit = new Unit(unitTitles, xmlUnit.getCode());

	}

	/**
	 * Parses the 'Properties' and 'Entries'-parts of the dataset. For each
	 * entry one feature is created. The feature consists of a geometry and
	 * attributes. Each property is interpreted as an attribute; except the
	 * property that contains a 'GeoReference'-element which is used for the
	 * geometry.
	 * 
	 * @throws Exception
	 */
	private void generateFeatures() throws Exception {

		if (logger.isDebugEnabled())
			logger.debug(
					"Parsing feature information from XML document for {} feature-entries.",
					this.features);

		Property[] propertyArray = this.xmlDoc.getDataset().getTableStructure()
				.getPropertyArray();
		Entry[] entryArray = this.xmlDoc.getDataset().getEntries()
				.getEntryArray();

		// create one feature per entry
		for (Entry entry : entryArray) {

			VgAttrFeature newFeature = new GmAttrFeature();

			processFeatureProperties(newFeature, propertyArray, entry);

			// only add the feature if a any geometry could be set!
			// else: do not add this feature, as it cannot be displayed later
			if (newFeature.getGeometry() != null)
				this.features.add(newFeature);
		}

	}

	/**
	 * Per entry, one feature with attributes is constructed. Each property is
	 * interpreted as the feature's attribute; except the property that contains
	 * a 'GeoReference'-element which is used for the geometry.
	 * 
	 * @param newFeature
	 *            the feature representing the entry
	 * @param propertyArray
	 *            the array of properties of the ENE-Dataset
	 * @param entry
	 *            the entry of the Ene-Dataset
	 * @throws Exception
	 */
	private void processFeatureProperties(VgAttrFeature newFeature,
			Property[] propertyArray, Entry entry) throws Exception {
		String[] entryValueArray = entry.getValueArray();
		// iterate over properties and set each property-value-combination
		// as attribute, except the geometry

		checkArrayLength(entryValueArray, propertyArray);

		for (int i = 0; i < propertyArray.length; i++) {
			Property property = propertyArray[i];
			String entryValue = entryValueArray[i];
			if (property.getGeoReference() != null) {
				// Note: It is assumed that each dataset only contains one
				// unique GeoReference-element that holds the complete geometry
				// of the feature
				setGeometry(newFeature, property, entryValue);
				// also add the property that is used for georeference to the
				// list of attributes
				// leads to redundant information, but in case of country code
				// names that may be useful for later purposes
				addAttribute(newFeature, property, entryValue);
			} else {
				// each other property and it's corresponding value is added as
				// a feature's attribute
				addAttribute(newFeature, property, entryValue);
			}

		}
	}

	private void addAttribute(VgAttrFeature newFeature, Property property,
			String entryValue) {
		((GmAttrFeature) newFeature).addAttribute(property.getTitle()
				.getStringValue(), "java.lang." + property.getDataType(),
				(Object) entryValue);
	}

	/**
	 * Compares the array-lengths of the properties and entries. They must be
	 * equal, as each property's value must be present for each entry.
	 * 
	 * @param entryValueArray
	 *            the array of values
	 * @param propertyArray
	 *            the array of properties
	 * @throws UnmatchingEntryPropertyArrayException
	 */
	private void checkArrayLength(String[] entryValueArray,
			Property[] propertyArray)
			throws UnmatchingEntryPropertyArrayException {

		if (entryValueArray.length != propertyArray.length)
			throw new UnmatchingEntryPropertyArrayException(
					"The lenghts of the entryValue-Array and the property-Array do not match, but have to be the same!");

	}

	/**
	 * Sets the geometry of the feature. The geometry depends on the type of
	 * dataset. The possible types can be checked in the enumeration
	 * {@link GeoReferenceFeatureTypeEnum}. For each type a different geometry
	 * type is used. </br></br>
	 * 
	 * <b>Example:</b>
	 * <p>
	 * If the dataset contains point-information that is directly described in
	 * the dataset, then a simple point feature needs to be created.</br> If the
	 * dataset uses ISO3166-alpha2-country-codes as georeference then the
	 * geometry of each country needs to be extracted from a different
	 * source-file.
	 * </p>
	 * 
	 * @param newFeature
	 *            the feature
	 * @param property
	 *            the property that contains the 'GeoReference'-element
	 * @param entryValue
	 *            the value of the property. Note: For each type there is a
	 *            different interpretation of the string
	 * @throws Exception
	 */
	protected abstract void setGeometry(VgAttrFeature newFeature,
			Property property, String entryValue) throws Exception;

	private ShapeReader determineEneShapeReader() throws IOException {
		ShapeReader shapeReader = null;

		switch (this.countryBordersLOD) {
		case DETAILED:
			shapeReader = new ShapeReader_Detailed();
			break;

		case SIMPLIFIED_50m:
			shapeReader = new ShapeReader_Simplified_50m();
			break;

		case SIMPLIFIED_110m:
			shapeReader = new ShapeReader_Simplified_110m();
			break;

		default:
			shapeReader = new ShapeReader_Detailed();
			break;
		}
		return shapeReader;
	}

	@Override
	public String toString() {
		return "AbstractXmlDataset [titles=" + Arrays.toString(titles)
				+ ", descriptions=" + Arrays.toString(descriptions) + ", unit="
				+ unit + ", publicReference=" + publicReference
				+ ", MetadataReference=" + MetadataReference + ", features="
				+ features + "]";
	}

}
