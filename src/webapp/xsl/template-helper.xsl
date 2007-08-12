<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
		xmlns:h="http://www.w3.org/1999/xhtml"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml"
		xmlns:xi="http://www.w3.org//2001/XInclude"
		exclude-result-prefixes="h xi">
	
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template name="font-application">
		<xsl:param name="candidate-node"/>
		<xsl:for-each select="$candidate-node/child::node()[not(self::h:div[@id='breadCrumb'])]">
			<xsl:choose>
				<xsl:when test="name()">
					<xsl:choose>
						<xsl:when test="self::h:div and (contains(@class, 'Box') or @class='searchHeader' or @class='popInContent')">
							<xsl:apply-templates select="."/>
						</xsl:when>
						<xsl:when test="normalize-space(string(.)) and name()!='h1'">
							<xsl:element name="{name()}">
								<xsl:copy-of select="@*[not(self::class)]"/>
								<xsl:attribute name="class">
									<xsl:choose>
										<xsl:when test="@class">
											<xsl:value-of select="concat(@class, ' largeFont')"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>largeFont</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:apply-templates select="child::node()"/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="normalize-space(string(.)) and not(self::comment())">
							<span class="largeFont">
								<xsl:copy-of select="."/>
							</span>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<!--identity copy-->
	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="@*">
		<xsl:copy-of select="."/>
	</xsl:template>

</xsl:stylesheet>
