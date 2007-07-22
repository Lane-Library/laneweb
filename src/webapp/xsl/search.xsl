<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="1.0">
    
    <xsl:param name="source"/>
    <xsl:param name="keywords"/>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="/doc">
        <xsl:apply-templates select="child::h:html[not(attribute::id)]"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='searchResults'">
                <xsl:apply-templates select="/doc/h:html[attribute::id]/h:body/child::node()"/>
            </xsl:when>
            <xsl:when test=".='keywords'">
                <xsl:value-of select="$keywords"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="attribute::href[contains(.,'{$keywords}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{$keywords}')"/>
            <xsl:value-of select="$keywords"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="h:p[parent::h:div[attribute::id='popInContent']]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:apply-templates select="/doc/h:html[attribute::id]//h:div[attribute::id='popInContent']/child::node()"/>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:div[attribute::id='popInContent' and ancestor::h:html[attribute::id]]"/>
    
    <xsl:template match="child::h:li[attribute::class='eLibraryTab']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(self::class)]"/>
            <xsl:choose>
                <xsl:when test="starts-with(attribute::id,$source)">
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class"/>Active</xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="attribute::class"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="child::h:a[parent::h:li[attribute::class='eLibraryTab']]">
        <xsl:choose>
            <xsl:when test="starts-with(parent::h:li/attribute::id,$source)">
                <xsl:apply-templates select="child::node()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>