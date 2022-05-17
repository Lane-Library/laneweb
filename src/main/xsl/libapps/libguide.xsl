<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">



    <xsl:template match="/ul">
        <xsl:copy>
            <xsl:apply-templates select="li" />
        </xsl:copy>
        <div class="tooltips">
            <xsl:call-template name="tooltips">
                <xsl:with-param name="node" select="./li"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template match="li">
        <xsl:variable name="id" select="replace(lower-case(./a/text()),' ' ,'-')"></xsl:variable>
        <xsl:variable name="description" select="./div/div/text()"></xsl:variable>
        <xsl:copy>
            <a>
                <xsl:attribute name="href" select="./a/@href"></xsl:attribute>
                <xsl:attribute name="id" select="$id"></xsl:attribute>
                <xsl:if test="$description != ''">
                    <xsl:attribute name="class">yui3-tooltip-trigger</xsl:attribute>
                </xsl:if>
                <xsl:value-of select="./a/text()" />
            </a>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="tooltips">
        <xsl:param name="node" />
        <xsl:for-each select="$node">
            <xsl:variable name="id" select="replace(lower-case(./a/text()),' ' ,'-')"></xsl:variable>
            <span>
                <xsl:attribute name="id" select="$id"></xsl:attribute>
                <xsl:value-of select="./div/div/text()"></xsl:value-of>
            </span>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>

