<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://lane.stanford.edu/laneclasses"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc"
    version="2.0">



    <xsl:template match="/lc:classes">
        <html>
            <body>
                <ul class="type1">
                    <xsl:apply-templates  select="lc:class[position() &lt; 4]"/>
                </ul>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="lc:class">
        <div class="yui-gd">
            <div class="yui-u first">
                <div class="month">
                    <xsl:value-of select="./lc:month/text()" />
                </div>
                <div class="day">
                    <xsl:value-of select="./lc:day/text()" />
                </div>
            </div>
            <div class="yui-u">
                <xsl:apply-templates select="./lc:title" />
                <div class="time">
                    <xsl:value-of select="replace(replace(./lc:time-begin/text(), 'am',''), 'pm','')" />
                    <xsl:text>–</xsl:text>
                    <xsl:value-of select="./lc:time-end"/>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="lc:title">
        <xsl:copy-of select="attribute::node()|child::node()" />
    </xsl:template>


</xsl:stylesheet>