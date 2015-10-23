<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s" version="2.0">

    <xsl:param name="page"/>

    <xsl:template name="paginationLinks">

        <xsl:variable name="no-page-query-string" select="concat('source=', $source, '&amp;q=', $query, '&amp;')"/>

        <div class="resourceListPagination">
            <div class="yui3-g">
                <div class="yui3-u-5-12">
                    <xsl:choose>
                        <xsl:when test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
                            <span id="pageStart">
                                <xsl:value-of select="number(/s:resources/@start) + 1"/>
                            </span>
                            <xsl:text> to </xsl:text>
                            <xsl:value-of select="number(/s:resources/@start) + number(/s:resources/@length)"/>
                            <xsl:text> of </xsl:text>
                            <a href="?{$no-page-query-string}page=all" class="no-bookmarking">
                                <xsl:value-of select="/s:resources/@size"/>
                                <xsl:text> results</xsl:text>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text> all </xsl:text>
                            <xsl:value-of select="/s:resources/@size"/>
                            <xsl:text> results</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                    <span id="searchInfo">&#160;<i class="fa fa-info-circle fa-lg"></i></span>
                    <span class="tooltips"><span id="searchInfoTooltip">Lane Search displays the top results from multiple sources then presents them in relevance rank order.</span></span>
                </div>
                <xsl:if test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
                    <div class="yui3-u-1-3">
                        <a href="?{$no-page-query-string}page=all">See All </a>
                        <i class="fa fa-arrow-right"/>

                    </div>
                    <div class="yui3-u-1-4">
                        <div style="float:right">
                            <xsl:text>View by page </xsl:text>
                            <xsl:call-template name="paginationNumbers">
                                <xsl:with-param name="page" select="number(0)"/>
                                <xsl:with-param name="query-string" select="$no-page-query-string"/>
                            </xsl:call-template>
                        </div>
                    </div>
                </xsl:if>
            </div>
        </div>
    </xsl:template>

    <xsl:template name="paginationNumbers">
        <xsl:param name="page"/>
        <xsl:param name="query-string"/>
        <xsl:choose>
            <xsl:when test="number($page) = number(/s:resources/@page)">
                <xsl:value-of select="number($page) + 1"/>
            </xsl:when>
            <xsl:otherwise>
                <a href="?{$query-string}page={number($page) + 1}">
                    <xsl:value-of select="number($page) + 1"/>
                </a>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="number($page + 1) != number(/s:resources/@pages)">
            <xsl:text> | </xsl:text>
            <xsl:call-template name="paginationNumbers">
                <xsl:with-param name="page" select="number($page + 1)"/>
                <xsl:with-param name="query-string" select="$query-string"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
