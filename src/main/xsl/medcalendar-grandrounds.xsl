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
                <!-- pull twice the number of GR events required (3) so 
                     seminars.js hides today's past events and displays upcoming ones  -->
                <xsl:variable name="events">
                    <xsl:apply-templates select="//vevent[f:isUpcomingGrandRound(.)]"/>
                </xsl:variable>
                <xsl:for-each select="$events/h:div[position() &lt;= 6]">
                    <xsl:sort select="@data-sort" order="ascending"/>
                    <xsl:copy>
                     <xsl:copy-of select="attribute::node()"/>
                    <!--  hide events beyond the third so seminars.js can unhide them if needed -->
                    <xsl:if test="position() > 3">
                        <xsl:attribute name="style">display:none;</xsl:attribute>
                    </xsl:if>
                     <xsl:copy-of select="node()"/>
                    </xsl:copy>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="vevent">
        <xsl:variable name="display-date" select="f:getBestDate(.)"/>
        <div class="event seminar" data-sort="{$display-date}">
            <div class="date grandrounds-date">
                <div class="month">
                    <xsl:value-of select="format-date($display-date,'[MNn,3-3]')" />
                </div>
                <div class="day">
                    <xsl:value-of select="format-date($display-date,'[D,2]')" />
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
        some events have repeatable date (rdate) elements 
        examine rdate  elements and return rdate or dtstart
    -->
    <xsl:function name="f:getBestDate" as="xsd:date">
        <xsl:param name="event" as="element()?"/>
        <xsl:choose>
            <xsl:when test="count($event/rdate)">
                <xsl:variable name="closeset-rdate" select="$event/rdate[f:dateString2Date(.) >= current-date()][1]"/>
                <xsl:choose>
                    <xsl:when test="f:dateString2Date($closeset-rdate) >= current-date()">
                        <xsl:value-of select="f:dateString2Date($closeset-rdate)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="f:dateString2Date($event/dtstart)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="f:dateString2Date($event/dtstart)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <!-- 
        filtering function: include upcoming events with "grand round" in title/summary
    -->
    <xsl:function name="f:isUpcomingGrandRound" as="xsd:boolean">
        <xsl:param name="event" as="element()?"/>
        <xsl:value-of select="contains(lower-case($event/summary/text()),'grand round') and f:getBestDate($event) >= current-date()"/>
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