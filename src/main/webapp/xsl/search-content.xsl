<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="facet"/>
    
    <xsl:variable name="active-facet">
        <xsl:choose>
            <xsl:when test="$facet"><xsl:value-of select="$facet"/></xsl:when>
            <xsl:otherwise>foundational</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="search-terms" select="/doc/s:search/s:query"/>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    <xsl:template match="h:style">
        <xsl:copy>
        <xsl:apply-templates select="attribute::node|child::node()"/>
        dt.<xsl:value-of select="$active-facet"/>{display:block}
        dd.<xsl:value-of select="$active-facet"/>{display:block}
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:div[@id='leftColumn']//h:a/@href">
        <xsl:choose>
            <xsl:when test="$search-terms">
                <xsl:attribute name="href">
                    <xsl:value-of select="concat(.,'&amp;source=clinical&amp;q=',$search-terms)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:li[starts-with(@id,$active-facet)]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:attribute name="class">activeFacet</xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:h1">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$search-terms">
                    <span>
                    <xsl:text>Search Results for "</xsl:text>
                    <span><xsl:value-of select="$search-terms"/></span>
                    <xsl:text>"</xsl:text>
                    </span>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:dt">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
            <xsl:if test="$search-terms">
                    <xsl:text> (</xsl:text>
                    <xsl:value-of select="format-number(/doc/s:search/s:engine/s:resource[@id=current()/@id]/s:hits,'###,###,###')"/>
                    <xsl:text>)</xsl:text> 
            </xsl:if>
        </xsl:copy>
        <xsl:choose>
            <xsl:when test="$search-terms">
                <xsl:apply-templates select="following-sibling::h:dd[1]" mode="results"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="following-sibling::h:dd[1]" mode="description"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:dd"/>
    
    <xsl:template match="h:dd" mode="description">
        <xsl:copy>
            <xsl:copy-of select="preceding-sibling::h:dt[1]/@class"/>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:dd" mode="results">
        <xsl:variable name="id" select="preceding-sibling::h:dt[1]/@id"/>
        <xsl:variable name="results" select="/doc/s:search/s:engine[@id=$id]/s:resource/s:content"/>
        <xsl:copy>
            <xsl:copy-of select="preceding-sibling::h:dt[1]/@class"/>
            <xsl:choose>
                <xsl:when test="count($results) &gt; 0">
                    <ul>
                        <xsl:apply-templates select="$results[position() &lt;= 2]"/>
                    </ul>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="s:content">
        <li><a href="{s:url}"><xsl:value-of select="s:title"/></a><xsl:text> </xsl:text><xsl:value-of select="s:description"/></li>
    </xsl:template>

</xsl:stylesheet>