<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns="http://www.w3.org/1999/xhtml"
                       xmlns:java="http://xml.apache.org/xalan/java/edu.stanford.laneweb.xslt.LanewebXSLTExtensions"
                       exclude-result-prefixes="h java"
                       version="1.0">
    
    <xsl:param name="keywords"/>
    <xsl:param name="label-element"/>
    <xsl:param name="label"/>
    
    <xsl:template match="h:html">
        <html>
            <head>
                <xsl:copy-of select="h:head/h:title"/>
            </head>
            <body>
                <xsl:apply-templates select="h:body/h:ul"/>
             </body>
        </html>
    </xsl:template>
    
    <xsl:template match="h:ul">
        <xsl:variable name="matches">
            <xsl:apply-templates select="h:li[java:contains(self::h:li,$keywords)]"/>
        </xsl:variable>
        <xsl:if test="string-length($matches) &gt; 0">
            <xsl:if test="$label != '' and $label-element != ''">
            <xsl:element name="{$label-element}"><xsl:value-of select="$label"/></xsl:element>
                </xsl:if>
        <ul>
            <xsl:copy-of select="$matches"/>
        </ul>
            </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:li">
        <li><a href="/howto/index.html?id={@id}"><xsl:value-of select="text()"/></a></li>
    </xsl:template>
    
</xsl:stylesheet>
