<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:st="http://lane.stanford.edu/search-templates/ns"
    xmlns="http://lane.stanford.edu/search-templates/ns"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    exclude-result-prefixes="h st xi"
    version="2.0">
    
    <xsl:template match="st:search-templates">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:html">
        <template id="{@id}">
            <xsl:apply-templates/>
        </template>
    </xsl:template>
    
    <xsl:template match="h:a[@id and contains(@class,'metasearch')]">
        <resource idref="{@id}"/>
    </xsl:template>
    
    <xsl:template match="xi:include">
        <xsl:variable name="href" select="replace(@href,'.[e|r]=',' ')"/>
        <xsl:for-each select="tokenize(string($href),' ')">
            <xsl:if test="not(starts-with(.,'cocoon'))">
                <engine idref="{.}"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="text()"/>
    
</xsl:stylesheet>