<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="2.0">
<!-- 
    This counts the number of li children of @class='lwSearchResults' and if 0 processes #noHitsText
    converted from eresources-count.stx because of intermittent performance problems in GCP
-->

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="h:*[@id='noHitsText']">
        <xsl:if test="count(//h:ul[@class='lwSearchResults']/h:li) = 0">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
