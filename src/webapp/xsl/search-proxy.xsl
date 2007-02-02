<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:s="http://irt.stanford.edu/search/2.0"
	exclude-result-prefixes="s" version="1.0">

	<xsl:param name="context" />
	<xsl:param name="sunetid" />
	<xsl:param name="ticket" />
	<xsl:param name="affiliation" />
	<xsl:param name="proxy-links" />
	<xsl:param name="guest-links"/>
	<xsl:variable name="proxy-url">http://laneproxy.stanford.edu/login?</xsl:variable>

	<xsl:template match="*">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="s:url">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="$guest-links = 'true'">
					<xsl:value-of select="."/>
				</xsl:when>
				<xsl:when test="$proxy-links = 'true' and ($affiliation = 'LPCH' or $affiliation = 'SHC')">
					<xsl:value-of select="$proxy-url" />
					<xsl:text>url=</xsl:text>
					<xsl:value-of select="." />
				</xsl:when>
				<xsl:when test="$proxy-links = 'true' and $ticket != '' and $sunetid != ''">
					<xsl:value-of select="$proxy-url" />
					<xsl:text>user=</xsl:text>
					<xsl:value-of select="$sunetid" />
					<xsl:text>&amp;ticket=</xsl:text>
					<xsl:value-of select="$ticket" />
					<xsl:text>&amp;url=</xsl:text>
					<xsl:value-of select="." />
				</xsl:when>
				<xsl:when test="$proxy-links = 'true'">
					<xsl:value-of select="concat($context,'/secure/login.html?url=',.)" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>

</xsl:stylesheet>
