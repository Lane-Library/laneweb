<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="2.0">

    <xsl:param name="lane-login-expiration-date"/>
   
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

    <xsl:template match="h:strong[@id='persistent-login-expiration-day']">
        <xsl:copy>
            <xsl:choose>
               <xsl:when test="$lane-login-expiration-date">
                  <xsl:value-of select="$lane-login-expiration-date"/>  
            </xsl:when>
               <xsl:otherwise>
                   <xsl:apply-templates select="attribute::node() | child::node()"/>
               </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

</xsl:stylesheet>
