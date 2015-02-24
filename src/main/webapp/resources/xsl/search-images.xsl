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
		<xsl:attribute name="style"> 
			<xsl:text>border: 6px solid </xsl:text>
		 <xsl:choose> 
		 	<xsl:when test="$source = 'cc-images-all'"> 
		 		<xsl:text> #b96d12;</xsl:text> 
		 	</xsl:when> 
			<xsl:when test="$source = 'rl-images-all'"> 
				<xsl:text> #8c1515;</xsl:text> 
			</xsl:when> 
			<xsl:when test="$source = 'pmc-images-all'"> 
				<xsl:text> #5E3032;</xsl:text> 
			</xsl:when> 
			<xsl:otherwise> 
				<xsl:text> #80982A;</xsl:text> 
			</xsl:otherwise> 
			</xsl:choose>
			</xsl:attribute>  
			<xsl:apply-templates select="node()|@*" />
			</xsl:copy> 
		</xsl:template>
		
	<xsl:template match="h:div[@class='result-summary']">
		<xsl:copy>
		 <xsl:attribute name="style"> 
		 	<xsl:text>background-color:</xsl:text> 
			<xsl:choose> <xsl:when test="$source = 'cc-images-all'"> <xsl:text> #b96d12;</xsl:text> 
			</xsl:when> <xsl:when test="$source = 'rl-images-all'"> <xsl:text> #8c1515;</xsl:text> 
			</xsl:when> <xsl:when test="$source = 'pmc-images-all'"> <xsl:text> #5E3032;</xsl:text> 
			</xsl:when> <xsl:otherwise> <xsl:text> #80982A;</xsl:text> </xsl:otherwise> 
			</xsl:choose> 
		</xsl:attribute> 
		<xsl:apply-templates select="node()|@*" />
		</xsl:copy>	
	</xsl:template>






</xsl:stylesheet> 