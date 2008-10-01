<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h" version="2.0">

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
    
    <xsl:template match="h:*[attribute::id='noHitsText']">
        <xsl:if test="$count = 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>