<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"  version="2.0">

   
    <xsl:template match="/classes">
        <html>
            <body>
                <xsl:apply-templates select="./class[position() &lt; 4]"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="class">
        <div class="event">
            <div class="yui3-g">
                <div class="yui3-u-1-6">
                    <div class="date lane-date">
                        <div class="month">
                            <xsl:value-of select="substring(./dates/date/month, 1,3)"/>
                        </div>
                        <div class="day">
                        	<xsl:if test="./dates/date/day &lt; 10">0</xsl:if>
                            <xsl:value-of select="./dates/date/day"/>
                        </div>
                    </div>
                </div>
                <div class="yui3-u-5-6">
                    <p>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                <xsl:value-of select="id"/>
                            </xsl:attribute>
                            <xsl:value-of select="name"/>
                        </a>
                        <br/>
                        <span class="time">
                            <xsl:value-of select="lower-case(./dates/date/time)"/>
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
