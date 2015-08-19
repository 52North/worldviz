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
package org.n52.v3d.worldviz.dataaccess.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlBitmapDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlCountryCodeDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlEsriShapeDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlPointDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.GeoReferenceFeatureTypeEnum;
import org.n52.v3d.worldviz.exception.NoValidFeatureTypeException;
import org.n52.v3d.worldviz.helper.FileHelper;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property.GeoReference;

/**
 * You may use this class to load ENE-relevant XML-data via the method
 * <code>loadDataset()</code>.<br/>
 * <br/>
 * 
 * In case you intend to load content of type {@link XmlCountryCodeDataset},
 * then please consider the use of the method
 * {@link DatasetLoader#setCountryBordersLOD(CountryBordersLODEnum)} in order to
 * specify which level of detail of the countries's borders you need.
 * 
 * @author Christian Danowski
 * 
 */
public class DatasetLoader {

	private Logger logger = LoggerFactory.getLogger(DatasetLoader.class);

	private CountryBordersLODEnum countryBordersLOD = CountryBordersLODEnum.DETAILED;

	private String xmlFilePath;

	/**
	 * Constructor.
	 * 
	 * @param xmlFilePath
	 *            <p>
	 *            the filePath to the xml-document: Note: Only such
	 *            XML-documents that are valid against the ENE
	 *            <b>Dataset.xsd</b> can be used
	 *            </p>
	 */
	public DatasetLoader(String xmlFilePath) {

		this.xmlFilePath = xmlFilePath;

	}

	public DatasetLoader(String xmlFilePath,
			CountryBordersLODEnum countryBordersLOD) {
		this.xmlFilePath = xmlFilePath;

		this.countryBordersLOD = countryBordersLOD;
	}

	/**
	 * This method/parameter is only relevant, if you load content of type
	 * {@link XmlCountryCodeDataset}!<br/>
	 * 
	 * @return the level of detail that will be used for the countries's
	 *         borders. The standard-value is set to
	 *         {@link CountryBordersLODEnum#DETAILED}.
	 */
	public CountryBordersLODEnum getCountryBordersLOD() {
		return countryBordersLOD;
	}

	/**
	 * This method is only relevant, if you load content of type
	 * {@link XmlCountryCodeDataset}!<br/>
	 * Via this method you can specify which level of detail shall be used for
	 * the countries's borders. The standard-value is set to
	 * {@link CountryBordersLODEnum#DETAILED}.
	 * 
	 * @param countryBordersLOD
	 *            the level of detail for the countries's borders
	 */
	public void setCountryBordersLOD(CountryBordersLODEnum countryBordersLOD) {
		this.countryBordersLOD = countryBordersLOD;
	}

	/**
	 * Parses the XML-file given in the constructor and creates an instance of
	 * <code>{@link XmlDataset}<code>.
	 * 
	 * @return an instance of the interface <i>XmlDataset</i>
	 * @throws Exception
	 *             if the <code>FeatureType</code>-attribute of the
	 *             <code>GeoReference</code>-tag of a <code>Property</code>
	 *             -element of the XMl document does not match any of the types
	 *             listed in the {@link GeoReferenceFeatureTypeEnum}
	 */
	public XmlDataset loadDataset() {
		if (logger.isInfoEnabled()) {
			logger.info("Starting to load the XML-dataset from {}.",
					this.xmlFilePath);
		}

		XmlDataset dataset = null;

		try {
			File xmlFile = new File(this.xmlFilePath);
			// check if file exists

			FileHelper.filesExist(xmlFile);

			// use XMLBeans to parse the document
			DatasetDocument doc = null;

			doc = DatasetDocument.Factory.parse(xmlFile);

			// now the question is, what data does the dataset contain? Images,
			// point data, country codes
			// for this the documents propertyArray is searched for the
			// GeoReference-element
			// Note: It is assumed, that only one unique GeoReference-element is
			// present
			GeoReference geoReference = extractGeoReference(doc);
			String featureType = geoReference.getFeatureType();
			String featureTypeWithoutENE = removeENEfromFeatureType(featureType);

			dataset = determineType(featureTypeWithoutENE, doc);

		} catch (FileNotFoundException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"An error (FileNotFound-Exception) occured during parsing the XML-file {}.",
						this.xmlFilePath, e);
		} catch (XmlException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"An error (XmlException) occured during parsing the XML-file {}.",
						this.xmlFilePath, e);
		} catch (IOException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"An error (IOException) occured during parsing the XML-file {}.",
						this.xmlFilePath, e);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"An error occured during determining the concrete instance of XmlDataset for file {}.",
						this.xmlFilePath, e);
		}
		
		if(dataset == null){
			if (logger.isWarnEnabled())
				logger.warn("The Xml-dataset should be initialized and loaded but is still NULL!");
		}

		return dataset;

	}

	/**
	 * Depending on the type of the XML-document a different implementation of
	 * the interface {@link XmlDataset} needs to be instantiated. <br/>
	 * The type is determined by the <code>FeatureType</code>-attribute of the
	 * <code>GeoReference</code>-tag of a <code>Property</code>-element. The
	 * possible types are listed in the enumeration
	 * <code>{@link GeoReferenceFeatureTypeEnum}</code>. For each type one
	 * implementation of the interface {@link XmlDataset} exists.
	 * 
	 * @param featureType
	 *            the feature type
	 * @param doc
	 *            the xml document
	 * @return an instance of <i>XmlDataset</i>
	 * @throws Exception
	 */
	private XmlDataset determineType(String featureType, DatasetDocument doc)
			throws Exception {

		if (featureType.equals(GeoReferenceFeatureTypeEnum.Bitmap.toString()))
			return new XmlBitmapDataset(doc);

		else if (featureType.equals(GeoReferenceFeatureTypeEnum.CountryCode
				.toString()))
			return new XmlCountryCodeDataset(doc, this.countryBordersLOD);
		else if (featureType.equals(GeoReferenceFeatureTypeEnum.EsriShape
				.toString()))
			return new XmlEsriShapeDataset(doc);
		else if (featureType.equals(GeoReferenceFeatureTypeEnum.Point
				.toString()))
			return new XmlPointDataset(doc);
		else
			throw new NoValidFeatureTypeException("The featureType '"
					+ featureType + "' is not valid!");
	}

	/**
	 * The featureType-string looks like: 'ENE:&lt;featureType&gt;'. As the
	 * enumeration {@link GeoReferenceFeatureTypeEnum} only lists
	 * &lt;featureType&gt; the 'ENE:'-part needs to be removed from the string.
	 * 
	 * @param featureType
	 *            a string that looks like 'ENE:&lt;featureType&gt;'
	 * @return a string that looks like '&lt;featureType&gt;'
	 */
	private String removeENEfromFeatureType(String featureType) {
		// the featureType looks like "ENE:Point"; thus we remove "ENE:" to
		// match the enum Parameters
		return featureType.split(":")[1];
	}

	/**
	 * Searches the Property-elements of the XML-document and looks for any
	 * GeoReference-element that is not equal to <code>null</code>.<br />
	 * 
	 * @param doc
	 *            the xml document
	 * @return the GeoReference-element
	 */
	private GeoReference extractGeoReference(DatasetDocument doc) {
		Property[] propertyArray = doc.getDataset().getTableStructure()
				.getPropertyArray();
		GeoReference geoRef = null;

		for (Property prop : propertyArray) {
			GeoReference localeGeoReference = prop.getGeoReference();
			// TODO check if GeoReference really is == null, if no element is
			// set
			if (localeGeoReference != null) {
				geoRef = localeGeoReference;
			}
		}
		return geoRef;

	}

}
