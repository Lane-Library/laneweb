<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns="http://www.w3.org/1999/xhtml"
                       xmlns:math="http://exslt.org/math"
                       exclude-result-prefixes="h math"
                       version="2.0">

    <!--
     return a random div element from a node set of divs
     Use:
        <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
           href="cocoon:/random?source=cocoon:/random-data.html"><xi:fallback/>
        </xi:include
        where random-data.html is a simple html doc with n child divs of the body tag
    -->    

    <xsl:template match="/">
        <xsl:call-template name="display-element">
            <xsl:with-param name="int" select="floor(math:random() * count(/h:html/h:body/h:div) + 1 )"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="display-element">
        <xsl:param name="int"/>
        <xsl:copy-of select="/h:html/h:body/h:div[$int]"/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>
