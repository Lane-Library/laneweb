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
    
    <xsl:template match="child::h:div[parent::h:div[attribute::id='eLibrarySearchResults']]">
        <xsl:if test="attribute::id = $source">
            <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
                <xi:include xmlns:xi="http://www.w3.org/2001/XInclude" 
                    href="cocoon://eresources/erdb?t={$source}&amp;q={$keywords}">
                    <xi:fallback>sorry, something failed during your search.</xi:fallback>
                </xi:include>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!--<xsl:template match="child::h:div[attribute::class='tipText']">
        <xsl:if test="attribute::id = concat($source,'TipText')">
            <xsl:copy>
                <xsl:attribute name="style">display:block;float:right</xsl:attribute>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>-->
    
    <xsl:template match="child::h:li[attribute::class='eLibraryTab']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(self::class)]"/>
            <xsl:choose>
                <xsl:when test="starts-with(attribute::id,$source)">
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class"/>Active</xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="attribute::class"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="child::h:a[parent::h:li[attribute::class='eLibraryTab']]">
        <xsl:choose>
            <xsl:when test="starts-with(parent::h:li/attribute::id,$source)">
                <xsl:apply-templates select="child::node()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>