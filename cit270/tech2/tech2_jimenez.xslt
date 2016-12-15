<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <body>
        <h2>
          5-Day Weather Forecast for GPS Location
          (<xsl:value-of select="data/location/point/@latitude" />, <xsl:value-of select="data/location/point/@longitude"/>)
        </h2>

        <table border="1">
          <tr>
            <td>Temperature</td>
            <xsl:for-each select="data/time-layout[layout-key='k-p24h-n5-1']/start-valid-time">
              <td style="text-align: center;">
                <strong>
                  <xsl:value-of select="@period-name"></xsl:value-of>
                  <xsl:text> </xsl:text>
                  (<xsl:value-of select="substring-before(substring-after(.,'-'),'T')"/>)
                </strong>
              </td>
            </xsl:for-each>
          </tr>

          <tr>
            <td>
              <xsl:value-of select="data/parameters/temperature[@type='maximum']/name" />
            </td>
            <xsl:for-each select="data/parameters/temperature[@type='maximum']/value">
              <td style="text-align: center;"><xsl:value-of select="text()" /></td>
            </xsl:for-each>
          </tr>

          <tr>
            <td>
              <xsl:value-of select="data/parameters/temperature[@type='minimum']/name" />
            </td>
            <xsl:for-each select="data/parameters/temperature[@type='minimum']/value">
              <td style="text-align: center;"><xsl:value-of select="text()"></xsl:value-of></td>
            </xsl:for-each>
          </tr>
        </table>

        <br />
        <br />

        <table border="1">
          <tr>
            <td>Conditions</td>
            <xsl:for-each select="data/time-layout[layout-key='k-p12h-n10-3']/start-valid-time">
              <td style="text-align: center;"><strong><xsl:value-of select="@period-name"></xsl:value-of></strong></td>
            </xsl:for-each>
          </tr>
          <tr>
            <td>
              <xsl:value-of select="data/parameters/probability-of-precipitation[@time-layout='k-p12h-n10-3']/name" />
            </td>
            <xsl:for-each select="data/parameters/probability-of-precipitation[@time-layout='k-p12h-n10-3']/value">
              <td style="text-align: center;"><xsl:value-of select="text()"></xsl:value-of></td>
            </xsl:for-each>
          </tr>
          <tr>
            <td>
              <xsl:value-of select="data/parameters/weather[@time-layout='k-p12h-n10-3']/name" />
            </td>
            <xsl:for-each select="data/parameters/weather[@time-layout='k-p12h-n10-3']/weather-conditions">
              <td style="text-align: center;"><xsl:value-of select="@weather-summary"></xsl:value-of></td>
            </xsl:for-each>
          </tr>
          <tr>
            <td>
              <xsl:value-of select="data/parameters/conditions-icon[@time-layout='k-p12h-n10-3']/name" />
            </td>
            <xsl:for-each select="data/parameters/conditions-icon[@time-layout='k-p12h-n10-3']/icon-link">
              <td style="text-align: center;"><img><xsl:attribute name="src"><xsl:value-of select="text()" /></xsl:attribute></img></td>
            </xsl:for-each>
          </tr>
        </table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
