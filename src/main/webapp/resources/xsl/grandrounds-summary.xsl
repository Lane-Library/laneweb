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
    
    
     <xsl:template match="doc">
        <xsl:apply-templates select="h:html[1]"/>
    </xsl:template>
    
    <xsl:template match="h:a[@id='medicine']/@href">
    <xsl:attribute name="href">
        <xsl:value-of select="/doc/h:html[2]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/@href"/>
    </xsl:attribute>
    <xsl:value-of select="/doc/h:html[2]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/text()" />
    </xsl:template>
    
    <xsl:template match="h:a[@id='em-medecin']/@href">
    <xsl:attribute name="href">
        <xsl:value-of select="/doc/h:html[3]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/@href"/>
    </xsl:attribute>
    <xsl:value-of select="/doc/h:html[3]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/text()" />
    </xsl:template>
    
    <xsl:template match="h:a[@id='pediatrics']/@href">
    <xsl:attribute name="href">
        <xsl:value-of select="/doc/h:html[4]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/@href"/>
    </xsl:attribute>
    <xsl:value-of select="/doc/h:html[4]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/text()" />
    </xsl:template>
    
    <xsl:template match="h:a[@id='otolaryngology']/@href">
    <xsl:attribute name="href">
        <xsl:value-of select="/doc/h:html[5]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/@href"/>
    </xsl:attribute>
    <xsl:value-of select="/doc/h:html[5]//h:div[@class='module']//h:ul/h:li[1]/h:div/h:h4[@class='title']/h:a[1]/text()" />
    </xsl:template>
    
    
    
</xsl:stylesheet>