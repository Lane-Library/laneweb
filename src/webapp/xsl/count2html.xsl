<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:sql="http://apache.org/cocoon/SQL/2.0" exclude-result-prefixes="sql">
    <xsl:output indent="yes"/>
    <xsl:template match="counts">
        <html>
            <head>
                <title>Click Counts per Day</title>
            </head>
            <body>
                <table>
                    <tr>
                        <th>day</th>
                        <th>count</th>
                    </tr>
                    <xsl:apply-templates select="sql:rowset/sql:row"/>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="sql:row">
        <tr>
            <td>
                <xsl:value-of select="sql:day"/>
            </td>
            <td style="text-align:right">
                <xsl:value-of select="sql:count"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
