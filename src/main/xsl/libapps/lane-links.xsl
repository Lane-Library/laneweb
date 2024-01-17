<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="h"
>
    
    <xsl:variable name="libapp-svg-url" >//libapps.s3.amazonaws.com/sites/18925/include/laneweb-svgs.svg#</xsl:variable>
  
   <xsl:template match="/">
      <xsl:apply-templates select="*"/>
   </xsl:template>
   
   <xsl:template match="h:a">
      <xsl:copy>
         <xsl:choose>
            <xsl:when test="starts-with(@href, '/') and not(starts-with(@href, '//')) ">
               <xsl:attribute name="href" select="concat('//lane.stanford.edu', @href)"/>
            </xsl:when>
            <xsl:otherwise>
             <xsl:attribute name="href" select="@href" />
            </xsl:otherwise>
         </xsl:choose>
           <xsl:attribute name="id" select="@id"/>
           <xsl:attribute name="class" select="@class"/>
         <xsl:apply-templates select="*|text()"/>
      </xsl:copy>
   </xsl:template>
   
   <xsl:template match="h:svg">
    <xsl:variable name="svg-value" select="substring-after(h:use/@href, '#')"></xsl:variable>
    <xsl:copy>
        <use>
            <xsl:attribute name="href" select="concat($libapp-svg-url, $svg-value)" />
        </use>
    </xsl:copy>
    </xsl:template>
   
     <!-- xincludes often include html/head and html/body, this ignores them-->
    <xsl:template match="h:html">
        <xsl:apply-templates select="h:body/child::node()"/>
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