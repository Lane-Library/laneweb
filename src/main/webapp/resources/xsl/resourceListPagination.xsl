<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0" exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:template name="paginationLinks">
        <xsl:variable name="no-page-query-string">
            <xsl:choose>
                <xsl:when test="$query and $source">
                    <xsl:value-of select="concat('source=', $source, '&amp;q=', $query, '&amp;')"/>
                </xsl:when>
                <xsl:when test="$alpha">
                    <xsl:value-of select="concat('a=', $alpha, '&amp;')"/>
                </xsl:when>
                <xsl:when test="$mesh">
                    <xsl:value-of select="concat('m=', $mesh, '&amp;')"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <div class="results-nav">
            <div class="yui-g">
                <div class="yui-u first">
                    <xsl:text>Displaying </xsl:text>
                    <xsl:choose>
                        <xsl:when test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
                            <xsl:value-of select="number(/s:resources/@start) + 1"/>
                            <xsl:text> to </xsl:text>
                            <xsl:value-of select="number(/s:resources/@start) + number(/s:resources/@length)"/>
                            <xsl:text> of </xsl:text>
                            <a href="?{$no-page-query-string}page=all"><xsl:value-of select="/s:resources/@size"/> matches</a>
                        </xsl:when>
                        <xsl:otherwise>all <xsl:value-of select="/s:resources/@size"/> matches</xsl:otherwise>
                    </xsl:choose>
                </div>
                <div class="yui-u" style="text-align:right">
                    <xsl:if test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
                        <a id="seeAll" href="?{$no-page-query-string}page=all">See All</a>
                        <xsl:call-template name="pageLinks">
                            <xsl:with-param name="page" select="number(0)"/>
                            <xsl:with-param name="query-string" select="$no-page-query-string"/>
                        </xsl:call-template>
                    </xsl:if>
                </div>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template name="pageLinks">
        <xsl:param name="page"/>
        <xsl:param name="query-string"/>
        <xsl:choose>
            <xsl:when test="number($page) = number(/s:resources/@page)">
                <xsl:value-of select="number($page) + 1"/>
            </xsl:when>
            <xsl:otherwise>
                <a href="?{$query-string}page={number($page) + 1}"><xsl:value-of select="number($page) + 1"/></a>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="number($page + 1) != number(/s:resources/@pages)">
            <xsl:text> | </xsl:text>
            <xsl:call-template name="pageLinks">
                <xsl:with-param name="page" select="number($page + 1)"/>
                <xsl:with-param name="query-string" select="$query-string"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>