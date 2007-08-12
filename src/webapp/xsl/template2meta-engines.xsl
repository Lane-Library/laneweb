<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns="http://www.w3.org/1999/xhtml"
                       exclude-result-prefixes="h"
                       version="2.0">

	<xsl:output method='xml' indent="no"/>

	<xsl:param name="source"/>

	<!--  given a search template source, extract metasearch engine ids and build GLOBALS.<source>Engines JavaScript array -->
	<xsl:template match="h:html">
		<xsl:variable name="count" select="count(//h:li[@id])"/>
		<h:javascript-array>GLOBALS.<xsl:value-of select="$source"/>Engines  = [
		<xsl:apply-templates select="//h:li[@id]">
			<xsl:with-param name="count" select="$count"/>
		</xsl:apply-templates>];&#10;</h:javascript-array>
	</xsl:template>
        
	<xsl:template match="h:li[@id]">
		<xsl:param name="count"/>
		<xsl:variable name="sep">
			<xsl:choose>
				<xsl:when test="$count = position()">
					<xsl:value-of select="''"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="','"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="@id!=''">
			"<xsl:value-of select="@id"/>"<xsl:value-of select="$sep"/>
		</xsl:if>
	</xsl:template>
    
</xsl:stylesheet>