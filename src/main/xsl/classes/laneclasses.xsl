<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml" version="2.0">

	<xsl:import href="laneclasses-common.xsl" />
	
	<xsl:template match="/doc/noncached-classes"/>
	
	<xsl:template match="/">
		<html><head><title>classes</title></head><body><xsl:apply-templates/></body></html>
	</xsl:template>

	<xsl:template match="lc:start_date">
		<xsl:variable name="date-tokens" select="tokenize(.,'(/| )')"/>
		<xsl:variable name="month">
			<xsl:value-of select="number($date-tokens[1])"/>
		</xsl:variable>
		<div class="month-day">
			<xsl:choose>
				<xsl:when test="$month = 1">January </xsl:when>
				<xsl:when test="$month = 2">February </xsl:when>
				<xsl:when test="$month = 3">March </xsl:when>
				<xsl:when test="$month = 4">April </xsl:when>
				<xsl:when test="$month = 5">May </xsl:when>
				<xsl:when test="$month = 6">June </xsl:when>
				<xsl:when test="$month = 7">July </xsl:when>
				<xsl:when test="$month = 8">August </xsl:when>
				<xsl:when test="$month = 9">September </xsl:when>
				<xsl:when test="$month = 10">October </xsl:when>
				<xsl:when test="$month = 11">November </xsl:when>
				<xsl:when test="$month = 12">December </xsl:when>
			</xsl:choose>
			<xsl:value-of select="$date-tokens[2]"/>
		</div>
		<div class="year">
			<xsl:value-of select="$date-tokens[3]"/>
		</div>
	</xsl:template>

	<xsl:template match="lc:event_name">
		<h4>
			<a href="/classes-consult/laneclass.html?class-id={../lc:module_id}">
				<xsl:value-of select="."/>
			</a>
		</h4>
	</xsl:template>

	<xsl:template match="lc:event_dates">
		<div class="time">
		<xsl:variable name="hour">
		  <xsl:value-of select="tokenize(lc:start_date[1], '( |:00 )')[2]"/>
		  </xsl:variable>
          <xsl:value-of select="concat($hour,' ', lower-case(substring-after(./lc:start_date[1]/text(),':00 ')))"/>
		  <xsl:text> – </xsl:text>
		<xsl:variable name="hour">
             <xsl:value-of select="tokenize(lc:end_date[1], '( |:00 )')[2]"/>
        </xsl:variable>
        	<xsl:value-of select="concat($hour, ' ', lower-case(substring-after(./lc:end_date[1]/text(),':00 ')))"/>
		</div>
	</xsl:template>


	<xsl:template match="lc:event_data">
		<div class="class">
			<div class="yui3-g">
				<div class="yui3-u-1-4">
					 <div class="date">
						<xsl:apply-templates select="./lc:event_dates/lc:start_date[1]" />
						<xsl:apply-templates select="./lc:event_dates" />
					</div>
					<div class="venue">
						<div class="yui3-g location">
							<div class="yui3-u-5-6">
								<xsl:apply-templates select="lc:venue"/>
							</div>
							<div class="yui3-u-1-6">
								<i class="fa fa-map-marker fa-2x"></i>
							</div>
						</div>
					</div>
				</div>
				<div class="yui3-u-3-4">
						<xsl:apply-templates select="lc:event_name"/>
						<div class="yui3-g">
							<div class="yui3-u-1-4">
								<xsl:apply-templates select="lc:event_instructors"/>
								<xsl:call-template name="remainingSeats"/>
							</div>
							<div class="yui3-u-3-4">
								<xsl:apply-templates select="lc:event_description"/>
							</div>
						</div>
				</div>
			</div>
		</div>
	</xsl:template>

 	
	<xsl:template match="lc:venue">
			<div class="venue-link">
	        <xsl:choose>
               <xsl:when test="./lc:venue_website != ''">
                   <a>
                       <xsl:attribute name="href">
                           <xsl:value-of select="./lc:venue_website/text()"/>
                       </xsl:attribute><xsl:value-of select="./lc:venue_name"/>
                   </a>
               </xsl:when>
               <xsl:otherwise>
                   <xsl:value-of select="./lc:venue_name"/>
             </xsl:otherwise>
           </xsl:choose>
           </div> 
	</xsl:template>
 
  
	<xsl:template match="lc:event_description">
		<xsl:choose>
			<xsl:when test="count(tokenize(., '\W+')[. != ''])  &gt; $description-length">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="."/>
					<xsl:with-param name="count" select="$description-length"/>
				</xsl:call-template>
				<xsl:text>...</xsl:text>
					<a href="/classes-consult/laneclass.html?class-id={../lc:module_id}"> More <i class="fa fa-arrow-right"/></a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


</xsl:stylesheet>

