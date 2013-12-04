<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
	version="2.0">

	<xsl:param name="days" />

	<xsl:param name="type" />

	<xsl:variable name="cDay" select="format-date(current-date(),'[D,2]')" />

	<xsl:variable name="cMonth"
		select="format-date(current-date(),'[MNn,*-3]')" />

	<xsl:variable name="cYear" select="format-date(current-date(),'[Y,4]')" />

	<xsl:variable name="moreUrl"
		select="concat('http://med.stanford.edu/seminars/validatecmecalendar.do?filter=true&amp;selMonth=',$cMonth,'&amp;selDay=',$cDay,'&amp;selYear=',$cYear,'&amp;futureNumberDays=',$days,'&amp;departmentId=0&amp;seminarLocation=0&amp;keyword=&amp;courseType=',$type)" />

	<xsl:variable name="page-title">
		<xsl:choose>
			<xsl:when test="$type = 'all'">
				All
			</xsl:when>
			<xsl:when test="$type = 'cme'">
				CME Courses
			</xsl:when>
			<xsl:when test="$type = 'gran'">
				Grand Rounds
			</xsl:when>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="seminars-node">
		<xsl:copy-of
			select="document(concat('cocoon://apps/seminars/',$type,'/',$cYear,'/',$cMonth,'/',$cDay,'/',$days,'.xml'))" />
	</xsl:variable>

	<xsl:variable name="seminars-formatted">
		<xsl:apply-templates select="$seminars-node//h:div[@class='eventInfo']" />
	</xsl:variable>

	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$page-title" />
				</title>
			</head>
			<body>
				<h3>
					<xsl:value-of select="$page-title" />
				</h3>
				<xsl:copy-of select="$seminars-formatted" />
				<a href="{$moreUrl}">
					More
					<xsl:value-of select="$page-title" />
					»
				</a>
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
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{$moreUrl}">
					<xsl:value-of select="$title" />
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:div[@class='eventInfo']">
		<xsl:variable name="title" select="h:div[@class='eventTitle']" />
		<xsl:variable name="url" select=".//h:a[1]/@href" />
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