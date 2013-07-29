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
        <xsl:variable name="_2013" select="atom:entry[starts-with(yt:recorded,'2013')]"/>
        <xsl:for-each select="$_2013[position() mod 2 = 1]">
            <div class="yui3-g">
            <xsl:call-template name="entry">
                <xsl:with-param name="entry" select="self::node()"/>
            </xsl:call-template>
            <xsl:call-template name="entry">
                <xsl:with-param name="entry" select="following-sibling::node()[starts-with(yt:recorded,'2013')][1]"/>
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
        <xsl:if test="$entry">
            <div class="yui3-u-1-2">
                <xsl:call-template name="create-markup">
                    <xsl:with-param name="href" select="$entry/atom:link[@rel='alternate']/@href"/>
                    <xsl:with-param name="src" select="$entry/media:group/media:thumbnail[@yt:name='mqdefault']/@url"/>
                    <xsl:with-param name="title" select="$entry/atom:title"/>
                    <xsl:with-param name="recorded" select="$entry/yt:recorded"/>
                    <xsl:with-param name="description" select="$entry/media:group/media:description"/>
                </xsl:call-template>
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="create-markup">
        <xsl:param name="href"/>
        <xsl:param name="src"/>
        <xsl:param name="title"/>
        <xsl:param name="recorded"/>
        <xsl:param name="description"/>
        <div class="module">
            <a style="border-bottom:none" href="{$href}" title="{$title}"><img style="width:100%;" class="drop-shadow-noimg" src="{$src}" alt="{$title}"/></a>
            <h5><xsl:value-of select="$title"/></h5>
            <p class="detailInfo"><xsl:value-of select="$recorded"/></p>
            <div><xsl:value-of select="$description"/></div>
        </div>
    </xsl:template>
    
</xsl:transform>
