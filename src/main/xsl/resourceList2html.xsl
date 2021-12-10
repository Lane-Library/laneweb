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
                    <div class="s-tb no-bookmarking"><div class="pure-g">
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
                </xsl:if>
                <ul class="lwSearchResults">
                    <xsl:apply-templates select="s:result"/>
                </ul>
                <xsl:if test="count(s:result) &gt;= 10 and number(@size) &gt;= number(@length)">
                    <div class="s-tb no-bookmarking"><div class="pure-g">
                      <div class="pure-u-14-24"/>
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

    <!-- transforms eresource bib result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <li class="resource" data-sid="{s:id}">
            <xsl:copy-of select="f:maybe-add-doi-attribute(.)"/>
            <span class="primaryType">
                <xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:if test="contains(s:primaryType, 'Book') or contains(s:primaryType, 'Journal')">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}"><i class="fa fa-book"></i></div>
            </xsl:if>
            <xsl:if test="s:primaryType = 'Article'">
                <div class="bookcover"><i class="fa fa-file-text-o fa-flip-horizontal"></i></div>
            </xsl:if>
            <xsl:copy-of select="f:primaryLink(s:link[1])"/>
            <xsl:apply-templates select="s:pub-author"/>
            <xsl:choose>
                <!-- Lane records get different link processing/order -->
                <xsl:when test="s:recordType = 'bib'">
                    <xsl:apply-templates select="s:pub-text"/>
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
        <div>
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

    <xsl:template match="s:locationName">
        <xsl:choose>
            <xsl:when test="../s:locationUrl">
                <a href="{../s:locationUrl}">
                    <!-- exclude rel attribute for non-http(s) links (e.g. mailto:xxx)-->
                    <xsl:if test="starts-with(../s:locationUrl,'http')">
                        <xsl:attribute name="rel">popup console 1020 800</xsl:attribute>
                    </xsl:if>
                    <xsl:apply-templates/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>

    <xsl:function name="f:build-source-info">
        <xsl:param name="eresource" />
        <div class="sourceInfo no-bookmarking">
            <span>
                <xsl:apply-templates select="$eresource/s:recordType"/>
            </span>
            <span class="permalink">
                <a title="click to copy a shareable link to this record" href="https://lane.stanford.edu/view/{$eresource/s:recordType}/{$eresource/s:recordId}">
                <i class="fa fa-link fa-rotate-90"></i> Get shareable link</a>
            </span>
        </div>
    </xsl:function>

    <xsl:function name="f:build-link-label">
        <xsl:param name="link" />
        <xsl:variable name="primaryType" select="$link/../s:primaryType"/>
        <xsl:variable name="simplePrimaryType" select="replace($primaryType,'(Journal|Book) ','')"/>
        <span>
            <xsl:choose>
                <xsl:when test="$link/@type = 'lane-print' or $link/@type = 'sul-print'">Print</xsl:when>
                <xsl:when test="$primaryType = $link/s:label">
                    <a href="{$link/s:url}" title="{$link/s:label}: {$link/../s:title}"><xsl:value-of select="$link/s:label"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$simplePrimaryType"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="$link/s:publisher">
                <xsl:text> : </xsl:text>
                <i>
                    <xsl:value-of select="$link/s:publisher" />
                </i>
            </xsl:if>
        </span>
    </xsl:function>

    <!--  assume authors are a comma-separated string; break the string at a separator before max-string-length -->
    <xsl:function name="f:split-authors">
        <xsl:param name="max-string-length"/>
        <xsl:param name="tokens"/>
        <xsl:param name="index"/>

        <xsl:choose>
            <xsl:when test="string-length(string-join($tokens[position() &lt; $index], ', ')) &gt; $max-string-length">
                <xsl:value-of select="f:split-authors($max-string-length, $tokens, $index - 1)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat(string-join($tokens[position() &lt; $index], ', '), ', ')"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="f:maybe-add-doi-attribute">
        <xsl:param name="eresource"/>
        <xsl:if test="$eresource/s:doi[1]">
            <xsl:attribute name="data-doi">
                <xsl:value-of select="lower-case($eresource/s:doi[1])"/>
            </xsl:attribute>
        </xsl:if>
    </xsl:function>
    
    <xsl:function name="f:primaryLink">
        <xsl:param name="link"/>
        <div>
            <a class="primaryLink" href="{$link/s:url}" title="{$link/../s:title}">
                <xsl:apply-templates select="$link/../s:title" />
            </a>
        </div>
    </xsl:function>

    <!-- used for Lane and SUL digital links -->
    <xsl:function name="f:handleDigitalLinks">
        <xsl:param name="links"/>
        <xsl:if test="count($links) = 1">
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: updated link icon instead? -->
                <span class="hldgsHeader available"><i class="fa fa-link"></i> Digital Access &#160;</span>
                <span>
                    <a href="{$links[1]/s:url}" title="{$links[1]/s:label}">
                        <xsl:value-of select="concat($links[1]/s:publisher, ' ', $links[1]/s:link-text)"/>
                    </a>
                </span>
            </div>
            <xsl:if test="$links[1]/@type = 'lane-getPassword'">
                <span class="getPassword">
                    <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                </span>
            </xsl:if>
            <xsl:if test="$links[1]/s:version-text">
                <span class="versionText">
                    <xsl:value-of select="$links[1]/s:version-text" />
                </span>
            </xsl:if>
            <xsl:if test="$links[1]/s:additional-text">
                <span class="additionalText">
                    <xsl:value-of select="$links[1]/s:additional-text" />
                </span>
            </xsl:if>
        </xsl:if>
        <xsl:if test="count($links) > 1">
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: updated link icon instead? -->
                <span class="hldgsHeader available"><i class="fa fa-link"></i> Digital Access &#160;</span>
                <span class="hldgsTrigger"/>
                <table class="hide-empty-columns">
                    <thead>
                        <tr>
                            <th>Provider</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="$links">
                            <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
                            <tr>
                                <td>
                                    <xsl:if test="not(s:publisher) and s:label">
                                        <xsl:value-of select="s:label"/>
                                    </xsl:if>
                                    <xsl:value-of select="s:publisher"/>
                                </td>
                                <td>
                                    <span>
                                        <a href="{s:url}" title="{s:label}">
                                            <xsl:value-of select="s:link-text"/>
                                            <xsl:text> </xsl:text>
                                        </a>
                                    </span>
                                    <xsl:if test="@type = 'lane-getPassword'">
                                        <span class="getPassword">
                                            <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                                        </span>
                                    </xsl:if>
                                    <xsl:if test="s:version-text">
                                        <span class="versionText">
                                            <xsl:value-of select="s:version-text" />
                                        </span>
                                    </xsl:if>
                                    <xsl:if test="s:additional-text">
                                        <span class="additionalText">
                                            <xsl:value-of select="s:additional-text" />
                                        </span>
                                    </xsl:if>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </xsl:if>
    </xsl:function>

    <xsl:function name="f:handleLanePrintLinks">
        <xsl:param name="links"/>
        <xsl:param name="eresource"/>
        <xsl:if test="count($links) > 0">
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: open book icon instead? -->
                <xsl:choose>
                    <xsl:when test="$eresource/s:available &gt; 0 or 
                                    ($eresource/s:total &gt; 0 and f:specialPrintAvailableLocations($links))">
                        <span class="hldgsHeader available"><i class="fa fa-book"></i> Print Available</span>
                        <span class="hldgsTrigger"/>
                        <xsl:if test="$eresource/s:available &gt; 0">
                            <span class="requestIt">
                                <a class="btn" href="https://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={$eresource/s:recordId}&amp;lw.req=true" rel="popup console 1020 800">Request Print</a>
                            </span>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="$eresource/s:total &gt; 0 and $eresource/s:available = 0">
                        <span class="hldgsHeader"><i class="fa fa-book"></i> Print Unavailable: Checked out</span>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="hldgsHeader"><i class="fa fa-book"></i> Print Unavailable: Status unknown</span>
                    </xsl:otherwise>
                </xsl:choose>
                <table class="hide-empty-columns">
                    <thead>
                        <tr>
                            <th>Location</th>
                            <th>Description</th>
                            <th>Call Number</th>
                            <th>Number of Items</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="$links">
                            <tr>
                                <td>
                                    <xsl:apply-templates select="s:locationName"/>
                                </td>
                                <td>
                                    <a href="{s:url}" title="{s:label}">
                                        <xsl:value-of select="s:link-text"/>
                                    </a>
                                </td>
                                <td>
                                    <xsl:value-of select="s:callnumber"/>
                                </td>
                                <td>
                                    <xsl:value-of select="s:available"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </xsl:if>
    </xsl:function>    

    <xsl:function name="f:descriptionTrigger">
        <xsl:param name="eresource"/>
        <xsl:if test="$eresource/s:description">
            <div class="resultInfo">
                <xsl:choose>
                    <xsl:when test="$eresource/@type = 'searchContent'">
                        <span class="descriptionTrigger searchContent no-bookmarking"/>
                        <xsl:apply-templates select="$eresource/s:contentId"/>
                    </xsl:when>
                    <xsl:when test="$eresource/s:recordType = 'pubmed'">
                        <span class="descriptionTrigger searchContent no-bookmarking"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="descriptionTrigger eresource no-bookmarking"/>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <xsl:apply-templates select="$eresource/s:description"/>
        </xsl:if>
    </xsl:function>

    <!--
    print locations with items that might not circulate should still show as "available" but not have a request button
    NOTE: brittle since relies on location names
     -->
    <xsl:function name="f:specialPrintAvailableLocations" as="xsd:boolean">
        <xsl:param name="links"/>
        <xsl:value-of select="count($links[matches(s:locationName,'(reserve|appointment)','i')]) > 0"/>
    </xsl:function>

</xsl:stylesheet>
