<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" 
    version="2.0">
    
    <xsl:param name="sfx-request"/>
    
    <xsl:variable name="sfx-title">
            <xsl:if test="/h:html/h:body/h:div/h:table/h:tbody/h:tr/h:td[@class='BlockTitle'] = 'Full Text'">
                <xsl:value-of select="normalize-space(/h:html/h:body/h:div/h:table/h:tbody/h:tr/h:td[@id='laneweb_source']/text())"/>
            </xsl:if>
    </xsl:variable>
    
    <xsl:template match="/">{
"openurl": "<xsl:value-of select="$sfx-request"/>",
"result": "<xsl:value-of select="$sfx-title"/>"
}</xsl:template>

</xsl:stylesheet>