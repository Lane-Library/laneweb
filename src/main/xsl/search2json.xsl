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
        <!-- case 103573 failed metasearch links have empty href
            on bioresearch and textbook search pages
            because status is missing sometimes, get status from engine -->
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="@s:status">
                    <xsl:value-of select="@s:status"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="ancestor::s:engine/@s:status"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="hits">
            <xsl:choose>
                <xsl:when test="string(number(s:hits)) = 'NaN'">null</xsl:when>
                <xsl:otherwise><xsl:value-of select="number(s:hits)"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        "<xsl:value-of select="@s:id"/>":
        {
        "status": "<xsl:value-of select="$status"/>",
        "url": "<xsl:value-of select="s:url"/>",
        "hits": <xsl:value-of select="$hits"/>
        }
        <xsl:if test="following::s:resource">,</xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
