<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:q="http://lane.stanford.edu/querymap/ns"
    version="2.0">

    <xsl:template match="q:query-map">
        <xsl:text>{</xsl:text>
        <xsl:apply-templates select="q:query|q:descriptor|q:resource-map"/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="q:query|q:descriptor">
        <xsl:text>'</xsl:text>
        <xsl:value-of select="name()"/>
        <xsl:text>':'</xsl:text>
        <xsl:call-template name="escape-apostrophe">
            <xsl:with-param name="string" select="."/>
        </xsl:call-template>
        <xsl:text>'</xsl:text>
        <xsl:if test="following-sibling::*">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="q:resource-map">
        <xsl:text>'resource-map':{</xsl:text>
        <xsl:apply-templates select="q:descriptor"/>
        <xsl:text>'resources':[</xsl:text>
        <xsl:apply-templates select="q:resource"/>
        <xsl:text>]</xsl:text>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="q:resource">
        <xsl:text>'</xsl:text>
        <xsl:value-of select="@idref"/>
        <xsl:text>'</xsl:text>
        <xsl:if test="following-sibling::*">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="escape-apostrophe">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string,&quot;'&quot;)">
                <xsl:value-of select="substring-before($string,&quot;'&quot;)"/>
                <xsl:text>\'</xsl:text>
                <xsl:call-template name="escape-apostrophe">
                    <xsl:with-param name="string" select="substring-after($string,&quot;'&quot;)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>