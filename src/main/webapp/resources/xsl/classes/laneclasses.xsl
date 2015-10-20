<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml" version="2.0">

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
		<h3>
			<xsl:if test="/doc/noncached-classes/eventlist/event[eventid = current()/../lc:module_id]/seats = '-\-\-'">
				<xsl:text>WAITLIST! </xsl:text>
			</xsl:if>
			<a href="/classes-consult/laneclass.html?class-id={../lc:module_id}">
				<xsl:value-of select="."/>
			</a>
		</h3>
	</xsl:template>

	<xsl:template match="lc:event_dates">
		<div class="time">
		  <xsl:value-of select="tokenize(lc:start_date[1], '( |:00 )')[2]"/>
          <xsl:text> – </xsl:text>
		<xsl:variable name="hour">
             <xsl:value-of select="tokenize(lc:end_date[1], '( |:00 )')[2]"/>
        </xsl:variable>
        	<xsl:value-of select="concat($hour,lower-case(substring-after(./lc:end_date[1]/text(),':00 ')))"/>
		</div>
	</xsl:template>

	<xsl:template match="lc:instructor">
		<xsl:variable name="position" select="position()"/>
		<xsl:variable name="last" select="last()"/>
		<div>
		<xsl:for-each select=".">
			<xsl:value-of select="lc:fname"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="lc:lname"/>
			<xsl:if test="$position != $last" >
				<xsl:text>&#160; &amp; &#160;</xsl:text>
			</xsl:if>
		</xsl:for-each>
		</div>
	</xsl:template>

	<xsl:template match="lc:event_description">
		<xsl:choose>
			<xsl:when test="count(tokenize(., '\W+')[. != ''])  &gt; 50">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="."/>
					<xsl:with-param name="count" select="50"/>
				</xsl:call-template>
				<xsl:text>...</xsl:text>
				<div>
					<a href="/classes-consult/laneclass.html?class-id={../lc:module_id}">More <i class="fa fa-arrow-right"/></a>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="lc:event_data">
		<div class="class">
			<div class="yui3-g">
				<div class="yui3-u-1-4">
					<div class="date same-height-1">
						<xsl:apply-templates select="lc:event_dates/lc:start_date[1]"/>
					</div>
				</div>
				<div class="yui3-u-3-4">
					<div class="details same-height-1">
						<xsl:apply-templates select="lc:event_name"/>
						<div class="yui3-g">
							<div class="yui3-u-1-4">
								<xsl:apply-templates select="lc:event_dates"/>
								<xsl:choose>
									<xsl:when test="lc:speaker/text()">
										<xsl:value-of select="lc:speaker"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="lc:event_instructors/lc:instructor"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats != '-\-\-'">
									<div>Seats left: <xsl:value-of select="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats"/></div>
								</xsl:if>
								<a href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={lc:module_id}" class="button alt1">
									<span>
										<xsl:choose>
											<xsl:when test="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats = '-\-\-'">Wait List</xsl:when>
											<xsl:otherwise>Register</xsl:otherwise>
										</xsl:choose>
									</span>
									<i class="icon fa fa-arrow-right"/>
								</a>
							</div>
							<div class="yui3-u-3-4">
								<xsl:apply-templates select="lc:event_description"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="firstWords">
		<xsl:param name="value"/>
		<xsl:param name="count"/>

		<xsl:if test="number($count) >= 1">
			<xsl:value-of select="concat(substring-before($value,' '),' ')"/>
		</xsl:if>
		<xsl:if test="number($count) > 1">
			<xsl:variable name="remaining" select="substring-after($value,' ')"/>
			<xsl:if test="string-length($remaining) > 0">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="$remaining"/>
					<xsl:with-param name="count" select="number($count)-1"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
