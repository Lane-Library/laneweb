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
                
                <xsl:apply-templates select="/doc/h:html//h:ul[@class='type1']/h:li"/>
                
            </channel>
        </rss>
    </xsl:template>
    
    <xsl:template match="h:li">
        <xsl:variable name="link">
            <xsl:call-template name="process-link">
                <xsl:with-param name="link" select="./h:div/h:h4/h:a/@href"/>
            </xsl:call-template>
        </xsl:variable>
        
        
        <item>
            <title><xsl:value-of select="./h:div/h:h4/h:a/text()"/></title>
            <link><xsl:value-of select="$link"/></link>
            <description>
                <xsl:text><![CDATA[<b>]]>Date<![CDATA[</b>: ]]></xsl:text><xsl:value-of select="./h:div[contains(@class,'yui-u first')]"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                <xsl:text><![CDATA[<b>]]>Presentation<![CDATA[</b>: ]]></xsl:text><xsl:value-of select="./h:div/h:h4/h:a/text()"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                <xsl:text><![CDATA[<b>]]>Speaker<![CDATA[</b>: ]]></xsl:text>
                 <xsl:choose>
                    <xsl:when test="./h:div/h:div[@class='lecturer']/h:a">
                        <xsl:text><![CDATA[<a href="]]></xsl:text><xsl:value-of select="./h:div/h:div[@class='lecturer']/h:a/@href"/><xsl:text><![CDATA[">]]></xsl:text>
                            <xsl:value-of select="./h:div/h:div[@class='lecturer']/h:a/text()"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                        <xsl:text><![CDATA[</a>]]></xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="./h:div/h:div[@class='lecturer']/text()"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:if test="./h:div/h:div[@class='affiliation']/text()">
                    <xsl:text><![CDATA[<b>]]>Speaker's Title<![CDATA[</b>: ]]></xsl:text><xsl:value-of select="./h:div/h:div[@class='affiliation']/text()"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                </xsl:if>
                <xsl:text><![CDATA[<b>]]>QuickTime Video<![CDATA[</b>: <a href="]]></xsl:text><xsl:value-of select="$link"/><xsl:text><![CDATA["/>]]></xsl:text> <xsl:value-of select="$link"/><xsl:text> <![CDATA[</a><br/><br/>]]></xsl:text>
            </description>
        </item>
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
