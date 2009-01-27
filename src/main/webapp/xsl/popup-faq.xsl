<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="2.0">

    <xsl:param name="id"/>
    
    <xsl:variable name="title" select="/h:html/h:head/h:title/text()"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="descendant::h:div[@class='excerpt']"/>
    </xsl:template>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="h:div[@class='excerpt']">
        <dl>
            <dt>
                <a href="/howto/index.html?id={$id}">
                    <xsl:value-of select="$title"/>
                </a>
            </dt>
            <dd><xsl:apply-templates/></dd>
        </dl>
    </xsl:template>

</xsl:stylesheet>
