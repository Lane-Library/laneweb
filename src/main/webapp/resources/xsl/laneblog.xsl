<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 	xmlns:h="http://www.w3.org/1999/xhtml"
	version="2.0">


	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
		
	</xsl:template>

	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>


	<xsl:template match="h:content">
		<xsl:copy-of select="h:p[position() &lt;= 2]" />
	</xsl:template>

	
</xsl:stylesheet>