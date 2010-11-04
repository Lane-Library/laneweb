<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    version="2.0">

    <xsl:variable name="now">
        <xsl:value-of select="format-dateTime(current-dateTime(), '[Y][M01][D01][H01][m01]')" />
    </xsl:variable>



    <xsl:template match="/">
        <classes>
            <xsl:apply-templates select="/classes" />
         </classes>
    </xsl:template>


    <xsl:template match="class">
      
        <xsl:variable name="time">
            <xsl:if test="contains(./time-end, 'pm')">
                <xsl:value-of select="format-number( number( translate(substring-before(./time-end , 'pm'), '\:', ''))+1200 , '0000')" />
            </xsl:if>
            <xsl:if test="contains(./time-end, 'am')">
                <xsl:value-of select="format-number( number( translate(substring-before(./time-end , 'am'), '\:', '')) , '0000')" />
            </xsl:if>
            <xsl:if test="contains(./time-end, 'Noon')">
                <xsl:value-of select="number(1200)"/>
            </xsl:if>
        </xsl:variable>
        
        <xsl:variable name="day">
            <xsl:value-of
                select="format-number(number(./day/text()), '00')" />
        </xsl:variable>
        <xsl:variable name="month">
            <xsl:value-of select="./month" />
        </xsl:variable>

        <xsl:variable name="year">
            <xsl:value-of select="./year/text()"/>
        </xsl:variable>
        

        <xsl:variable name="monthNum">
            <xsl:if test="$month = 'Jan'">01</xsl:if>
            <xsl:if test="$month = 'Feb'">02</xsl:if>
            <xsl:if test="$month = 'Mar'">03</xsl:if>
            <xsl:if test="$month = 'Apr'">04</xsl:if>
            <xsl:if test="$month = 'May'">05</xsl:if>
            <xsl:if test="$month = 'Jun'">06</xsl:if>
            <xsl:if test="$month = 'Jul'">07</xsl:if>
            <xsl:if test="$month = 'Aug'">08</xsl:if>
            <xsl:if test="$month = 'Sep'">09</xsl:if>
            <xsl:if test="$month = 'Oct'">10</xsl:if>
            <xsl:if test="$month = 'Nov'">11</xsl:if>
            <xsl:if test="$month = 'Dec'">12</xsl:if>
        </xsl:variable>

        <xsl:variable name="dateTime">
            <xsl:value-of select="concat($year, $monthNum, $day, $time)" />
        </xsl:variable>


        <xsl:if test="$now &lt; $dateTime">
                <xsl:copy-of select="self::node()"/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
