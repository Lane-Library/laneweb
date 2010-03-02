<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="xi:include/@href" xmlns:xi="http://www.w3.org/2001/XInclude">
        <xsl:attribute name="href">
            <xsl:choose>
                <xsl:when test="contains(.,'?')">
                    <xsl:value-of select="substring-before(.,'?')"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="replace(substring-after(.,'?e='),'&amp;e=',',')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
</xsl:stylesheet>
