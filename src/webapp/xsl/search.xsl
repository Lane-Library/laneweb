<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                xmlns:s="http://irt.stanford.edu/search/2.0"
                exclude-result-prefixes="h sql s"
                version="2.0">
    
    <xsl:param name="source"/>
    <xsl:param name="keywords"/>
    <xsl:param name="q"/>
    
    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$keywords"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    
    
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html[not(attribute::id)]"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='searchResults'">
                <xsl:apply-templates select="/doc/h:html[attribute::id]/h:body/child::node()"/>
            </xsl:when>
            <xsl:when test=".='search-terms'">
                <xsl:value-of select="$search-terms"/>
            </xsl:when>
            <xsl:when test=".='keywords'">
                <xsl:value-of select="$search-terms"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:meta[@name='LW.searchId']">
        <xsl:copy>
        <xsl:apply-templates select="@*"/>
        <xsl:attribute name="content">
            <xsl:value-of select="/doc/s:search/@id"></xsl:value-of>
        </xsl:attribute>
        </xsl:copy>
    </xsl:template>
    
    
    <xsl:template match="attribute::href[contains(.,'{$keywords}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{$keywords}')"/>
            <xsl:value-of select="$search-terms"/>
        </xsl:attribute>
    </xsl:template>
 
    
    <xsl:template match="attribute::href[contains(.,'{id}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{id}')"/>
             <xsl:value-of select="/doc/s:search/@id"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="h:li[attribute::class='eLibraryTab']">
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
    
    <xsl:template match="h:li[attribute::class='eLibraryTab']/h:a">
        <xsl:choose>
            <xsl:when test="starts-with(parent::h:li/attribute::id,$source)">
                <xsl:apply-templates/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:li[attribute::class='eLibraryTab']/h:a/h:span[attribute::class='tabHitCount']">
        <xsl:variable name="genre" select="substring-before(parent::h:a/parent::h:li/attribute::id,'Tab')"/>
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:value-of select="/doc/sql:rowset/sql:row[sql:genre=$genre]/sql:hits"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>