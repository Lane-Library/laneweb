<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:r="http://lane.stanford.edu/results/1.0"
    xmlns:f="https://lane.stanford.edu/functions"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="f h r s xsd" version="2.0">

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

    <xsl:include href="search-browse-common.xsl"/>

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
                <xsl:if test="number(@size) &gt; 0">
                    <div class="s-tb no-bookmarking">
                      <div>
                        <xsl:call-template name="resultsText"/>
                      </div>
                      <div>
                        <xsl:call-template name="sortBy"/>
                      </div>
                      <div>
                        <xsl:call-template name="paginationLinks"/>
                      </div>
                    </div>
                </xsl:if>
                <ul class="lwSearchResults">
                    <xsl:apply-templates select="s:result"/>
                </ul>
                <xsl:if test="count(s:result) &gt;= 10 and number(@size) &gt;= number(@length)">
                    <div class="s-tb no-bookmarking">
                      <div/>
                      <div>
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

    <!-- transforms metasearch result node into displayable -->
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

            <xsl:copy-of select="f:descriptionTrigger(.)"/>

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
        <li class="resource" data-sid="{s:id}">
            <xsl:copy-of select="f:maybe-add-doi-attribute(.)"/>
            <span class="primaryType">
                <xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:if test="contains(s:primaryType, 'Book') or contains(s:primaryType, 'Journal')">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}"><i class="fa-light fa-book"></i></div>
            </xsl:if>
            <xsl:if test="s:primaryType = 'Article'">
                <div class="bookcover"><i class="fa-regular fa-file-lines fa-flip-horizontal"></i></div>
            </xsl:if>
            <xsl:copy-of select="f:primaryLink(s:link[1])"/>
            <xsl:apply-templates select="s:pub-author"/>
            <xsl:choose>
                <!-- Lane records get different link processing/order -->
                <xsl:when test="s:recordType = 'bib'">
                    <!-- don't show citation/pub-text when already present in "access via" link to parent record -->
                    <xsl:if test="s:link/s:locationName != s:pub-text"> 
                        <xsl:apply-templates select="s:pub-text"/>
                    </xsl:if>
                    <xsl:copy-of select="f:descriptionTrigger(.)"/>
                    <xsl:copy-of select="f:handleDigitalLinks(s:link[@type = 'lane-digital' or @type = 'lane-getPassword' or @type = 'lane-impactFactor'])"/>
                    <xsl:copy-of select="f:handleLanePrintLinks(s:link[@type = 'lane-print'], .)"/>
                </xsl:when>
                <xsl:when test="s:recordType = 'sul'">
                    <xsl:apply-templates select="s:pub-text"/>
                    <xsl:copy-of select="f:descriptionTrigger(.)"/>
                    <xsl:copy-of select="f:handleDigitalLinks(s:link[@type = 'normal'])"/>
                    <xsl:apply-templates select="s:link[@type = 'sul-print']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="s:link[position() = 1]"/>
                    <xsl:apply-templates select="s:pub-text"/>
                    <xsl:apply-templates select="s:link[position() > 1]"/>
                    <xsl:copy-of select="f:descriptionTrigger(.)"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:copy-of select="f:build-source-info(.)"/>
        </li>
    </xsl:template>

    <xsl:template match="s:desc-link">
        <a href="{s:link}"><xsl:value-of select="s:label"/></a>
    </xsl:template>

    <!-- confusing! Lane records (recordType = 'bib') don't use this; Lane records use handleXxxXxxLinks functions -->
    <!-- sul records  use this for 'sul-print' type links only -->
    <!-- it is also used for pubmed, web, etc. records -->
    <xsl:template match="s:link">
        <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
        <xsl:if test="(position() > 1 or (s:link-text and 'null' != s:link-text) or s:version-text or s:publisher)">
            <div class="resultInfo">
                <xsl:copy-of select="f:build-link-label(.)"/>
                <xsl:if test="$simple-primary-type != string(s:label)">
                    <span>
                        <a href="{s:url}" title="{s:label}">
                            <xsl:value-of select="s:link-text" />
                        </a>
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
        <div class="author">
            <xsl:choose>
                <!--  when there are more than approximately 2 lines of authors (250 chars), include a toggle after the first line (105 chars)-->
                <xsl:when test="contains(substring(., 0, $max-first-line-length), ', ') and string-length(.) > 250">
                    <xsl:variable name="authorTokens" select="tokenize(.,', ')"/>
                    <xsl:variable name="authorString">
                        <xsl:value-of select="f:split-authors($max-first-line-length, $authorTokens, 12)"/>
                    </xsl:variable>
                    <xsl:value-of select="$authorString"/>
                    <span> ... </span>
                    <span class="authorsTrigger no-bookmarking active">
                        <a href="#"> Show More </a>
                        <i class="fa-regular fa-angles-down fa-xs"></i>
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

    <xsl:template match="s:desc-label">
        <xsl:if test="position() > 1">
            <br/>
        </xsl:if>
        <strong>
            <xsl:value-of select="."/>
        </strong>
        <xsl:text>: </xsl:text>
    </xsl:template>

</xsl:stylesheet>
