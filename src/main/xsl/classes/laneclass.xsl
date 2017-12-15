<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h"
	version="2.0">

	<xsl:import href="laneclasses.xsl" />
	

	<xsl:template match="/doc/noncached-classes"/>

	<xsl:param name="class-id" />

	<xsl:variable name="internal-id">
		<xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:internal_id/text()" />
	</xsl:variable>

 	<xsl:template match="/doc/lc:classes">
		<html>
			<body>
				<xsl:apply-templates select="./lc:event_data[./lc:module_id = $class-id]" />
			</body>
		</html>
	</xsl:template>
 
	<xsl:template match="lc:event_data">
	 	<h4>
			<xsl:value-of select="./lc:event_name/text()" />
		</h4>
		<xsl:apply-templates select="./lc:event_description" /> 
		<xsl:apply-templates select="./lc:downloads/lc:download_description" /> 
		 <xsl:for-each select="/doc/lc:classes/lc:event_data/lc:internal_id[ ./text() = $internal-id]/..">
			<xsl:call-template name="class-detail" />
		</xsl:for-each> 
	</xsl:template>


	 
 	<xsl:template match="lc:event_description">
	 	<div class="class-description">
	 		<xsl:copy-of select="node()"/>
	 	</div>
	</xsl:template>

	<xsl:template match="lc:download_description">
		<div class="handout-description" >
			<xsl:copy-of select="node()"/>
		</div>
	</xsl:template>
  

	<xsl:template name="class-detail">
		<div class="class">
			<div class="pure-g">
				<div class="pure-u-1-4">
					<div class="date">
						<xsl:apply-templates select="./lc:event_dates/lc:start_date[1]" />
						<xsl:apply-templates select="./lc:event_dates" />
					</div>
					<div class="venue">
						<xsl:apply-templates select="lc:venue"/>
					</div>
				</div>
				<div class="pure-u-3-4">
					<div class="details">
						<xsl:apply-templates select="lc:event_instructors" />
						<xsl:call-template name="remainingSeats"/>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>







</xsl:stylesheet>
