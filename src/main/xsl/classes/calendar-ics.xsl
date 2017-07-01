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


	<xsl:template match="/classes">
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
				<xsl:apply-templates select="class[ id/text() = $class-id]"/>
			</xsl:when> 
			<xsl:when test="$email">
				<xsl:apply-templates select="class[./instructors//instructor/email = $email]"/>
			</xsl:when>
		 	<xsl:otherwise> 
				<xsl:apply-templates select="class"/>
			 </xsl:otherwise> 
		 </xsl:choose> 
		<xsl:text>
END:VCALENDAR</xsl:text>
	</xsl:template>
	

	<xsl:template match="class">
	<xsl:param name="year"  select="dates/date[1]/year"/>
	<xsl:text>
BEGIN:VEVENT
UID:LANE_CLASS_</xsl:text><xsl:value-of select="id"/><xsl:text>@lane.stanford.edu
URL:http://lane.stanford.edu/classes-consult/laneclass.html?class-id=</xsl:text><xsl:value-of select="id"/><xsl:text>
DTSTAMP:</xsl:text><xsl:value-of select="$today"/><xsl:text>
DTSTART:</xsl:text><xsl:value-of select="dates/date[1]/start-date-time"/><xsl:text>
DTEND:</xsl:text><xsl:value-of select="dates/date[1]/end-date-time"/>
<xsl:text> 
SUMMARY:</xsl:text>
		<xsl:value-of
			select="replace(./name/text(), ',','\\,')" />
		<xsl:text>
DESCRIPTION:</xsl:text>
		<xsl:value-of select="replace(concat(substring(normalize-space(./description), 1, 150), '....'), ',','\\,')" />
		<xsl:text>
ORGANIZER;CN=</xsl:text>
			<xsl:apply-templates select="./instructors/instructor" />
			<xsl:text>:MAILTO:</xsl:text>
			<xsl:choose>
				<xsl:when test="instructors/instructor/email/text() != ''">
					<xsl:value-of select="instructors/instructor/email/text() " />
				</xsl:when>
				<xsl:otherwise>
				<xsl:value-of select="instructors/instructor/email/text() " />
					<xsl:text>LaneAskUs@stanford.edu</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
	<xsl:text>
LOCATION:</xsl:text><xsl:value-of select="replace(./location/name/text(), ',' , '\\,' )" />
		<xsl:text>
END:VEVENT</xsl:text> 

 	</xsl:template>

	<xsl:template match="instructor">
		<xsl:variable name="position" select="position()"/>
		<xsl:variable name="last" select="last()"/>
		<xsl:for-each select=".">
			<xsl:value-of select="replace(fristName/text(), ',' , '\\,' )"/> 
					<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="replace(lastName/text(), ',' , '\\,' )"/>
			<xsl:if test="$position != $last" >
				<xsl:text>&#160; &amp; &#160;</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>	


</xsl:stylesheet>
