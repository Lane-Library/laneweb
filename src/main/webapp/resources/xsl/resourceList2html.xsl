<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s" version="2.0">

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="ipgroup"/>

    <xsl:param name="query"/>

    <xsl:param name="emrid"/>

    <xsl:variable name="guest-mode">
        <xsl:if test="$ipgroup = 'OTHER' and $proxy-links = 'false'">true</xsl:if>
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
                <xsl:call-template name="paginationLinks"/>
                <h3 class="eresources">&#160;</h3>
                <ul class="lwSearchResults">
                    <xsl:apply-templates select="s:result"/>
                </ul>
                <xsl:if test="number(@size) &gt; 100">
                    <xsl:call-template name="paginationLinks"/>
                </xsl:if>
                <div id="search-content-counts">
                    <!-- empty div causes problems when facets are imported with JS -->
                    <xsl:text>&#160;</xsl:text>
                    <xsl:for-each select="s:contentHitCounts/s:resource">
                        <span id="{@resourceId}">
                            <a href="{@resourceUrl}">
                                <xsl:value-of select="@resourceHits"/>
                            </a>
                        </span>
                    </xsl:for-each>
                </div>
                <xsl:if test="/s:resources/s:contentHitCounts/s:resource[contains(@resourceId,'pubmed')]">
                    <span id="showPubMedStrategies">true</span>
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

        <li>
            <xsl:if test="s:description">
                <xsl:attribute name="class" select="'hvrTrig'"/>
            </xsl:if>
            <div>
                <a class="primaryLink" href="{$primaryLink}">
                    <xsl:apply-templates select="s:title"/>
                </a>
            </div>

            <!-- display authors if NOT clinical or peds interface -->
            <xsl:if test="not(starts-with($source,'clinical') or starts-with($source,'peds')) and string-length(s:pub-author) > 1">
                <xsl:apply-templates select="s:pub-author"/>
            </xsl:if>

            <div class="pubTitle">
                <xsl:variable name="hits" select="number(s:resourceHits)"/>
                <xsl:choose>
                    <xsl:when test="s:pub-text">
                        <xsl:value-of select="s:pub-text"/>
                        <xsl:if test="$resourceName = 'PubMed' or $hits &lt;= $moreResultsLimit">
                            <span class="sourceLink">
                                <xsl:text> - </xsl:text>
                                <xsl:value-of select="$resourceName"/>
                            </span>
                        </xsl:if>
                        <xsl:apply-templates select="s:contentId"/>
                        <br/>
                        <xsl:if test="$resourceName != 'PubMed' and $moreResultsLimit &lt; $hits">
                            <a href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/></a>
                            <xsl:text> &#187;</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="$resourceName != 'PubMed' and $moreResultsLimit &lt; $hits">
                                <a href="{s:resourceUrl}">All results from <xsl:value-of select="$resourceName"/></a>
                                <xsl:text> &#187;</xsl:text>
                                <xsl:if test="$emrid and $resourceName = 'UpToDate'">
                                    <span class="utdCMEnote"> &#8592; Use this link for CME</span>
                                </xsl:if>
                            </xsl:when>
                            <xsl:when test="not(s:resourceHits) or $moreResultsLimit &gt;= $hits">
                                <span class="sourceLink">
                                    <xsl:value-of select="$resourceName"/>
                                </span>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <xsl:apply-templates select="s:description"/>
        </li>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <li>
            <xsl:if test="s:description">
                <xsl:attribute name="class" select="'hvrTrig'"/>
            </xsl:if>
            <xsl:apply-templates select="s:link"/>
            <xsl:apply-templates select="s:recordType"/>
            <xsl:apply-templates select="s:description"/>
        </li>
    </xsl:template>

    <xsl:template match="s:recordType[not(../s:link/s:label[.='catalog record'])]">
        <div class="moreResults">
            <xsl:choose>
                <xsl:when test=". = 'auth'">
                    <span class="sourceLink">Lane Community Info File</span>
                </xsl:when>
                <!-- add catalog link to all bibs except those that already have one (history) -->
                <xsl:when test=". = 'bib'">
                    <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}">Lane Catalog record</a>
                </xsl:when>
                <xsl:when test=". = 'web'">
                    <span class="sourceLink">Lane Web Page</span>
                </xsl:when>
                <xsl:when test=". = 'class'">
                    <span class="sourceLink">Lane Class</span>
                </xsl:when>
                <xsl:when test=". = 'print'">
                    <span class="sourceLink">Print Material</span>
                </xsl:when>
            </xsl:choose>
        </div>
    </xsl:template>

    <xsl:template match="s:recordType"/>

    <xsl:template match="s:description">
        <div class="hvrTarg">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:link[1]">
        <div>
            <a class="primaryLink" href="{s:url}" title="{../s:title}">
                <xsl:apply-templates select="../s:title"/>
            </a>
            <!--<xsl:value-of select="s:additional-text"/>-->
            <xsl:if test="@type = 'getPassword'">
                <a href="/secure/ejpw.html" title="Get Password">Get Password</a>
            </xsl:if>
        </div>
        <xsl:if test="../s:author">
            <div><xsl:value-of select="../s:author"/></div>
        </xsl:if>
        <xsl:if test="s:publisher">
            <div><xsl:value-of select="s:publisher"/></div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <div>
            <a href="{s:url}" title="{s:label}">
                <xsl:value-of select="s:link-text"/>
            </a>
            <xsl:value-of select="s:additional-text"/>
            <xsl:if test="@type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a href="/secure/ejpw.html" title="Get Password">Get Password</a>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="s:link[not(1)][@type = 'impactFactor']">
        <div>
            <a href="{s:url}">Impact Factor</a>
        </div>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div class="pubAuthor">
            <xsl:value-of select="."/>
        </div>
    </xsl:template>

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:variable name="pmid">
            <xsl:value-of select="substring-after(.,'PMID:')"/>
        </xsl:variable>
        <span class="pmid">
            <xsl:text> PMID: </xsl:text>
            <a href="{concat($pubmed-baseUrl,$pmid,'?otool=stanford')}">
                <xsl:value-of select="$pmid"/>
            </a>
        </span>
    </xsl:template>

    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>

    <xsl:template match="s:desc-label">
        <xsl:if test="position() > 1">
            <br/>
        </xsl:if>
        <span class="abstractLabel">
            <xsl:value-of select="."/>
        </span>
        <xsl:text>: </xsl:text>
    </xsl:template>

</xsl:stylesheet>
