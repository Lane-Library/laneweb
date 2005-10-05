<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="1.0">
    <xsl:template match="/">
        <html>
            <head>
                <title>eResources</title>
            </head>
            <body>
                <ul class="eresources">
                    <!--<xsl:apply-templates select="*/h:html/h:body/h:ul/h:li">-->
                     <xsl:apply-templates select="*/h:html/h:body/h:ul/h:li[not(preceding::h:li[@class='eresource']/@id = @id)]"> 
                        <xsl:sort select="translate(h:span/h:span[@class='filing'],' ','.')"/>
                    </xsl:apply-templates>
                </ul>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="h:li[@class='eresource']">
        <xsl:variable name="type" select="ancestor::h:html/h:head/h:title/text()"/>
        <xsl:variable name="type-code">
            <xsl:choose>
                <xsl:when test="$type='eJournals'">a</xsl:when>
                <xsl:when test="$type='Databases'">b</xsl:when>
                <xsl:when test="$type='eBooks'">c</xsl:when>
                <xsl:when test="$type='Clinical Calculators'">d</xsl:when>
                <!--<xsl:when test="$type='misc'">e</xsl:when>-->
            </xsl:choose>
        </xsl:variable>
        <xsl:copy>
            <xsl:attribute name="class">
                <xsl:value-of select="$type-code"/>
            </xsl:attribute>
            <xsl:apply-templates select="@id|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
