<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc"
    version="2.0">
    
    <xsl:import href="laneclasses-common.xsl"/>    
    
    
    <xsl:template match="/lc:classes">
        <rss version="2.0">
            <channel>
                <title><xsl:text>Lane Classes</xsl:text></title>
                <link><xsl:text>http://lane.stanford.edu/classes-consult/laneclasses.html</xsl:text></link>
                <description><xsl:text>Lane Classes</xsl:text></description>
                <language><xsl:text>en-us</xsl:text></language>
                <ttl><xsl:text>1440</xsl:text></ttl>
                <image>
                    <title><xsl:text>Lane Classes</xsl:text></title>
                    <url>http://lane.stanford.edu/favicon.ico</url>
                    <link><xsl:text>http://lane.stanford.edu/classes-consult/laneclasses.html</xsl:text></link>
                </image>
                <xsl:apply-templates select="./lc:event_data"/>
                
            </channel>
        </rss>  
    </xsl:template>
    
    <xsl:template match="lc:event_data">
        
        <item>
            <title><xsl:value-of select="./lc:event_name/text()"/></title>
            <link>
                <xsl:value-of select="concat('http://lane.stanford.edu/classes-consult/laneclass.html?class-id=', ./lc:module_id/text())"/>
            </link>
            <description>
                <xsl:text><![CDATA[<b>]]>Date<![CDATA[</b>: ]]>
                    </xsl:text>
                        <xsl:call-template  name="month"/>
                <xsl:text> </xsl:text>
                <xsl:call-template  name="day"/>
                <xsl:text>, </xsl:text>
                <xsl:call-template  name="start-time"/>
                <xsl:text>-</xsl:text>
                <xsl:call-template  name="end-time"/>
                    <xsl:text>
                 <![CDATA[<br/>]]></xsl:text>
                <xsl:text><![CDATA[<b>]]>Presentation<![CDATA[</b>: ]]></xsl:text><xsl:value-of select="./lc:event_name/text()"/><xsl:text><![CDATA[<br/>]]></xsl:text>
                <xsl:text><![CDATA[<b>]]>Speaker<![CDATA[</b>: ]]></xsl:text>
                 <xsl:choose>
                    <xsl:when test="./lc:more_info_url">
                        <xsl:text><![CDATA[<a href="]]></xsl:text><xsl:value-of select="./lc:more_info_url"/><xsl:text><![CDATA[">]]></xsl:text>
                            <xsl:value-of select="./lc:speaker"/>
                        <xsl:text><![CDATA[</a>]]></xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="./lc:speaker"/>                        
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text><![CDATA[<br/><br/>]]></xsl:text>
            </description>
        </item>
    </xsl:template>
    
    
    
</xsl:stylesheet>
