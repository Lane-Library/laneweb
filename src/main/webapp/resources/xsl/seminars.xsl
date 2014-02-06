<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/seminars/ns"
	version="2.0">

	<xsl:variable name="type" select="s:link/@s:type" />

	<xsl:variable name="seminars-node">
		<xsl:copy-of
			select="document(concat('cocoon://apps/seminars/',s:link/@s:type,'/',s:link/@s:year,'/',s:link/@s:month,'/',s:link/@s:day,'/',s:link/@s:days,'.xml'))" />
	</xsl:variable>

	<xsl:variable name="seminars-formatted">
		<xsl:apply-templates select="$seminars-node//h:div[@class='eventInfo']" />
	</xsl:variable>

	<xsl:template match="/">
		<html>
			<head>
				<title>seminars</title>
			</head>
			<body>
				<xsl:copy-of select="$seminars-formatted" />
				<a href="{s:link/@s:url}">More »</a>
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
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{s:link/@s:url}">
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

		<div class="seminar">
			<div class="yui3-g">
				<div class="yui3-u-1-6">
					<div class="month">
						<xsl:value-of select="$month" />
					</div>
					<div class="day">
						<xsl:value-of select="$day" />
					</div>
				</div>
				<div class="yui3-u-5-6">
					<div class="semTitle">
						<xsl:copy-of select="$anchor" />
					</div>
					<xsl:if test="$time != ''">
						<div class="time">
							<xsl:value-of select="$time" />
						</div>
					</xsl:if>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>