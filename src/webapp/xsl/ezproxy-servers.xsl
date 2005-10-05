<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns="http://www.w3.org/1999/xhtml" 
    xmlns:h="http://www.w3.org/1999/xhtml" 
    xmlns:exslt="http://exslt.org/common"
    exclude-result-prefixes="h exslt">
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:variable name="servers">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:apply-templates select="exslt:node-set($servers)/h:div[not(following::h:div=.)]">
            <xsl:sort select="."/>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template match="h:li[@class='_856u' and not(ancestor::h:ul[@class='mfhd']/h:li[@class='noproxy'])]">
        <xsl:variable name="no-scheme" select="substring-after(.,'://')"/>
        <div>
            <xsl:choose>
                <xsl:when test="contains($no-scheme,'/')">
                    <xsl:value-of select="substring-before($no-scheme, '/')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$no-scheme"/>
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    <xsl:template match="h:div">
        <xsl:value-of select="."/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>
