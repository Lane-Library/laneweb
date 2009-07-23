<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="rss">
        <ol class="citationList">
            <xsl:apply-templates select="channel/item"/>
        </ol>
    </xsl:template>
    
    <xsl:template match="item">
        <li>
            <xsl:choose>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <a href="{concat('http://www.ncbi.nlm.nih.gov/pubmed/',substring-after(guid,':'),'?otool=stanford&amp;holding=F1000,F1000M')}"><xsl:value-of select="title"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <a href="{link}"><xsl:value-of select="title"/></a>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="category|author"/>
        </li>
    </xsl:template>

    <xsl:template match="category|author">
        <div><xsl:value-of select="."/></div>
    </xsl:template>
</xsl:stylesheet>
