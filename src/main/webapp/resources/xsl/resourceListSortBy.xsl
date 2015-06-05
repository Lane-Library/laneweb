<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:s="http://lane.stanford.edu/resources/1.0" xmlns:fx="http://lane.stanford.edu/fx"
	exclude-result-prefixes="h s" version="2.0">

	<xsl:param name="query-string" />

	<xsl:param name="sort" />

	<xsl:variable name="query-string-no-page"
		select="replace($query-string,'&amp;page=\d+','')" />

	<xsl:variable name="base-query"
		select="replace($query-string-no-page,'&amp;sort=(\w| |,|_|%2C)+','')" />

	<xsl:variable name="sorts">
		<s:sorts>
			<s:sort name="relevance" default="true" />
			<s:sort name="title" arg="title_sort asc,year desc" />
			<s:sort name="year (new to old)" arg="year desc,title_sort asc" />
			<s:sort name="year (old to new)" arg="year asc,title_sort asc" />
		</s:sorts>
	</xsl:variable>

	<xsl:variable name="active-sort-name">
		<xsl:choose>
			<xsl:when test="count($sorts//s:sort[@arg = $sort]) = 1">
				<xsl:value-of select="$sorts//s:sort[@arg = $sort]/@name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sorts//s:sort[@default = 'true']/@name" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template name="sortBy">
		<xsl:if test="number(/s:resources/@size) &gt; 1">
			<div class="view-by sort">
				<div class="general-dropdown dropdown">
					<div class="general-dropdown-trigger">
						Sort by <xsl:value-of select="$active-sort-name" />
						<i class="fa fa-angle-double-down"></i>
					</div>
					<div class="general-dropdown-content dropdown-content">
						<ul class="pagingLabels">
							<xsl:for-each select="$sorts//s:sort">
								<xsl:variable name="anchor">
									<xsl:choose>
										<xsl:when test="@arg">
											<a href="?{$base-query}&amp;sort={replace(./@arg,' ','+')}">
												<xsl:value-of select="@name" />
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="?{$base-query}">
												<xsl:value-of select="@name" />
											</a>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<li>
									<xsl:copy-of select="$anchor" />
									<xsl:if test="@arg = $sort or ($sort = '' and not(@arg))">
										<i class="fa fa-check" />
									</xsl:if>
								</li>
							</xsl:for-each>
						</ul>
					</div>
				</div>
			</div>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>