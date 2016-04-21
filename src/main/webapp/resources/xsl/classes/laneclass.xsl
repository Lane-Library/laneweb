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
	 	<h3>
			<xsl:value-of select="./lc:event_name/text()" />
		</h3>
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
			<div class="yui3-g">
				<div class="yui3-u-1-4">
					<div class="date same-height-1">
						<xsl:apply-templates select="./lc:event_dates/lc:start_date[1]" />
						<xsl:apply-templates select="./lc:event_dates" />
					</div>
				</div>
				<div class="yui3-u-3-4  same-height-1" style="height:200px">
					<div class="details">
						<xsl:apply-templates select="lc:event_instructors/lc:instructor" />
						<div>
							<a href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={lc:module_id}" class="button alt1">
								<span>
									<xsl:choose>
										<xsl:when test="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats = '-\-\-'">
											Wait List
										</xsl:when>
										<xsl:otherwise>
											Register
										</xsl:otherwise>
									</xsl:choose>
								</span>
								<i class="icon fa fa-arrow-right" />
							</a>
							<xsl:if test="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats != '-\-\-'">
								<div class="seats">
									Seats left:
									<xsl:value-of select="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats" />
								</div>
							</xsl:if>
						</div>
						<div style="margin-top:30px">
							<xsl:apply-templates select="lc:venue" />
						</div>

					</div>
				</div>
			</div>
		</div>
	</xsl:template>



	<xsl:template match="lc:venue">
		<div>
			<xsl:variable name="link">
				<xsl:value-of select="./lc:venue_website" />
			</xsl:variable>
			<xsl:variable name="name">
				<span>
					<xsl:attribute name="itemprop">location</xsl:attribute>
					<xsl:value-of select="./lc:venue_name" />
				</span>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$link != ''">
					<div>
						<xsl:copy-of select="$name" />
					</div>
					<div>
						<a>
							<xsl:attribute name="href">
                                <xsl:value-of select="./lc:venue_website/text()" />
                            </xsl:attribute>
							<xsl:text>Location Map </xsl:text>
							<xsl:if test="ends-with($link, '.pdf')">
								<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
							</xsl:if>
						</a>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<xsl:copy-of select="$name" />
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>


</xsl:stylesheet>
