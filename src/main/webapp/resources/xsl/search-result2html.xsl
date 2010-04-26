<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="show"/>

    <xsl:variable name="search-terms">
        <xsl:value-of select="/s:resources/s:query"/>
    </xsl:variable>

    <xsl:variable name="result-count">
        <xsl:value-of select="count(//s:result)"/>
    </xsl:variable>
    
    <xsl:variable name="current-set">
        <xsl:choose>
            <xsl:when test="$show">
                <xsl:value-of select="$show"/>
            </xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="next-set">
        <xsl:choose>
            <xsl:when test="number($current-set) + 20 &lt; $result-count">
                <xsl:value-of select="number($current-set) + 20"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="prev-set">
        <xsl:choose>
            <xsl:when test="number($current-set) - 20 >= 0">
                <xsl:value-of select="number($current-set) - 20"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
        
    <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
    <xsl:variable name="moreResultsLimit">10</xsl:variable>

    <xsl:template match="/">
        <html>
            <head>
                <title>search results</title>
            </head>
            <body>
                <dl>
                    <xsl:choose>
                        <xsl:when test="$show = 'all'">
                            <xsl:apply-templates select="//s:result"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="//s:result[position() &gt;= $current-set and position() &lt;= ($current-set + 20)]"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </dl>
                <div id="results-nav">
                    <span class="show"><xsl:value-of select="$current-set"/></span>
                    <span class="result-count"><xsl:value-of select="$result-count"/></span>
                    <span class="previous"><xsl:value-of select="$prev-set"/></span>
                    <span class="next"><xsl:value-of select="$next-set"/></span>
                    <span class="show-all"><xsl:if test="$result-count > 20 and $show != 'all'">true</xsl:if></span>
                </div>
                <div id="search-content-counts" style="display:none;">
                    <xsl:for-each
                        select="//s:result[@type='searchContent' and not(s:resourceId=preceding-sibling::node()/s:resourceId)]">
                        <span id="{s:resourceId}">
                            <a href="{s:resourceUrl}">
                                <xsl:value-of select="s:resourceHits"/>
                            </a>
                        </span>
                    </xsl:for-each>
                </div>
                <div class="tooltips" style="display:none;">
                    <xsl:for-each
                        select="//s:result/s:description|//s:result[not(s:description)]/s:title">
                        <xsl:call-template name="tooltip"/>
                    </xsl:for-each>
                </div>
                <xsl:if test="count(//s:result[s:resourceName='PubMed']/s:pub-title) > 0">
                    <ul id="pubmedJournalLinks">
                        <xsl:for-each
                            select="distinct-values(//s:result[s:resourceName = 'PubMed']/s:pub-title)">
                            <xsl:sort select="." order="ascending" data-type="text"/>
                            <xsl:if test="position() &lt;= 10">
                                <li>
                                    <a rel="popup standard"
                                        href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&amp;otool=stanford&amp;term={$search-terms} AND &quot;{.}&quot;[Journal]">
                                        <xsl:value-of select="."/>
                                    </a>
                                </li>
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
                <xsl:otherwise>
                    <xsl:value-of select="s:resourceName"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <dd>
            <ul>
                <li>
                    <a title="{s:title}" href="{s:url}"
                        id="{s:id}" rel="popup standard">
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
                            <xsl:when test="$moreResultsLimit &lt; number(s:resourceHits)">
                                <xsl:text> - </xsl:text>
                                <a rel="popup standard"
                                    title="all {format-number(s:resourceHits,'###,###,##0')} results from {s:resourceName}"
                                    href="{s:resourceUrl}">more</a>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </li>
            </ul>
        </dd>
    </xsl:template>

    <!-- tranforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <dd>
            <ul>
                <li>
                    <xsl:choose>
                        <xsl:when
                            test="s:versions/s:version[1]/s:links/s:link[1]/@type = 'getPassword'">
                            <xsl:call-template name="buildAnchor">
                                <xsl:with-param name="type">first</xsl:with-param>
                                <xsl:with-param name="link">
                                    <xsl:copy-of
                                        select="s:versions/s:version[1]/s:links/s:link[2]/node()"/>
                                </xsl:with-param>
                                <xsl:with-param name="title" select="s:title"/>
                                <xsl:with-param name="eresourceId" select="s:id"/>
                            </xsl:call-template>
                            <xsl:call-template name="firstLinkText">
                                <xsl:with-param name="version" select="s:versions/s:version[1]"/>
                            </xsl:call-template>
                            <xsl:call-template name="buildAnchor">
                                <xsl:with-param name="type">getPassword</xsl:with-param>
                                <xsl:with-param name="link">
                                    <xsl:copy-of
                                        select="s:versions/s:version[1]/s:links/s:link[1]/node()"/>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:apply-templates select="s:versions//s:link[@type!='getPassword']"
                                mode="remainder-links"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="buildAnchor">
                                <xsl:with-param name="type">first</xsl:with-param>
                                <xsl:with-param name="link">
                                    <xsl:copy-of
                                        select="s:versions/s:version[1]/s:links/s:link[1]/node()"/>
                                </xsl:with-param>
                                <xsl:with-param name="title" select="s:title"/>
                                <xsl:with-param name="eresourceId" select="s:id"/>
                            </xsl:call-template>
                            <xsl:call-template name="firstLinkText">
                                <xsl:with-param name="version" select="s:versions/s:version[1]"/>
                            </xsl:call-template>
                            <xsl:apply-templates select="s:versions//s:link" mode="remainder-links"
                            />
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="s:recordType = 'auth'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane Community Info File</span>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'bib'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane Catalog</span>
                                <xsl:text> - </xsl:text>
                                <a rel="popup standard" href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">details</a>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'faq'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane FAQ</span>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'news'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane News</span>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'web'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane Web Page</span>
                            </div>
                        </xsl:when>
                    </xsl:choose>
                </li>
            </ul>
        </dd>
    </xsl:template>

    <xsl:template match="s:link" mode="remainder-links">
        <xsl:if test="position() != 1">
            <xsl:call-template name="buildAnchor">
                <xsl:with-param name="type" select="@type"/>
                <xsl:with-param name="link">
                    <xsl:copy-of select="node()"/>
                </xsl:with-param>
                <xsl:with-param name="version">
                    <xsl:copy-of select="../../node()"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="firstLinkText">
        <xsl:param name="version"/>
        <xsl:text> </xsl:text>
        <xsl:if
            test="not($version/s:summaryHoldings or $version/s:publisher or $version/s:dates or $version/s:description)">
            <xsl:value-of select="$version/s:links/s:link[1]/s:label"/>
        </xsl:if>
        <xsl:for-each
            select="$version/s:summaryHoldings|$version/s:publisher|$version/s:dates|$version/s:description">
            <xsl:value-of select="."/>
            <xsl:if test="position() != last()">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:if test="$version/s:links/s:link/s:instruction">
            <xsl:text>, </xsl:text>
            <xsl:value-of select="$version/s:links/s:link/s:instruction"/>
        </xsl:if>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template name="linkText">
        <xsl:param name="link"/>
        <xsl:param name="type"/>
        <xsl:param name="version"/>
        <xsl:choose>
            <xsl:when
                test="($type = 'getPassword' and count($version//s:link) = 2) or count($version//s:link) = 1">
                <xsl:value-of select="$version/s:summaryHoldings"/>
                <xsl:text>, </xsl:text>
                <xsl:value-of select="$version/s:dates"/>
            </xsl:when>
            <xsl:when test="$link/s:label">
                <xsl:value-of select="$link/s:label"/>
            </xsl:when>
            <xsl:when test="$link/s:url">
                <xsl:value-of select="$link/s:url"/>
            </xsl:when>
        </xsl:choose>
        <xsl:if test="$version/s:description">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$version/s:description"/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="buildAnchor">
        <xsl:param name="type"/>
        <xsl:param name="link"/>
        <xsl:param name="version"/>
        <xsl:param name="title"/>
        <xsl:param name="eresourceId"/>
        <xsl:choose>
            <xsl:when test="$type = 'first'">
                <a title="{$title}" href="{$link/s:url}"
                    id="eresource-{$eresourceId}" rel="popup standard">
                    <xsl:apply-templates select="$title"/>
                </a>
            </xsl:when>
            <xsl:when test="$type = 'normal'">
                <div>
                    <a href="{$link/s:url}" title="{$link/s:label}">
                        <xsl:call-template name="linkText">
                            <xsl:with-param name="type" select="@type"/>
                            <xsl:with-param name="link">
                                <xsl:copy-of select="$link"/>
                            </xsl:with-param>
                            <xsl:with-param name="version">
                                <xsl:copy-of select="$version"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </a>
                    <xsl:if test="$link/s:instruction">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$link/s:instruction"/>
                    </xsl:if>
                    <xsl:if test="$version/s:publisher">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$version/s:publisher"/>
                    </xsl:if>
                </div>
            </xsl:when>
            <xsl:when test="$type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a href="{$link/s:url}" title="{$link/s:label}">
                    <xsl:value-of select="$link/s:label"/>
                </a>
            </xsl:when>
            <xsl:when test="$type = 'impactFactor'">
                <div>
                    <a href="{$link/s:url}" title="{$link/s:label}">Impact Factor</a>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="tooltip">
        <xsl:variable name="tooltip-id">
            <xsl:choose>
                <xsl:when test="parent::node()/@type = 'eresource'">
                    <xsl:value-of select="concat('eresource-',parent::node()/s:id,'Tooltip')"/>
                </xsl:when>
                <xsl:when test="parent::node()/@type = 'searchContent'">
                    <xsl:value-of select="concat(parent::node()/s:id,'Tooltip')"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="tooltip-width">
            <xsl:choose>
                <xsl:when test="string-length(.) > 500">width:60%</xsl:when>
                <xsl:otherwise>default</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <span id="{$tooltip-id}">
            <xsl:if test="$tooltip-width != 'default'">
                <xsl:attribute name="style">
                    <xsl:value-of select="$tooltip-width"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="name() = 'description'">
                <xsl:apply-templates select="."/>
            </xsl:if>
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