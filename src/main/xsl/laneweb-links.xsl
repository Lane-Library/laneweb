<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="h">

    <!-- the request-path ( not including parameters and context ) -->
    <xsl:param name="servlet-path"/>

    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <xsl:when test="contains(., '://') and contains(.,'{search-terms}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{search-terms\}',$regex-query)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="starts-with(.,'http://') and starts-with($path,'/secure')">
                <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and not(ancestor::h:head) and starts-with(.,'http')">
                <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="starts-with(.,'/Shibboleth.sso/Login')">
                <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                    <xsl:text>&amp;target=</xsl:text>
                    <xsl:value-of select="substring-after($return,'target=')"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link" select="."/>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="@action | @src">
        <xsl:attribute name="{name()}">
            <xsl:choose>
                <!-- permit schemeless urls -->
                <xsl:when test="starts-with(.,'//')">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:when test="starts-with(.,'/')">
                    <xsl:value-of select="concat($base-path,.)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="@*[.='{referrer}']">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="$referrer"/>
        </xsl:attribute>
    </xsl:template>

    <!-- {request-uri} in an attribute will be resolved to $request-uri + [?$query-string] -->
    <xsl:template match="@*[.='{request-uri}']">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="$path"/>
            <xsl:if test="$query-string">
                <xsl:value-of select="concat('?',$query-string)"/>
            </xsl:if>
        </xsl:attribute>
    </xsl:template>


    <!-- ======================  NAMED TEMPLATES  =========================== -->

    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="not(contains($link,'/secure/login.html')) and not(contains($link, 'sourceid='))">
                <xsl:choose>
                    <xsl:when test="contains($link, '?')">&amp;</xsl:when>
                    <xsl:otherwise>?</xsl:otherwise>
                </xsl:choose>
                <xsl:if test="$sourceid">
                    <xsl:text>sourceid=</xsl:text>
                    <xsl:value-of select="$sourceid"/>
                </xsl:if>
            </xsl:if>
        </xsl:variable>
        <xsl:attribute name="{$attr}">
            <!-- prepend the base-path if it is an absolute link -->
            <xsl:if test="starts-with($link, '/') and not(starts-with($link, '//')) and not(starts-with($link,$base-path))">
                <xsl:value-of select="$base-path"/>
            </xsl:if>
            <!-- replace keywords/search-terms TODO: unify this so only replaceing one thing -->
            <xsl:choose>
                <xsl:when test="contains($link,'{search-terms}')">
                    <xsl:value-of select="replace($link,'\{search-terms\}',$regex-query)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bsearch-terms%7D')">
                    <xsl:value-of select="replace($link,'%7Bsearch-terms%7D',$regex-query)"/>
                </xsl:when>
                <xsl:when test="$link = '/secure/login.html'">
                    <xsl:copy-of select="."/>
                    <xsl:text>?url=</xsl:text>
                    <xsl:value-of select="encode-for-uri($servlet-path)" />
                    <xsl:if test="string-length($query-string) != 0">
                        <xsl:variable name="no-proxylinks-query">
                            <xsl:variable name="starts-with-proxylinks" select="starts-with($query-string, 'proxy-links=')"/>
                            <xsl:for-each select="tokenize($query-string, '&amp;')">
                                <xsl:if test="not(starts-with(.,'proxy-links='))">
                                    <xsl:choose>
                                        <xsl:when test="$starts-with-proxylinks">
                                            <xsl:if test="position() != 2">
                                                <xsl:text>&amp;</xsl:text>
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:if test="position() != 1">
                                                <xsl:text>&amp;</xsl:text>
                                            </xsl:if>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:value-of select="."/>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:variable>
                        <xsl:if test="string-length($no-proxylinks-query) != 0">
                            <xsl:value-of select="encode-for-uri(concat('?', $no-proxylinks-query))"/>
                        </xsl:if>
                    </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$link"/>
                </xsl:otherwise>
            </xsl:choose>
            <!-- replace links ending with / so they end with /index.html -->
            <xsl:if test="not(contains($link, '?')) and ends-with($link,'/')">
                <xsl:text>index.html</xsl:text>
            </xsl:if>
            <xsl:if test="$sourceid and name(..) != 'link' and name(..) != 'img' and not(starts-with($link,'#'))">
                <xsl:value-of select="$param-string"/>
            </xsl:if>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
