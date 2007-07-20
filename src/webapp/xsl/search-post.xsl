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
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="h:div[attribute::id = 'popInContent']/child::h:p">
        <xsl:if test="starts-with(attribute::id,$source)">
            <xsl:copy>
                <xsl:if test="count(//h:dt) > 0">
                    <xsl:attribute name="style">visibility:hidden</xsl:attribute>
                </xsl:if>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>