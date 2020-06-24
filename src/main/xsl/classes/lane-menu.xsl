<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
   <xsl:template match="/classes">
      <html>
         <body>
            <div class="menu-container">
               <h3>Categories</h3>
               <ul class="menu">
                  <li>
                     <a href="https://lane-stanford.libcal.com/calendar/classes/?cid=12663">By Date</a>
                  </li>
                  <xsl:for-each-group select="./class/categories//category" group-by="name">
                     <xsl:sort select="."/>
                     <xsl:apply-templates select="name"/>
                  </xsl:for-each-group>
               </ul>
            </div>
         </body>
      </html>
   </xsl:template>
   <xsl:template match="name">
      <li>
         <a>
            <xsl:attribute name="href">
                     <xsl:value-of select="concat('/classes-consult/laneclasses.html?id=', ../id)"/>                               
                  </xsl:attribute>
            <xsl:value-of select="."/>
         </a>
      </li>
   </xsl:template>
</xsl:stylesheet>