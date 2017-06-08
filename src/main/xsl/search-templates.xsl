<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:st="http://lane.stanford.edu/search-templates/ns"
    xmlns="http://lane.stanford.edu/search-templates/ns"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    xmlns:r="http://lane.stanford.edu/results/1.0"
    exclude-result-prefixes="h r s st"
    version="2.0">
    
    <xsl:template match="st:search-templates">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:apply-templates select="*|@*"/>
    </xsl:template>
    
    <xsl:template match="h:html[@id]">
        <xsl:variable name="templates">
            <template id="{@id}">
                <xsl:apply-templates/>
            </template>
        </xsl:variable>
        <xsl:if test="count($templates/st:template/*) > 0">
            <xsl:copy-of select="$templates"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="@r:ref[starts-with(.,'resource@')][ends-with(.,'/@count')]">
        <engine idref="{substring-before(substring-after(.,'@'),'/')}"/>
    </xsl:template>
    
    <xsl:template match="h:html"/>
    
    <xsl:template match="h:a[@id and contains(@class,'metasearch')]">
        <xsl:variable name="res-id" select="@id"/>
        <xsl:if test="count(/st:search-templates/s:search/s:engine/s:resource[@s:id = $res-id]) > 0">
            <engine idref="{@id}"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="text()|@*"/>
    
    
</xsl:stylesheet>