<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses"
	exclude-result-prefixes="lc" version="2.0">

	<xsl:template match="eventlist">
		<xsl:copy>
			<xsl:apply-templates select="child::node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="event">
		<xsl:copy>
			<xsl:apply-templates select="child::node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="eventid">
		<xsl:attribute name="id"><xsl:value-of select="text()" />  </xsl:attribute>
		<xsl:apply-templates select="child::node()" />
	</xsl:template>

	<xsl:template match="seats">
		<xsl:value-of select="text()" />
	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>
