<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns="http://www.w3.org/1999/xhtml" version="2.0">
  
	<xsl:import href="libcal-common.xsl" /> 
 
  
   <xsl:template match="/classes">
      <html>
         <head>
            <title>classes</title>
         </head>
         <body>
            <xsl:apply-templates/>
         </body>
      </html>
   </xsl:template>
   
   <xsl:template match="class">
      <div class="class">
         <div class="pure-g">
            <div class="pure-u-1-4">
               <div class="date">
                  <xsl:apply-templates select="start"/>
               </div>
               <div class="venue">
                  <xsl:apply-templates select="location"/>
               </div>
            </div>
            <div class="pure-u-3-4">
               <xsl:apply-templates select="title"/>
               <div class="pure-g">
                  <div class="pure-u-1-4">
                     <xsl:apply-templates select="presenter"/>
                     <xsl:apply-templates select="remainingSeats"/>
                  </div>
                  <div class="pure-u-3-4">
                     <xsl:value-of select="short_description"/>
                  </div>
               </div>
            </div>
         </div>
      </div>
   </xsl:template>
   
    <xsl:template match="title">
      <h4>
         <a>
            <xsl:attribute name="href">
                 <xsl:apply-templates select="../url"/>                         
          </xsl:attribute>
            <xsl:value-of select="."/>
         </a>
      </h4>
   </xsl:template>

   
   
</xsl:stylesheet>