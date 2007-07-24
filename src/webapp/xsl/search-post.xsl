<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h" version="1.0">

    <xsl:param name="source"/>
    <xsl:param name="keywords"/>

    <xsl:variable name="count">
        <xsl:value-of select="count(//h:dt)"/>
    </xsl:variable>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

    <xsl:template match="h:div[attribute::id='popInContent']/h:p">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="$count > 1">
                    <xsl:apply-templates select="attribute::node() | child::node()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="attribute::node() | child::node()[not(attribute::id='sortBy')]"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:p[attribute::id='noHitsText']">
        <xsl:if test="$count = 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <xsl:template
        match="h:li[attribute::class='eLibraryTabActive']/h:a/h:span[attribute::class='tabHitCount']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:attribute name="style">visibility:visible</xsl:attribute>
            <xsl:value-of select="$count"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>