<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:h="http://www.w3.org/1999/xhtml" version="2.0">
    
    <xsl:param name="category"/>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="h:html">
        <xsl:apply-templates select="descendant::h:dl"/>
    </xsl:template>
    
    <xsl:template match="h:dl">
        <ul>
            <xsl:apply-templates select="h:dt[contains(comment(),'_show_me_')]"/>
            <li class="moreItem"><a href="/howto/index.html?category={$category}">More</a></li>
        </ul>
    </xsl:template>
    
    <xsl:template match="h:entry">
        <dt>
            <xsl:apply-templates/>
        </dt>
    </xsl:template>
    
</xsl:stylesheet>
