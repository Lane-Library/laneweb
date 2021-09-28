<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"   xmlns="http://www.w3.org/1999/xhtml"    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:apply-templates    select="libcal/locations/location[lid= '17374']/weeks/week" />
    </xsl:template>

    <xsl:template match="week">
        <ul>
          <xsl:for-each select="./*">
             <li><xsl:value-of select="name()"/><xsl:text>: </xsl:text> <xsl:value-of select="./rendered/text()"/>    </li>
          </xsl:for-each>  
        </ul>
    </xsl:template> 
    


</xsl:stylesheet>