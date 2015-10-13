<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s" version="2.0">

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

    <xsl:template match="/s:resources">
        <html>
            <head>
                <title>search results</title>
            </head>
            <body>
                <xsl:call-template name="paginationLinks"/>
                <h3 class="eresources">&#160;</h3>
                <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                    href="cocoon://content/search/lane-beta-link.html">
                    <xi:fallback></xi:fallback>
                </xi:include>
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
                <span><strong>Article</strong> Digital</span>
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
                    <i class="fa fa-external-link"></i></a>
            </div>
        </li>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <xsl:variable name="total" select="number(s:total)"/>
        <xsl:variable name="available" select="number(s:available)"/>
        <li>
            <xsl:apply-templates select="s:link"/>
            <div class="resultInfo">
                <span>
                    <xsl:apply-templates select="s:primaryType"/>
                </span>
                <xsl:if test="s:recordType = 'print' and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:if test="s:description">
                    <span class="descriptionTrigger eresource"/>
                </xsl:if>
                
                    <xsl:if test="s:recordType = 'bib'">
                        <span>
                            <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog Record</a>
                        </span>
                    </xsl:if>
                
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo">
                <xsl:apply-templates select="s:recordType"/>
            </div>
            <xsl:if test="s:recordType != 'print' and $total &gt; 0">
                <div>Also available: <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Print</a></div>
            </xsl:if>
        </li>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:text>Source: </xsl:text>
        <xsl:choose>
            <xsl:when test=". = 'auth'">
                <a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&amp;Search_Arg={$url-encoded-query}&amp;SL=None&amp;Search_Code=FT*&amp;CNT=50">Lane Community Info Results <i class="fa fa-external-link"></i></a>
            </xsl:when>
            <xsl:when test=". = 'bib'">
                <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&amp;Search_Arg={$url-encoded-query}&amp;SL=None&amp;Search_Code=FT*&amp;CNT=50">Lane Catalog Results <i class="fa fa-external-link"></i></a>
            </xsl:when>
            <xsl:when test=". = 'class'">
                <a href="/classes-consult/laneclasses.html">Lane Classes</a>
            </xsl:when>
            <xsl:when test=". = 'print'">
                <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&amp;Search_Arg={$url-encoded-query}&amp;SL=None&amp;Search_Code=FT*&amp;CNT=50">Lane Catalog Results <i class="fa fa-external-link"></i></a>
            </xsl:when>
            <xsl:when test=". = 'web' or . = 'laneblog'">
                <a href="/index.html">Lane Website</a>
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
                <xsl:apply-templates select="../s:title"/>
            </a>
            <xsl:if test="s:holdings-dates">
                <xsl:text> </xsl:text>
                <xsl:value-of select="s:holdings-dates"/>
            </xsl:if>
            <xsl:if test="@type = 'getPassword'">
                <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
            </xsl:if>
        </div>
        <xsl:if test="../s:author">
            <div><xsl:value-of select="../s:author"/></div>
        </xsl:if>
        <xsl:if test="s:additional-text">
            <div><xsl:value-of select="s:additional-text"/></div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <div>
            Also available: <a href="{s:url}" title="{s:label}">
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
            <xsl:value-of select="."/>
        </div>
    </xsl:template>

    <xsl:template match="s:contentId[starts-with(.,'PMID:')]">
        <xsl:variable name="pmid">
            <xsl:value-of select="substring-after(.,'PMID:')"/>
        </xsl:variable>
        <span><a href="{concat($pubmed-baseUrl,$pmid,'?otool=stanford')}">PMID: <xsl:value-of select="$pmid"/><xsl:text> </xsl:text><i class="fa fa-external-link"/></a></span>
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

</xsl:stylesheet>
