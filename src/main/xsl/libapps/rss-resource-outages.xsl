<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rss="http://purl.org/rss/1.0/" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml" version="2.0">

    <xsl:template match="/rss/channel">
        <xsl:choose>
            <xsl:when test="count(//item) > 0">
                <xsl:apply-templates select="item" />
            </xsl:when>
            <xsl:otherwise>
                <div class="module font-size-2xl">No resource outages at this time.</div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="item">
        <div>
            <xsl:attribute name="class">module scroll-margin-lg</xsl:attribute>
            <xsl:attribute name="id">
                <xsl:value-of select="substring-after(link, '=')" />
            </xsl:attribute>
            <h2 class="level4 border-thin border-thick">
                <xsl:value-of select="title"></xsl:value-of>
            </h2>
            <div>
                <xsl:value-of select="pubDate"></xsl:value-of>
            </div>
            <div class="space-top-2xl">
                <xsl:copy-of select="parse-xml-fragment(./description)" />
            </div>
        </div>
    </xsl:template>


</xsl:stylesheet>