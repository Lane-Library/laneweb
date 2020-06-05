<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="h"
>


   <xsl:template match="/doc">
      <ul>
         <xsl:for-each select="//h:li">
            <xsl:sort select="."/>
            <xsl:copy-of select="."/>
         </xsl:for-each>
      </ul>
   </xsl:template>

</xsl:stylesheet>