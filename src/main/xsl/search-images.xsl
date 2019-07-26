<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" 
	xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns:d="doc"
	exclude-result-prefixes="h d"
	version="2.0">

	<xsl:param name="source" />
	
  	<xsl:template match="/d:doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

 	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
 	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>
  
	<xsl:template match="h:div[@class='search-image-content']/@class">
		<xsl:attribute name="class">
			<xsl:value-of select="concat(.,' ', $source,'-border')"/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="h:div[@id='tabs-image-search']/h:ul/h:li[@class=$source]/@class">
			<xsl:attribute name="class">
			  <xsl:copy-of select="." />
			 <xsl:text> search-image-selected-tab</xsl:text>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="h:div[@class='result-summary']/@class">
		<xsl:attribute name="class">
			  <xsl:copy-of select="." />
			  <xsl:value-of select="concat(' ',$source)"/>
	   </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:div[@id='tabs-image-search']/h:ul/h:li[@class=$source]//h:a/h:span">
		<xsl:variable name="copyright" select="substring(@id, 10)"></xsl:variable>
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="/d:doc/map/entry/string[text() = $copyright]/following-sibling::string != ''">
					<xsl:value-of select="/d:doc/map/entry/string[text() = $copyright]/following-sibling::string"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:text> images with</xsl:text>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:div[@id='tabs-image-search']/h:ul/h:li[@class!=$source]//h:a/h:span">
		<xsl:variable name="copyright" select="substring(@id, 10)"></xsl:variable>
		<xsl:copy>
			<xsl:text>(</xsl:text>
			<xsl:choose>
				<xsl:when test="/d:doc/map/entry/string[text() = $copyright]/following-sibling::string != ''">
					<xsl:value-of select="/d:doc/map/entry/string[text() = $copyright]/following-sibling::string"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:text>)</xsl:text>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet> 