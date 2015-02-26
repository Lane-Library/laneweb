<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="h" version="2.0">

	<xsl:param name="source" />
	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>

	<!-- copy attributes by default -->
	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>


	<xsl:template match="h:div[@id='search-image-content']">
		<xsl:copy>
			<xsl:attribute name="class"> 
				<xsl:value-of select="concat($source,'-border')"></xsl:value-of>
			</xsl:attribute>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:div[@id='tabs-image-search']/h:ul/h:li/@class">
		<xsl:for-each select=".">
			<xsl:attribute name="class">
			  <xsl:copy-of select="." />
				<xsl:if test=".=$source"> 
			 		<xsl:text> search-image-selected-tab</xsl:text>
			 	</xsl:if>	  
		</xsl:attribute>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="h:div[@class='result-summary'][1]/@class">
		<xsl:attribute name="class">
			  <xsl:copy-of select="." />
			  <xsl:value-of select="concat(' ',$source)" />
	   </xsl:attribute>
	</xsl:template>

</xsl:stylesheet> 