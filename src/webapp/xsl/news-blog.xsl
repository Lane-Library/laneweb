<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    exclude-result-prefixes="h xi"
    version="1.0">
    
    <xsl:param name="type"/>
    <xsl:param name="id"/>
    
    <xsl:template match="h:ul[parent::h:body]">
        <xsl:choose>
            <xsl:when test="$id != ''">
                <xsl:apply-templates select="h:li[@id = $id]" mode="central"/>
            </xsl:when>
            <xsl:when test="$type='Central News'">
                <xsl:apply-templates select="h:li[.//h:li[@class='category' and text() = $type]]" mode="central"/>
            </xsl:when>
            <xsl:otherwise>
                <ul>
                    <xsl:apply-templates select="h:li[.//h:li[@class='category' and text() = $type]]" mode="list" />
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:li" mode="central">
        <div class="newsItem">
            <h3>
                <xsl:choose>
                    <xsl:when test="h:ul/h:li[@class='more']/node()">
                        <xsl:apply-templates select="h:ul/h:li[@class='more']/node()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="node()[1]"/>
                    </xsl:otherwise>
                </xsl:choose>
                </h3>
            <xsl:apply-templates select="h:ul/h:li[@class = 'body']/*"/>
        </div>
    </xsl:template>
    
    <xsl:template match="h:li" mode="list">
        <li>
            <xsl:choose>
                <xsl:when test="h:ul/h:li[@class='more']/node()">
                    <xsl:apply-templates select="h:ul/h:li[@class='more']/node()"/>
                </xsl:when>
                <xsl:otherwise>
                    <a href="news.html?id={@id}"><xsl:apply-templates select="node()[1]"/></a>
                </xsl:otherwise>
            </xsl:choose>
        </li>
    </xsl:template>
        
    <xsl:template match="h:html|h:body">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="h:head"/>
    
    
    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>
