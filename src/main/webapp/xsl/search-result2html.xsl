<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" 
    xmlns="http://www.w3.org/1999/xhtml" 
    xmlns:s="http://lane.stanford.edu/search-results/1.0" 
    exclude-result-prefixes="h s" version="2.0">
    
    
    <xsl:variable name="search-terms">
        <xsl:value-of select="/s:results/s:query"/>
    </xsl:variable>
    
     <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
     <xsl:variable name="resultLimit">10</xsl:variable>

    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>search results</title>
            </head>
            <body>
                <dl>
                    <xsl:apply-templates select="//s:result"/>
                </dl>
                <div id="search-content-counts" style="display:none;">
                    <xsl:for-each select="//s:result[@type='searchContent' and not(s:resourceId=preceding-sibling::node()/s:resourceId)]">
                        <span id="{s:resourceId}">
                            <a href="{s:resourceUrl}"><xsl:value-of select="s:resourceHits"/></a>
                        </span>
                    </xsl:for-each>
                </div>
                <div class="tooltips" style="display:none;">
                    <xsl:for-each select="//s:result/s:description|//s:result[not(s:description)]/s:title">
                        <xsl:call-template name="tooltip">
                            <xsl:with-param name="text" select="."/>
                        </xsl:call-template>
                    </xsl:for-each>
                </div>
                <xsl:if test="count(//s:result[s:resourceName='PubMed']/s:pub-title) > 0">
                    <ul id="pubmedJournalLinks">
                        <xsl:for-each select="distinct-values(//s:result[s:resourceName = 'PubMed']/s:pub-title)">
                            <xsl:sort select="." order="ascending" data-type="text"/>
                            <xsl:if test="position() &lt;= 10">
                                <li><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&amp;otool=stanford&amp;term={$search-terms} AND &quot;{.}&quot;[Journal]"><xsl:value-of select="."/></a></li>
                            </xsl:if>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
    
    <!-- tranforms article result node into displayable -->
    <xsl:template match="s:result[@type='searchContent']">
        <xsl:variable name="resourceName">
            <xsl:choose>
                <xsl:when test="starts-with(s:resourceName,'PubMed')">PubMed</xsl:when>
                <xsl:otherwise><xsl:value-of select="s:resourceName"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <dd xmlns="http://www.w3.org/1999/xhtml">
            <ul>
                <li>
                    <a title="{concat('article -- ',s:resourceId,' -- ',s:title)}" href="{s:url}" id="{s:id}" target="_blank">
                        <xsl:apply-templates select="s:title"/>
                    </a>
                    
                    <xsl:apply-templates select="s:author"/>
                    <div class="pubTitle">
                        <xsl:apply-templates select="s:pub-title"/>
                        <xsl:apply-templates select="s:pub-date"/>
                        <xsl:apply-templates select="s:pub-volume"/>
                        <xsl:apply-templates select="s:pub-issue"/>
                        <xsl:apply-templates select="s:page"/>
                        <xsl:apply-templates select="s:contentId"/>
                    </div>
                    
                    <div class="moreResults">
                        <span class="sourceLink">
                            <xsl:value-of select="$resourceName"/>
                        </span>
                        <xsl:choose>
                            <xsl:when test="$resourceName = 'PubMed'">
                                <xsl:text> - </xsl:text>
                                <a href="#" rel="popup local pubmedMoreStrategy">more</a>
                            </xsl:when>
                            <xsl:when test="$resultLimit &lt; number(s:resourceHits)">
                                <xsl:text> - </xsl:text>
                                <a target="_blank" title="all {format-number(s:resourceHits,'###,###,##0')} results from {s:resourceName}" href="{s:resourceUrl}">more</a>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </li>
            </ul>
        </dd>
    </xsl:template>
    
    <!-- tranforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <dd xmlns="http://www.w3.org/1999/xhtml">
            <ul>
                <li>
                    <a title="{concat('eresource -- ',s:title)}" href="{s:versions/s:version[1]/s:links/s:link[@type != 'getPassword' and position() = 1]/s:url}" id="eresource-{s:id}" target="_blank">
                        <xsl:apply-templates select="s:title"/>
                    </a>
                    <xsl:apply-templates select="s:versions/s:version[position() = 1]" mode="first"/>
                    <xsl:apply-templates select="s:versions//s:link[position() > 1]"/>
                </li>
            </ul>
        </dd>
    </xsl:template>
    
    <xsl:template match="s:version" mode="first">
        <xsl:text> [</xsl:text>
        <xsl:if test="not(s:summaryHoldings or s:publisher or s:dates or s:description)">
            <span>
                <xsl:value-of select="s:links/s:link[1]/s:label"/>
            </span>
        </xsl:if>
        <xsl:for-each select="s:summaryHoldings|s:publisher|s:dates|s:description">
            <span class="{name()}">
                <xsl:value-of select="."/>
                <xsl:if test="position() != last()">
                    <xsl:text> </xsl:text>
                </xsl:if>
            </span>
        </xsl:for-each>
        <xsl:text>] </xsl:text>
    </xsl:template>
    
    <xsl:template match="s:link">
        <xsl:call-template name="buildAnchor">
            <xsl:with-param name="type" select="@type"/>
            <xsl:with-param name="link">
                <xsl:copy-of select="node()"/>
            </xsl:with-param>
            <xsl:with-param name="version">
                <xsl:copy-of select="../../node()"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="buildAnchor">
        <xsl:param name="link"/>
        <xsl:param name="type"/>
        <xsl:param name="version"/>

        <xsl:choose>
            <xsl:when test="$type = 'normal'">
                <div>
                    <a href="{$link/s:url}" title="{$link/s:label}">
                        <xsl:for-each select="$version/s:summaryHoldings|$version/s:dates|$link/s:label">
                            <span class="{name()}">
                                <xsl:value-of select="."/>
                                <xsl:if test="position() != last()">
                                    <xsl:text> </xsl:text>
                                </xsl:if>
                            </span>
                        </xsl:for-each>
                    </a>
                    <xsl:if test="$version/s:publisher">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$version/s:publisher"/>
                    </xsl:if>
                </div>
            </xsl:when>
            <xsl:when test="$type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a href="{$link/s:url}" title="{$link/s:label}"><xsl:value-of select="$link/s:label"/></a>
            </xsl:when>
            <xsl:when test="$type = 'impactFactor'">
                <div><a href="{$link/s:url}" title="{$link/s:label}">Impact Factor</a></div>
            </xsl:when>
        </xsl:choose>
        
    </xsl:template>

    <xsl:template name="tooltip">
        <xsl:variable name="tooltip-id">
            <xsl:choose>
                <xsl:when test="parent::node()/@type = 'eresource'">
                    <xsl:value-of select="concat('eresource-',parent::node()/s:id,'Tooltip')"></xsl:value-of>
                </xsl:when>
                <xsl:when test="parent::node()/@type = 'searchContent'">
                    <xsl:value-of select="concat(parent::node()/s:id,'Tooltip')"></xsl:value-of>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="tooltip-width">
            <xsl:choose>
                <xsl:when test="string-length(.) > 500">width:60%</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <span style="{$tooltip-width}" id="{$tooltip-id}">
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <xsl:template match="s:description">
        <xsl:variable name="tooltip-width">
            <xsl:choose>
                <xsl:when test="string-length(.) > 500">width:60%</xsl:when>
            </xsl:choose>
        </xsl:variable>
            <span style="{$tooltip-width}" id="{concat(parent::node()/s:id,'Tooltip')}">
                <xsl:apply-templates/>
            </span>
    </xsl:template>

    <xsl:template match="s:author">
        <div class="pubAuthor">
            <xsl:value-of select="."/>
        </div>
    </xsl:template>
    
    <xsl:template match="s:pub-title">
            <xsl:value-of select="."/>
            <xsl:text>. </xsl:text>
    </xsl:template>
    
    <xsl:template match="s:pub-date">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="s:pub-volume">
        <xsl:text>;</xsl:text>
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="s:pub-issue">
        <xsl:text>(</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="s:page">
        <xsl:text>:</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>.</xsl:text>
    </xsl:template>
    
    <xsl:template match="s:contentId">
        <small>
            <xsl:text> </xsl:text>
            <xsl:if test="contains(../s:resourceId,'pubmed')">PMID:</xsl:if>
            <xsl:value-of select="."/>
        </small>
    </xsl:template>
        
    <xsl:template match="s:keyword">
        <strong>
	        <xsl:value-of select="."/>
        </strong>
    </xsl:template>
    
</xsl:stylesheet>