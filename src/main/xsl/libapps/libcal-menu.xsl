<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
   <xsl:template match="/classes">
      <html>
         <body>
            <div  id="mobile-menu-header" class="menu-container">
            	<span>
               		<a href="#mobile-menu-header" class="menu-toggle">
						<i class="fa-solid fa-angle-down fa-lg"></i>
					</a>
					<a href="#off" id="off"  class="menu-toggle">
						<i class="fa-solid fa-xmark fa-lg"></i>
					</a>
			   </span>	
               <h3>Categories</h3>
               <ul class="menu">
                  <li>
                     <a href="/calendar/classes/?cid=12663&amp;t=d">By Date</a>
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
                     <xsl:value-of
               select="concat('/calendar/classes/?cid=12663&amp;t=d&amp;ct=', ../id )"/>                               
                  </xsl:attribute>
            <xsl:value-of select="."/>
         </a>
      </li>
   </xsl:template>
</xsl:stylesheet>