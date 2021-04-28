<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="h"
>

    <xsl:template match="h:html">
        <div class="module  same-height-1 libguide-slide-container">
            <i id="previous-libguide-slide" class="fa fa-chevron-left" />
            <i id="next-libguide-slide" class="fa fa-chevron-right" />
            <div id="libguide-slide">
                <h3>Feature Guides</h3>
                <xsl:apply-templates select="//h:div[@id='s-lib-cpane-31076085']/h:div" />
            </div>
        </div>
    </xsl:template>


    <xsl:template match="h:div">
        <xsl:copy>
            <xsl:attribute name="class">slide</xsl:attribute>
            <xsl:apply-templates select="attribute::node()|child::node()" />
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