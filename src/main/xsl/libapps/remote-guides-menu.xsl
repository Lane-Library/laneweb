<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h">
   
  
   <xsl:template match="h:ul">
      <ul>
         <xsl:apply-templates select="h:li"/>
      </ul>
   </xsl:template>
   
   <xsl:template match="h:li">
      <li>
         <a>
            <xsl:attribute name="href" select="h:a/@href"/>
            <xsl:value-of select="./h:a/text()"/>
         </a>
      </li>
   </xsl:template>
</xsl:stylesheet>