<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">
    
    <xsl:param name="banner"/>
    <xsl:variable name="empty" select="''"/>
    
    <xsl:variable name="banner-wanted">
        <xsl:choose>
            <xsl:when test="string-length($banner) = 0">1</xsl:when>
            <xsl:otherwise><xsl:value-of select="$banner"/></xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:template match="/">
        <xsl:apply-templates select="h:html/h:body/h:div[position() = $banner-wanted]"/>
    </xsl:template>
    
    <xsl:template match="/h:html/h:body/h:div">
        <html><body>
            <xsl:copy>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
            <!-- only construct navigation if there is more than one banner -->
            <xsl:if test="count(/h:html/h:body/h:div) &gt; 1">
                <xsl:call-template name="create-nav"/>
            </xsl:if>
        </body></html>
    </xsl:template>
    
    <!-- default element match, copies the element and applies templates on all childeren and attributes -->
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- default attribute match, copies the attribute -->
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="h:i">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:comment/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template name="create-nav">
        <ul class="banner-nav">
            <xsl:for-each select="/h:html/h:body/h:div">
                <li><a>
                    <xsl:attribute name="href" select="concat('?banner=',position())"/>
                    <xsl:if test="position() = $banner-wanted">
                        <xsl:attribute name="class" select="'banner-nav-active'"/>
                    </xsl:if>
                    <xsl:text> </xsl:text>
                </a></li>
            </xsl:for-each>
        </ul>
    </xsl:template>
    
</xsl:stylesheet>