<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <!-- 
                //FIXME: uncomment before putting into production
                
            <xsl:when
                test="starts-with(.,'http://lane.stanford.edu') and not(contains(.,'cookiesFetch'))">
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link"
                        select="substring-after(.,'http://lane.stanford.edu')"/>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:when>
            -->
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

    <xsl:template match="@*[.='{request-uri}']">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="$path"/>
        </xsl:attribute>
    </xsl:template>


    <!-- ======================  NAMED TEMPLATES  =========================== -->

    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="not(contains($link,'/secure/login.html'))">
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
            <xsl:if test="starts-with($link, '/') and not(starts-with($link,$base-path))">
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
