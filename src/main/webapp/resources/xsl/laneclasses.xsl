<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">


    <xsl:template match="/classes">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <body>
                <ul class="type1">
                    <xsl:apply-templates />
                </ul>
            </body>
        </html>
    </xsl:template>


    <xsl:template match="class">
        <xsl:if test="position() mod 4 !=0 ">
            <li class="odd yui-gf">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
        <xsl:if test="position() mod 4  = 0 ">
            <li class="even yui-gf">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
    </xsl:template>

    <xsl:template name="decorator">
        <div class="yui-u">
            <h4>
                <xsl:apply-templates select="./title" />
            </h4>
            <div class="lecturer">
                <xsl:value-of select="./lecturer/text()" />
            </div>
        </div>
        <div class="yui-u first date">
            <strong>
                <xsl:value-of select="./month/text()" />
                <xsl:text> </xsl:text>
                <xsl:value-of select="./day/text()" />
            </strong>
            <br />
            <xsl:value-of select="replace(replace(./time-begin/text(), 'am',''), 'pm','')" />
            <xsl:text>-</xsl:text>
            <xsl:value-of select="./time-end/text()" />
        </div>
    </xsl:template>

    <xsl:template match="title">
        <xsl:copy-of select="attribute::node()|child::node()" />
    </xsl:template>

</xsl:stylesheet>