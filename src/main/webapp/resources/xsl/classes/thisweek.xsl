<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc" version="2.0">

    <xsl:import href="laneclasses-common.xsl"/>

    <xsl:template match="/lc:classes">
        <html>
            <body>
                <xsl:apply-templates select="lc:event_data[position() &lt; 4]"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="lc:event_data">
        <div class="event">
            <div class="yui3-g">
                <div class="yui3-u-1-6">
                    <div class="date lane-date">
                        <div class="month">
                            <xsl:call-template name="month"/>
                        </div>
                        <div class="day">
                            <xsl:call-template name="day"/>
                        </div>
                    </div>
                </div>
                <div class="yui3-u-5-6">
                    <p>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                <xsl:value-of select="lc:module_id/text()"/>
                            </xsl:attribute>
                            <xsl:value-of select="./lc:event_name"/>
                        </a>
                        <br/>
                        <span class="time">
                            <xsl:call-template name="start-time"/>
                            <xsl:text> – </xsl:text>
                            <xsl:call-template name="end-time"/>
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
