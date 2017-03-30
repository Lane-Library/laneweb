<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:r="http://lane.stanford.edu/results/1.0"
    xmlns:a="aggregate"
    exclude-result-prefixes="h s a r" version="2.0">

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="ipgroup"/>

    <xsl:param name="query"/>

    <xsl:param name="url-encoded-query"/>

    <xsl:variable name="guest-mode">
        <xsl:if test="$ipgroup = 'OTHER' and $proxy-links = 'false'">true</xsl:if>
    </xsl:variable>

    <xsl:variable name="pubmed-baseUrl">http://www.ncbi.nlm.nih.gov/pubmed/</xsl:variable>

    <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
    <xsl:variable name="moreResultsLimit">10</xsl:variable>

    <xsl:include href="resourceListPagination.xsl"/>

    <xsl:include href="resourceListSortBy.xsl"/>

    <xsl:template match="attribute::node() | child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>

   <xsl:template match="a:doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

    <xsl:template match="h:ul[@class='lwSearchResults']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:apply-templates select="/a:doc/r:results/s:result"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="s:resources">
        <html>
            <head>
                <title>search results</title>
            </head>
            <body>
                <div class="yui3-g no-bookmarking">
                  <div class="yui3-u-1-3">
                    <xsl:call-template name="resultsText"/>
                  </div>
                  <div class="yui3-u-1-3">
                    <xsl:call-template name="sortBy"/>
                  </div>
                  <div class="yui3-u-1-3">
                    <xsl:call-template name="paginationLinks"/>
                  </div>
                </div>
                <xsl:if test="count(s:result) &gt; 0">
                    <h3 class="eresources">&#160;</h3>
                </xsl:if>
                <ul class="lwSearchResults">
                    <xsl:apply-templates select="s:result"/>
                </ul>
                <xsl:if test="count(s:result) &gt;= 10 and number(@size) &gt;= number(@length)">
                    <div class="yui3-g no-bookmarking">
                      <div class="yui3-u-1-3"/>
                      <div class="yui3-u-1-3">
                        <xsl:call-template name="sortBy"/>
                      </div>
                      <div class="yui3-u-1-3">
                        <xsl:call-template name="paginationLinks"/>
                      </div>
                    </div>
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
            <div>
                <a class="primaryLink" href="{$primaryLink}">
                    <xsl:apply-templates select="s:title"/>
                </a>
            </div>

            <!-- display authors if NOT clinical or peds interface -->
            <xsl:if test="not(starts-with($source,'clinical') or starts-with($source,'peds')) and string-length(s:pub-author) > 1">
                <xsl:apply-templates select="s:pub-author"/>
            </xsl:if>

            <xsl:if test="s:pub-text">
                <div class="citation">
                        <xsl:value-of select="s:pub-text"/>
                </div>
            </xsl:if>

            <div class="resultInfo">
                <span class="primaryType"><strong>Article</strong> Digital</span>
                <xsl:if test="s:description">
                    <span class="descriptionTrigger searchContent"/>
                        </xsl:if>
                        <xsl:apply-templates select="s:contentId"/>
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo">
                <xsl:text>Source: </xsl:text>
                <a href="{s:resourceUrl}">
                    <xsl:value-of select="s:resourceName"/>
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="format-number(number(s:resourceHits),'###,###,##0')"/>
                    <xsl:text> </xsl:text>
                </a>
            </div>
        </li>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <xsl:variable name="total" select="number(s:total)"/>
        <xsl:variable name="available" select="number(s:available)"/>
        <li>
            <xsl:if test="contains(s:primaryType, 'Book')">
                <img class="bookcover" data-bibid="{s:recordId}"/>
            </xsl:if>
            <xsl:apply-templates select="s:link[not(starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')) or position() = 1]"/>
            <xsl:apply-templates select="s:pub-author"/>
            <xsl:apply-templates select="s:pub-text"/>
            <div class="resultInfo">
                <span class="primaryType">
                    <xsl:apply-templates select="s:primaryType"/>
                </span>
                <xsl:if test="contains(s:primaryType,'Print') and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="s:description and s:recordType = 'pubmed'">
                        <span class="descriptionTrigger searchContent"/>
                    </xsl:when>
                    <xsl:when test="s:description">
                        <span class="descriptionTrigger eresource"/>
                    </xsl:when>
                </xsl:choose>

                <xsl:if test="s:recordType = 'pubmed'">
	                <span><a href="{concat($pubmed-baseUrl,s:recordId,'?otool=stanford')}">PMID: <xsl:value-of select="s:recordId"/></a></span>
                </xsl:if>

                <xsl:if test="s:recordType = 'bib'">
                    <span>
                        <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog Record</a>
                    </span>
                </xsl:if>

                <xsl:if test="s:recordType = 'auth'">
                    <span>
                        <a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Community Info Record</a>
                    </span>
                </xsl:if>

            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo">
                <xsl:apply-templates select="s:recordType"/>
            </div>
            <xsl:apply-templates select="s:link[position() > 1 and starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')]"/>
        </li>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:variable name="label"><xsl:text>Source: </xsl:text></xsl:variable>
        <xsl:choose>
            <xsl:when test=". = 'pubmed'">
                <xsl:copy-of select="$label"/><a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=search&amp;holding=f1000%2CF1000M&amp;otool=Stanford&amp;term={$url-encoded-query}">PubMed</a>
            </xsl:when>
            <xsl:when test=". = 'auth'">
                <xsl:copy-of select="$label"/><a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}">Lane Community Info</a>
            </xsl:when>
            <xsl:when test=". = 'class'">
                <xsl:copy-of select="$label"/><a href="/classes-consult/laneclasses.html">Lane Classes</a>
            </xsl:when>
            <xsl:when test=". = 'web' or . = 'laneblog'">
                <xsl:copy-of select="$label"/><a href="/index.html">Lane Website</a>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="description">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:link[1]">
        <div>
            <a class="primaryLink" href="{s:url}" title="{../s:title}">
                <xsl:apply-templates select="../s:title" />
            </a>
        </div>
        <xsl:if test="s:holdings-dates">
            <a href="{s:url}" title="{../s:title}">
                <xsl:value-of select="s:holdings-dates" />
            </a>
        </xsl:if>
        <xsl:if test="@type = 'getPassword'">
            <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
        </xsl:if>
        <xsl:if test="s:additional-text">
            <xsl:text> </xsl:text>
            <xsl:value-of select="s:additional-text" />
        </xsl:if>
        <xsl:if test="../s:author">
            <div>
                <xsl:value-of select="../s:author" />
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <xsl:variable name="print" select="starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')"/>
        <div>
            <xsl:if test="$print">Also available: </xsl:if>
            <a href="{s:url}" title="{s:label}">
                <xsl:if test="$print">Print &#8211; </xsl:if>
                <xsl:value-of select="s:link-text"/>
            </a>
            <xsl:if test="s:additional-text">
                <xsl:text> </xsl:text>
                <xsl:value-of select="s:additional-text"/>
            </xsl:if>
            <xsl:if test="@type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a href="/secure/ejpw.html" title="Get Password">Get Password</a>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="s:link[@type = 'impactFactor']">
        <div>
            <a href="{s:url}">Impact Factor</a>
        </div>
    </xsl:template>

    <xsl:template match="s:primaryType">
            <xsl:choose>
                <xsl:when test="starts-with(.,'Book') or starts-with(.,'Journal')">
                    <strong><xsl:value-of select="substring-before(., ' ')"/></strong>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="substring-after(., ' ')"/>
                </xsl:when>
                <xsl:otherwise>
                    <strong><xsl:value-of select="."/></strong>
                </xsl:otherwise>
            </xsl:choose>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div>
            <xsl:choose>
                <!--  when there are more than approximately 2 lines of authors (250 chars), include a toggle after the first line (115 chars)-->
                <xsl:when test="string-length(.) > 250">
                    <xsl:variable name="authorTokens" select="tokenize(.,', ')"/>
                    <xsl:variable name="authorString">
                        <xsl:call-template name="split-authors">
                            <xsl:with-param name="tokens" select="$authorTokens"/>
                            <xsl:with-param name="max-string-length" select="105"/>
                            <xsl:with-param name="index" select="12"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="$authorString"/>
                    <span> ... </span>
                    <span class="authorsTrigger no-bookmarking active">
                        <a href="#"> Show More </a>
                        <i class="fa fa-angle-double-down"></i>
                    </span>
                    <span class="authors-hide"><xsl:value-of select="substring-after(.,$authorString)"/></span>
                </xsl:when>
                <xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:variable name="pmid">
            <xsl:value-of select="substring-after(.,'PMID:')"/>
        </xsl:variable>
        <span><a href="{concat($pubmed-baseUrl,$pmid,'?otool=stanford')}">PMID: <xsl:value-of select="$pmid"/></a></span>
    </xsl:template>

    <xsl:template match="s:contentId"/>

    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>

    <xsl:template match="s:desc-label">
        <xsl:if test="position() > 1">
            <br/>
        </xsl:if>
        <strong>
            <xsl:value-of select="."/>
        </strong>
        <xsl:text>: </xsl:text>
    </xsl:template>

    <xsl:template match="s:title">
        <xsl:apply-templates/>
    </xsl:template>

    <!--  assume authors are a comma-separated string; break the string at a separator before max-string-length -->
    <xsl:template name="split-authors">
        <xsl:param name="max-string-length"/>
        <xsl:param name="tokens"/>
        <xsl:param name="index"/>

        <xsl:choose>
            <xsl:when test="string-length(string-join($tokens[position() &lt; $index], ', ')) &gt; $max-string-length">
                <xsl:call-template name="split-authors">
                  <xsl:with-param name="tokens" select="$tokens"/>
                  <xsl:with-param name="max-string-length" select="$max-string-length"/>
                  <xsl:with-param name="index" select="$index - 1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat(string-join($tokens[position() &lt; $index], ', '), ', ')"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
