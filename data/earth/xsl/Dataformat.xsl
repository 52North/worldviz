<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" version="4.0"
  encoding="UTF-8" indent="yes"/>
  <xsl:variable name="DR" select="DatasetDescription/DatasetReference" />
  <xsl:template match="/">
    <html>
      <body>
        <h2>GI Projekt "Gespr&#228;ch mit der Erde"</h2>
        <h3>Metainformationen</h3>
        <div style="position:absolute; right:20%; top:40px;">
          <img src="logo_hs_bochum.gif" alt="HS Bochum"></img>
        </div>
        <table border="1" width="80%">
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">Title</th>
            <td bgcolor="#ffffff">
              <xsl:for-each select="document($DR)//Dataset//GeneralInformation//Theme//Title">
                <xsl:if test="position() &gt; 1">
                  <br></br>
                </xsl:if>
                <!--<xsl:value-of select="Title[@language='DE']" />-->
                <xsl:value-of select="." />
                <xsl:if test="count(@*) &gt; 0 ">
                  (<xsl:value-of select="@*" />)
                </xsl:if>

              </xsl:for-each>
            </td>
          </tr>
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">SpatialScaleHint</th>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="SpatialScaleHint"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th rowspan="2" style="text-align:left">TimePeriod</th>
            <td style="text-align:left">
              <table>
                <tr>
                  <th>begin</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="//begin"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <td style="text-align:left">
              <table>
                <tr>
                  <th>end</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="//end"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">DatasetReference</th>
            <td bgcolor="#ffffff">
              <a>
                <xsl:attribute name="href">
                  <xsl:value-of select="$DR"/>
                </xsl:attribute>
                <xsl:value-of select="$DR"/>
              </a>
            </td>
          </tr>
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">MetadatasetReference</th>
            <xsl:value-of select="Title" />
            <xsl:for-each select="document($DR)">
              <td bgcolor="#ffffff">
                <a>
                  <xsl:attribute name="href">
                    <xsl:value-of select="/Dataset/GeneralInformation/MetadataReference"/>
                  </xsl:attribute>
                  <xsl:value-of select="/Dataset/GeneralInformation/MetadataReference"/>
                </a>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">SourceFileReference</th>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <a>
                  <xsl:attribute name="href">
                    <xsl:choose>
                    <xsl:when test="substring-before(SourceFileReference, '/')='sourceFiles'">
                    </xsl:when>
                      <xsl:otherwise>
                        <xsl:text>sourceFiles/</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                    <xsl:value-of select="SourceFileReference"/>
                  </xsl:attribute>
                  <xsl:value-of select="SourceFileReference"/>
                </a>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th rowspan="4" style="text-align:left">Source</th>
            <td style="text-align:left">
              <table>
                <tr>
                  <th>ContactPersons</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="//ContactPersons"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <td style="text-align:left">
              <table>
                <tr>
                  <th>URI</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <a>
                  <xsl:attribute name="href">
                    <xsl:value-of select="Source/URI"/>
                  </xsl:attribute>
                  <xsl:value-of select="Source/URI"/>
                </a>
                <xsl:if test="count(Source/URI/@*) &gt; 0 ">
                  (<xsl:value-of select="Source/URI/@*" />)
                </xsl:if>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <td style="text-align:left">
              <table>
                <tr>
                  <th>AlternativeURIs</th>
                </tr>
              </table>
            </td>
            <td bgcolor="#ffffff">
              <xsl:for-each select="DatasetDescription/Source/AlternativeURIs/URI">
                <xsl:if test="position()&gt; 1">
                  <br></br>
                </xsl:if>
                <a>
                  <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                  </xsl:attribute>
                  <xsl:value-of select="."/>
                </a>
                <xsl:if test="count(@*) &gt; 0 ">
                  (<xsl:value-of select="@*" />)
                </xsl:if>
              </xsl:for-each>
            </td>
          </tr>
          <tr bgcolor="#9acd32">
            <td style="text-align:left">
              <table>
                <tr>
                  <th>Notes</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="Source/Notes"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th colspan="2" style="text-align:left">CopyrightInformation</th>
            <xsl:for-each select="DatasetDescription">
              <td bgcolor="#ffffff">
                <xsl:value-of select="CopyrightInformation"/>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <th rowspan="2" style="text-align:left">ImportProcess</th>
            <td style="text-align:left">
              <table>
                <tr>
                  <th>Description</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription/ImportProcess/Description">
              <td bgcolor="#ffffff" align="justify">
                <xsl:value-of select="."/>
                <xsl:if test="count(@*) &gt; 0 ">
                  (<xsl:value-of select="@*" />)
                </xsl:if>
              </td>
            </xsl:for-each>
          </tr>
          <tr bgcolor="#9acd32">
            <td style="text-align:left">
              <table>
                <tr>
                  <th>QualityCheck</th>
                </tr>
              </table>
            </td>
            <xsl:for-each select="DatasetDescription/ImportProcess/QualityCheck">
              <td bgcolor="#ffffff">
                <xsl:value-of select="."/>
                <xsl:if test="count(@*) &gt; 0 ">
                  <xsl:for-each select="@*">
                    (<xsl:value-of select="name(.)" />
                    <xsl:text> = </xsl:text><xsl:value-of select="." />)
                  </xsl:for-each>
                </xsl:if>
              </td>
            </xsl:for-each>
          </tr>
        </table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
