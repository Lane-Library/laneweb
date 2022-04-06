<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h">
   
  
   <xsl:template match="doc">
         <xsl:apply-templates select="h:html/h:body/h:header"/>
   </xsl:template>
   
     <xsl:template match="h:header">
      <xsl:copy>
         <xsl:apply-templates select="attribute::node()|child::node()"/>
      </xsl:copy>
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
   
   <xsl:template match="child::node()">
      <xsl:copy>
         <xsl:apply-templates select="attribute::node()|child::node()"/>
      </xsl:copy>
   </xsl:template>
   <xsl:template match="attribute::node()">
      <xsl:copy-of select="."/>
   </xsl:template>
</xsl:stylesheet>
   