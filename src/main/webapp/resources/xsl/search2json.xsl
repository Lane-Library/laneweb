<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="s" version="2.0">
    
    <xsl:template match="s:search">
        {
        "id": "<xsl:value-of select="@s:id"/>",
        "status": "<xsl:value-of select="@s:status"/>",
        "query": "<xsl:value-of select="encode-for-uri(s:query)"/>",
        "resources": {
        <xsl:apply-templates select="//s:resource"/>
        }
        }
    </xsl:template>
    
    <xsl:template match="s:resource">
        "<xsl:value-of select="@s:id"/>":
        {
        "status": "<xsl:value-of select="@s:status"/>",
        "url": "<xsl:value-of select="s:url"/>",
        "hits": 
        <xsl:choose>
            <xsl:when test="string(number(s:hits)) = 'NaN'">null</xsl:when>
            <xsl:otherwise><xsl:value-of select="number(s:hits)"/></xsl:otherwise>
        </xsl:choose>
        }
        <xsl:if test="following::s:resource">,</xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
