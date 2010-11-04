<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="2.0">



    <xsl:template match="/classes">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <body>
                <ul class="type1">
                    <xsl:apply-templates  select="class[position() &lt; 4]"/>
                </ul>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="class">
        <div class="yui-gd">
            <div class="yui-u first">
                <div class="month">
                    <xsl:value-of select="./month/text()" />
                </div>
                <div class="day">
                    <xsl:value-of select="./day/text()" />
                </div>
            </div>
            <div class="yui-u">
                <xsl:apply-templates select="./title" />
                <div class="time">
                    <xsl:value-of select="replace(replace(./time-begin/text(), 'am',''), 'pm','')" />
                    <xsl:text>–</xsl:text>
                    <xsl:value-of select="./time-end"/>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="title">
        <xsl:copy-of select="attribute::node()|child::node()" />
    </xsl:template>


</xsl:stylesheet>