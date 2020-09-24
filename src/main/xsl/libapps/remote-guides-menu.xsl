<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
   
   <xsl:param name="laneguide-uri" />
   <xsl:variable name="laneguide-url">
      <xsl:value-of select="tokenize($laneguide-uri, ':')[position() &lt;= 2]" separator=":" />
   </xsl:variable>
   <xsl:template match="ul">
      <ul>
         <xsl:apply-templates select="li"/>
      </ul>
   </xsl:template>
   
   <xsl:template match="li">
      <li>
         <a>
            <xsl:attribute name="href" select="replace(a/@href , $laneguide-url ,'/libguides')"/>
            <xsl:value-of select="./a/text()"/>
         </a>
      </li>
   </xsl:template>
</xsl:stylesheet>