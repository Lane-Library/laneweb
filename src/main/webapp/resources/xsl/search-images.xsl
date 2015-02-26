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
			 		<xsl:text> #556222;</xsl:text> 
			 	</xsl:when> 
				<xsl:when test="$source = 'rl-images-all'"> 
					<xsl:text> #8c1515;</xsl:text> 
				</xsl:when> 
				<xsl:when test="$source = 'pmc-images-all'"> 
					<xsl:text> #b96d12;</xsl:text> 
				</xsl:when> 
				<xsl:otherwise> 
					<xsl:text> #80982A;</xsl:text> 
				</xsl:otherwise> 
			</xsl:choose>
		</xsl:attribute>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy> 
	</xsl:template>


	<xsl:template match="h:div[@id='tabs-image-search']/h:ul/h:li/@class">
		<xsl:for-each select=".">
			<xsl:attribute name="class">
			  <xsl:copy-of select="." />
				<xsl:choose> 
			 	<xsl:when test=".='free-image' and $source = 'images-all'"> 
			 		<xsl:text> search-image-selected-tab</xsl:text>
			 	</xsl:when> 
				<xsl:when test=".='cc-image' and $source = 'cc-images-all'"> 
			 		<xsl:text> search-image-selected-tab</xsl:text>
			 	</xsl:when> 
				<xsl:when test=".='pmc-image' and $source = 'pmc-images-all'"> 
			 		<xsl:text> search-image-selected-tab</xsl:text>
			 	</xsl:when> 
				<xsl:when test=".='restricted-image' and $source = 'rl-images-all'"> 
			 		<xsl:text> search-image-selected-tab</xsl:text>
			 	</xsl:when> 
			</xsl:choose>			  
		</xsl:attribute>
		</xsl:for-each>
	</xsl:template>	

	
	<xsl:template match="h:div[@class='result-summary'][1]">
		<xsl:copy>
		 <xsl:attribute name="style"> 
		 	<xsl:text>background-color:</xsl:text> 
			<xsl:choose> <xsl:when test="$source = 'cc-images-all'"> <xsl:text> #556222;</xsl:text> 
			</xsl:when> <xsl:when test="$source = 'rl-images-all'"> <xsl:text> #8c1515;</xsl:text> 
			</xsl:when> <xsl:when test="$source = 'pmc-images-all'"> <xsl:text> #b96d12;</xsl:text> 
			</xsl:when> <xsl:otherwise> <xsl:text> #80982A;</xsl:text> </xsl:otherwise> 
			</xsl:choose> 
		</xsl:attribute> 
		<xsl:apply-templates select="node()|@*" />
		</xsl:copy>	
	</xsl:template>

</xsl:stylesheet> 