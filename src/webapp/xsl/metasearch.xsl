<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:variable name="keywords" select="/doc/s:search/s:query/text()"/>
    <xsl:variable name="searchId" select="/doc/s:search/@id"/>
    
    <xsl:template match="/doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates/>
            <xsl:if test="not(h:meta[@name='LW.keywords'])">
                <meta name="LW.keywords" content="{$keywords}"/>
            </xsl:if>
            <meta name="LW.searchId" content="{$searchId}"/>
            <meta name="LW.searchMode" content="original"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:meta[@name='LW.keywords']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="content">
                <xsl:value-of select="$keywords"></xsl:value-of>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="node()[attribute::id = 'noscriptRefresh' and /doc/s:search/@status = 'successful']"/>
    
    <xsl:template match="attribute::href[contains(.,'{$id}') or contains(.,'{$q}')]">
        <xsl:variable name="q">
            <xsl:value-of select="replace(.,'\{\$q\}',$keywords)"/>
        </xsl:variable>
        <xsl:attribute name="href">
            <xsl:value-of select="replace($q,'\{\$id\}',$searchId)"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:a[attribute::class = 'metasearch']">
        <xsl:variable name="id" select="@id"/>
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="href">
                <xsl:value-of select="/doc/s:search/s:engine/s:resource[@id = $id]/s:url"/>
            </xsl:attribute>
            <xsl:apply-templates select="node()|child::node()"/>
        </xsl:copy>
            <xsl:if test="/doc/s:search/s:engine/s:resource[@id = $id]/@status = 'successful'">
                <span class="jshidden"><xsl:value-of select="format-number(/doc/s:search/s:engine/s:resource[@id = $id]/s:hits, '###,###')"/></span>
            </xsl:if>
    </xsl:template>
    
    <!-- assuming every metasearch element is nested in a span or li that should be hidden by default -->
    <xsl:template match="node()[child::node()[position() = 1 and attribute::class = 'metasearch']]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="class">
                <xsl:value-of select="'jshidden'"/>
            </xsl:attribute>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
</xsl:stylesheet>