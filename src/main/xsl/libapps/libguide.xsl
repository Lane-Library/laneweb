<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">



    <xsl:template match="/ul">
        <xsl:copy>
            <xsl:apply-templates select="*" />
        </xsl:copy>
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
            <xsl:call-template name="tooltip">
                <xsl:with-param name="id" select="$id" />
                <xsl:with-param name="description" select="$description" />
            </xsl:call-template>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="tooltip">
        <xsl:param name="id" />
        <xsl:param name="description" />
        <xsl:if test="$description != ''">
            <div class="tooltips">
                <span>
                    <xsl:attribute name="id" select="concat($id, 'Tooltip')"></xsl:attribute>
                    <xsl:value-of select="$description"></xsl:value-of>
                </span>
            </div>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>

