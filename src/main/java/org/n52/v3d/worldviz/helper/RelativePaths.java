package org.n52.v3d.worldviz.helper;

/**
 * This class is meant to store all relative paths to files, that are used in
 * this project. This way, a relative path has to be changed only in this class
 * instead of changing it wherever it is needed.
 * 
 * @author Christian Danowski
 * 
 */
public final class RelativePaths {

	// general
	private static final String DATA_FOLDER = "data/";
        
        private static final String TEST_FOLDER = "test/";
        
        
	private static final String EARTH_SUB_FOLDER = DATA_FOLDER + "earth/";
	private static final String DATA_SOURCE_FILES_FOLDER = EARTH_SUB_FOLDER
			+ "sourceFiles/";

	// xsd
	private static final String XSD_FOLDER = EARTH_SUB_FOLDER + "xsd/";
	private static final String DATASET_XSD = XSD_FOLDER + "Dataset.xsd";
	private static final String DATASET_DESCRIPTION_XSD = XSD_FOLDER
			+ "DatasetDescription.xsd";

	// nuclear power plants
	public static final String AKW_STANDORTE_XML = EARTH_SUB_FOLDER
			+ "AKW_Standorte.xml";
	public static final String AKW_STANDORTE_Meta_XML = EARTH_SUB_FOLDER
			+ "AKW_Standorte_meta.xml";
	public static final String AKW_STANDORTE_CSV = DATA_SOURCE_FILES_FOLDER
			+ "AKW_Standorte.csv";

	// CO2-emissions
	public static final String CARBON_EMISSIONS_XML = EARTH_SUB_FOLDER
			+ "CarbonEmissions.xml";
	public static final String CARBON_EMISSIONS_META_XML = EARTH_SUB_FOLDER
			+ "CarbonEmissions_meta.xml";
	public static final String CARBON_EMISSIONS_CSV = DATA_SOURCE_FILES_FOLDER
			+ "UNEP-EDE__edgar_co2_total__1396438740.csv";

	// CO2-emissions per capita
	public static final String CARBON_EMISSIONS_PER_CAPITA_XML = EARTH_SUB_FOLDER
			+ "CarbonEmissionsPerCapita.xml";
	public static final String CARBON_EMISSIONS_PER_CAPITA_META_XML = EARTH_SUB_FOLDER
			+ "CarbonEmissionsPerCapita_meta.xml";
	public static final String CARBON_EMISSIONS_PER_CAPITA_CSV = DATA_SOURCE_FILES_FOLDER
			+ "UNEP-EDE__CO2_emissions_wb__1420442209.csv";

	// electricity generation
	public static final String ELECTRICITY_GENERATION_XML = EARTH_SUB_FOLDER
			+ "ElectricityGeneration.xml";
	public static final String ELECTRICITY_GENERATION_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityGeneration_meta.xml";
	public static final String ELECTRICITY_GENERATION_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Strom-Bruttostromerzeugung.csv";

	// electricity production from specific ressources
	public static final String ELECTRICITY_PRODUCTION_FROM_SPECIFIC_RESSOURCE_XML = EARTH_SUB_FOLDER
			+ "ElectricityProductionFromSpecificRessource.xml";
	public static final String ELECTRICITY_PRODUCTION_FROM_SPECIFIC_RESSOURCE_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityProductionFromSpecificRessource_meta.xml";
	public static final String ELECTRICITY_PRODUCTION_FROM_SPECIFIC_RESSOURCE_CSV = DATA_SOURCE_FILES_FOLDER
			+ "ElectricityProductionFromSpecificRessource.csv";

	// electricity export
	public static final String ELECTRICITY_EXPORT_XML = EARTH_SUB_FOLDER
			+ "ElectricityExport.xml";
	public static final String ELECTRICITY_EXPORT_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityExport_meta.xml";
	public static final String ELECTRICITY_EXPORT_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Strom-Export.csv";

	// electricity import
	public static final String ELECTRICITY_IMPORT_XML = EARTH_SUB_FOLDER
			+ "ElectricityImport.xml";
	public static final String ELECTRICITY_IMPORT_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityImport_meta.xml";
	public static final String ELECTRICITY_IMPORT_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Strom-Import.csv";

	// electricity consumption
	public static final String ELECTRICITY_CONSUMPTION_XML = EARTH_SUB_FOLDER
			+ "ElectricityConsumption.xml";
	public static final String ELECTRICITY_CONSUMPTION_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityConsumption_meta.xml";
	public static final String ELECTRICITY_CONSUMPTION_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Strom-Verbrauch.csv";

	// electricity access
	public static final String ELECTRICITY_ACCESS_XML = EARTH_SUB_FOLDER
			+ "ElectricityAccess.xml";
	public static final String ELECTRICITY_ACCESS_META_XML = EARTH_SUB_FOLDER
			+ "ElectricityAccess_meta.xml";
	public static final String ELECTRICITY_ACCESS_CSV = DATA_SOURCE_FILES_FOLDER
			+ "electricity_access.csv";

	// gas resources
	public static final String GAS_RESOURCES_XML = EARTH_SUB_FOLDER
			+ "GasResources.xml";
	public static final String GAS_RESOURCES_META_XML = EARTH_SUB_FOLDER
			+ "GasResources_meta.xml";
	public static final String GAS_RESOURCES_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Gasreserven.csv";

	// oil resources
	public static final String OIL_RESOURCES_XML = EARTH_SUB_FOLDER
			+ "OilResources.xml";
	public static final String OIL_RESOURCES_META_XML = EARTH_SUB_FOLDER
			+ "OilResources_meta.xml";
	public static final String OIL_RESOURCES_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Oelreserven.csv";

	// hard coal resources
	public static final String HARD_COAL_RESOURCES_XML = EARTH_SUB_FOLDER
			+ "HardCoalResources.xml";
	public static final String HARD_COAL_RESOURCES_META_XML = EARTH_SUB_FOLDER
			+ "HardCoalResources_meta.xml";
	public static final String HARD_COAL_RESOURCES_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Steinkohle.csv";

	// soft lignite (brown coal) resources
	public static final String SOFT_LIGNITE_RESOURCES_XML = EARTH_SUB_FOLDER
			+ "SoftLigniteResources.xml";
	public static final String SOFT_LIGNITE_RESOURCES_META_XML = EARTH_SUB_FOLDER
			+ "SoftLigniteResources_meta.xml";
	public static final String SOFT_LIGNITE_RESOURCES_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Weichkohle.csv";

	// Countries Shape detail (noch TM_WorldBorders)
	// TODO ersetzen durch Datensatz von www.naturalearthdata.com ???
	// TODO umbenennen
	public static final String COUNTRIES_SHAPE_XML = EARTH_SUB_FOLDER
			+ "CountriesShape.xml";
	public static final String COUNTRIES_SHAPE_META_XML = EARTH_SUB_FOLDER
			+ "CountriesShape_meta.xml";
	public static final String COUNTRIES_SHAPE_ESRI_SHAPE = EARTH_SUB_FOLDER
			+ "TM_WORLD_BORDERS-0.3/TM_WORLD_BORDERS-0.3.shp";

	// Countries Shape simplified 50m raster
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_50m_XML = EARTH_SUB_FOLDER
			+ "CountriesShape_simplified_50m.xml";
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_50m_META_XML = EARTH_SUB_FOLDER
			+ "CountriesShape_simplified_50m_meta.xml";
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_50m_ESRI_SHAPE = EARTH_SUB_FOLDER
			+ "ne_50m_admin_0_countries/ne_50m_admin_0_countries.shp";

	// Countries Shape simplified 110m raster
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_110m_XML = EARTH_SUB_FOLDER
			+ "CountriesShape_simplified_110m.xml";
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_110m_META_XML = EARTH_SUB_FOLDER
			+ "CountriesShape_simplified_110m_meta.xml";
	public static final String COUNTRIES_SHAPE_SIMPLIFIED_110m_ESRI_SHAPE = EARTH_SUB_FOLDER
			+ "ne_110m_admin_0_countries/ne_110m_admin_0_countries.shp";

	// Country Codes detailed(noch TM_WorldBorders)
	// TODO ersetzen durch Datensatz von www.naturalearthdata.com ???
	// TODO umbenennen
	public static final String COUNTRY_CODES_XML = EARTH_SUB_FOLDER
			+ "CountryCodes.xml";
	public static final String COUNTRY_CODES_META_XML = EARTH_SUB_FOLDER
			+ "CountryCodes_meta.xml";
	public static final String COUNTRY_CODES_CSV = EARTH_SUB_FOLDER
			+ "sourceFiles/TM_WORLD_BORDERS-0.3.csv";

	// Country Codes simplified 50m raster
	public static final String COUNTRY_CODES_SIMPLIFIED_50m_XML = EARTH_SUB_FOLDER
			+ "CountryCodes_simplified_50m.xml";
	public static final String COUNTRY_CODES_SIMPLIFIED_50m_META_XML = EARTH_SUB_FOLDER
			+ "CountryCodes_simplified_50m_meta.xml";
	public static final String COUNTRY_CODES_SIMPLIFIED_50m_CSV = EARTH_SUB_FOLDER
			+ "sourceFiles/ne_50m_admin_0_countries.csv";

	// Country Codes simplified 100m raster
	public static final String COUNTRY_CODES_SIMPLIFIED_110m_XML = EARTH_SUB_FOLDER
			+ "CountryCodes_simplified_110m.xml";
	public static final String COUNTRY_CODES_SIMPLIFIED_110m_META_XML = EARTH_SUB_FOLDER
			+ "CountryCodes_simplified_110m_meta.xml";
	public static final String COUNTRY_CODES_SIMPLIFIED_110m_CSV = EARTH_SUB_FOLDER
			+ "sourceFiles/ne_110m_admin_0_countries.csv";

	// Countries Point
	public static final String COUNTRIES_POINT_XML = EARTH_SUB_FOLDER
			+ "CountriesPoint.xml";
	public static final String COUNTRIES_POINT_META_XML = EARTH_SUB_FOLDER
			+ "CountriesPoint_meta.xml";

	// Earth at night
	public static final String EARTH_AT_NIGHT_XML = EARTH_SUB_FOLDER
			+ "EarthAtNight.xml";
	public static final String EARTH_AT_NIGHT_META_XML = EARTH_SUB_FOLDER
			+ "EarthAtNight_meta.xml";
	public static final String EARTH_AT_NIGHT_JPG = EARTH_SUB_FOLDER
			+ "EarthAtNight.jpg";

	// Land Cover
	public static final String LAND_COVER_XML = EARTH_SUB_FOLDER + "LandCover.xml";
	public static final String LAND_COVER_META_XML = EARTH_SUB_FOLDER
			+ "LandCover_meta.xml";
	public static final String LAND_COVER_JPG = EARTH_SUB_FOLDER + "LandCover.jpg";

	// NuclearAccidents
	public static final String NUCLEAR_ACCIDENTS_XML = EARTH_SUB_FOLDER
			+ "NuclearAccidents.xml";
	public static final String NUCLEAR_ACCIDENTS_META_XML = EARTH_SUB_FOLDER
			+ "NuclearAccidents_meta.xml";
	public static final String NUCLEAR_ACCIDENTS_CSV = DATA_SOURCE_FILES_FOLDER
			+ "Kernkraftwerke_Unfaelle.csv";

	// population
	public static final String POPULATION_XML = EARTH_SUB_FOLDER + "Population.xml";
	public static final String POPULATION_META_XML = EARTH_SUB_FOLDER
			+ "Population_meta.xml";
	public static final String POPULATION_CSV = DATA_SOURCE_FILES_FOLDER
			+ "UNEP-EDE__pop_total__1398251955.csv";
        
        public static final String PAJEK_GRAPH = DATA_FOLDER + "graph";
        
        public static final String PAJEK_GRAPH_NET = PAJEK_GRAPH + ".net";
        
        public static final String PAJEK_GRAPH_X3D = TEST_FOLDER + "graph" + ".x3d";
        
        public static final String PAJEK_GRAPH_HTML = TEST_FOLDER + "graph" + ".html";
        
        public static final String STYLE_CONFIGURATION_XML = DATA_FOLDER +"WvizConfig.xml";

}
