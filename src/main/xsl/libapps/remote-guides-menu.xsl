<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
   
  
   <xsl:template match="ul">
      <ul>
         <xsl:apply-templates select="li"/>
      </ul>
   </xsl:template>
   
   <xsl:template match="li">
      <li>
         <a>
            <xsl:attribute name="href" select="a/@href"/>
            <xsl:value-of select="./a/text()"/>
         </a>
      </li>
   </xsl:template>
</xsl:stylesheet>