<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:r="http://lane.stanford.edu/results/1.0"
    xmlns:a="aggregate"
    exclude-result-prefixes="h s a r" version="2.0">

    <xsl:param name="facets"/>

    <xsl:param name="ipgroup"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="page"/>

    <xsl:param name="query"/>

    <xsl:param name="query-string"/>

    <xsl:param name="sort" />

    <xsl:param name="source"/>
    
    <!-- used to enable image promotion -->
    <xsl:param name="sourceid"/>

    <xsl:param name="url-encoded-query"/>

    <xsl:variable name="guest-mode">
        <xsl:if test="$ipgroup = 'OTHER' and $proxy-links = 'false'">true</xsl:if>
    </xsl:variable>

    <xsl:variable name="images-url">
        <xsl:value-of select="concat('/search.html?source=images-all&amp;q=',$url-encoded-query)"/>
    </xsl:variable>

    <!--  placement of image search results -->
    <xsl:variable name="images-promo-position">3</xsl:variable>

    <!--  number of images to show in search results -->
    <xsl:variable name="images-promo-show">5</xsl:variable>

    <xsl:variable name="images-promo-enabled" select="'true'"/>

    <xsl:variable name="pubmed-baseUrl">http://www.ncbi.nlm.nih.gov/pubmed/</xsl:variable>

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
                <div class="no-bookmarking"><div class="pure-g">
                  <div class="pure-u-1-3">
                    <xsl:call-template name="resultsText"/>
                  </div>
                  <div class="pure-u-1-3">
                    <xsl:call-template name="sortBy"/>
                  </div>
                  <div class="pure-u-1-3">
                    <xsl:call-template name="paginationLinks"/>
                  </div>
                </div></div>
                <xsl:if test="count(s:result) &gt; 0">
                    <h4 class="eresources">&#160;</h4>
                </xsl:if>
                <ul class="lwSearchResults">
                    <xsl:apply-templates select="s:result"/>
                </ul>
                <xsl:if test="count(s:result) &gt;= 10 and number(@size) &gt;= number(@length)">
                    <br/>
                    <div class="no-bookmarking"><div class="pure-g">
                      <div class="pure-u-1-3"/>
                      <div class="pure-u-1-3">
                        <xsl:call-template name="sortBy"/>
                      </div>
                      <div class="pure-u-1-3">
                        <xsl:call-template name="paginationLinks"/>
                      </div>
                    </div></div>
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

        <li class="resource" data-sid="{s:contentId}">
            <span class="primaryType">Article</span>
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
                <xsl:if test="s:description">
                    <span class="descriptionTrigger searchContent no-bookmarking"/>
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
        <xsl:call-template name="images-promotion">
            <xsl:with-param name="result-position" select="position()"/>
        </xsl:call-template>
        <li class="resource" data-sid="{s:id}">
            <span class="primaryType">
                <xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:if test="contains(s:primaryType, 'Book') or contains(s:primaryType, 'Journal')">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}"><i class="fa fa-book"></i></div>
            </xsl:if>
            <xsl:apply-templates select="s:link[not(starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=') or @type = 'impactFactor') or position() = 1]"/>
            <xsl:apply-templates select="s:pub-text"/>
            <xsl:apply-templates select="s:link[position() > 1 and starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')]"/>
            <div class="resultInfo">
                <xsl:choose>
                    <xsl:when test="s:description and s:recordType = 'pubmed'">
                        <span class="descriptionTrigger searchContent no-bookmarking"/>
                    </xsl:when>
                    <xsl:when test="s:description">
                        <span class="descriptionTrigger eresource no-bookmarking"/>
                    </xsl:when>
                </xsl:choose>
                <xsl:if test="contains(s:primaryType,'Print') and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:if test="s:recordType = 'pubmed'">
                    <span><a href="{concat($pubmed-baseUrl,s:recordId,'?otool=stanford')}">PMID: <xsl:value-of select="s:recordId"/></a></span>
                </xsl:if>
                <xsl:if test="s:recordType = 'bib'">
                    <xsl:apply-templates select="s:link[@type = 'impactFactor']"/>
                </xsl:if>
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo">
                <xsl:apply-templates select="s:recordType"/>
                <span class="permalink no-bookmarking">
                    <a title="permalink to this result" href="/view/{s:recordType}/{s:recordId}"><i class="fa fa-link fa-rotate-90"></i></a>
                </span>
            </div>
        </li>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:variable name="label"><xsl:text>Source: </xsl:text></xsl:variable>
        <xsl:choose>
            <xsl:when test=". = 'pubmed'">
                <xsl:copy-of select="$label"/><a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=search&amp;holding=f1000%2CF1000M&amp;otool=Stanford&amp;term={$url-encoded-query}">PubMed</a>
            </xsl:when>
            <xsl:when test=". = 'sul'">
                <xsl:copy-of select="$label"/><a href="https://searchworks.stanford.edu/view/{../s:recordId}" title="SearchWorks Record">SearchWorks</a>
            </xsl:when>
            <xsl:when test=". = 'bib'">
                <xsl:copy-of select="$label"/><a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}" title="Lane Catalog Record">Lane Catalog</a>
            </xsl:when>
            <xsl:when test=". = 'auth'">
                <xsl:copy-of select="$label"/><a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}" title="Lane Community Info Record">Lane Community Info</a>
            </xsl:when>
            <xsl:when test=". = 'class'">
                <xsl:copy-of select="$label"/><a href="/classes-consult/laneclasses.html">Lane Classes</a>
            </xsl:when>
            <xsl:when test=". = 'web' or . = 'laneblog'">
                <xsl:copy-of select="$label"/><a href="/index.html">Lane Website</a>
            </xsl:when>
            <xsl:when test=". = 'redivis'">
                <xsl:copy-of select="$label"/><a href="https://redivis.com/StanfordPHS">Redivis - Stanford Center for Population Health Sciences</a>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="description">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:desc-link">
        <a href="{s:link}"><xsl:value-of select="s:label"/></a>
    </xsl:template>

    <xsl:template match="s:desc-linebreak">
        <br/>
    </xsl:template>

    <xsl:template match="s:link[1]">
        <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
        <div>
            <a class="primaryLink" href="{s:url}" title="{../s:title}">
                <xsl:apply-templates select="../s:title" />
            </a>
        </div>
        <xsl:apply-templates select="../s:pub-author"/>
        <xsl:if test="(s:link-text and 'null' != s:link-text) or @type = 'getPassword' or s:version-text or s:publisher">
            <div class="resultInfo">
                <xsl:call-template name="build-link-label">
                    <xsl:with-param name="link" select="."/>
                    <xsl:with-param name="primaryType" select="../s:primaryType"/>
                    <xsl:with-param name="simplePrimaryType" select="$simple-primary-type"/>
                </xsl:call-template>
                <xsl:if test="$simple-primary-type != string(s:label) and s:link-text != 'Lane Catalog Record'">
                    <span>
                        <a href="{s:url}" title="{s:label}">
                            <xsl:value-of select="s:link-text" />
                        </a>
                    </span>
                </xsl:if>
                <xsl:if test="@type = 'getPassword'">
                    <span>
                        <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                    </span>
                </xsl:if>
                <xsl:if test="s:version-text">
                    <span class="versionText">
                        <xsl:value-of select="s:version-text" />
                    </span>
                </xsl:if>
                <xsl:if test="s:additional-text">
                    <span>
                        <xsl:value-of select="s:additional-text" />
                    </span>
                </xsl:if>
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
        <div class="resultInfo">
            <xsl:call-template name="build-link-label">
                <xsl:with-param name="link" select="."/>
                <xsl:with-param name="primaryType" select="../s:primaryType"/>
                <xsl:with-param name="simplePrimaryType" select="$simple-primary-type"/>
            </xsl:call-template>
            <xsl:if test="$simple-primary-type != string(s:label)">
                <span>
                    <a href="{s:url}" title="{s:label}">
                        <xsl:value-of select="s:link-text"/>
                    </a>
                </span>
            </xsl:if>
            <xsl:if test="@type = 'getPassword'">
                <span>
                    <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                </span>
            </xsl:if>
            <xsl:if test="s:version-text">
                <span class="versionText">
                    <xsl:value-of select="s:version-text" />
                </span>
            </xsl:if>
            <xsl:if test="s:additional-text">
                <span>
                    <xsl:value-of select="s:additional-text" />
                </span>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="s:link[@type = 'impactFactor' and position() > 1]">
            <span><a href="{s:url}">Impact Factor</a></span>
    </xsl:template>

    <xsl:template match="s:primaryType">
            <xsl:choose>
                <xsl:when test="starts-with(.,'Book') or starts-with(.,'Journal')">
                    <xsl:value-of select="substring-before(., ' ')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <xsl:variable name="max-first-line-length" select="105"/>
        <div>
            <xsl:choose>
                <!--  when there are more than approximately 2 lines of authors (250 chars), include a toggle after the first line (105 chars)-->
                <xsl:when test="contains(substring(., 0, $max-first-line-length), ', ') and string-length(.) > 250">
                    <xsl:variable name="authorTokens" select="tokenize(.,', ')"/>
                    <xsl:variable name="authorString">
                        <xsl:call-template name="split-authors">
                            <xsl:with-param name="tokens" select="$authorTokens"/>
                            <xsl:with-param name="max-string-length" select="$max-first-line-length"/>
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

    <xsl:template match="s:pub-text">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template name="build-link-label">
        <xsl:param name="link" />
        <xsl:param name="primaryType" />
        <xsl:param name="simplePrimaryType" />
        <span>
            <xsl:choose>
                <xsl:when test="starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=') or (contains(s:url,'//searchworks.stanford.edu/view') and ../s:recordType!= 'sul')">Print</xsl:when>
                <xsl:when test="$primaryType = s:label">
                    <a href="{s:url}" title="{s:label}"><xsl:value-of select="s:label"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$simplePrimaryType"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="s:publisher">
                <xsl:text> : </xsl:text>
                <i>
                    <xsl:value-of select="s:publisher" />
                </i>
            </xsl:if>
        </span>
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

    <xsl:template name="images-promotion">
        <xsl:param name="result-position"/>
        <xsl:if test="$images-promo-enabled and $images-promo-position = $result-position and $facets = ''">
            <xsl:variable name="imageUrl" select="concat('cocoon://apps/search/image/preview?q=',$url-encoded-query)"/>
            <xsl:variable name="imageResults" select="document($imageUrl)"/>
            <xsl:if test="count($imageResults//string) >= $images-promo-show">
                <li class="no-bookmarking">
                    <span class="primaryType" style="display:none;">Image Search Promo</span>
                    <a class="primaryLink" href="{$images-url}" title="More images from Lane's Bio-Image Search">Results from Lane's Bio-Image Search</a>
                    <span class="imageSurvey">
                        <span class="surveyLinks">
                            Useful?
                            <a href="#"><i class="fa fa-smile-o fa-lg" aria-hidden="true"></i> Yes</a>
                            <a href="#"><i class="fa fa-frown-o fa-lg" aria-hidden="true"></i> No</a>
                        </span>
                        <span class="surveySent">
                            Thank you for your feedback! 
                            Please send further suggestions to <a href="/help/feedback.html#askus" rel="lightbox"><i class="fa fa-envelope fa-fw"></i>Ask Us</a>.
                        </span>
                    </span>
                    <div id="imageList" class="searchPromo">
                        <div class="pure-g">
                            <xsl:for-each select="$imageResults//string[position() &lt;= $images-promo-show]">
                                <div class="pure-u-1-5">
                                    <a href="{$images-url}" title="More images from Bio-Image Search"><img src="{.}"/></a>
                                </div>
                            </xsl:for-each>
                        </div>
                    </div>
                </li>
            </xsl:if>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
