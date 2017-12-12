<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:openSearch="http://a9.com/-/spec/opensearch/1.1/"
    xmlns:media="http://search.yahoo.com/mrss/"
    xmlns:yt="http://www.youtube.com/xml/schemas/2015"
    xmlns:gd="http://schemas.google.com/g/2005"
    exclude-result-prefixes="atom h openSearch media yt gd"
    version="2.0">
    
    <xsl:template match="atom:feed">
        <xsl:for-each select="atom:entry[position() &lt; 5 and position() mod 2 = 1]">
            <div class="pure-g">
                <xsl:call-template name="entry">
                    <xsl:with-param name="entry" select="self::node()"/>
                </xsl:call-template>
                <xsl:call-template name="entry">
                    <xsl:with-param name="entry" select="following-sibling::atom:entry[1]"/>
                </xsl:call-template>
            </div>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="child::node()|attribute::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template name="entry">
        <xsl:param name="entry"/>
        <div class="pure-u-1-2">
            <div class="module landing">
            <iframe type="text/html" src="//www.youtube.com/embed/{$entry/yt:videoId}" frameborder="0"/>
            <h3><xsl:value-of select="$entry/atom:title"/></h3>
            </div>
        </div>
    </xsl:template>
    
</xsl:transform>
