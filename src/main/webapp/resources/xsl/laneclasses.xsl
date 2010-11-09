<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc h"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">


    <xsl:template match="/lc:classes">
        <html>
            <body>
     
                    <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>


    <xsl:template match="lc:class">
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
                <xsl:apply-templates select="./lc:title" />
            </h4>
            <div class="lecturer">
                <xsl:value-of select="./lc:lecturer/text()" />
            </div>
        </div>
        <div class="yui-u first date">
            <strong>
                <xsl:value-of select="./lc:month/text()" />
                <xsl:text> </xsl:text>
                <xsl:value-of select="./lc:day/text()" />
            </strong>
            <br />
            <xsl:value-of select="replace(replace(./lc:time-begin/text(), 'am',''), 'pm','')" />
            <xsl:text>-</xsl:text>
            <xsl:value-of select="./lc:time-end/text()" />
        </div>
    </xsl:template>

    <xsl:template match="lc:title">
        <xsl:copy-of select="attribute::node()|child::node()" />
    </xsl:template>

</xsl:stylesheet>