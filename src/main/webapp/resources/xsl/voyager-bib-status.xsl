<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    
    <xsl:param name="bib"/>
    
    <xsl:template match="/">
        <xsl:variable name="item-count" select="count(response/items/institution/item)"/>
        <xsl:variable name="available-item-count" select="count(response/items/institution/item[itemData[@name='itemStatus' and . = 'Not Charged']])"/>
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="$item-count > 0 and $item-count = $available-item-count">true</xsl:when>
                <xsl:otherwise>false</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <bib id="{$bib}" available="{$status}"/>
    </xsl:template>


</xsl:stylesheet>