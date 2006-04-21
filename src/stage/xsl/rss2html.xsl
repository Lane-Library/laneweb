<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:rss="http://purl.org/rss/1.0/"
    exclude-result-prefixes="rdf rss"
    version="1.0">
    
  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
    
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
    
  <xsl:template match="rdf:RDF|rss">
    <h2>
      <xsl:value-of select="rss:channel/rss:title|channel/title"/>
    </h2>
    <ul>
      <xsl:apply-templates select="rss:item|channel/item"/>
    </ul>
  </xsl:template>
    
  <xsl:template match="rss:item|item">
    <li>
      <a href="{rss:link|link}">
        <xsl:value-of select="rss:title|title"/>
      </a>
    </li>
  </xsl:template>

</xsl:stylesheet>