<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:template match="/doc">
        <xsl:apply-templates select="s:search"/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="s:resource">
        <xsl:variable name="id" select="@id"/>
        <xsl:if test="/doc/h:html/h:body//h:a[@class='metasearch' and @id = $id]">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
        
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
</xsl:stylesheet>