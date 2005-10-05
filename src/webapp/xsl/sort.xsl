<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="1.0">

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
                
  <xsl:template match="h:ul[@class='eresources']">
      <xsl:copy>
          <xsl:copy-of select="@*"/>
          <xsl:apply-templates>
              <xsl:sort select="translate(h:span/h:span[@class='filing'],' ','.')"/>
          </xsl:apply-templates>
      </xsl:copy>
  </xsl:template>

</xsl:stylesheet>

