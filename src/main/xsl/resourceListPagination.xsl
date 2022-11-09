<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    exclude-result-prefixes="h s" version="2.0">
    
    <xsl:variable name="base-query-string" select="concat('?',replace($query-string,'&amp;page=\d+',''))"/>

    <xsl:template name="paginationLinks">
        <xsl:if test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
           <div class="s-pagination">
               <xsl:call-template name="paginationNumbers"/>
           </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="paginationNumbers">
        <xsl:if test="number(/s:resources/@page) &gt;= 1">
            <a class="previous" href="{$base-query-string}&amp;page={/s:resources/@page}" title="previous"> Previous </a>
        </xsl:if>
        <form class="pagingForm" name="pagination">
          <input type="hidden" name="q" value="{$query}"/>
          <input type="hidden" name="source" value="{$source}"/>
          <xsl:if test="string-length($sort) > 0">
              <input type="hidden" name="sort" value="{$sort}"/>
          </xsl:if>
          <xsl:if test="string-length($facets) > 0">
              <input type="hidden" name="facets" value="{$facets}"/> 
          </xsl:if>
          <label for="page"> Page </label>
          <input type="text" name="page" value="{number(/s:resources/@page + 1)}"/>
          <span id="pageStart" class="hidden"><xsl:value-of select="number(/s:resources/@length * /s:resources/@page +1)"></xsl:value-of></span>
          <label for="pages"> of <xsl:value-of select="format-number(/s:resources/@pages,'###,##0')" /></label>
          <input type="hidden" name="pages" value="{number(/s:resources/@pages)}"/>
        </form>
        <xsl:choose>
            <xsl:when test="number(/s:resources/@pages) &gt; number(/s:resources/@page) + 1">
                <a class="next" href="{$base-query-string}&amp;page={number(/s:resources/@page) + 2}" title="next"> Next </a>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>