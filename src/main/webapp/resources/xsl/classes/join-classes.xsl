<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h"
	version="2.0">



	<xsl:template match="doc">
		<xsl:apply-templates select="eventlist[1]" />
	</xsl:template>


	<xsl:template match="eventlist">
		<xsl:copy>
			<xsl:apply-templates select="event" />
			<xsl:for-each select="/doc/eventlist[2]/event">
				<xsl:variable name="event_id" select="./eventid/text()"></xsl:variable>
				<xsl:if test="count(/doc/eventlist[1]/event/eventid[./text() =  $event_id]) = 0">
					<xsl:apply-templates select="." />
				</xsl:if>
			</xsl:for-each>
		</xsl:copy>
	</xsl:template>


	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="attribute::node()">
		<xsl:copy-of select="self::node()" />
	</xsl:template>


</xsl:stylesheet>