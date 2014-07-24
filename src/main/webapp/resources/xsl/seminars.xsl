<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/seminars/ns"
	version="2.0">

	<xsl:variable name="type" select="/s:seminars/@s:type" />

	<xsl:template match="/s:seminars">
		<html>
			<head>
				<title>seminars</title>
			</head>
			<body>
				<xsl:apply-templates select="h:html/h:body//h:div[@class='eventInfo'][position() &lt; 3]" />
                <div class="more classes">
                    <a href="{/s:seminars/@s:url}">CME Calendar <i class="icon fa fa-arrow-right"></i></a>
                </div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="build-anchor">
		<xsl:param name="url" />
		<xsl:param name="title" />
		<xsl:choose>
			<xsl:when test="contains($url,'do?semid=')">
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{concat('http://med.stanford.edu/seminars/',$url)}">
					<xsl:value-of select="$title" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{/s:seminars/@s:url}">
					<xsl:value-of select="$title" />
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:div[@class='eventInfo']">
		<xsl:variable name="title" select="h:div[@class='eventTitle']" />
		<xsl:variable name="url" select="(.//h:a/@href)[1]" />
		<xsl:variable name="datelineNode"
			select="preceding-sibling::h:div[@class='dateline'][1]" />
		<xsl:variable name="date"
			select="replace($datelineNode/h:span[@class='date'],'\(.*\)','')" />
		<xsl:variable name="month"
			select="replace($date,'([A-Za-z]{2}) .*','$1')" />
		<xsl:variable name="day"
			select="replace($date,'.* ([0-9]{2}), .*','$1')" />
		<xsl:variable name="time">
			<xsl:variable name="replaced_time"
				select="replace($date,'.* ([0-9]{1,2}:[0-9]{2} .M - [0-9]{1,2}:[0-9]{2} .M)','$1')" />
			<xsl:choose>
				<xsl:when test="$replaced_time = $date" />
				<xsl:otherwise>
					<xsl:value-of select="lower-case($replaced_time)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="anchor">
			<xsl:call-template name="build-anchor">
				<xsl:with-param name="url" select="$url" />
				<xsl:with-param name="title" select="$title" />
			</xsl:call-template>
		</xsl:variable>

		<div>
			<xsl:attribute name="class">
				<xsl:choose>
					<!-- see case 98500 align the color blocks for classes -->
					<xsl:when test="$type='cme' and position() = 1">same-height-8</xsl:when>
					<xsl:when test="$type='cme' and position() = 2">same-height-9</xsl:when>
					<xsl:otherwise>seminar</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<div class="yui3-g">
				<div>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="$type='gran'">yui3-u-1-6</xsl:when>
							<xsl:when test="$type='cme'">yui3-u-1-4</xsl:when>
						</xsl:choose>
					</xsl:attribute>
					<div>
						<xsl:attribute name="class">
							<xsl:text>date </xsl:text>
							<xsl:choose>
								<xsl:when test="$type='gran'">grandrounds-date</xsl:when>
								<xsl:when test="$type='cme'">cme-date</xsl:when>
								<xsl:otherwise><xsl:value-of select="$type"/></xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
					<div class="month">
						<xsl:value-of select="$month" />
					</div>
					<div class="day">
						<xsl:value-of select="$day" />
					</div>
					</div>
				</div>
				<div>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="$type='gran'">yui3-u-5-6</xsl:when>
							<xsl:when test="$type='cme'">yui3-u-3-4</xsl:when>
						</xsl:choose>
					</xsl:attribute>
					<p>
						<xsl:copy-of select="$anchor" />
						<xsl:if test="$time != ''">
							<br/><span class="time"><xsl:value-of select="$time" /></span>
						</xsl:if>
					</p>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>