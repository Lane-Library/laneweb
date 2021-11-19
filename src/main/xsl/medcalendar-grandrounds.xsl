<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:f="https://lane.stanford.edu/functions"
    version="2.0">

    <!--
        LANEWEB-10831: alternative source for grand rounds data
        filter and transform https://medicinecalendars.stanford.edu/ event data 
    -->

    <xsl:template match="/">
        <html>
            <head>
                <title>medcalendar events</title>
            </head>
            <body>
                <!-- pull twice the number of GR events required (3) so seminars.js can hide today's past events and display upcoming ones  -->
                <xsl:apply-templates select="//vevent[f:isUpcomingGrandRound(.)][position() &lt;= 6]">
                </xsl:apply-templates>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="vevent">
        <div class="event seminar">
            <!--  hide events beyond the third so seminars.js can unhide them if needed -->
            <xsl:if test="position() > 3">
                <xsl:attribute name="style">display:none;</xsl:attribute>
            </xsl:if>
            <div class="date grandrounds-date">
                <div class="month">
                    <xsl:value-of select="format-date(f:dateString2Date(dtstart),'[MNn,3-3]')" />
                </div>
                <div class="day">
                    <xsl:value-of select="format-date(f:dateString2Date(dtstart),'[D,2]')" />
                </div>
            </div>
            <div>
                <p>
                    <a title="{concat(summary,' [', 'gran-seminar]')}" href="{url/@uri}">
                        <xsl:value-of select="summary" />
                    </a>
                    <br/>
                    <span class="time">
                        <xsl:value-of select="format-dateTime(f:dateString2DateTime(dtstart),'[h1]:[m01] [Pn,2-2]')" />
                        <xsl:text> - </xsl:text>
                        <xsl:value-of select="format-dateTime(f:dateString2DateTime(dtend),'[h1]:[m01] [Pn,2-2]')" />
                    </span>
                </p>
            </div>
        </div>
    </xsl:template>

    <!-- 
        filtering function: include upcoming events with "grand round" in title/summary
    -->
    <xsl:function name="f:isUpcomingGrandRound" as="xsd:boolean">
        <xsl:param name="event" as="element()?"/>
        <xsl:value-of select="contains(lower-case($event/summary/text()),'grand round') and f:dateString2Date($event/dtstart) >= current-date()"/>
    </xsl:function>

    <!--  parse xsd:date from a string  -->
    <xsl:function name="f:dateString2Date" as="xsd:date">
        <xsl:param name="dtString"/>
        <xsl:variable name="date">
            <xsl:choose>
                <xsl:when test="string-length($dtString) >= 8">
                    <xsl:value-of select="concat(substring($dtString, 1, 4), '-', substring($dtString, 5, 2), '-', substring($dtString, 7, 2))"/>
                </xsl:when>
                <xsl:otherwise>2000-01-01</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="xsd:date($date)"/>
    </xsl:function>

    <!--  parse xsd:dateTime from a string  -->
    <xsl:function name="f:dateString2DateTime" as="xsd:dateTime">
        <xsl:param name="dtString"/>
        <xsl:variable name="date" select="f:dateString2Date($dtString)"/>
        <xsl:variable name="time">
            <xsl:choose>
                <xsl:when test="string-length($dtString) = 15">
                    <xsl:value-of select="concat(substring($dtString, 10, 2), ':', substring($dtString, 12, 2), ':', substring($dtString, 14, 2))"/>
                </xsl:when>
                <xsl:otherwise>00:00:00</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="dateTime(xsd:date($date),xsd:time($time))"/>
    </xsl:function>

</xsl:stylesheet>