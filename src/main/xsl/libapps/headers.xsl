<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
 exclude-result-prefixes="h">

    
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html/h:body/*" />
    </xsl:template>

    <xsl:template match="h:header">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:div[@class='hero-unit']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()" />
        </xsl:copy>
    </xsl:template>

     <xsl:template match="h:form">
        <xsl:copy>
            <xsl:copy-of select="attribute::node()" />
            <xsl:attribute name="action" select="concat('https://lane.stanford.edu', @action)" />
            <xsl:apply-templates select="*|text()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:select/h:option">
        <xsl:copy>
            <xsl:copy-of select="attribute::node()" />
            <xsl:attribute name="data-help" select="concat('https://lane.stanford.edu', @data-help)" />
            <xsl:apply-templates select="*|text()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="." />
    </xsl:template>
</xsl:stylesheet>
    