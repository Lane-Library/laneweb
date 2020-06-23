<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">

   <xsl:import href="libcal-common.xsl"/>

   <xsl:param name="class-id"/>

   <xsl:template match="/classes">
      <html>
         <body>
            <xsl:apply-templates select="class[id = $class-id ]"/>
         </body>
      </html>
   </xsl:template>

   <xsl:template match="class">
      <xsl:variable name="title" select="title"/>
      <h4>
         <xsl:value-of select="title"/>
      </h4>
      <xsl:apply-templates select="description"/>
      <xsl:for-each select="/classes/class/title[ ./text() = $title]/..">
         <xsl:call-template name="class-detail"/>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="class-detail">
      <div class="class">
         <div class="pure-g">
            <div class="pure-u-1-4">
               <div class="date">
                  <xsl:apply-templates select="start"/>
                  <xsl:apply-templates select="event_dates"/>
               </div>
               <div class="venue">
                  <xsl:apply-templates select="location"/>
               </div>
            </div>
            <div class="pure-u-3-4">
               <div class="details">
                  <xsl:apply-templates select="presenter"/>
                  <xsl:apply-templates select="remainingSeats"/>
               </div>
            </div>
         </div>
      </div>
   </xsl:template>
   
   <xsl:template match="description">
      <div class="class-description">
         <xsl:copy-of select="node()"/>
      </div>
   </xsl:template>
</xsl:stylesheet>