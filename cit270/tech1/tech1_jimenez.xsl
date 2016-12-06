<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <head>
        <style>
          tr      { background-color: #efefef; text-align: center; }
          tr.CIT  { background-color: #d9ffff; }
          tr.IS   { background-color: #fce4d1; }
          tr.COMP { background-color: #ffff0a; }
          tr.HEAD { background-color: #81ee7e; }
        </style>
      </head>
      <body>
        <h2>Path to Graduation</h2>

        <xsl:for-each select="catalog/semester">
          <h3><xsl:value-of select="@name"/></h3>

          <table border="1">
            <tr class="HEAD">
              <th>Catalog Number</th>
              <th>Title</th>
              <th>Prerequisites</th>
            </tr>

            <xsl:for-each select="class">
              <tr>
                <xsl:attribute name="class">
                   <xsl:value-of select="dept" />
                </xsl:attribute>
                <td>
                  <xsl:value-of select="dept"/>
                  <xsl:text> </xsl:text>
                  <xsl:value-of select="number"/>
                </td>
                <td>
                  <xsl:value-of select="title"/>
                </td>
                <td>
                  <xsl:value-of select="prereq"/>
                </td>
              </tr>
            </xsl:for-each>
          </table>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>