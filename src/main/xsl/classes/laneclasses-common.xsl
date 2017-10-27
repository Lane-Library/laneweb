<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://lane.stanford.edu/laneclasses"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc"
    version="2.0">

	<xsl:variable name="description-length" select="number(50)"></xsl:variable>

    <xsl:template name="month">
        <xsl:variable name="month">
             <xsl:value-of select="substring-before(./lc:event_dates/lc:start_date[1]/text(),'/')" />
        </xsl:variable>
           <xsl:if test="$month = '1'">Jan</xsl:if>
            <xsl:if test="$month = '2'">Feb</xsl:if>
            <xsl:if test="$month = '3'">Mar</xsl:if>
            <xsl:if test="$month = '4'">Apr</xsl:if>
            <xsl:if test="$month = '5'">May</xsl:if>
            <xsl:if test="$month = '6'">Jun</xsl:if>
            <xsl:if test="$month = '7'">Jul</xsl:if>
            <xsl:if test="$month = '8'">Aug</xsl:if>
            <xsl:if test="$month = '9'">Sep</xsl:if>
            <xsl:if test="$month = '10'">Oct</xsl:if>
            <xsl:if test="$month = '11'">Nov</xsl:if>
            <xsl:if test="$month = '12'">Dec</xsl:if>
    </xsl:template>

    <xsl:template name="day">
        <xsl:variable name="day">
             <xsl:value-of select="substring-before(substring-after(./lc:event_dates/lc:start_date[1]/text(),'/'),'/')" />
        </xsl:variable>
           <xsl:value-of select="format-number( number( $day), '00')"/>
    </xsl:template>


    <xsl:template name="start-time">
    <xsl:variable name="hour">
             <xsl:value-of select="substring-before(substring-after(./lc:event_dates/lc:start_date[1]/text(),' '),':00 ')" />
             </xsl:variable>
        <xsl:value-of select="concat($hour, ' ' , lower-case(substring-after(./lc:event_dates/lc:end_date[1]/text(),':00 ')))"/>
    </xsl:template>

    <xsl:template name="end-time">
        <xsl:variable name="hour">
            <xsl:value-of select="substring-before(substring-after(./lc:event_dates/lc:end_date[1]/text(),' '),':00 ')" />
        </xsl:variable>
        <xsl:value-of select="concat($hour, ' ', lower-case(substring-after(./lc:event_dates/lc:end_date[1]/text(),':00 ')))"/>
    </xsl:template>

    <xsl:template name="firstWords">
        <xsl:param name="value"/>
        <xsl:param name="count"/>
        <xsl:variable name="words" select="tokenize($value, ' ')"/>
        <xsl:for-each select="$words[position() &lt;= $count]">
            <xsl:value-of select="."/>
            <xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:template>

	<xsl:template match="lc:event_instructors">
		<xsl:variable name="nodesCount" select="count(./lc:instructor)" />
		<div class="instructor">
		Instructor(s): 
		<xsl:for-each select="./lc:instructor">
			<xsl:variable name="position" select="position()" />
				<div>
				<b>
				<xsl:value-of select="lc:fname" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="lc:lname" />
				  <xsl:if test="$nodesCount != $position and $nodesCount != 1">
					<xsl:text> &amp; </xsl:text>
				</xsl:if> 
				</b>
				</div>
		</xsl:for-each>
	</div>
	</xsl:template>
	
	
	<xsl:template name="remainingSeats">
		<div class="register">
			<div>
			<xsl:choose>
				<xsl:when test="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats = '0'">
				<a href="https://www.onlineregistrationcenter.com/waitinglistform.asp?m=257&amp;c={lc:module_id}" class="button alt1 waitlist">
					<span>Wait List</span>
					<i class="icon fa fa-arrow-right"/>
				</a>
				</xsl:when>
				<xsl:otherwise>
				<a href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={lc:module_id}" class="button alt1">
					<span>Register</span>
					<i class="icon fa fa-arrow-right"/>
				</a>
				<div>Seats left: <xsl:value-of select="/doc/noncached-classes/eventlist/event[eventid = current()/lc:module_id]/seats"/></div>
				</xsl:otherwise>			
			</xsl:choose>
			</div>
		</div>
	</xsl:template>
	

</xsl:stylesheet>