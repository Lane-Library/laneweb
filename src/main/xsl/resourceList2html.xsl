<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:r="http://lane.stanford.edu/results/1.0"
    exclude-result-prefixes="h s r" version="2.0">

    <xsl:param name="facets"/>

    <xsl:param name="ipgroup"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="page"/>

    <xsl:param name="query"/>

    <xsl:param name="query-string"/>

    <xsl:param name="sort" />

    <xsl:param name="source"/>
    
    <xsl:param name="url-encoded-query"/>

    <xsl:variable name="guest-mode">
        <xsl:if test="$ipgroup = 'OTHER' and $proxy-links = 'false'">true</xsl:if>
    </xsl:variable>

    <xsl:variable name="pubmed-baseUrl">https://pubmed.ncbi.nlm.nih.gov/</xsl:variable>

    <xsl:include href="resourceListPagination.xsl"/>

    <xsl:include href="resourceListSortBy.xsl"/>

    <xsl:template match="attribute::node() | child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>

   <xsl:template match="/doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

    <xsl:template match="h:ul[@class='lwSearchResults']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:apply-templates select="/doc/r:results/s:result"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="s:resources">
        <html>
            <head>
                <title>search results</title>
            </head>
            <body>
                <div class="no-bookmarking"><div class="pure-g">
                  <div class="pure-u-7-24">
                    <xsl:call-template name="resultsText"/>
                  </div>
                  <div class="pure-u-7-24">
                    <xsl:call-template name="sortBy"/>
                  </div>
                  <div class="pure-u-10-24">
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
                      <div class="pure-u-7-24"/>
                      <div class="pure-u-7-24">
                        <xsl:call-template name="sortBy"/>
                      </div>
                      <div class="pure-u-10-24">
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
                <xsl:apply-templates select="s:pub-text"/>
            </xsl:if>

            <div class="resultInfo">
                <xsl:if test="s:description">
                    <span class="descriptionTrigger searchContent no-bookmarking"/>
                        </xsl:if>
                        <xsl:apply-templates select="s:contentId"/>
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo">
                <span>
                    <xsl:text>Source: </xsl:text>
                    <a href="{s:resourceUrl}">
                        <xsl:value-of select="s:resourceName"/>
                        <xsl:text> - </xsl:text>
                        <xsl:value-of select="format-number(number(s:resourceHits),'###,###,##0')"/>
                        <xsl:text> </xsl:text>
                    </a>
                </span>
            </div>
        </li>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <xsl:variable name="total" select="number(s:total)"/>
        <xsl:variable name="available" select="number(s:available)"/>
       <li class="resource" data-sid="{s:id}">
            <xsl:if test="s:doi[1]">
                <xsl:attribute name="data-doi">
                    <xsl:value-of select="lower-case(s:doi[1])"/>
                </xsl:attribute>
            </xsl:if>
            <span class="primaryType">
                <xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:if test="contains(s:primaryType, 'Book') or contains(s:primaryType, 'Journal')">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}"><i class="fa fa-book"></i></div>
            </xsl:if>
            <xsl:if test="s:primaryType = 'Article'">
                <div class="bookcover"><i class="fa fa-file-text-o fa-flip-horizontal"></i></div>
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
                <xsl:if test="s:recordType = 'bib'">
                    <xsl:apply-templates select="s:link[@type = 'impactFactor']"/>
                </xsl:if>
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo no-bookmarking">
                <span>
                    <xsl:apply-templates select="s:recordType"/>
                </span>
                <xsl:if test="contains(s:primaryType,'Print') and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:if test="s:primaryType = 'Book Print' and $available &gt; 0">
                    <span class="requestIt">
                        <a class="btn alt" title="Request this item" href="https://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}&amp;lw.req=true" rel="popup console 1020 800">Request</a>
                    </span>
                </xsl:if>
                <span class="permalink">
                    <a title="click to copy a shareable link to this record" href="https://lane.stanford.edu/view/{s:recordType}/{s:recordId}">
                    <i class="fa fa-link fa-rotate-90"></i> Get shareable link</a>
                </span>
            </div>
        </li>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:variable name="label"><xsl:text>Source: </xsl:text></xsl:variable>
        <xsl:choose>
            <xsl:when test=". = 'pubmed'">
                <xsl:copy-of select="$label"/><a href="https://pubmed.ncbi.nlm.nih.gov/{../s:recordId}/?otool=Stanford" title="PubMed: PMID {../s:recordId}">PubMed</a>
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
                <xsl:copy-of select="$label"/><a href="https://redivis.com/StanfordPHS" title="Redivis - Stanford Center for Population Health Sciences">Redivis</a>
            </xsl:when>
            <!-- 
                TODO: testing for PMC indexing
                remove if RM/Thea decides not to use PMC records
                may also need to add to eresources2html if RM wants PMC items browseable
            -->
            <xsl:when test=". = 'dnlm'">
                <xsl:copy-of select="$label"/><a href="https://www.ncbi.nlm.nih.gov/nlmcatalog/?term={../s:recordId}" title="NLM Catalog">NLM Catalog</a>
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
                <xsl:otherwise><xsl:apply-templates/></xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:variable name="pmid">
            <xsl:value-of select="substring-after(.,'PMID:')"/>
        </xsl:variable>
        <span><a href="{concat($pubmed-baseUrl,$pmid,'/?otool=stanford')}">PMID: <xsl:value-of select="$pmid"/></a></span>
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
        <div class="citation">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template name="build-link-label">
        <xsl:param name="link" />
        <xsl:param name="primaryType" />
        <xsl:param name="simplePrimaryType" />
        <span>
            <xsl:choose>
                <xsl:when test="starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=') or (contains(s:url,'//searchworks.stanford.edu/view') and ../s:recordType!= 'sul')">Print</xsl:when>
                <xsl:when test="$primaryType = s:label">
                    <a href="{s:url}" title="{s:label}: {../s:title}"><xsl:value-of select="s:label"/></a>
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
</xsl:stylesheet>
