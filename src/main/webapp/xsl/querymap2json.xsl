<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:q="http://lane.stanford.edu/querymap/ns"
    xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:template match="h:html"/>
    
    <xsl:template match="q:query-map">
        <xsl:text>{"query":"</xsl:text>
        <xsl:call-template name="escape">
            <xsl:with-param name="string" select="q:query"/>
        </xsl:call-template>
        <xsl:text>"</xsl:text>
        <xsl:if test="q:descriptor">
            <xsl:text>,</xsl:text>
            <xsl:apply-templates select="q:descriptor"/>
            <xsl:if test="q:resource-map">
                <xsl:text>,</xsl:text>
                <xsl:apply-templates select="q:resource-map"/>
            </xsl:if>
        </xsl:if>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="q:descriptor">
        <xsl:text>"descriptor":"</xsl:text>
        <xsl:call-template name="escape">
            <xsl:with-param name="string" select="."/>
        </xsl:call-template>
        <xsl:text>"</xsl:text>
    </xsl:template>
    
    <xsl:template match="q:resource-map">
        <xsl:text>"resourceMap":{</xsl:text>
        <xsl:apply-templates select="q:descriptor"/>
        <xsl:text>,"resources":[</xsl:text>
        <xsl:apply-templates select="q:resource"/>
        <xsl:text>]</xsl:text>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="q:resource">
        <xsl:text>{"id":"</xsl:text>
        <xsl:value-of select="@idref"/>
        <xsl:text>","label":"</xsl:text>
        <xsl:value-of select="//h:li[@id = current()/@idref]"/>
        <xsl:text>"}</xsl:text>
        <xsl:if test="following-sibling::q:resource">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="escape">
        <xsl:param name="string"/>
        <xsl:variable name="escape-apos" select="replace($string,&quot;'&quot;,&quot;/'&quot;)"/>
        <xsl:value-of select="replace($escape-apos,'&quot;','/&quot;')"/>
    </xsl:template>

</xsl:stylesheet>