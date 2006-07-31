<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
		xmlns:h="http://www.w3.org/1999/xhtml"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml"
		exclude-result-prefixes="h">
	
	<xsl:include href="template-helper.xsl"/>

	<xsl:param name="request-uri"/>
	
	<!-- large font for all, originally for feedback form -->
	<xsl:template match="h:body[not(descendant::h:div[@id='contentBody'])]">
		<xsl:copy>
			<div id="contentBody">
				<!--<xsl:copy-of select="h:div[@id='breadCrumb']"/>-->
				<xsl:call-template name="font-application">
					<xsl:with-param name="candidate-node" select="."/>
				</xsl:call-template>
			</div>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
