<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc h"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl"/>    

    <xsl:template match="/lc:classes">
        <html>
            <body>
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>


    <xsl:template match="lc:event_data">
        <xsl:if test="position() mod 2 !=0 ">
            <li class="odd yui-gf">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
        <xsl:if test="position() mod 2  = 0 ">
            <li class="even yui-gf">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
    </xsl:template>

    <xsl:template name="decorator">
        <div class="yui-u">
            <h4>
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                        <xsl:value-of select="lc:module_id/text()"/>
                    </xsl:attribute>
                    <xsl:value-of select="./lc:event_name" />
                </a> 
            </h4>
            <div class="lecturer">
                <xsl:value-of select="./lc:speaker/text()" />
            </div>
        </div>
        <div class="yui-u first date">
            <strong>
                <xsl:call-template name="month" />
                <xsl:text> </xsl:text>
                <xsl:call-template name="day" />
            </strong>
            <br />
            <xsl:call-template name="start-time" />
                <xsl:text>–</xsl:text>
            <xsl:call-template name="end-time" />

        </div>
    </xsl:template>

    

</xsl:stylesheet>