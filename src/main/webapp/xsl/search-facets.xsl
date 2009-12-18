<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:sql="http://apache.org/cocoon/SQL/2.0"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h sql s"
    version="2.0">
    
    <xsl:param name="source"/>
    <xsl:param name="q"/>
    
    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
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
            <!-- adds a space to avoid self-closing elements and funny chars from &#160; -->
            <xsl:when test=".='no-self-close'">
                <xsl:comment> </xsl:comment>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="attribute::href[contains(.,'{$keywords}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{$keywords}')"/>
            <xsl:value-of select="encode-for-uri($search-terms)"/>
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
    
    <xsl:template match="node()[contains(attribute::class,'searchFacet')]">
        <!-- swap in engine url when param is true: used for pubmedMore strategy popups -->
        <xsl:param name="pubmedMore-link"/>
        
        <!-- facetId: "article-all" from "article-allFacet"-->
        <xsl:variable name="facetId" select="substring-before(attribute::id,'Facet')"/>
        
        <!-- countFacetId: "all" from "article-allFacet"-->
        <xsl:variable name="countFacetId" select="replace(attribute::id,'\w+-(.*)Facet','$1')"/>
        
        <!-- get hit count from search app or eresources sql -->
        <xsl:variable name="hit-count">
            <xsl:choose>
                <xsl:when test="//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId]">
                    <xsl:value-of select="number(//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId])"/>
                </xsl:when>
                <xsl:when test="/doc/sql:rowset/sql:row[sql:genre=$countFacetId]/sql:hits">
                    <xsl:value-of select="number(/doc/sql:rowset/sql:row[sql:genre=$countFacetId]/sql:hits)"/>
                </xsl:when>
                <xsl:otherwise>NaN</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()" />
            <xsl:choose>
                <!-- always show requested facet, even if zero hits -->
                <xsl:when test="$facetId = $source">
                    <xsl:apply-templates select="attribute::node()" />
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class" /> current</xsl:attribute>
                </xsl:when>
                <!-- hide facet if zero hits and NOT -all facet -->
                <xsl:when test="number($hit-count) = 0 and $countFacetId != 'all'">
                    <xsl:apply-templates select="attribute::node()" />
                    <xsl:attribute name="style">display:none;</xsl:attribute>
                </xsl:when>
            </xsl:choose>
            
            <xsl:choose>
                <xsl:when test="$pubmedMore-link = 'true'">
                    <xsl:apply-templates select="h:a" mode="pubmedMore-link"/>
                    <span id="{$facetId}HitSpan">
                        <xsl:text> </xsl:text>
                        <xsl:if test="$hit-count !='NaN' and number($hit-count) != 0">
                            <xsl:value-of
                                select="format-number(number($hit-count),'###,###,##0')" />
                        </xsl:if>
                    </span>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()" />
                </xsl:otherwise>
            </xsl:choose>
            
            <!-- uncomment to turn hit counts back on; also need to remove previous facetIdHitSpan
                <span id="{$facetId}HitSpan">
                <xsl:text> </xsl:text>
                <xsl:if test="$hit-count !='NaN' and number($hit-count) != 0">
                <xsl:value-of
                select="format-number(number($hit-count),'###,###,##0')" />
                </xsl:if>
                </span>
            -->            
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:span[@id='pubmedMoreStrategies']">
        <ul>
            <xsl:for-each select="//node()[contains(attribute::class,'searchFacet') and contains(attribute::id,'pubmed') and not(matches(attribute::id,'pubmed_guidelines|pubmed_cochrane_reviews'))]">
                <xsl:apply-templates select="self::node()">
                    <xsl:with-param name="pubmedMore-link">true</xsl:with-param>
                </xsl:apply-templates>
            </xsl:for-each>
        </ul>
    </xsl:template>
    
    <xsl:template match="node()[@id='pubmedJournals']">
        <xsl:if test="//node()[@id='pubmedJournalLinks']">
            <xsl:copy>
                <xsl:copy-of select="self::node()" />
                <xsl:copy-of select="//node()[@id='pubmedJournalLinks']"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="node()[@id='pubmedJournalLinks']"/>
    
    <xsl:template match="h:a" mode="pubmedMore-link">
        <xsl:variable name="countFacetId" select="replace(../attribute::id,'\w+-(.*)Facet','$1')"/>
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()" />
            <xsl:attribute name="href"><xsl:value-of select="//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId]/h:a/@href"/></xsl:attribute>
            <xsl:attribute name="target">_blank</xsl:attribute>
            <xsl:apply-templates select="child::node()" />
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>