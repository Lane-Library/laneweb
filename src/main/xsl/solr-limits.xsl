<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="2.0">
<!-- process #solrLimits if facets parameter present to prevent interface 
    bounce when #solrLimits populated via JS -->

    <xsl:param name="facets"/>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="h:*[@id='solrLimits']">
        <xsl:if test="string-length($facets) > 0">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>