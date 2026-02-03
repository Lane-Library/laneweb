<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml">


    <xsl:template match="/">
        <span>
            <xsl:text>Resource Outages: </xsl:text>
            <a href="/contacts/resource-outages.html">
                <xsl:apply-templates select="//span[contains( @class, 'label-danger')]" />
            </a>
        </span>
    </xsl:template>


    <xsl:template match="span">
        <xsl:apply-templates select="../../../h4" />
        <xsl:if test="last() > 1  and position() != last()">
            <xsl:text>, </xsl:text>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
    


