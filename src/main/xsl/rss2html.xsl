<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:rss="http://purl.org/rss/1.0/"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="rdf rss"
    version="2.0">
    
  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
    
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
    
  <xsl:template match="rdf:RDF|rss">
    <ul>
      <xsl:apply-templates select="rss:item|channel/item"/>
    </ul>
  </xsl:template>
    
  <xsl:template match="rss:item|item">
	<li>
		<xsl:choose>
			<xsl:when test="starts-with(guid,'PubMed:')">
				<a xmlns="http://www.w3.org/1999/xhtml" id="pubmed_{substring-after(guid,':')}"
					href="{concat('http://www.ncbi.nlm.nih.gov/pubmed/',substring-after(guid,':'),'?otool=stanford&amp;holding=F1000,F1000M')}"
					title="feed link---{../../channel/title}---{title}">
					<xsl:value-of select="title" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a xmlns="http://www.w3.org/1999/xhtml" href="{link|rss:link}"
					title="feed link---{../../channel/title|../rss:channel/rss:title}---{title|rss:title}">
					<xsl:value-of select="rss:title|title" />
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</li>
</xsl:template>

</xsl:stylesheet>