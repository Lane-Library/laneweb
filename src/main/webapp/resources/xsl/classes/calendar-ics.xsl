<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:lc="http://lane.stanford.edu/laneclasses"
	exclude-result-prefixes="lc" version="2.0">

   <xsl:param name="class-id" />  
   <xsl:param name="email"/>  
   
  <xsl:variable name="today">
        <xsl:value-of select="year-from-date(current-date())"/>
        <xsl:value-of select="format-number(number(month-from-date(current-date())), '00')"/>
        <xsl:value-of select="format-number(number(day-from-date(current-date())), '00')"/>
        <xsl:text>T040000Z</xsl:text>
    </xsl:variable>
    
<xsl:variable name="currentYear">
		<xsl:value-of select="year-from-date(current-date())"></xsl:value-of>
	</xsl:variable>
	
		<xsl:variable name="previousYear">
		<xsl:value-of select="number($currentYear -1)"/>
	</xsl:variable>


	<xsl:template match="/lc:classes">
		<xsl:text>BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//lane.stanford.edu//Classes Events v1.0//EN
METHOD:PUBLISH
TZID:California-Los_Angeles
</xsl:text>
<xsl:choose>
<xsl:when test="$email">
<xsl:text>X-WR-CALNAME:My Lane Classes</xsl:text>
			</xsl:when>
			<xsl:otherwise>
<xsl:text>X-WR-CALNAME:Lane Library Classes</xsl:text>
			</xsl:otherwise>
</xsl:choose>
<xsl:text>
X-WR-TIMEZONE:America/Los_Angeles
X-WR-CALDESC:Lane Medical Library offers an array of courses and presentations, including: database searching (PubMed, SCOPUS, etc.); reference/PDF/bibliography management (EndNote, Zotero); writing (grants, biomedical and scientific manuscripts); and  local tours (School of Medicine architectural history).  Registration is free to all Stanford affiliates (including SHC and LPCH).</xsl:text>
	<xsl:choose>
			<xsl:when test="$class-id">
				<xsl:call-template name="VEVENT" ><xsl:with-param name="classId" select="$class-id" /></xsl:call-template>
			</xsl:when>
			 <xsl:when test="$email">
				<xsl:for-each select="./lc:event_data[./lc:event_instructors/lc:instructor/lc:email[ contains(.,$email)]]">
				   <xsl:call-template name="VEVENT" ><xsl:with-param name="classId" select="./lc:module_id/text()" /></xsl:call-template>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="./lc:event_data">
				  	<xsl:call-template name="VEVENT" ><xsl:with-param name="classId" select="./lc:module_id/text()" /></xsl:call-template>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>
END:VCALENDAR</xsl:text>
	</xsl:template>
	

	<xsl:template name="VEVENT">
	<xsl:param name="classId" />
	<xsl:param name="date"  select="./lc:event_dates/lc:start_date[1]/text()"/>
	<xsl:if test="contains($date, $currentYear) or contains($date, $previousYear) ">
	<xsl:text>
BEGIN:VEVENT
UID:LANE_CLASS_</xsl:text><xsl:value-of select="$classId"/><xsl:text>@lane.stanford.edu
URL:http://lane.stanford.edu/classes-consult/laneclass.html?class-id=</xsl:text><xsl:value-of select="$classId"/><xsl:text>
DTSTAMP:</xsl:text><xsl:value-of select="$today"/><xsl:text>
DTSTART:</xsl:text>
		<xsl:call-template name="formatDate">
			<xsl:with-param name="dateTime"
				select="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_dates/lc:start_date/text()" />
		</xsl:call-template>
		<xsl:text>
DTEND:</xsl:text>
		<xsl:call-template name="formatDate">
			<xsl:with-param name="dateTime"
				select="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_dates/lc:end_date/text()" />
		</xsl:call-template>
		<xsl:text>
SUMMARY:</xsl:text>
		<xsl:value-of
			select="replace(/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_name/text(), ',','\\,')" />
		<xsl:text>
DESCRIPTION:</xsl:text>
		<xsl:value-of select="replace(concat(substring(normalize-space(/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_description), 1, 150), '....'), ',','\\,')" />
	<xsl:choose>
		<xsl:when test="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:speaker/text() != ''">
			<xsl:text>
ORGANIZER:</xsl:text>
			<xsl:value-of
				select="replace(/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:speaker/text(), ',' , '\\,' )" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>
ORGANIZER;CN=</xsl:text>
			<xsl:apply-templates
				select="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_instructors/lc:instructor" />
			<xsl:text>:MAILTO:</xsl:text>
			<xsl:choose>
				<xsl:when
					test="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_instructors/lc:instructor/lc:email/text() != ''">
					<xsl:value-of
						select="/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:event_instructors/lc:instructor/lc:email/text()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>LaneAskUs@stanford.edu</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text>
LOCATION:</xsl:text><xsl:value-of select="replace(/lc:classes/lc:event_data/lc:module_id[ ./text() = $classId]/../lc:venue/lc:venue_name/text(), ',' , '\\,' )" />
		<xsl:text>
END:VEVENT</xsl:text>
	</xsl:if>
	</xsl:template>

	<xsl:template match="lc:instructor">
		<xsl:variable name="position" select="position()"/>
		<xsl:variable name="last" select="last()"/>
		<xsl:for-each select=".">
			<xsl:value-of select="replace(lc:fname/text(), ',' , '\\,' )"/> 
					<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="replace(lc:lname/text(), ',' , '\\,' )"/>
			<xsl:if test="$position != $last" >
				<xsl:text>&#160; &amp; &#160;</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>	

	<xsl:template name="formatDate">
		<xsl:param name="dateTime" />
		<xsl:variable name="month">
			<xsl:value-of
				select="format-number( number(substring-before($dateTime,'/')),'00')" />
		</xsl:variable>
		<xsl:variable name="day">
			<xsl:value-of
				select="format-number(number(substring-before(substring-after($dateTime,'/'),'/')), '00')" />
		</xsl:variable>
		<xsl:variable name="year">
			<xsl:value-of
				select="substring-after(substring-after(substring-before($dateTime,' '),'/'),'/')" />
		</xsl:variable>
		<xsl:variable name="time">
			<xsl:value-of select="substring-after($dateTime,' ')" />
		</xsl:variable>
		<xsl:variable name="hour">
			<xsl:choose>
				<xsl:when test="substring-after($time,' ') = 'PM' and substring-before($time,':') != '12'">
					<xsl:value-of select="number(substring-before($time,':'))+12" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="substring-before($time,':')" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="mm">
			<xsl:value-of select="substring-before(substring-after($time,':'),':')" />
		</xsl:variable>
		<xsl:value-of select="$year" />
		<xsl:value-of select="$month" />
		<xsl:value-of select="$day" />
		<xsl:text>T</xsl:text>
		<xsl:value-of select="format-number( number($hour), '00')" />
		<xsl:value-of select="$mm" />
		<xsl:text>00</xsl:text>
	</xsl:template>

</xsl:stylesheet>
