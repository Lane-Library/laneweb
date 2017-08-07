<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://lane.stanford.edu/laneclasses"
    version="2.0">

	<xsl:template match="instructors">
		<xsl:variable name="nodesCount" select="count(./instructor)" />
		
	<div class="instructor">
		Instructor(s): 
		<xsl:for-each select="./instructor">
			<xsl:variable name="position" select="position()" />
				<div>
				<b>
				<xsl:value-of select="firstName" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="lastName" />
				  <xsl:if test="$nodesCount != $position and $nodesCount != 1">
					<xsl:text>,</xsl:text>
				</xsl:if> 
				</b>
				</div>
		</xsl:for-each>
	</div>
	</xsl:template>
	
	<xsl:template match="remainingSeats">
			<xsl:if test=" text() != '0'">
				<a href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={id}" class="button alt1">
					<span>Register</span>
					<i class="icon fa fa-arrow-right"/>
				</a>
				<div>Seats left: <xsl:value-of select="text()"/></div>
			</xsl:if>
			<xsl:if test=" text()  = '0'">
				<a href="https://www.onlineregistrationcenter.com/register/222/waitinglistform.asp?m=257&amp;c={id}" class="button alt1 waitList">
					<span>Wait List	</span>
					<i class="icon fa fa-arrow-right"/>
				</a>
			</xsl:if>
	</xsl:template>
	
	<xsl:template match="dates">
		<div class="date">
			<div class="month-day">
				<xsl:value-of select="./date/month"/><xsl:text>  </xsl:text><xsl:value-of select="date/day"/>
			</div>
			<div class="year">
				<xsl:value-of select="date/year"/>
			</div>
			<div class="time">
				<xsl:value-of select="date/time"/>
			</div>
		</div>
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
	</xsl:template>

</xsl:stylesheet>