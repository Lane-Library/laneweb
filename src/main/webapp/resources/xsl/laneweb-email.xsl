<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <xsl:template match="h:a|h:area">
        <xsl:choose>
            <!-- obfuscate email addresses with javascript -->
            <xsl:when test="starts-with(@href, 'mailto:')">
                <xsl:variable name="address">
                    <xsl:text>'+'ma'+''+'il'+'to'+':'</xsl:text>
                    <xsl:call-template name="js-split">
                        <xsl:with-param name="string" select="substring-after(@href,'mailto:')"/>
                    </xsl:call-template>
                    <xsl:text>+'</xsl:text>
                </xsl:variable>
                <script type="text/javascript">
                    <xsl:comment>
                        <xsl:text>&#xD;document.write('&lt;</xsl:text>
                        <xsl:value-of select="name()"/>
                        <xsl:text> href="</xsl:text>
                        <xsl:value-of select="$address"/>
                        <xsl:text>"</xsl:text>
                        <xsl:for-each select="attribute::node()[not(name() = 'href')]">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="name()"/>
                            <xsl:text>="</xsl:text>
                            <xsl:value-of select="."/>
                            <xsl:text>"</xsl:text>
                        </xsl:for-each>
                        <xsl:text>&gt;</xsl:text>
                        <xsl:for-each select="*">
                            <xsl:text>'+'&lt;</xsl:text>
                            <xsl:value-of select="name()"/>
                            <xsl:for-each select="attribute::node()">
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="name()"/>
                                <xsl:text>="</xsl:text>
                                <xsl:value-of select="."/>
                                <xsl:text>"</xsl:text>
                            </xsl:for-each>
                            <xsl:text>&gt;</xsl:text>
                        </xsl:for-each>
                        <xsl:text>'</xsl:text>
                        <xsl:call-template name="js-split">
                            <xsl:with-param name="string" select="normalize-space()"/>
                        </xsl:call-template>
                        <xsl:text>+'&lt;/</xsl:text>
                        <xsl:value-of select="name()"/>
                        <xsl:text>&gt;');&#xD;</xsl:text>
                    </xsl:comment>
                </script>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- obfuscated email href (don't copy, processed elsewhere) -->
    <xsl:template match="attribute::href[starts-with(.,'mailto:')]"/>

    <xsl:template name="tokenize-email">
        <xsl:param name="string"/>
        <xsl:value-of select="concat('|',substring($string,1,1))"/>
        <xsl:if test="string-length($string) &gt; 1">
            <xsl:call-template name="tokenize-email">
                <xsl:with-param name="string" select="substring($string,2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="js-split">
        <xsl:param name="string"/>
        <xsl:variable name="char">
            <xsl:value-of select="substring($string,1,1)"/>
        </xsl:variable>
        <xsl:text>+'</xsl:text>
        <xsl:if test="$char = &quot;'&quot;">
            <xsl:text>\</xsl:text>
        </xsl:if>
        <xsl:value-of select="$char"/>
        <xsl:text>'</xsl:text>
        <xsl:if test="string-length($string) &gt; 1">
            <xsl:call-template name="js-split">
                <xsl:with-param name="string" select="substring($string,2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
