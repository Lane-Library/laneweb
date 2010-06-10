<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:dir="http://apache.org/cocoon/directory/2.0"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="dir h"
    version="2.0">
    
    <xsl:param name="page"/>
    
    <xsl:template match="/">
        <rss version="2.0">
            <channel>
                <title><xsl:value-of select="/doc/h:html/h:head/h:title"/></title>
                <link><xsl:value-of select="concat('http://lane.stanford.edu',$page)"/></link>
                <description><xsl:value-of select="/doc/h:html/h:head/h:title"/></description>
                <language>en-us</language>
                <lastBuildDate><xsl:value-of select="/doc/dir:file/@date"/></lastBuildDate>
                <ttl>1440</ttl>
                <image>
                    <title><xsl:value-of select="/doc/h:html/h:head/h:title"/></title>
                    <url>http://lane.stanford.edu/favicon.ico</url>
                    <link><xsl:value-of select="concat('http://lane.stanford.edu',$page)"/></link>
                </image>
                
                <xsl:variable name="content-table" select="/doc/h:html//h:table[contains(@class, 'striped')]"/>
                
                <!-- will only generate item list from grandrounds and laneclasses pages -->
                <xsl:choose>
                    <xsl:when test="contains($page,'grandrounds.html')">
                        <xsl:apply-templates select="$content-table[1]/h:tr[not(@class='tableHeader') and count(h:td) = count($content-table[1]/h:tr[@class='tableHeader']/h:td)]">
                            <xsl:with-param name="type" select="'grandrounds'"/>
                            <xsl:sort data-type="text" order="descending" select="h:td[@class='updateDate']"/>
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:when test="contains($page,'workshops/laneclasses.html')">
                        <xsl:apply-templates select="$content-table[1]/h:tr[not(h:td[.='inactive']) and not(@class='tableHeader') and count(h:td) = count($content-table[1]/h:tr[@class='tableHeader']/h:td)]">
                            <xsl:with-param name="type" select="'workshops'"/>
                            <xsl:sort data-type="text" order="ascending" select="h:td[@class='updateDate']"/>
                        </xsl:apply-templates>
                    </xsl:when>
                </xsl:choose>
            </channel>
        </rss>
    </xsl:template>
    
    <xsl:template match="h:tr">
        <xsl:param name="type"/>
        <xsl:variable name="header" select="../h:tr[@class='tableHeader']/h:td[not(@class='updateDate')]"/>
        <xsl:variable name="tds" select="h:td[not(@class='updateDate')]"/>
        
        <xsl:variable name="link">
            <xsl:choose>
                <xsl:when test="$type = 'grandrounds'">
                    <xsl:call-template name="process-link">
                        <xsl:with-param name="link" select="$tds[5]/h:a/@href"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'workshops'">
                    <xsl:call-template name="process-link">
                        <xsl:with-param name="link" select="$tds[2]/h:a/@href"/>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        
        <xsl:variable name="title">
            <xsl:choose>
                <xsl:when test="$type = 'grandrounds'">
                    <xsl:value-of select="$tds[2]/text()"/>
                </xsl:when>
                <xsl:when test="$type = 'workshops'">
                    <xsl:value-of select="$tds[2]/h:a/text()"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        
        <item>
            <title><xsl:value-of select="$title"/></title>
            <link><xsl:value-of select="$link"/></link>
            <description>
                <xsl:for-each select="$tds">
                    <xsl:variable name="pos" select="position()"/>
                    <xsl:choose>
                        <!-- simple text for td's w/o child nodes -->
                        <xsl:when test="count(*) = 0 and string-length(normalize-space()) &gt; 1">
                            <xsl:text><![CDATA[<b>]]></xsl:text><xsl:value-of select="$header[$pos = position()]"/><xsl:text><![CDATA[</b>: ]]></xsl:text>
                            <xsl:value-of select="."/>
                            <xsl:text><![CDATA[<br />]]></xsl:text>
                        </xsl:when>
                        <!-- td's w/ a elements in them -->
                        <xsl:when test="count(h:a) &gt; 0">
                            <xsl:for-each select="h:a">
                                <xsl:variable name="link">
                                    <xsl:call-template name="process-link">
                                        <xsl:with-param name="link" select="@href"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:text><![CDATA[<b>]]></xsl:text><xsl:value-of select="$header[$pos = position()]"/><xsl:text><![CDATA[</b>: <a href="]]></xsl:text>
                                <xsl:value-of select="$link"/>
                                <xsl:text><![CDATA[">]]></xsl:text>
                                <xsl:choose>
                                    <xsl:when test="string-length(normalize-space()) &gt; 1">
                                        <xsl:value-of select="."/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$link"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:text><![CDATA[</a><br />]]></xsl:text>
                            </xsl:for-each>
                        </xsl:when>
                        <!-- td's w/ spans or brs  -->
                        <xsl:when test="count(h:span) &gt; 0 or count(h:br) &gt; 0">
                            <xsl:text><![CDATA[<b>]]></xsl:text><xsl:value-of select="$header[$pos = position()]"/><xsl:text><![CDATA[</b>: ]]></xsl:text>
                            <xsl:value-of select="."/>
                            <xsl:text><![CDATA[<br />]]></xsl:text>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </description>
            <!-- link isn't unique enough for guid ... <guid><xsl:value-of select="$link"/></guid>-->
            <xsl:apply-templates select="h:td[@class='updateDate']"/>
        </item>
    </xsl:template>
    
    <xsl:template match="h:td[@class='updateDate']">
        <!--
            expecting input in one of two formats:
            1: 2008-09-04
            2: Thu, 07 Aug 2008 17:00:15 -0700
        -->
        <xsl:variable name="updateDate">
            <xsl:choose>
                <xsl:when test="string-length(.) = 10">
                    <xsl:value-of select="concat(format-date(.,'[FNn,*-3], [D01] [MNn,*-3] [Y]'),' 00:00:00 -0700')"/>
                </xsl:when>
                <xsl:when test="string-length(.) = 31">
                    <xsl:value-of select="."/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <pubDate>
            <xsl:value-of select="$updateDate"/>
        </pubDate>
    </xsl:template>
    
    <xsl:template name="process-link">
        <xsl:param name="link"/>
        <xsl:choose>
            <xsl:when test="starts-with($link,'/')">
                <xsl:value-of select="concat('http://lane.stanford.edu',$link)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$link"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>
