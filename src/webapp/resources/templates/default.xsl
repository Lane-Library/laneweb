<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
		xmlns:h="http://www.w3.org/1999/xhtml"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml"
		exclude-result-prefixes="h xi">
	
	<xsl:param name="request-uri"/>
	<xsl:param name="a"/>
	
	<xsl:variable name="alpha">
		<xsl:value-of select="$a"/>
	</xsl:variable>
	
	<xsl:variable name="eLibrary-type">
		<xsl:value-of select="substring(substring-after($request-uri, 'online/'), 1, 2)"/>
	</xsl:variable>
	<xsl:variable name="eLibrary-search-code">
		<xsl:choose>
			<xsl:when test="$eLibrary-type='ej'">ej</xsl:when>
			<xsl:when test="$eLibrary-type='eb'">book</xsl:when>
			<xsl:when test="$eLibrary-type='cc'">cc</xsl:when>
			<xsl:when test="$eLibrary-type='db'">database</xsl:when>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template match="h:div[contains(@class, 'Box') and not(contains(@class, 'inBox'))]">
		<xsl:variable name="current-class-selection"><xsl:value-of select="@class"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="@class = 'noBordersBox'">
				<div class="noBordersBox largeFont">
					<xsl:apply-templates select="child::node()"/>
				</div>
			</xsl:when>
			<xsl:when test="not(contains(@class, 'BoxContent'))">
				<div>
					<xsl:for-each select="@*">
						<xsl:choose>
							<xsl:when test="name()='class' and (contains(.,'long') or contains(.,'short'))">
								<xsl:attribute name="class">
									<xsl:value-of select="concat('s', concat(substring-after(., 'longS'), substring-after(., 'shortS')))" />
								</xsl:attribute><!-- replacing capital w/ lowercase 's', e.g. 'shortSideBlueBox' in content; only one of 'short' or 'long' qualifiers present at a time, so OK to concatenate-->
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
					
					<xsl:copy-of select="h:h2[1] | h:h2[@class]" /><!-- avoid duplicate display of h2 in RSS feeds, but keep styling for SquareTitles, etc.-->
					<xsl:if test="@class='centerGreenBox' or @class='centerBlueBox'">
						<xsl:if test="child::h:div[@id='otherPortalOptions'] and contains($request-uri, 'portals')">
							
						</xsl:if>
						<br style="clear: both;"/><!--clearance for floats-->
					</xsl:if>
					
					<xsl:if test="contains(@class, 'white') and h:h2">
						<div class="whiteBoxDivider">&#160;</div>
					</xsl:if>
					
					<div>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="contains(@class,'long')">
									<xsl:value-of select="concat('s', substring-after(@class, 'longS'), 'LongContent', ' ' , 'mediumFont')"/>
								</xsl:when>
								<xsl:when test="contains(@class,'short')">
									<xsl:value-of select="concat('s', substring-after(@class, 'shortS'), 'ShortContent', ' ' , 'mediumFont')"/>
								</xsl:when>
								<xsl:when test="ancestor::h:td[@class='centralColumn']/h:div[1][@class=$current-class-selection]">
									<xsl:value-of select="concat(@class, 'Content', ' ' , 'largeFont')"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(@class, 'Content', ' ' , 'mediumFont')"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:if test="contains(@class, 'white') and not(h:h2)">
							<xsl:attribute name="style">text-align: center;</xsl:attribute>
						</xsl:if><!--content of white boxes to be centered when no logos present-->
						<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
					</div>
				</div>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="h:div[@id='otherPortalOptions']">
		<!--want it mapped to nothing; processed for top box in central column of portal pages-->
	</xsl:template>

<!--alphabet incorporation-->
	<xsl:template name="alphabet-display">
		<xsl:param name="alphabet-string"/>
		
		<xsl:variable name="letter">
			<xsl:value-of select="substring-before($alphabet-string, ' ')" />
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$letter != ''">
				<xsl:variable name="lowercase-letter">
					<xsl:value-of select="translate($letter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$lowercase-letter!=$alpha">
						<span class="eLibraryTab"><!--check for # and substitute w/ %23-->
							<a href="{$eLibrary-type}browse.html?a={$lowercase-letter}&amp;t={$eLibrary-search-code}">
								<xsl:value-of select="$letter"/>
							</a>
						</span>
						<xsl:text> </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<span class="eLibraryTabActive">
							<xsl:value-of select="$letter"/>
						</span>
						<xsl:text> </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:call-template name="alphabet-display">
					<xsl:with-param name="alphabet-string" select="normalize-space(substring-after($alphabet-string, $letter))"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<span class="eLibraryTab">
					<a href="{$eLibrary-type}browse.html?a={$alphabet-string}&amp;t={$eLibrary-search-code}">
						<xsl:value-of select="$alphabet-string"/>
					</a>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
	
<!--check for # and substitute w/ %23-->
<!--
	<xsl:template name="alphabet-browse-url-build">
		<xsl:param name="browse-letter"/>
		
		<xsl:choose>
			<xsl:when test="$browse-letter!=$alpha">
				<span class="eLibraryTab">
					<a href="{$eLibrary-type}browse.html?a={$browse-letter}&amp;t={$eLibrary-search-code}">
						<xsl:value-of select="$capital-letter"/>
					</a>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<span class="eLibraryTabActive">
					<xsl:value-of select="$capital-letter"/>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
-->
	<xsl:template match="h:div[@id='alphabeticalBrowse']">
		<xsl:call-template name="alphabet-display">
			<xsl:with-param name="alphabet-string" select="normalize-space(text())"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="@*">
		<xsl:copy-of select="."/>
	</xsl:template>

</xsl:stylesheet>
