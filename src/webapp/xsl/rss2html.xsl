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
    
    <xsl:template match="rdf:RDF">
        <h2>
            <xsl:value-of select="rss:channel/rss:title"/>
        </h2>
        <ul>
            <!-- <xsl:apply-templates select="rss:item[not(self::node()=following-sibling::rss:item)]"/> -->
            <xsl:apply-templates select="rss:item"/>
        </ul>
    </xsl:template>
    
    <xsl:template match="rss:item">
        <li>
            <a href="{rss:link}">
                <xsl:value-of select="rss:title"/>
            </a>
        </li>
    </xsl:template>

</xsl:stylesheet>