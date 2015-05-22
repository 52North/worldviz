<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns="http://www.opengis.net/kml/2.2"
  xmlns:kml="http://www.opengis.net/kml/2.2"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="kml"
>
  <xsl:output method="xml" indent="yes"/>
  <xsl:variable name="coords" select="document('..\CountriesPoint.xml')"/>
  <xsl:variable name="shape" select="document('..\kml\CountriesShape.kml')"/>
  <!--Welche Ausgabe soll erfolgen? 'point' oder 'poly'-->
  <xsl:param name="pointOrPoly" />
  <xsl:variable name="max">
    <!--Gibt das maximale Value zurück (position=1) oder ein anderes, um die Ergebnisse zu normalisieren-->
    <xsl:call-template name="getMax">
      <xsl:with-param name="position" select="200" />
    </xsl:call-template>
  </xsl:variable>

  <!--Maximum-->
  <xsl:template name="getMax">
    <xsl:param name="position" />
    <xsl:for-each select="Dataset/Entries">
      <xsl:for-each select="Entry/Value">
        <xsl:sort select="." data-type="number" order="descending"/>
        <xsl:if test="position() = $position">
          <xsl:value-of select="."/>
        </xsl:if>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="/">
    <kml>
      <Document>
        <name>
          <xsl:value-of select="Dataset/GeneralInformation/Theme/Title"/>
        </name>
        <description>
          <xsl:value-of select="Dataset/GeneralInformation/Theme/Description"/>
          <xsl:text> Given in </xsl:text>
          <xsl:value-of select="Dataset/GeneralInformation/Unit/Title"/>
        </description>
        <open>1</open>
        <Style id="default">
          <!--BallonStyle - Anzeige der Placemark-Informationen-->
          <BalloonStyle>
            <text>
              <!--output-escaping steuert die Umwandlung bestimmter Zeichen in deren Zeichencode (<,>,äöü...)-->
              <xsl:text disable-output-escaping="yes">
              &lt;<![CDATA[![CDATA[<p><b><font size='+2'>$[name]</font></b></p>
          <p><font size='+1'>Carbon Emission $[description]: $[Snippet] ]]></xsl:text>
              <xsl:value-of select="Dataset/GeneralInformation/Unit/Code"/>
              <xsl:text disable-output-escaping="yes"><![CDATA[</font></p>
          <p></p>
          <p>Source: ]]></xsl:text>
              <xsl:value-of select="Dataset/GeneralInformation/PublicReference"/>
              <xsl:text disable-output-escaping="yes"><![CDATA[ </p>
          <p>More Informations: <a href=']]></xsl:text>
              ../<xsl:value-of select="Dataset/GeneralInformation/MetadataReference"/>
              <xsl:text disable-output-escaping="yes"><![CDATA['>Metadata</a></p>]]]]>&gt;
            </xsl:text>
            </text>
          </BalloonStyle>
        </Style>
        <!--template 'Folder' einfügen-->
        <xsl:call-template name="folder"/>

      </Document>
    </kml>
  </xsl:template>

  <xsl:template name="folder">
    <xsl:for-each select="Dataset/TableStructure/Property[@dataType='Float']">
      <xsl:variable name="year" select="Title"/>
      <xsl:variable name="yearpos" select="position()+1"/>
      <xsl:variable name="begin" select="substring-before(TimeReference/begin,'+')"/>
      <xsl:variable name="end" select="substring-before(TimeReference/end,'+')"/>
      <Folder>
        <name>
          <xsl:value-of select="Title"/>
        </name>
        <!--TimeSpan, um ein gewünschtes Jahr zur Datenanzeige wählen zu können-->
        <TimeSpan>
          <begin>
            <xsl:value-of select="$begin"/>
          </begin>
          <end>
            <xsl:value-of select="$end"/>
          </end>
        </TimeSpan>
        <xsl:call-template name="placemark">
          <xsl:with-param name="year" select="$year"/>
          <xsl:with-param name="yearpos" select="$yearpos"/>
        </xsl:call-template>
      </Folder>
    </xsl:for-each>
  </xsl:template>

  <!--Placemark-->
  <xsl:template name="placemark">
    <xsl:param name="year"/>
    <xsl:param name="yearpos"/>
    <xsl:for-each select="./.././../Entries/Entry">
      <!--CountryCode von Eingabedatei-->
      <xsl:variable name="ccthis" select="Value[1]"/>
      <!--Emissionswert des jeweiligen Jahres-->
      <xsl:variable name="carbonvalue" select="Value[$yearpos]"/>
      <Placemark>
        <xsl:attribute name="id">
          <xsl:value-of select="$ccthis"/>
          <xsl:value-of select="$year"/>
        </xsl:attribute>
        <description>
          <xsl:value-of select="$year"/>
        </description>
        <Snippet>
          <xsl:value-of select="$carbonvalue"/>
        </Snippet>
        <styleUrl>#default</styleUrl>
        <!--Style Definitionen aufrufen-->
        <xsl:call-template name="style">
          <xsl:with-param name="carbonvalue" select="$carbonvalue" />
        </xsl:call-template>

        <xsl:choose>
          <xsl:when test="$pointOrPoly='point'">
            <!--Placemark mit Points erstellen-->
            <xsl:call-template name="point">
              <xsl:with-param name="ccthis" select="$ccthis"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:when test="$pointOrPoly='poly'">
            <!--oder Placemark mit Polygonen erstellen
            !!!Große Datenmenge!!!-->
            <xsl:call-template name="polygon">
              <xsl:with-param name="ccthis" select="$ccthis"/>
              <xsl:with-param name="carbonvalue" select="$carbonvalue"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="point">
              <xsl:with-param name="ccthis" select="$ccthis"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </Placemark>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="point">
    <xsl:param name="ccthis" />
    <xsl:for-each select="$coords//Dataset//Entries//Entry">
      <!--Wenn coords.CountryCode = Eingabedatei.CountryCode-->
      <xsl:if test="Value[1]=$ccthis">
        <!--Punktkoordinaten des jeweiligen Landes-->
        <xsl:variable name="lng" select="Value[6]"/>
        <xsl:variable name="lat" select="Value[7]"/>
        <name>
          <!--Englischer Landesname-->
          <xsl:value-of select="Value[3]"/>
        </name>
        <Point>
          <coordinates>
            <xsl:value-of select="$lng"/>
            <xsl:text>,</xsl:text>
            <xsl:value-of select="$lat"/>
            <xsl:text>,0</xsl:text>
          </coordinates>
        </Point>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="polygon" >
    <xsl:param name="ccthis" />
    <xsl:param name="carbonvalue" />
    <xsl:for-each select="$shape//kml:kml//kml:Document//kml:Folder//kml:Placemark">
      <!--Wenn coords.CountryCode = Eingabedatei.CountryCode-->
      <xsl:if test="kml:ExtendedData//kml:SchemaData//kml:SimpleData[2]=$ccthis">
        <name>
          <xsl:value-of select="kml:name"/>
        </name>
        <!--Es gibt immer nur eins von beiden, daher ohne if-Abfrage-->
        <xsl:copy-of select="kml:Polygon"/>
        <xsl:copy-of select="kml:MultiGeometry"/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>


  <xsl:template name="style">
    <xsl:param name="carbonvalue"/>
    <xsl:variable name="colorgreen">
      <xsl:call-template name="getColor">
        <xsl:with-param name="dec">
          <xsl:value-of select="format-number(255-($carbonvalue div $max)*255,'0')"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="colorred">
      <xsl:call-template name="getColor">
        <xsl:with-param name="dec">
          <xsl:value-of select="format-number(($carbonvalue div $max)*255,'0')"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <Style>
      <LineStyle>
        <color>ff0000ff</color>
      </LineStyle>
      <PolyStyle>
        <fill>1</fill>
        <color>
          <xsl:text>ff00</xsl:text>
          <xsl:value-of select="$colorgreen"/>
          <xsl:value-of select="$colorred"/>
        </color>
      </PolyStyle>
      <IconStyle>
        <Icon>
          <href>icon.png</href>
        </Icon>
        <color>
          <xsl:text>ff00</xsl:text>
          <xsl:value-of select="$colorgreen"/>
          <xsl:value-of select="$colorred"/>
        </color>
        <scale>
          <xsl:choose>
            <xsl:when test="$carbonvalue > $max">
              <xsl:value-of select="5.00"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="format-number(($carbonvalue div $max)*4,'0.00')"/>
            </xsl:otherwise>
          </xsl:choose>
        </scale>
      </IconStyle>
    </Style>
  </xsl:template>

  <xsl:template name="getColor">
    <xsl:param name="dec" />
    <xsl:variable name="hex">
      <xsl:call-template name="ConvertDecToHex">
        <xsl:with-param name="index">
          <xsl:value-of select="$dec"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="string-length($hex)=1">
        <xsl:text>0</xsl:text>
        <xsl:value-of select="$hex"/>
      </xsl:when>
      <xsl:when test="string-length($hex)=0">00</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$hex"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--http://www.getsymphony.com/download/xslt-utilities/view/78502/-->
  <xsl:template name="ConvertDecToHex">
    <xsl:param name="index" />
    <xsl:if test="$index > 0">
      <xsl:if test="$index &lt;= 255">
        <xsl:call-template name="ConvertDecToHex">
          <xsl:with-param name="index" select="floor($index div 16)" />
        </xsl:call-template>
        <xsl:choose>
          <xsl:when test="$index mod 16 &lt; 10">
            <xsl:value-of select="$index mod 16" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$index mod 16 = 10">a</xsl:when>
              <xsl:when test="$index mod 16 = 11">b</xsl:when>
              <xsl:when test="$index mod 16 = 12">c</xsl:when>
              <xsl:when test="$index mod 16 = 13">d</xsl:when>
              <xsl:when test="$index mod 16 = 14">e</xsl:when>
              <xsl:when test="$index mod 16 = 15">f</xsl:when>
              <xsl:otherwise>a</xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
    </xsl:if>
    <xsl:if test="$index &gt; 255">ff</xsl:if>
  </xsl:template>

</xsl:stylesheet>
