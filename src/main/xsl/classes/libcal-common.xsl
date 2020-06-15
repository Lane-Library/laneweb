<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
   <xsl:template match="url">
      <xsl:variable name="title" select="../title"/>
      <xsl:choose>
         <xsl:when test="count(//class[title = $title]) &gt; 1">
            <xsl:value-of select="concat('/classes-consult/laneclass.html?class-id=', ../id)"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="public"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   <xsl:template match="start">
      <div class="month-day">
         <xsl:value-of select="month"/>
         <xsl:text> </xsl:text>
         <xsl:value-of select="day"/>
      </div>
      <div class="year">
         <xsl:value-of select="year"/>
      </div>
      <div class="time">
         <xsl:value-of select="hour"/>
         <xsl:text> – </xsl:text>
         <xsl:value-of select="../end/hour"/>
      </div>
   </xsl:template>
   <xsl:template match="location">
      <div class="location">
         <xsl:value-of select="."/>
         <xsl:text>&#160;&#160;&#160;</xsl:text>
         <i class="fa fa-map-marker fa-2x"></i>
      </div>
   </xsl:template>
   <xsl:template match="presenter">
      <div class="instructor">
         Instructor(s):
         <div>
            <b>
               <xsl:value-of select="."/>
            </b>
         </div>
      </div>
   </xsl:template>
   <xsl:template match="remainingSeats">
      <div class="register">
         <div>
            <a class="button alt1">
               <xsl:attribute name="href">
               <xsl:apply-templates select="../url/public"/>                               
            </xsl:attribute>
               <span>Register</span>
               <i class="icon fa fa-arrow-right"/>
            </a>
            <div class="remaining-seats">
               Seats left:
               <xsl:value-of select="."/>
            </div>
         </div>
      </div>
   </xsl:template>
</xsl:stylesheet>