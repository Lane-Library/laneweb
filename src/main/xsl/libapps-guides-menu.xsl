<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
   exclude-result-prefixes="oai dc oai_dc" xmlns:oai="http://www.openarchives.org/OAI/2.0/" xmlns:dc="http://purl.org/dc/elements/1.1/"
   xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
>
   
   <xsl:template match="/">
      <ul>
         <xsl:apply-templates select="/oai:OAI-PMH/oai:ListRecords/oai:record/oai:metadata"/>
      </ul>
   </xsl:template>
   
   <xsl:template match="oai:metadata">
      <li>
         <a>
            <xsl:attribute name="href" select="replace(./oai_dc:dc/dc:identifier ,'https://lane-stanford.libguides.com/','/libguides/')"/>
            <xsl:value-of select="./oai_dc:dc/dc:title"/>
         </a>
      </li>
   </xsl:template>
   
</xsl:stylesheet>