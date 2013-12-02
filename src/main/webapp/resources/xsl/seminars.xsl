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
				<a
					href="http://med.stanford.edu/seminars/validatecmecalendar.do?filter=true&amp;selMonth={$cMonth}&amp;selDay={$cDay}&amp;selYear={$cYear}&amp;futureNumberDays={$days}&amp;departmentId=0&amp;seminarLocation=0&amp;keyword=&amp;courseType={$type}">
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
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{$url}">
					<xsl:value-of select="$title" />
				</a>
			</xsl:when>
			<xsl:when test="string-length($url) &gt; 0">
				<a title="{concat($title,' [',$type, '-seminar]')}" href="{$url}">
					<xsl:value-of select="$title" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$title" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="h:div[@class='eventInfo']">
		<xsl:variable name="title" select="h:div[@class='eventTitle']" />
		<xsl:variable name="location" select="h:div[@class='eventText']" />
		<xsl:variable name="url" select=".//h:a[1]/@href" />
		<xsl:variable name="datelineNode"
			select="preceding-sibling::h:div[@class='dateline'][1]" />
		<xsl:variable name="date"
			select="replace($datelineNode/h:span[@class='date'],'\(.*\)','')" />
		<xsl:variable name="dept"
			select="substring-after($datelineNode/text()[2],'|')" />
		<xsl:variable name="cme-instructions" select="h:div[3]" />
		<xsl:variable name="anchor">
			<xsl:call-template name="build-anchor">
				<xsl:with-param name="url" select="$url" />
				<xsl:with-param name="title" select="$title" />
			</xsl:call-template>
		</xsl:variable>

		<div class="seminar">
			<div class="semTitle">
				<xsl:copy-of select="$anchor" />
			</div>
			<div class="semDept">
				<xsl:value-of select="$dept" />
			</div>
			<div class="semDate">
				<xsl:value-of select="$date" />
			</div>
			<div class="semLoc">
				<xsl:value-of select="$location" />
			</div>
			<div class="semInstr">
				<xsl:value-of select="$cme-instructions" />
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>