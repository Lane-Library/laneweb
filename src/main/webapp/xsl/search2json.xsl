<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="s" version="2.0">

<xsl:param name="content"/>
<xsl:param name="content-to-display"/>
<xsl:variable name="num-to-display"><xsl:value-of select="number($content-to-display)"/></xsl:variable>
   
   
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
                <xsl:value-of select="format-number(s:hits, '###,##0')"/>
            </xsl:if>
        </xsl:variable>
        "<xsl:value-of select="@s:id"/>":
        {
            "status": "<xsl:value-of select="@s:status"/>",
            "url": "<xsl:value-of select="s:url"/>",
            "hits": "<xsl:value-of select="$hitcount"/>"<xsl:text/>
            <xsl:if test="$content = 'true' and ./s:content">
                	<xsl:text>,
    	    "contents":[</xsl:text>
             	    <xsl:apply-templates select="./s:content[ $num-to-display &lt; position() ]"/>
            ]<xsl:text/>
             </xsl:if>
        }
        <xsl:if test="following::s:resource">,</xsl:if>
    </xsl:template>
    
    <xsl:template match="s:content">
                  {
                       "title": "<xsl:value-of select="s:title"/>",
                       "description": "<xsl:value-of select="s:description"/>",
                       "url": "<xsl:value-of select="s:url"/>"
                  }
                   <xsl:if test="position() !=  $num-to-display">,</xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
