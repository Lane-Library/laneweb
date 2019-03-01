<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:param name="request-uri"/>

    <xsl:variable name="base-url">
        <xsl:value-of select="concat(replace($request-uri,'\.html','browse.html'),@href)"/>
    </xsl:variable>

    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

    <xsl:template match="h:a">
        <xsl:choose>
            <xsl:when test="number(/doc/map/entry[string = current()/text()]/long) &gt; 0">
                <a href="{concat($base-url,@href)}">
                    <xsl:value-of select="."/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <a class="disabled">
                    <xsl:value-of select="."/>
                </a>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:stylesheet>