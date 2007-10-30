<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="query-string"/>
    <xsl:param name="source"/>
    
    <xsl:variable name="keywords" select="/doc/s:search/s:query/text()"/>
    <xsl:variable name="searchId" select="/doc/s:search/@id"/>
    <xsl:variable name="searchMode" select="/doc/h:html/h:head/h:meta[@name='LW.searchMode']/@content"/>
    
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
            <meta name="LW.searchId" content="{$searchId}"/>
            <xsl:choose>
                <!-- when source isn't an html file, assume source is one of the old metasearch templates and complete path -->
                <xsl:when test="not(ends-with($source,'.html'))">
                    <meta name="LW.searchTemplate" content="/search2/{$source}.html"/>
                </xsl:when>
                <xsl:otherwise>
                    <meta name="LW.searchTemplate" content="{$source}"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="not(h:meta[@name='LW.keywords' and not(@content='')])">
                <meta name="LW.keywords" content="{$keywords}"/>
            </xsl:if>
            <xsl:if test="not(h:script[@src='/javascript/metasearch.js'])">
                <script type="text/javascript" src="/javascript/metasearch.js">//</script>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body">
        <xsl:copy>
            <xsl:if test="/doc/s:search/@status != 'successful'">
                <noscript>
                    <h2>
                        Search still running ... <a href="search2.html?javascript=false&amp;source={$source}&amp;id={$searchId}&amp;keywords={$keywords}">Click to see more hit counts</a>
                    </h2>
                </noscript>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::href[contains(.,'{$id}') or contains(.,'{$q}')]">
        <xsl:variable name="q">
            <xsl:value-of select="replace(.,'\{\$q\}',$keywords)"/>
        </xsl:variable>
        <xsl:attribute name="href">
            <xsl:value-of select="replace($q,'\{\$id\}',$searchId)"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="node()[attribute::class = 'metasearch']">
        <xsl:variable name="id" select="@id"/>
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="href">
                <xsl:value-of select="/doc/s:search/s:engine/s:resource[@id = $id]/s:url"/>
            </xsl:attribute>
            <xsl:apply-templates select="node()|child::node()"/>
        </xsl:copy>
        <xsl:if test="contains($query-string,'javascript=false') and /doc/s:search/s:engine/s:resource[@id = $id]/@status = 'successful'">
            <span><xsl:value-of select="format-number(/doc/s:search/s:engine/s:resource[@id = $id]/s:hits, '###,###')"/></span>
        </xsl:if>
    </xsl:template>
    
    <!-- assuming every metasearch element is nested in a parent element that should be hidden by default -->
    <xsl:template match="node()[child::node()[attribute::class = 'metasearch']]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="$searchMode = 'original' and not(contains($query-string,'javascript=false'))">
                <xsl:attribute name="style">
                    <xsl:value-of select="'display:none;'"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
</xsl:stylesheet>