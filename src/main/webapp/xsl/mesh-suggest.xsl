<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    
    <xsl:template match="/">
        <xsl:text>{"result": [</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>]}</xsl:text>
    </xsl:template>
    
    <xsl:template match="results/result">
        <xsl:variable name="c" select="count(/results/result)"/>
        <xsl:text>{"mesh": "</xsl:text><xsl:value-of select="display_heading"/><xsl:text>"}</xsl:text>
        <xsl:if test="following::result">,</xsl:if>
    </xsl:template>
    
</xsl:stylesheet>