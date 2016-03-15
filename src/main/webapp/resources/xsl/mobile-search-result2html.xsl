<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="request-uri"/>
    
    <xsl:param name="query-string"/>

    <xsl:param name="rid"/>

    <xsl:variable name="consumable-query-string">
        <xsl:value-of select="replace($query-string,'&amp;page=\w+','')"/>
    </xsl:variable>
    
    <xsl:variable name="base-link">
        <xsl:value-of select="concat($request-uri,'?',$consumable-query-string)"/>
    </xsl:variable>
    
    <xsl:variable name="search-terms">
        <xsl:value-of select="/s:resources/s:query"/>
    </xsl:variable>

    <xsl:variable name="searchType">
        <xsl:choose>
            <xsl:when test="contains($request-uri,'m/search/lane')">lane</xsl:when>
            <xsl:when test="contains($request-uri,'m/search/clinical')">clinical</xsl:when>
            <xsl:when test="contains($request-uri,'m/search/ped')">ped</xsl:when>
            <xsl:when test="contains($request-uri,'m/search/er/type/book')">book</xsl:when>
            <xsl:when test="contains($request-uri,'m/search/er/type/ej')">journal</xsl:when>
            <xsl:otherwise>lane</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="resultsString">
        <xsl:choose>
           <xsl:when test="number(/s:resources/@size) = 1">result</xsl:when>
           <xsl:otherwise>results</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <!-- number of result titles to return per resource; not enforced here, only used for when to build "more" links -->
    <xsl:variable name="moreResultsLimit">10</xsl:variable>

    <xsl:template match="/s:resources">

        <xsl:choose>
            <!-- abstract/info view of a single record -->
            <xsl:when test="contains($query-string,'rid=')">
                <xsl:apply-templates select="s:result[s:id = $rid]" mode="full"/>
            </xsl:when>
            <xsl:otherwise>
                <ul class="results">
                    <li class="rHead">
                        <xsl:value-of select="format-number(@size, '###,##0')"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$resultsString"/>
                        <xsl:if test="string-length($search-terms)">
                            for <strong>  <xsl:value-of select="s:query"/></strong>
                        </xsl:if>
                    </li>
                    <xsl:apply-templates select="s:result" mode="brief"/>
                    <xsl:call-template name="paginationLinks"/>
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:result[@type='searchContent']" mode="brief">
        <xsl:variable name="resourceName">
            <xsl:choose>
                <xsl:when test="starts-with(s:resourceName,'PubMed')">PubMed</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="s:resourceName"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <li rank="{position()}">
            <a target="_blank" href="{s:url}">
                <xsl:apply-templates select="s:title"/>
            </a>
            <xsl:value-of select="s:pub-text"/>
            <div class="sourceLink">
                <xsl:value-of select="$resourceName"/>
            </div>
            <xsl:choose>
                <xsl:when test="s:description and contains(s:resourceId,'pubmed')">
                    <a href="{concat($base-link,'&amp;rid=',s:id,'&amp;page=',number(/s:resources/@page)+1)}" class="more">abstract &#xBB;</a>
                </xsl:when>
                <xsl:when test="s:description">
                    <a href="{concat($base-link,'&amp;rid=',s:id,'&amp;page=',number(/s:resources/@page)+1)}" class="more">more info &#xBB;</a>
                </xsl:when>
            </xsl:choose>
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
        <div class="absInfo">
            <a target="_blank" href="{s:url}">
                <xsl:apply-templates select="s:title"/>
            </a>
            <xsl:apply-templates select="s:pub-author[string-length(.) > 1]"/>
            <div class="pubInfo">
                <xsl:choose>
                    <xsl:when test="s:pub-text">
                        <xsl:value-of select="s:pub-text"/>
                        <span class="sourceLink">
                            <xsl:text> - </xsl:text>
                            <xsl:value-of select="$resourceName"/>
                        </span>
                        <xsl:apply-templates select="s:contentId"/>
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
    </xsl:template>

    <xsl:template match="s:result[@type='eresource']" mode="brief">
        <li rank="{position()}">
            <xsl:apply-templates select="s:link[1]"/>
            <xsl:apply-templates select="s:recordType"/>
            <xsl:value-of select="s:pub-text"/>
            <xsl:apply-templates select="s:primaryType"/>
            <xsl:choose>
                <xsl:when test="s:description and contains(s:link[1],'pubmed')">
                    <a href="{concat($base-link,'&amp;rid=',s:id,'&amp;page=',number(/s:resources/@page)+1)}" class="more">abstract &#xBB;</a>
                </xsl:when>
                <xsl:when test="s:description or count(s:link) > 1">
                    <a href="{concat($base-link,'&amp;rid=',s:id,'&amp;page=',number(/s:resources/@page)+1)}" class="more">more info &#xBB;</a>
                </xsl:when>
            </xsl:choose>
        </li>
    </xsl:template>
    
    <xsl:template match="s:result[@type='eresource']" mode="full">
        <div class="absInfo">
            <xsl:apply-templates select="s:link"/>
            <xsl:apply-templates select="s:recordType">
                <xsl:with-param name="mode">full</xsl:with-param>
            </xsl:apply-templates>
            <xsl:apply-templates select="s:pub-author[string-length(.) > 1]"/>
            <xsl:if test="s:pub-text">
                <div><xsl:value-of select="s:pub-text"/></div>
            </xsl:if>
            <xsl:apply-templates select="s:primaryType"/>
            <xsl:apply-templates select="s:description"/>
        </div>  
    </xsl:template>
    
    <xsl:template match="s:link[1]">
        <a target="_blank" class="newWindow" href="{s:url}">
            <xsl:apply-templates select="../s:title"/>
        </a>
        <xsl:value-of select="s:additional-text"/>
        <xsl:if test="@type = 'getPassword'">
            <a target="_blank" href="/secure/ejpw.html" title="Get Password" data-ajax="false">Get Password</a>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="s:link">
        <div>
            <a target="_blank" href="{s:url}" title="{s:label}">
                <xsl:value-of select="s:link-text"/>
            </a>
            <xsl:value-of select="s:additional-text"/>
            <xsl:if test="@type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a target="_blank" href="/secure/ejpw.html" title="Get Password" data-ajax="false">Get Password</a>
            </xsl:if>
        </div>
    </xsl:template>
    
    <xsl:template match="s:link[not(1)][@type='impactFactor']">
        <div>
            <a target="_blank" href="{s:url}">Impact Factor</a>
        </div>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="rDesc">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div class="pubAuthor">
            <xsl:value-of select="."/>
        </div>
    </xsl:template>

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:text> - </xsl:text>
        <span class="pmid">
            <xsl:text> PMID: </xsl:text>
            <a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/{substring-after(.,'PMID:')}?otool=stanford"><xsl:value-of select="substring-after(.,'PMID:')"/></a>
        </span>
    </xsl:template>
    
    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:param name="mode"/>
        <xsl:choose>
            <xsl:when test=". = 'auth'">
                <div class="moreResults">
                    <span class="sourceLink">Lane Community Info File</span>
                </div>
            </xsl:when>
            <!-- add catalog link to all bibs except those that already have one (history) -->
            <xsl:when test="$mode = 'full' and . = 'bib' and not(../s:link/s:label[.='catalog record'])">
                <div class="moreResults">
                    <a target="_blank" href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}">Lane Catalog Record</a>
                </div>
            </xsl:when>
            <xsl:when test=". = 'web'">
                <div class="moreResults">
                    <span class="sourceLink">Lane Web Page</span>
                </div>
            </xsl:when>
            <xsl:when test=". = 'print'">
                <div class="moreResults">
                    <span class="sourceLink">Print Material</span>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:desc-label">
        <xsl:if test="position() > 1">
            <div />
        </xsl:if>
        <strong>
            <xsl:value-of select="."/>
        </strong>
        <xsl:text>: </xsl:text>
    </xsl:template>

    <xsl:template match="s:primaryType">
        <div>
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
        </div>
    </xsl:template>

    <!-- add Next toggle to search results -->
    <xsl:template name="paginationLinks">
        <xsl:if test="number(/s:resources/@pages) &gt; number(/s:resources/@page) + 1">
            <li class="more resultsNav">
                <a href="{concat($base-link,'&amp;page=',number(/s:resources/@page) + 2)}">next &#xBB;</a>
            </li>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
