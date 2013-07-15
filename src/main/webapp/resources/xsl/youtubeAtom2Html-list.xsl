<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:openSearch="http://a9.com/-/spec/opensearch/1.1/"
    xmlns:media="http://search.yahoo.com/mrss/"
    xmlns:yt="http://gdata.youtube.com/schemas/2007"
    xmlns:gd="http://schemas.google.com/g/2005"
    exclude-result-prefixes="atom h openSearch media yt gd"
    version="2.0">
    
    <xsl:template match="atom:feed">
        <ul>
            <xsl:apply-templates select="atom:entry"/>
        </ul>
    </xsl:template>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="child::node()|attribute::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="atom:entry">
            <li>
                <xsl:call-template name="create-markup">
                    <xsl:with-param name="href" select="atom:link[@rel='alternate']/@href"/>
                    <xsl:with-param name="src" select="media:group/media:thumbnail[@yt:name='default']/@url"/>
                    <xsl:with-param name="title" select="atom:title"/>
                </xsl:call-template>
            </li>
    </xsl:template>
    
    <xsl:template name="create-markup">
        <xsl:param name="href"/>
        <xsl:param name="src"/>
        <xsl:param name="title"/>
        <div class="module">
            <a href="{$href}" title="{$title}"><img src="{$src}" alt="{$title}"/>
                <xsl:value-of select="$title"/></a>
        </div>
    </xsl:template>
    
</xsl:transform>
