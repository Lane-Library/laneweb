<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:h="http://www.w3.org/1999/xhtml"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://www.w3.org/1999/xhtml"
exclude-result-prefixes="h xi">

	<xsl:template match="h:div[contains(@class, 'Box')]">
		<xsl:choose>
			<xsl:when test="not(contains(@class, 'BoxContent'))">
				<div>
					<xsl:for-each select="@*">
						<xsl:choose>
							<xsl:when test="name()='class' and (contains(.,'long') or contains(.,'short'))">
								<xsl:attribute name="class"><xsl:value-of select="concat('s', concat(substring-after(., 'longS'), substring-after(., 'shortS')))" /></xsl:attribute><!-- replacing capital w/ lowercase 's', e.g. 'shortSideBlueBox' in content; only one of 'short' or 'long' qualifiers present at a time, so OK to concatenate-->
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
					
					<xsl:copy-of select="h:h2" />
					
					<xsl:if test="contains(@class, 'white') and h:h2">
						<div class="whiteBoxDivider">&#160;</div>
					</xsl:if>
					
					<div>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="contains(@class,'long')">
									<xsl:value-of select="concat('s', substring-after(@class, 'longS'), 'LongContent')"/>
								</xsl:when>
								<xsl:when test="contains(@class,'short')">
									<xsl:value-of select="concat('s', substring-after(@class, 'shortS'), 'ShortContent')"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(@class, 'Content')"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:if test="contains(@class, 'white') and not(h:h2)"><xsl:attribute name="style">text-align: center;</xsl:attribute></xsl:if><!--content of white boxes to be centered when no logos present-->
						<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
					</div>
				</div>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

<!--
	add aggregation+XSLT for alphabet incorporation
-->


	<xsl:template name="alphabet-display">
		<xsl:param name="alphabet-string"/>
		<xsl:param name="eLibrary-type"/>

		<xsl:variable name="letter">
			<xsl:value-of select="substring-before($alphabet-string, ' ')" />
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$letter != ''">
				<xsl:variable name="capital-letter"><xsl:value-of select="translate($letter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/></xsl:variable>
				<span style="color:blue;">
					<a href="{$eLibrary-type}browse.html?a={$capital-letter}&amp;t={$eLibrary-type}">
						<xsl:value-of select="$letter"/>
					</a>
				</span><xsl:text> </xsl:text>
				
				<xsl:variable name="new-alphabet-string">
					<xsl:value-of select="normalize-space(substring-after($alphabet-string, $letter))"/>
				</xsl:variable>

				<xsl:call-template name="alphabet-display">
					<xsl:with-param name="alphabet-string" select="$new-alphabet-string"/>
					<xsl:with-param name="eLibrary-type" select="$eLibrary-type"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<span style="color:blue;">
					<a href="{$eLibrary-type}">
						<xsl:value-of select="$alphabet-string"/>
					</a>
				</span>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>


	<xsl:template match="h:div[@id='alphabeticalBrowse']">
		<span style="color: red;">
			<xsl:call-template name="alphabet-display">
				<xsl:with-param name="alphabet-string" select="normalize-space(text())"/>
				<xsl:with-param name="eLibrary-type" select="'ej'"/>
			</xsl:call-template>
		</span>
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
