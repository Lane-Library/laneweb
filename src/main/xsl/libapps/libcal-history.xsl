<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
   version="2.0">

  

   <xsl:template match="/classes">
      <html>
         <body>
            <xsl:apply-templates select="class[position() &lt; 4]" />
         </body>
      </html>
   </xsl:template>

   <xsl:template match="class">
      <div class="event">
         <div class="pure-g">
            <div class="pure-u-1-6">
               <div class="event-date">
                  <div>
                   <xsl:value-of select="start/month" />
                  </div>
                  <div>
                     <xsl:value-of select="start/day" />
                  </div>
               </div>
            </div>
            <div class="pure-u-5-6">
               <div class="event-info">
                  <a>
                     <xsl:attribute name="href">
                       <xsl:value-of select="url/public" />
                     </xsl:attribute>
                     <xsl:value-of select="title" />
                  </a>
                  <span>
                     <xsl:value-of select="start/hour" />
                     <xsl:text> – </xsl:text>
                     <xsl:value-of select="end/hour" />
                  </span>
               </div>
            </div>
         </div>
      </div>
   </xsl:template>

</xsl:stylesheet>
