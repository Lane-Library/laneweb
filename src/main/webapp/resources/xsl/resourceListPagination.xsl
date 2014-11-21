<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:s="http://lane.stanford.edu/resources/1.0"
	xmlns:fx="http://lane.stanford.edu/fx"
	exclude-result-prefixes="h s" version="2.0">
	
	<xsl:param name="page"/>
	
	<xsl:function name="fx:pad-string" xmlns:fx="http://lane.stanford.edu/fx">
		<xsl:param name="stringToPad"/> 
		<xsl:variable name="paddingLength" select="40"/>
		<xsl:choose>
			<xsl:when test="string-length($stringToPad) &lt;= $paddingLength">
				<xsl:sequence select="substring(string-join ((concat($stringToPad, ' '), for $i in (1 to $paddingLength) return '.'),''),1,$paddingLength)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($stringToPad,1,number($paddingLength)-3),'...')"></xsl:value-of>             
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:template name="paginationLinks">
		<xsl:param name="browse-mode" />
		<xsl:variable name="no-page-query-string">
			<xsl:choose>
				<xsl:when test="$query and $source">
					<xsl:value-of
						select="concat('source=', $source, '&amp;q=', $query, '&amp;')" />
				</xsl:when>
				<xsl:when test="$alpha">
					<xsl:value-of select="concat('a=', $alpha, '&amp;')" />
				</xsl:when>
				<xsl:when test="$mesh">
					<xsl:value-of select="concat('m=', $mesh, '&amp;')" />
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<div class="resourceListPagination">
					<xsl:choose>
						<xsl:when
							test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
							<span id="pageStart"><xsl:value-of select="number(/s:resources/@start) + 1" /></span>
							<xsl:text> to </xsl:text>
							<xsl:value-of
								select="number(/s:resources/@start) + number(/s:resources/@length)" />
							<xsl:text> of </xsl:text>
							<a href="?{$no-page-query-string}page=all">
								<xsl:value-of select="/s:resources/@size" />
								<xsl:text> matches</xsl:text>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> all </xsl:text>
							<xsl:value-of select="/s:resources/@size" />
							<xsl:text> matches</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				<xsl:if
					test="number(/s:resources/@size) &gt; number(/s:resources/@length)">
					<xsl:choose>
						<xsl:when test="$browse-mode = 'true'">
							<xsl:variable name="chooseString">
								<xsl:choose>
									<xsl:when test="string-length($alpha) = 1">
										<xsl:value-of select="concat('Choose ', upper-case($alpha), 'a-', upper-case($alpha), 'z')" />
									</xsl:when>
									<xsl:otherwise>Choose A-Z</xsl:otherwise>
								</xsl:choose>									
							</xsl:variable>
							<a class="pagingButton gray-btn" href="#">
								<span>
									<xsl:value-of select="$chooseString"/>
								</span>
							</a>
							<ul class="pagingLabels">
								<xsl:for-each select="/s:resources/s:pagingLabel">
									<li>
										<a href="{concat($request-uri,'?',$no-page-query-string,'page=',position())}">
											<ul>
												<li>
														<xsl:value-of select="fx:pad-string(@start)" />
												</li>
												<li>
													<span class="plDash"> &#8212; </span>
														<xsl:value-of select="fx:pad-string(@end)" />
												</li>
												<li class="plResults">
													<xsl:value-of select="concat(' (', @results, ')')" />
												</li>
											</ul>
										</a>
									</li>
								</xsl:for-each>
							</ul>
							<a style="float:right;margin:0 15px 0 10px" href="?{$no-page-query-string}page=all">See All</a>
						</xsl:when>
						<xsl:otherwise>
							<a style="float:right;margin:0 15px 0 10px" href="?{$no-page-query-string}page=all">See All</a>
							<div style="float:right">
								<xsl:call-template name="paginationNumbers">
									<xsl:with-param name="page" select="number(0)" />
									<xsl:with-param name="query-string" select="$no-page-query-string" />
								</xsl:call-template>
							</div>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template name="paginationNumbers">
		<xsl:param name="page" />
		<xsl:param name="query-string" />
		<xsl:choose>
			<xsl:when test="number($page) = number(/s:resources/@page)">
				<xsl:value-of select="number($page) + 1" />
			</xsl:when>
			<xsl:otherwise>
				<a href="?{$query-string}page={number($page) + 1}">
					<xsl:value-of select="number($page) + 1" />
				</a>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="number($page + 1) != number(/s:resources/@pages)">
			<xsl:text> | </xsl:text>
			<xsl:call-template name="paginationNumbers">
				<xsl:with-param name="page" select="number($page + 1)" />
				<xsl:with-param name="query-string" select="$query-string" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>