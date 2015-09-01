<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:s="http://lane.stanford.edu/resources/1.0"
	exclude-result-prefixes="h s" version="2.0">
	
	<xsl:param name="page"/>
	
	<xsl:param name="facets"/>

    <xsl:variable name="base-query-string" select="concat('?',replace($query-string,'&amp;page=\d+',''))"/>

    <xsl:template name="resultsText">
        <xsl:variable name="end">
          <xsl:choose>
            <xsl:when test="(number(/s:resources/@start) + number(/s:resources/@length)) &gt; number(/s:resources/@size)">
                <xsl:value-of select="number(/s:resources/@size)" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="number(/s:resources/@start) + number(/s:resources/@length)" />
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        
		<xsl:choose>
		    <xsl:when test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
	            <span id="pageStart"><xsl:value-of select="format-number(number(/s:resources/@start) + 1,'###,##0')" /></span>
	            <xsl:text> to </xsl:text>
	            <xsl:value-of
	                select="format-number($end,'###,##0')" />
	            <xsl:text> of </xsl:text>
	            <xsl:value-of select="format-number(/s:resources/@size, '###,##0')" />
	            <xsl:text> results</xsl:text>
		    </xsl:when>
		    <xsl:otherwise>
		        <xsl:if test="number(/s:resources/@size) = 1">
		            <xsl:text> one result </xsl:text>
		        </xsl:if>
		        <xsl:if test="number(/s:resources/@size) > 1">
		            <xsl:text> all </xsl:text>
		            <xsl:value-of select="/s:resources/@size" />
		            <xsl:text> results</xsl:text>
		        </xsl:if>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
    <xsl:template name="paginationLinks">
		<xsl:if test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
		   <div class="s-pagination">
		       <xsl:call-template name="paginationNumbers"/>
		   </div>
		</xsl:if>
    </xsl:template>
    
	<xsl:template name="paginationNumbers">
		<xsl:choose>
			<xsl:when test="number(/s:resources/@page) &gt;= 1">
	            <a class="pagingButton" href="{$base-query-string}&amp;page=1" title="first"> <i class="fa fa-fast-backward"></i> </a>
	            <a class="pagingButton previous" href="{$base-query-string}&amp;page={/s:resources/@page}" title="previous"> <i class="fa fa-backward"></i> </a>
			</xsl:when>
			<xsl:otherwise>
                <span class="pagingButton disabled"> <i class="fa fa-fast-backward"></i> </span>
                <span class="pagingButton disabled"> <i class="fa fa-backward"></i> </span>
			</xsl:otherwise>
		</xsl:choose>
		<form class="pagingForm" name="pagination">
		  <xsl:if test="string-length($facets) > 0">
			  <input type="hidden" name="facets" value="{$facets}"/>
		  </xsl:if>
		  <xsl:if test="string-length($sort) > 0">
			  <input type="hidden" name="sort" value="{$sort}"/>
		  </xsl:if>
		  <input type="hidden" name="source" value="{$source}"/>
		  <input type="hidden" name="q" value="{$query}"/>
		  <label for="page"> Page </label>
		  <input type="text" name="page" value="{number(/s:resources/@page + 1)}"/>
		  <label for="pages"> of <xsl:value-of select="format-number(/s:resources/@pages,'###,##0')" /></label>
		  <input type="hidden" name="pages" value="{number(/s:resources/@pages)}"/>
		</form>
        <xsl:choose>
			<xsl:when test="number(/s:resources/@pages) &gt; number(/s:resources/@page) + 1">
				<a class="pagingButton next" href="{$base-query-string}&amp;page={number(/s:resources/@page) + 2}" title="next"> <i class="fa fa-forward"></i> </a>
                <a class="pagingButton" href="{$base-query-string}&amp;page={/s:resources/@pages}" title="last"> <i class="fa fa-fast-forward"></i> </a>
			</xsl:when>
            <xsl:otherwise>
                <span class="pagingButton disabled"> <i class="fa fa-forward"></i> </span>
                <span class="pagingButton disabled"> <i class="fa fa-fast-forward"></i> </span>
            </xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>