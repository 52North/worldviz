<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
  xmlns="http://www.opengis.net/kml/2.2"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/">
    <kml>
      <Document>
        <name>
          <xsl:value-of select="Dataset/GeneralInformation/Theme/Title"/>
        </name>
        <description>
          <xsl:value-of select="Dataset/GeneralInformation/Theme/Description"/>
        </description>
        <open>1</open>
        <Style id="default">
          <IconStyle>
            <Icon>
              <href>icon.png</href>
            </Icon>
          </IconStyle>
          <BalloonStyle>
            <text>
              <xsl:text disable-output-escaping="yes">
              &lt;<![CDATA[![CDATA[<p><b><font size='+2'>$[name]</font></b></p>
          <!--<p><font size='-2'>LngLat: $[address]</font></p>-->
          <p><font size='+2'>INES scale level: $[description]</font></p>
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
        <xsl:apply-templates/>
      </Document>
    </kml>
  </xsl:template>
  
  <xsl:template match="Dataset">
    <xsl:for-each select="Entries/Entry">
      <xsl:sort select ="Value[4]"></xsl:sort>
      <Placemark>
        <xsl:attribute name="id">
          <xsl:value-of select="Value[1]"/>
        </xsl:attribute>
        <TimeSpan>
          <begin>
            <xsl:value-of select="Value[4]"/>
          </begin>
          <!-- <end>
            <xsl:value-of select="Value[4]"/>-12-31
          </end>-->
        </TimeSpan>
        <xsl:variable name="lng" select="substring-before(Value[2],',')" />
        <xsl:variable name="lat" select="substring-after(Value[2],',')" />
        <name>
          <xsl:value-of select="Value[3]"/>
          <xsl:text>&#160;</xsl:text>
          <!--Leerzeichen-->
          <xsl:value-of select="Value[4]"/>
        </name>
        <address>
          <xsl:value-of select="Value[2]"/>
        </address>
        <description>
          <xsl:value-of select="Value[5]"/>
        </description>
        <styleUrl>#default</styleUrl>
        <Style>
          <IconStyle>
        <xsl:choose>
          <xsl:when test="Value[5]&lt;3.00">
            <color>ff4bd1cc</color>
          </xsl:when>
          <xsl:when test="Value[5]&gt;=3.00 and Value[5]&lt;4.00">
            <color>ff04ddfb</color>
          </xsl:when>
          <xsl:when test="Value[5]&gt;=4.00 and Value[5]&lt;5.00">
            <color>ff44cefc</color>
          </xsl:when>
          <xsl:when test="Value[5]&gt;=5.00 and Value[5]&lt;6.00">
            <color>ff3c93f2</color>
          </xsl:when>
          <xsl:when test="Value[5]&gt;=6.00 and Value[5]&lt;7.00">
            <color>ff0c4de7</color>
          </xsl:when>
          <xsl:when test="Value[5]&gt;=7.00">
            <color>ff9553eb</color>
          </xsl:when>
          <xsl:otherwise>
            <color>ff7f7f7f</color>
          </xsl:otherwise>
        </xsl:choose>
            <scale>
              <xsl:value-of select="Value[5]"/>
            </scale>
          </IconStyle>
        </Style>
        <Point>
          <coordinates>
            <xsl:value-of select="$lng"/>
            <xsl:text>,</xsl:text>
            <xsl:value-of select="$lat"/>
            <xsl:text>,0</xsl:text>
          </coordinates>
        </Point>
      </Placemark>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
