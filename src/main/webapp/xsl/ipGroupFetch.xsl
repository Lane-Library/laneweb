<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

 <xsl:param name="ip-group"/>
 <xsl:param name="callback"/>

 <xsl:template match="/">
  <xsl:choose>
   <xsl:when test="$callback">
    <xsl:value-of select="$callback"/>('<xsl:value-of select="$ip-group"/>'); </xsl:when>
   <xsl:otherwise>
    <xsl:value-of select="$ip-group"/>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>

</xsl:stylesheet>