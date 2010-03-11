<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://lane.stanford.edu/resources/1.0" version="2.0">

    <xsl:param name="show"/>

    <xsl:template match="s:resources">
        <xsl:copy>
            <xsl:attribute name="count" select="count(s:result)"/>
            <xsl:attribute name="show" select="$show"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="s:result">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="s:result[position() &lt; number($show)]"/>

    <xsl:template match="s:result[position() &gt; number($show) + 20]"/>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
