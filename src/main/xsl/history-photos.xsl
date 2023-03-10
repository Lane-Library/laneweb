<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://purl.org/rss/1.0/"
    xmlns:rss="http://purl.org/rss/1.0/"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="rss h"
    version="2.0">

    <xsl:param name="query"/>

    <!-- copy elements by default -->
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- copy attributes by default -->
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="//rss/channel">
        <div class="history-photo-thumbs">
            <xsl:apply-templates select="//item[position() &lt;= 10]"/>
        </div>
        <xsl:if test="count(//item) &gt;= 10">
            <div>
                <a href="https://exhibits.stanford.edu/medhistory/catalog?f%5Bformat_main_ssim%5D%5B%5D=Image&amp;search_field=search&amp;q={$query}">More Images for <strong><xsl:value-of select="$query"/></strong><xsl:text> </xsl:text><i class="fa-solid fa-arrow-right"></i></a>
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="item">
        <a href="{link}/">
            <img width="75" src="https://purl.stanford.edu/{substring-after(link,'https://exhibits.stanford.edu/medhistory/catalog/')}.jpg" alt="{title}"/>
        </a>
    </xsl:template>

</xsl:stylesheet>