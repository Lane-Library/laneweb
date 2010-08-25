<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="request-uri"/>
    
    <xsl:param name="query-string"/>

    <xsl:param name="rid"/>

    <xsl:variable name="consumable-query-string">
        <xsl:value-of select="replace($query-string,'&amp;show=\w+','')"/>
    </xsl:variable>
    
    <xsl:variable name="base-link">
        <xsl:value-of select="concat($request-uri,'?',$consumable-query-string)"/>
    </xsl:variable>
    
    <xsl:variable name="search-terms">
        <xsl:value-of select="/s:resources/s:query"/>
    </xsl:variable>

    <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
    <xsl:variable name="moreResultsLimit">10</xsl:variable>

    <xsl:template match="/">

        <xsl:choose>
            <xsl:when test="contains($query-string,'rid=')">
                <xsl:apply-templates select="//s:result[s:id = $rid]" mode="full"/>
            </xsl:when>
            <xsl:when test="contains($query-string,'show=')">
                <xsl:apply-templates select="//s:result" mode="brief"/>
                <xsl:call-template name="paginationLinks"/>
            </xsl:when>
            <xsl:otherwise>
                <ul class="panel" style="padding:0px;" id="results" title="Results" selected="true">
                    <li class="rHead">
                        <xsl:value-of select="format-number(/s:resources/@size, '###,##0')"/> results
                        <xsl:if test="string-length($search-terms)">
                            for <strong>  <xsl:value-of select="/s:resources/s:query"/></strong>
                        </xsl:if>
                    </li>
                    <xsl:apply-templates select="//s:result" mode="brief"/>
                    <xsl:call-template name="paginationLinks"/>
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:result[@type='searchContent']" mode="brief">
        <li>
            <a target="_blank" href="{s:url}">
                <xsl:apply-templates select="s:title"/>
            </a>
            <xsl:if test="s:description">
                <a href="{concat($base-link,'&amp;show=all&amp;rid=',s:id)}" class="moreInfo">more info</a>
            </xsl:if>
        </li>
    </xsl:template>
    
    <xsl:template match="s:result[@type='searchContent']" mode="full">
        <xsl:variable name="resourceName">
            <xsl:choose>
                <xsl:when test="starts-with(s:resourceName,'PubMed')">PubMed</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="s:resourceName"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <div class="panel">
            <a target="_blank" href="{s:url}">
                <xsl:apply-templates select="s:title"/>
            </a>
            <div class="more">
                <xsl:apply-templates select="s:pub-author[string-length(.) > 1]"/>
                <div class="pubTitle">
                    <xsl:choose>
                        <xsl:when test="s:pub-title">
                            <xsl:apply-templates select="s:pub-title"/>
                            <xsl:apply-templates select="s:pub-date"/>
                            <xsl:apply-templates select="s:pub-volume"/>
                            <xsl:apply-templates select="s:pub-issue"/>
                            <xsl:apply-templates select="s:page"/>
                            <span class="sourceLink">
                                <xsl:text> - </xsl:text>
                                <xsl:value-of select="$resourceName"/>
                            </span>
                            <xsl:apply-templates select="s:contentId"/>
                            <br />
                            <xsl:if test="$resourceName != 'PubMed' and $moreResultsLimit &lt; number(s:resourceHits)">
                                <a target="_blank" href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/></a>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <span class="sourceLink">
                                <xsl:value-of select="$resourceName"/>
                            </span>
                            <xsl:if test="$resourceName != 'PubMed' and $moreResultsLimit &lt; number(s:resourceHits)">
                                <xsl:text> - </xsl:text>
                                <a target="_blank" href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/></a>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
                <xsl:apply-templates select="s:description"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="s:result[@type='eresource']" mode="brief">
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
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="buildAnchor">
                        <xsl:with-param name="type">first</xsl:with-param>
                        <xsl:with-param name="link">
                            <xsl:copy-of
                                select="s:versions/s:version[1]/s:links/s:link[1]/node()"/>
                        </xsl:with-param>
                        <xsl:with-param name="title" select="s:title"/>
                    </xsl:call-template>
                    <xsl:call-template name="firstLinkText">
                        <xsl:with-param name="version" select="s:versions/s:version[1]"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="s:description or count(s:versions/s:version) > 1">
                <a href="{concat($base-link,'&amp;show=all&amp;rid=',s:id)}" class="moreInfo">more info</a>
            </xsl:if>
        </li>
    </xsl:template>
    
    <xsl:template match="s:result[@type='eresource']" mode="full">
        <div class="panel">
                <div>
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
                        <!-- add catalog link to all bibs except those that already have one (history) -->
                        <xsl:when test="s:recordType = 'bib' and not(s:versions//s:label[.='catalog record'])">
                            <div class="moreResults">
                                <a target="_blank" href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog record</a>
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
                </div>
                <xsl:apply-templates select="s:description"/>
        </div>  
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
                test="($type = 'getPassword' and count($version//s:link) = 2) or ($version/s:summaryHoldings and count($version//s:link) = 1)">
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
        <xsl:choose>
            <xsl:when test="$type = 'first'">
                <a target="_blank" href="{$link/s:url}">
                    <xsl:apply-templates select="$title"/>
                </a>
            </xsl:when>
            <xsl:when test="$type = 'normal'">
                <div>
                    <a target="_blank" href="{$link/s:url}" title="{$link/s:label}">
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
                <a target="_blank" href="{$link/s:url}" title="{$link/s:label}">
                    <xsl:value-of select="$link/s:label"/>
                </a>
            </xsl:when>
            <xsl:when test="$type = 'impactFactor'">
                <div>
                    <a target="_blank" href="{$link/s:url}">Impact Factor</a>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="resInfo">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:pub-author">
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

    <xsl:template match="s:contentId[contains(../s:resourceId,'pubmed')]">
        <xsl:text> - </xsl:text>
        <span class="pmid">
            <xsl:text> PMID:</xsl:text>
            <a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/{.}?otool=stanford"><xsl:value-of select="."/></a>
        </span>
    </xsl:template>

    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>

    <!-- add Previous, Next, All toggles to search results -->
    <xsl:template name="paginationLinks">
        <li class="resultsNav">
            <!-- 
            <xsl:if test="/s:resources/s:pagination/@previous != 'false'">
                <a href="{concat($base-link,/s:resources/s:pagination/@previous)}">Previous</a>
            </xsl:if>
            -->
            <xsl:if test="/s:resources/s:pagination/@next != 'false'">
                <a target="_replace" href="{concat($base-link,'&amp;show=',/s:resources/s:pagination/@next)}">more ...</a>
            </xsl:if>
            <!-- 
                <xsl:if test="/s:resources/s:pagination/@showAll = 'true'">
                <a href="{concat($base-link,'all')}">Show All <xsl:value-of select="format-number(/s:resources/@size, '###,##0')"/> Results</a>
            </xsl:if>
            -->
        </li>
    </xsl:template>

</xsl:stylesheet>