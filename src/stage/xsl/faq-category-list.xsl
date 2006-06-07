<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:param name="category"/>
    
    <xsl:template match="/h:html/h:body/h:ul">
        <xsl:copy>
            <xsl:apply-templates select="h:li[h:ul/h:li[@class='primaryCategory'] = $category
                                              and contains(h:ul/h:li[@class='keywords'],'_show_me_')]"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:li">
        <xsl:copy>
            <a href="/howto/index.html?id={@id}"><xsl:value-of select="text()"/></a>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>
