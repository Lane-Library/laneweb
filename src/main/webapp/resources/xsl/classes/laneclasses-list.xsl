<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc" version="2.0">


    <xsl:template match="/lc:classes">
        <html>
            <body>
                        <xsl:for-each-group select="lc:event_data" group-by="lc:event_name">
                            <xsl:apply-templates select="current-group()[last()]" />
                        </xsl:for-each-group>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="lc:event_data">
        <li>
            <a>
                <xsl:attribute name="href">
                            <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                            <xsl:value-of select="lc:module_id/text()" />
                        </xsl:attribute>
                <xsl:value-of select="./lc:event_name" />
            </a>
        </li>
    </xsl:template>


</xsl:stylesheet>