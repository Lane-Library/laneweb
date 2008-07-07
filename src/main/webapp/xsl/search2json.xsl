<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="s" version="2.0">

    <xsl:template match="s:search">
{
    "id": "<xsl:value-of select="@s:id"/>",
    "status": "<xsl:value-of select="@s:status"/>",
    "query": "<xsl:value-of select="encode-for-uri(s:query)"/>",
    "resources": {
    <xsl:apply-templates select="//s:resource"/>
    }
}
    </xsl:template>

    <xsl:template match="s:resource">
        <xsl:variable name="hitcount">
            <xsl:if test="s:hits">
                <xsl:value-of select="format-number(s:hits, '###,###')"/>
            </xsl:if>
        </xsl:variable>
        "<xsl:value-of select="@s:id"/>":
        {
        "status": "<xsl:value-of select="@s:status"/>",
        "url": "<xsl:value-of select="s:url"/>",
        "hits": "<xsl:value-of select="$hitcount"/>"
        }
        <xsl:if test="following-sibling::s:resource">,</xsl:if>
    </xsl:template>
</xsl:stylesheet>
