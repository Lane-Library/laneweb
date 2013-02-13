<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="alpha"/>

    <xsl:param name="request-uri"/>
    
    <xsl:param name="source"/>
    
    <xsl:param name="proxy-links"/>
    
    <xsl:param name="ipgroup"/>
    
    <xsl:param name="query"/>
    
    <xsl:param name="emrid"/>
    
    <xsl:param name="mesh"/>
    
    <xsl:variable name="search-terms">
        <xsl:value-of select="/s:resources/s:query"/>
    </xsl:variable>

    <xsl:variable name="guest-mode">
        <xsl:if test="$ipgroup = 'OTHER' and $proxy-links = 'false'">true</xsl:if>
    </xsl:variable>
    
    <xsl:variable name="er-browse-mode">
        <xsl:if test="contains($request-uri,'biomed-resources')">true</xsl:if>
    </xsl:variable>
    
    
    <xsl:variable name="pubmed-baseUrl">http://www.ncbi.nlm.nih.gov/pubmed/</xsl:variable>

    <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
    <xsl:variable name="moreResultsLimit">10</xsl:variable>
    
    <xsl:include href="resourceListPagination.xsl"/>
    
    <xsl:template match="/s:resources">
        <html>
            <head>
                <title>search results</title>
            </head>
            <body>
                <xsl:if test="$er-browse-mode = 'true' and number(@size) &gt; 100">
                    <xsl:call-template name="paginationLinks">
                        <xsl:with-param name="browse-mode" select="$er-browse-mode"/>
                    </xsl:call-template>
                </xsl:if>
                <dl class="lwSearchResults">
                    <xsl:apply-templates select="//s:result"/>
                </dl>
                <xsl:if test="number(@size) &gt; 100">
                <xsl:call-template name="paginationLinks">
                    <xsl:with-param name="browse-mode" select="$er-browse-mode"/>
                </xsl:call-template>
                </xsl:if>
                <div id="search-content-counts">
                    <!-- empty div causes problems when facets are imported with JS -->
                    <xsl:text>&#160;</xsl:text>
                    <xsl:for-each
                        select="/s:resources/s:contentHitCounts/s:resource">
                        <span id="{@resourceId}">
                            <a href="{@resourceUrl}">
                                <xsl:value-of select="@resourceHits"/>
                            </a>
                        </span>
                    </xsl:for-each>
                </div>
                <xsl:if test="/s:resources/s:contentHitCounts/s:resource[contains(@resourceId,'pubmed')]">
                    <span id="showPubMedStrategies" style="display:none;">true</span>
                </xsl:if>
            </body>
        </html>
    </xsl:template>

    <!-- transforms article result node into displayable -->
    <xsl:template match="s:result[@type='searchContent']">
        <xsl:variable name="resourceName">
            <xsl:choose>
                <xsl:when test="starts-with(s:resourceName,'PubMed')">PubMed</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="s:resourceName"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="primaryLink">
            <xsl:choose>
                <!-- point to PubMed directly instead of SFX when in guest mode -->
                <xsl:when test="$resourceName = 'PubMed' and $guest-mode = 'true'">
                    <xsl:value-of select="concat($pubmed-baseUrl,substring-after(s:contentId,'PMID:'))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="s:url"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <dd>
            <ul>
                <xsl:call-template name="ulClass"/>
                <li>
                    <a class="primaryLink" href="{$primaryLink}">
                        <xsl:apply-templates select="s:title"/>
                    </a>
                    
                    <!-- display authors if NOT clinical or peds interface -->
                    <xsl:if test="not(starts-with($source,'clinical') or starts-with($source,'peds')) and string-length(s:pub-author) > 1">
                        <xsl:apply-templates select="s:pub-author"/>
                    </xsl:if>
                    
                    <div class="pubTitle">
                        <xsl:choose>
                            <xsl:when test="s:pub-title">
                                <xsl:apply-templates select="s:pub-title"/>
                                <xsl:apply-templates select="s:pub-date"/>
                                <xsl:apply-templates select="s:pub-volume"/>
                                <xsl:apply-templates select="s:pub-issue"/>
                                <xsl:apply-templates select="s:page"/>
                                <xsl:if test="$resourceName = 'PubMed' or number(s:resourceHits) &lt;= $moreResultsLimit">
                                    <span class="sourceLink">
                                        <xsl:text> - </xsl:text>
                                        <xsl:value-of select="$resourceName"/>
                                    </span>
                                </xsl:if>
                                <xsl:apply-templates select="s:contentId"/>
                                <br />
                                <xsl:if test="$resourceName != 'PubMed' and $moreResultsLimit &lt; number(s:resourceHits)">
                                    <a href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/>  </a> &#187;
                                </xsl:if>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$resourceName != 'PubMed' and $moreResultsLimit &lt; number(s:resourceHits)">
                                        <a href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/>  </a> &#187;
                                        <xsl:if test="$emrid and $resourceName = 'UpToDate'"> <span class="utdCMEnote"> &#8592; Use this link for CME</span></xsl:if>
                                    </xsl:when>
                                    <xsl:when test="$moreResultsLimit &gt;= number(s:resourceHits)">
                                        <span class="sourceLink">
                                            <xsl:value-of select="$resourceName"/>
                                        </span>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </li>
                <xsl:apply-templates select="s:description"/>
            </ul>
        </dd>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <dd>
            <ul>
                <xsl:call-template name="ulClass"/>
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
                                <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog record</a>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'web'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane Web Page</span>
                            </div>
                        </xsl:when>
                        <xsl:when test="s:recordType = 'class'">
                            <div class="moreResults">
                                <span class="sourceLink">Lane Class</span>
                            </div>
                        </xsl:when>
                    </xsl:choose>
                </li>
                <xsl:apply-templates select="s:description"/>
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
                <a class="primaryLink" href="{$link/s:url}">
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
                    <a href="{$link/s:url}">Impact Factor</a>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:description">
        <li class="hvrTarg">
            <xsl:apply-templates/>
        </li>
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

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:variable name="pmid">
            <xsl:value-of select="substring-after(.,'PMID:')"/>
        </xsl:variable>
        <span class="pmid">
            <xsl:text> PMID: </xsl:text>
            <a href="{concat($pubmed-baseUrl,$pmid,'?otool=stanford')}"><xsl:value-of select="$pmid"/></a>
        </span>
    </xsl:template>
    
    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>
    
    <xsl:template match="s:desc-label">
        <xsl:if test="position() > 1">
            <br />
        </xsl:if>
        <span class="abstractLabel">
            <xsl:value-of select="."/>
        </span>
        <xsl:text>: </xsl:text>
    </xsl:template>

    <xsl:template name="ulClass">
        <xsl:attribute name="class">
            <xsl:value-of select="concat('r-', /s:resources/@start + position())"/>
            <xsl:if test="s:description">
                <xsl:text> hvrTrig</xsl:text>
            </xsl:if>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>