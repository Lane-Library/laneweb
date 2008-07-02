<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://lane.stanford.edu/sfx/ns"
    exclude-result-prefixes="h"
    version="2.0">

    <xsl:param name="sfx-request"/>

    <xsl:variable name="sfx-title">
        <xsl:copy-of select="normalize-space(/html/body/div/table/tr/td[@id='laneweb_source']/text())"/>
    </xsl:variable>
    
    <xsl:template match="/">
        <response>
            <openurl><xsl:value-of select="$sfx-request"/></openurl>
            <result>
            <xsl:choose>
                <xsl:when test="/html/body/div/table/tr/td[@class='BlockTitle'] = 'Full Text'">
                    <xsl:value-of select="$sfx-title"/>
                </xsl:when>
                <xsl:otherwise>0</xsl:otherwise>
            </xsl:choose>
            </result>
        </response>
    </xsl:template>
  
</xsl:stylesheet>