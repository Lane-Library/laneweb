<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" version="2.0">
    
    <xsl:param name="liaison"/>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::id='liaisons']/h:li">
        <xsl:if test="attribute::id=$liaison or 'all'=$liaison">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
