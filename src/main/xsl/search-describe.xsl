<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    xmlns:st="http://lane.stanford.edu/search-templates/ns"
    xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="s st"
    version="2.0">

    <xsl:template match="text()" />

    <xsl:template match="s:search">
        <html>
            <head>
                <title>Search Engine Descriptions</title>
                <style type="text/css">
                    h1 {color:olive}
                    div.engine
                    {border:thin olive solid;margin:1em;hight:auto}
                    h2
                    {background:olive;color:white;margin:0;padding:0}
                    table.engine td{border-bottom:thin olive dashed}

                    table.engine td{padding-right:50px}

                    table.portal
                    td{padding-right:10px;padding-left:10px;border-style:dashed;border-color:olive;border-width:1px
                    }

                    a {text-decoration:none}

                </style>
            </head>
            <body>
                <h1>Search Engine Descriptions </h1>
                <xsl:apply-templates
                    select="s:engine">
                    <xsl:sort select="s:description" />
                </xsl:apply-templates>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="s:engine">
        <div class="engine">
            <h2>
                <xsl:choose>
                    <xsl:when test="s:description">
                        <xsl:value-of
                            select="s:description" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of
                            select="s:resource/s:description" />
                    </xsl:otherwise>
                </xsl:choose>
            </h2>
            <h3>
                engine:
                <xsl:value-of select="@s:id" />
            </h3>
            <table class="engine">
                <xsl:apply-templates
                    select="s:resource">
                    <xsl:sort select="s:description" />
                </xsl:apply-templates>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="s:resource">
        <tr>
            <td>
                resource:
                <xsl:value-of select="@s:id" />
            </td>
            <td>
                <xsl:call-template
                    name="resource-portals">
                    <xsl:with-param name="resource"
                        select="@s:id" />
                </xsl:call-template>
            </td>
            <td>
                description:
                <xsl:value-of select="s:description" />
            </td>
        </tr>
    </xsl:template>

    <xsl:template name="resource-portals">
        <xsl:param name="resource" />
        <table class="portal">
            <xsl:variable name="not">
                <xsl:if
                    test="count(//st:search-templates/st:template/st:engine[@idref=$resource]) &lt; 1">
                    <xsl:text>not </xsl:text>
                </xsl:if>
            </xsl:variable>
            <tr>
                <th>
                    <xsl:value-of select="$not" />used
                </th>
            </tr>
            <tr>
                <xsl:for-each
                    select="//st:search-templates/st:template/st:engine[@idref=$resource]">
                    <td>
                        <xsl:value-of select="../@id" />
                    </td>
                </xsl:for-each>
            </tr>
        </table>
    </xsl:template>

</xsl:stylesheet>
