<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:s="http://lane.stanford.edu/resources/1.0"
	xmlns:fx="http://lane.stanford.edu/fx"
	exclude-result-prefixes="h s" version="2.0">
	
	<xsl:param name="page"/>
	
	<xsl:variable name="base-query-string">
		<xsl:if test="$query and $source">
			<xsl:value-of
				select="concat('source=', $source, '&amp;q=', $query, '&amp;')" />
		</xsl:if>
	</xsl:variable>
	
	<xsl:template name="paginationLinks">
		<xsl:param name="top"/>
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
		
		<div class="resourceListPagination no-bookmarking">
			<xsl:choose>
				<xsl:when
					test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
					<xsl:if test="$top = 'true'">
						<span id="pageStart"><xsl:value-of select="format-number(number(/s:resources/@start) + 1,'###,##0')" /></span>
						<xsl:text> to </xsl:text>
						<xsl:value-of
							select="format-number($end,'###,##0')" />
						<xsl:text> of </xsl:text>
						<xsl:value-of select="format-number(/s:resources/@size, '###,##0')" />
						<xsl:text> results</xsl:text>
					</xsl:if>
                   <div class="s-pagination">
                       <xsl:call-template name="paginationNumbers"/>
                   </div>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> all </xsl:text>
					<xsl:value-of select="/s:resources/@size" />
					<xsl:text> results</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	
	<xsl:template name="paginationNumbers">
		<xsl:choose>
			<xsl:when test="number(/s:resources/@page) &gt; 1">
	            <a class="pagingButton" href="?{$base-query-string}page=0"> &lt;&lt; First </a>
	            <a class="pagingButton" href="?{$base-query-string}page={number(/s:resources/@page) - 2}"> &lt; Previous </a>
			</xsl:when>
			<xsl:otherwise>
	            <span class="pagingButton disabled"> &lt; &lt; First </span>
	            <span class="pagingButton disabled"> &lt; Previous </span>
			</xsl:otherwise>
		</xsl:choose>
		<form class="pagingForm" name="pagination">
		  <input type="hidden" name="source" value="{$source}"/>
		  <input type="hidden" name="q" value="{$query}"/>
		  <label for="p"> Page </label>
		  <input type="text" name="p" value="{number(/s:resources/@page)}"/>
		  <label for="pages"> of <xsl:value-of select="format-number(/s:resources/@pages,'###,##0')" /></label>
		  <input type="hidden" name="pages" value="{number(/s:resources/@pages)}"/>
		</form>
        <xsl:choose>
			<xsl:when test="number(/s:resources/@pages) &gt; number(/s:resources/@page)">
				<a class="pagingButton" href="?{$base-query-string}page={/s:resources/@page}"> Next &gt; </a>
                <a class="pagingButton" href="?{$base-query-string}page={number(/s:resources/@pages) - 1}"> Last &gt;&gt; </a>
			</xsl:when>
            <xsl:otherwise>
                <span class="pagingButton disabled"> Next &gt; </span>
                <span class="pagingButton disabled"> Last &gt;&gt; </span>
            </xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>