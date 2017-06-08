<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">

    <xsl:param name="userid"/>
    <xsl:param name="ticket"/>
    <xsl:param name="proxy-links"/>

    <xsl:variable name="proxy-url">https://login.laneproxy.stanford.edu/login?</xsl:variable>
    <xsl:variable name="post-url">
        <xsl:choose>
            <xsl:when test="$proxy-links = 'true' and $userid != '' and $ticket != ''">
                <xsl:value-of select="$proxy-url"/>
                <xsl:text>user=</xsl:text>
                <xsl:value-of select="$userid"/>
                <xsl:text>&amp;ticket=</xsl:text>
                <xsl:value-of select="$ticket"/>
                <xsl:text>&amp;url=</xsl:text>
                <xsl:value-of select="map/entry[string='postAction']/string-array/string[1]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="map/entry[string='postAction']/string-array/string[1]"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:template match="/">

        <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
            <head>
                <title>Post Method Helper</title>
                <script type="text/javascript"> 
function submitForm()
{
    document.defaultForm.submit();
}

</script>

            </head>
            <body onload="submitForm()">


                <form action="{$post-url}" name="defaultForm" method="post">
                    <xsl:for-each select="map/entry">
                        <xsl:if test="string !='postAction'">
                            <input type="hidden" name="{string}" value="{string-array/string[1]}"/>
                        </xsl:if>
                    </xsl:for-each>
                </form>

                <table>
                    <tr>
                        <td>Please, wait while we redirect you.</td>
                    </tr>
                </table>
            </body>
        </html>
    </xsl:template>


</xsl:stylesheet>
