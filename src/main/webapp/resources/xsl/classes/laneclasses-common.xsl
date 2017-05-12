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

</xsl:stylesheet>