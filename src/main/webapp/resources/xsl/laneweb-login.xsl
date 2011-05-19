<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- the next 6 template matches handle the login state and show links depending on that state -->
    <!-- process the list only if off campus -->
    <xsl:template match="h:ul[attribute::id='login']">
        <xsl:if test="matches($ipgroup,'^(OTHER|PAVA|LPCH|SHC|ERR)$')">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <!-- the 1st #login li is the login link or the users name or the emrid -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[1]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="string-length($name) &gt; 0">
                    <xsl:value-of select="$name"/>
                </xsl:when>
                <xsl:when test="string-length($sunetid) &gt; 0">
                    <xsl:value-of select="$sunetid"/>
                </xsl:when>
                <xsl:when test="string-length($emrid) &gt; 0">
                    EPIC id: <xsl:value-of select="$emrid"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- the 2nd #login li is the account link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[2]">
        <xsl:if test="string-length($sunetid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>


    <!-- the 3rd #login li is the logout link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[3]">
        <xsl:if test="string-length($sunetid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <!-- the 4rd #login li is the proxy-off toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[4]">
        <xsl:if test="string-length($sunetid) = 0 and $proxy-links = 'true'">
            <xsl:copy>
                <a>
                    <xsl:attribute name="href">
                        <xsl:choose>
                            <xsl:when
                                test="string-length($query-string) = 0 or $query-string = 'proxy-links=true'">
                                <xsl:text>?proxy-links=false</xsl:text>
                            </xsl:when>
                            <xsl:when test="ends-with($query-string, 'proxy-links=true')">
                                <xsl:text>?</xsl:text>
                                <xsl:value-of
                                    select="substring-before($query-string,'proxy-links=true')"/>
                                <xsl:text>proxy-links=false</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of
                                    select="concat('?',$query-string,'&amp;proxy-links=false')"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </a>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <!-- the 5th #login li is the proxy-on toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[5]">
        <xsl:if test="string-length($sunetid) = 0 and $proxy-links = 'false'">
            <xsl:copy>
                <a>
                    <xsl:attribute name="href">
                        <xsl:choose>
                            <xsl:when
                                test="string-length($query-string) = 0 or $query-string = 'proxy-links=false'">
                                <xsl:text>?proxy-links=true</xsl:text>
                            </xsl:when>
                            <xsl:when test="ends-with($query-string, 'proxy-links=false')">
                                <xsl:text>?</xsl:text>
                                <xsl:value-of
                                    select="substring-before($query-string,'proxy-links=false')"/>
                                <xsl:text>proxy-links=true</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of
                                    select="concat('?',$query-string,'&amp;proxy-links=true')"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </a>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
