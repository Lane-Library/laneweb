<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml"
	version="2.0">



	<xsl:template match="doc">
		<xsl:apply-templates select="h:html" />
	</xsl:template>

	<xsl:template match="h:li">
		<xsl:if
			test="contains(string-join(/doc/classes/class/categories//category/text(), ',' ), ./h:a/@id)">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node()|child::node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>



	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>


</xsl:stylesheet>