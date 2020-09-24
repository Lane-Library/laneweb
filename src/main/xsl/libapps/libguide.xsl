<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h"
>
   <xsl:param name="laneguide-uri" />
   <xsl:variable name="laneguide-dns">
      <xsl:value-of select="substring-before( substring-after( $laneguide-uri , '://'), ':')"/>
   </xsl:variable>
   
   <xsl:template match="h:html">
      <html>
         <head>
            <xsl:copy-of select="h:head/h:title"/>
            <xsl:apply-templates select="h:head/h:link"/>
            <link rel="stylesheet" type="text/css" href="/resources/css/lane-all.css?${project.version}-${git.commit.id.abbrev}" />
            <link rel="stylesheet" type="text/css" href="//libapps.s3.amazonaws.com/sites/18925/include/libapps.css"/>
            <xsl:apply-templates select="h:head/h:script"/> 
         </head>
         <body>
            <xsl:apply-templates select="h:body/h:div[@class = 'content centered']/*"/>
         </body>
      </html>
   </xsl:template>
  
  
   <xsl:template match="child::node()">
      <xsl:copy>
         <xsl:apply-templates select="attribute::node()|child::node()"/>
      </xsl:copy>
   </xsl:template>
   <xsl:template match="attribute::node()">
      <xsl:copy-of select="."/>
   </xsl:template>
   
  <xsl:template match="h:script">
      <xsl:copy>
         <xsl:if test="@src != ''">
          <xsl:copy-of select="@*"/>
               <xsl:if test="starts-with(@src, '/web/')">
                  <xsl:attribute name="src" select="replace(@src, '/web/', concat ('//', $laneguide-dns ,'/web/'))"/>
               </xsl:if>               
         </xsl:if>
         <xsl:copy-of select="replace(text(), '/web/',  concat ('//', $laneguide-dns ,'/web/'))"/>
      </xsl:copy>
   </xsl:template>
    
   <xsl:template match="h:link">
      <xsl:if test="@rel='stylesheet' and  not(starts-with(@href,'//libapps.s3.amazonaws.com'))">
         <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:choose>
               <xsl:when test="@href != ''  and starts-with(@href, '/web/')">
                  <xsl:attribute name="href" select="replace(@href, '/web/',  concat ('//', $laneguide-dns ,'/web/'))"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:attribute name="href" select="@href"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:copy>
      </xsl:if>
   </xsl:template>
   
   <xsl:template match="h:a">
      <xsl:copy>
         <xsl:copy-of select="@*"/>
         <xsl:choose>
            <xsl:when test="starts-with(@href,  concat ('https://', $laneguide-dns ,'/'))">
               <xsl:attribute name="href" select="replace(@href, concat ('https://', $laneguide-dns ,'/') ,'/libguides/')"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:attribute name="href" select="@href"/>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:apply-templates select="*|text()"/>
      </xsl:copy>
   </xsl:template>
   
   
 <xsl:template match="h:li[@id='s-lg-guide-header-url']/h:span[@class='s-lg-text-greyout']">
      <xsl:copy>
         <xsl:attribute name="class" select="@class"/>
         <xsl:value-of select="replace(./text(), concat ( $laneguide-dns ,'/') ,'lane.stanford.edu/libguides/')"/>
      </xsl:copy> 
   </xsl:template> 
   
</xsl:stylesheet>