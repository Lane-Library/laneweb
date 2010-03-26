<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h" version="2.0">

    <xsl:param name="alpha"/>
    <xsl:param name="query"/>
    <xsl:param name="mesh"/>
    <xsl:param name="mode"/>
    <xsl:param name="type"/>

    <xsl:variable name="url">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">
                <xsl:text>/search.html?q=</xsl:text>
                <xsl:value-of select="$query"/>
            </xsl:when>
            <xsl:when test="string-length($mesh) = 0">
                <xsl:text>/online/</xsl:text>
                <xsl:choose>
                    <xsl:when test="$type = 'database'">
                        <xsl:text>db</xsl:text>
                    </xsl:when>
                    <xsl:when test="$type = 'book'">
                        <xsl:text>eb</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$type"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>browse.html?</xsl:text>
                <xsl:if test="$alpha != ''">
                    <xsl:text>a=</xsl:text>
                    <xsl:value-of select="$alpha"/>
                </xsl:if>
                <xsl:if test="$alpha = ''">
                    <xsl:text>all</xsl:text>
                </xsl:if>
            </xsl:when>
            <xsl:when test="$type = 'ej'">
                <xsl:text>/online/ejsubjectbrowse.html?m=</xsl:text>
                <xsl:value-of select="$mesh"/>
            </xsl:when>
            <xsl:when test="$type = 'book'">
                <xsl:text>/online/ebsubjectbrowse.html?m=</xsl:text>
                <xsl:value-of select="$mesh"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="title-string">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">Search</xsl:when>
            <xsl:otherwise>Browse</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <xsl:variable name="title-variable">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">
                <xsl:value-of select="$query"/>
            </xsl:when>
            <xsl:when test="string-length($mesh) != 0">
                <xsl:value-of select="$mesh"/>
            </xsl:when>
            <xsl:when test="string-length($alpha) != 0">
                <xsl:value-of select="$alpha"/>
            </xsl:when>
            <xsl:otherwise>All</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <xsl:template match="h:html">
        <rss version="2.0">
            <channel>
                <title>LaneConnex <xsl:value-of select="$title-string"/> Results for <xsl:value-of
                        select="$title-variable"/></title>
                <link>
                    <xsl:value-of select="$url"/>
                </link>
                <description>LaneConnex <xsl:value-of select="$title-string"/> Results for
                        <xsl:value-of select="$title-variable"/></description>
                <language>en-us</language>
                <ttl>1440</ttl>
                <image>
                    <title>Laneconnex</title>
                    <url>/favicon.ico</url>
                    <link>
                        <xsl:value-of select="$url"/>
                    </link>
                </image>
                <xsl:apply-templates select="h:body/h:dl/h:dd/h:ul/h:li"/>
            </channel>
        </rss>
    </xsl:template>


    <xsl:template match="h:li">
        <item>
            <title>
                <xsl:value-of select="../../preceding-sibling::h:dt[1]/text()"/>: <xsl:value-of
                    select="h:a"/> - <xsl:value-of select="text()"/>
            </title>
            <link>http://lane.stanford.edu/secure/proxy?url=<xsl:value-of
                    select="h:a[1]/@href"/></link>
            <description>
                <xsl:value-of select="../../preceding-sibling::h:dt[1]/text()"/>: <xsl:value-of
                    select="h:a"/> - <xsl:value-of select="text()"/>
            </description>
        </item>
    </xsl:template>


</xsl:stylesheet>
