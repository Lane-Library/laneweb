<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"	xmlns="http://www.w3.org/1999/xhtml"	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<xsl:apply-templates
			select="libcal/locations/location[lid= '12359']/rendered" />
	</xsl:template>


	<xsl:template match="rendered">
		<span>
			<xsl:copy-of select="./text()"></xsl:copy-of>
		</span>
	</xsl:template>

</xsl:stylesheet>