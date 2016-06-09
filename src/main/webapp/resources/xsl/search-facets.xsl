<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h"
    version="2.0">
    
    <xsl:param name="source"/>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html[1]"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='searchResults'">
                <xsl:apply-templates select="/doc/h:html[2]/h:body/child::node()"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="node()[contains(attribute::class,'searchFacet')]">
        <!-- swap in engine url when param is true: used for pubmedMore strategy popups -->
        <xsl:param name="pubmedMore-link"/>
        
        <!-- facetId: "article-all" from "article-allFacet"-->
        <xsl:variable name="facetId" select="substring-before(attribute::id,'Facet')"/>
        <xsl:message select="concat('facetId=',$facetId)"/>
        
        <!-- countFacetId: "all" from "article-allFacet"-->
        <xsl:variable name="countFacetId" select="replace(attribute::id,'\w+-(.*)Facet','$1')"/>
        <xsl:message select="concat('countFacetId=',$countFacetId)"/>
        
        <xsl:message select="concat('//h:div[attribute::id=&quot;search-content-counts&quot;]/h:span[attribute::id=$countFacetId]=',//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId])"/>
        
        <!-- get hit count from search app (s:search OR search-content-counts) or eresources sql -->
        <xsl:variable name="hit-count">
            <xsl:choose>
                <xsl:when test="//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId]">
                    <xsl:value-of select="number(//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId])"/>
                </xsl:when>
                <xsl:otherwise>NaN</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:message select="concat('hit-count=',$hit-count)"/>
        
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()" />
            <xsl:choose>
                <!-- always show requested facet, even if zero hits -->
                <xsl:when test="$facetId = $source">
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class" /> current-facet</xsl:attribute>
                </xsl:when>
                <!-- make facet inactive if zero hits -->
                <xsl:when test="number($hit-count) = 0">
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class" /> inactiveFacet</xsl:attribute>
                </xsl:when>
                <!-- mark facets as "searchableFacet" when hit-count is NaN ... this means JS will check for active/inactive-->
                <xsl:when test="$hit-count = 'NaN'">
                    <xsl:attribute name="class"><xsl:value-of select="attribute::class" /> searchableFacet</xsl:attribute>
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
                <xsl:when test="number($hit-count) = 0">
                    <xsl:value-of select="child::node()/text()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:span[@id='pubmedMoreStrategies']">
        <xsl:if test="//node()[@id='showPubMedStrategies']">
            <div class="rightSearchTips">
                <div class="heading" style="
                    font-weight: bold;
                    padding: 10px 0 5px;
                    border-bottom: solid 1px rgb(144,144,144);
                    margin-bottom:4px;">PubMed Searches</div>
                <ul>
                    <xsl:for-each select="//node()[contains(attribute::class,'searchFacet') and contains(attribute::id,'pubmed') and not(matches(attribute::id,'pubmed_cochrane_reviews'))]">
                        <xsl:variable name="countFacetId" select="replace(attribute::id,'\w+-(.*)Facet','$1')"/>
                        <xsl:if test="//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId]">
                            <xsl:apply-templates select="self::node()">
                                <xsl:with-param name="pubmedMore-link">true</xsl:with-param>
                            </xsl:apply-templates>
                        </xsl:if>
                    </xsl:for-each>
                </ul>
            </div>
        </xsl:if>
    </xsl:template>
        
    <xsl:template match="h:a" mode="pubmedMore-link">
        <xsl:variable name="countFacetId" select="replace(../attribute::id,'\w+-(.*)Facet','$1')"/>
        <a title="Pubmed Searches: {.}" href="{//h:div[attribute::id='search-content-counts']/h:span[attribute::id=$countFacetId]/h:a/@href}">
            <xsl:value-of select="."/>
            <xsl:text> </xsl:text>
        </a>
    </xsl:template>
    
</xsl:stylesheet>