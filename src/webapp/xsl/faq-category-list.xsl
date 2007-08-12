<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:h="http://www.w3.org/1999/xhtml" version="2.0">
    <xsl:param name="category"/>
    
    <xsl:template match="/h:html/h:body/h:ul">
        <xsl:variable name="more-category">
            <xsl:choose>
                <xsl:when test="contains($category,'&amp;')">
                    <xsl:value-of select="substring-before($category,'&amp;')"/>
                    <xsl:text>%26</xsl:text>
                    <xsl:value-of select="substring-after($category,'&amp;')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$category"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:copy>
            <xsl:apply-templates select="h:li[h:ul/h:li[@class='primaryCategory'] = $category
                                              and contains(h:ul/h:li[@class='keywords'],'_show_me_')]"/>
            <li class="moreItem"><a href="/howto/index.html?category={$more-category}">More</a></li>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:li[not(ancestor::h:div)]">
        <xsl:copy>
            <a href="/howto/index.html?id={@id}"><xsl:value-of select="text()"/></a>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>
