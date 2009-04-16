<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h"
    version="2.0">
    
    <xsl:param name="a"/>
    <xsl:param name="q"/>
    <xsl:param name="t"/>
    <xsl:param name="m"/>
    <xsl:param name="type"/>
    


	<xsl:variable name="url">
		<xsl:if test="$type = 'search'">
		    <xsl:text>http://lane.stanford.edu/search.html?q=</xsl:text>
			<xsl:value-of select="$q"/>
			<xsl:if test="$t != ''">&amp;source=<xsl:value-of select="$t"/></xsl:if>
			<xsl:if test="$t = ''">&amp;source=all</xsl:if>
		</xsl:if>
		<xsl:if test="$type = 'ejounal'">
			<xsl:text>http://lane.stanford.edu/online/ejbrowse.html?</xsl:text>
			<xsl:if test="$a != ''"><xsl:text>a=</xsl:text><xsl:value-of select="$a"/></xsl:if>
			<xsl:if test="$a = ''"><xsl:text>all</xsl:text></xsl:if>
		</xsl:if>
		<xsl:if test="$type = 'mesh'">
			<xsl:text>http://lane-local.stanford.edu/online/ejsubjectbrowse.html?m=</xsl:text>
		    <xsl:value-of select="$m"/>
		</xsl:if>
	</xsl:variable>


	<xsl:variable name="option">
		<xsl:if test="$type = 'search'"><xsl:value-of select="$q"/></xsl:if>
		<xsl:if test="$type = 'mesh'"><xsl:value-of select="$m"/></xsl:if>
		<xsl:if test="$type = 'ejournal'">
			<xsl:if test="$a != ''"><xsl:value-of select="$a"/></xsl:if>
			<xsl:if test="$a = ''">All</xsl:if>
		</xsl:if>
	</xsl:variable>


    <xsl:template match="h:html">
        <rss version="2.0"> 
            <channel>
                <title><xsl:text>LaneConnex Search/Browse Results for </xsl:text><xsl:value-of select="$option"/></title>
                <link><xsl:value-of select="$url"/></link>
                <description><xsl:text>LaneConnex Search/Browse Results for </xsl:text><xsl:value-of select="$option"/></description>
                <language>en-us</language>
                <ttl>1440</ttl>
                <image>
                    <title><xsl:text>Laneconnex</xsl:text></title>
                    <url>http://lane.stanford.edu/favicon.ico</url>
                    <link><xsl:value-of select="$url"/></link>
                </image>
                <xsl:apply-templates select="h:body/h:dl/h:dd/h:ul/h:li"/>
            </channel>
        </rss>
    </xsl:template>
    
    
    <xsl:template match="h:li">
    	<item>
            <title>
            	<xsl:value-of select="../../preceding-sibling::h:dt[1]/text()"/>: <xsl:value-of select="h:a"/> - <xsl:value-of select="text()"/>
            </title>
            <link>http://lane.stanford.edu/apps/proxy/credential?url=<xsl:value-of select="h:a[1]/@href"/></link>
            <description>
            	<xsl:value-of select="../../preceding-sibling::h:dt[1]/text()"/>: <xsl:value-of select="h:a"/> - <xsl:value-of select="text()"/>
            </description>
    	 </item>
    </xsl:template>
    
    
</xsl:stylesheet>
