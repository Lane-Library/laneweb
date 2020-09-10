<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="h"
>
   <xsl:template match="/">
      <xsl:apply-templates select="*"/>
   </xsl:template>
   
   <xsl:template match="h:a">
      <xsl:copy>
         <xsl:choose>
            <xsl:when test="starts-with(@href, '/libguides')">
               <xsl:attribute name="href" select="replace(@href,'/libguides/','https://lane-stanford.libguides.com/')"/>
            </xsl:when>
             <xsl:when test="starts-with(@href, '/') and not(starts-with(@href, '//')) ">
               <xsl:attribute name="href" select="concat('//lane.stanford.edu', @href)"/>
            </xsl:when>
            <xsl:otherwise>
             <xsl:attribute name="href" select="@href" />
            </xsl:otherwise>
         </xsl:choose>
         <xsl:apply-templates select="*|text()"/>
      </xsl:copy>
   </xsl:template>
   
   
   <xsl:template match="child::node()">
      <xsl:copy>
         <xsl:apply-templates select="attribute::node()|child::node()"/>
      </xsl:copy>
   </xsl:template>
   <xsl:template match="attribute::node()">
      <xsl:copy-of select="."/>
   </xsl:template>
</xsl:stylesheet>