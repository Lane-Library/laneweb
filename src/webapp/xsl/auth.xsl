<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="1.0">
    
  <xsl:param name="url"/>
    
    <xsl:variable name="webauth-url">https://laneproxy.stanford.edu/cgi-bin/ezp-webauth?url=</xsl:variable>
    
    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
         <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="@class[.='lw_webauth']">
        <xsl:attribute name="href">
            <xsl:value-of select="$webauth-url"/>
            <xsl:value-of select="$url"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="@class[.='lw_direct']">
        <xsl:attribute name="href">
            <xsl:value-of select="$url"/>
        </xsl:attribute>
    </xsl:template>
    
</xsl:stylesheet>
