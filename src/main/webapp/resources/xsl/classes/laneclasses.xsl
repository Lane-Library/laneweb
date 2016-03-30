<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml"
	version="2.0">

	<xsl:import href="laneclasses-common.xsl" />

	<xsl:template match="/doc/noncached-classes" />

	<xsl:template match="/">
		<html>
			<head>
				<title>classes</title>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="lc:start_date">
		<xsl:variable name="date-tokens" select="tokenize(.,'(/| )')" />
		<xsl:variable name="month">
			<xsl:value-of select="number($date-tokens[1])" />
		</xsl:variable>
		<div class="month-day">
			<xsl:choose>
				<xsl:when test="$month = 1">
					January
				</xsl:when>
				<xsl:when test="$month = 2">
					February
				</xsl:when>
				<xsl:when test="$month = 3">
					March
				</xsl:when>
				<xsl:when test="$month = 4">
					April
				</xsl:when>
				<xsl:when test="$month = 5">
					May
				</xsl:when>
				<xsl:when test="$month = 6">
					June
				</xsl:when>
				<xsl:when test="$month = 7">
					July
				</xsl:when>
				<xsl:when test="$month = 8">
					August
				</xsl:when>
				<xsl:when test="$month = 9">
					September
				</xsl:when>
				<xsl:when test="$month = 10">
					October
				</xsl:when>
				<xsl:when test="$month = 11">
					November
				</xsl:when>
				<xsl:when test="$month = 12">
					December
				</xsl:when>
			</xsl:choose>
			<xsl:value-of select="$date-tokens[2]" />
		</div>
		<div class="year">
			<xsl:value-of select="$date-tokens[3]" />
		</div>
	</xsl:template>

	<xsl:template match="lc:event_name">
		<h3>
			<xsl:if test="/doc/noncached-classes/eventlist/event[eventid = current()/../lc:module_id]/seats = '-\-\-'">
				<xsl:text>WAITLIST! </xsl:text>
			</xsl:if>
			<a href="/classes-consult/laneclass.html?class-id={../lc:module_id}">
				<xsl:value-of select="." />
			</a>
		</h3>
	</xsl:template>

	<xsl:template match="lc:event_dates">
		<div>
			<xsl:variable name="hour">
				<xsl:value-of select="tokenize(lc:start_date[1], '( |:00 )')[2]" />
			</xsl:variable>
			<xsl:value-of select="concat($hour,' ', lower-case(substring-after(./lc:end_date[1]/text(),':00 ')))" />
			<xsl:text> – </xsl:text>
			<xsl:variable name="hour">
				<xsl:value-of select="tokenize(lc:end_date[1], '( |:00 )')[2]" />
			</xsl:variable>
			<xsl:value-of select="concat($hour, ' ', lower-case(substring-after(./lc:end_date[1]/text(),':00 ')))" />
		</div>
	</xsl:template>

	<xsl:template match="lc:instructor">
		<xsl:variable name="position" select="position()" />
		<xsl:variable name="last" select="last()" />
		<xsl:for-each select=".">
			<xsl:value-of select="lc:fname" />
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="lc:lname" />
			<xsl:if test="$position != $last">
				<xsl:text>&#160; &amp; &#160;</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="lc:event_data">
		<div class="class">
			<div class="yui3-g">
				<div class="yui3-u-1-4">
					<div class="date same-height-1">
						<xsl:apply-templates select="lc:event_dates/lc:start_date[1]" />
						<xsl:apply-templates select="lc:event_dates" />
					</div>
				</div>
				<div class="yui3-u-3-4  same-height-1">
					<div class="details">
						<xsl:apply-templates select="lc:event_name" />
						<div>
							With <b>
							<xsl:choose>
								<xsl:when test="lc:speaker/text()">
									<xsl:value-of select="lc:speaker/text()" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="lc:event_instructors/lc:instructor" />
								</xsl:otherwise>
							</xsl:choose>
								</b>
							<div>
								<xsl:apply-templates select="lc:event_description" />
							</div>
							<div class="class-detail">	
								<div class="yui3-g">
									<div class="yui3-u-1-2">
										<a href="">Download Handouts <i class="fa fa-arrow-right"/>	</a>
										<xsl:apply-templates select="lc:venue"/>
						           </div>
									<div class="yui3-u-1-2">

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
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				</div>
				
		
	</xsl:template>


	<xsl:template match="lc:venue">
		<div>
		  <xsl:text>At </xsl:text>
                <xsl:variable name="link">
                    <xsl:value-of select="./lc:venue_website"/>
                </xsl:variable>
                <xsl:variable name="name">
                    <span>
                            <xsl:attribute name="itemprop">location</xsl:attribute>
                        	<xsl:value-of select="./lc:venue_name"/>
                    </span>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="./lc:venue_website/text()"/>
                            </xsl:attribute>
                            <xsl:copy-of select="$name"/>
                        </a>
                        <xsl:if test="ends-with($link, '.pdf')">
                            <xsl:text> (.pdf)</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$name"/>
                    </xsl:otherwise>
                </xsl:choose>
             </div>
	</xsl:template>


</xsl:stylesheet>
