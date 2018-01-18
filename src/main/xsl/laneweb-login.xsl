<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">
    
    <!-- when web server returns 401/403 errors, user will not be populated and request will be
        /error_authz.html or /error_authn.html (configured in web server) -->
    <xsl:variable name="logged-in" select="string-length($userid) &gt; 0"/>
    <xsl:variable name="auth-error" select="matches($path,'^/error_auth[nz].html$')"/>
    <xsl:variable name="proxy-ipgroup" select="matches($ipgroup,'^(OTHER|PAVA|ERR)$')"/>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[position()&lt;6]/@class">
        <!-- position is the position of the parent li -->
        <xsl:variable name="position" select="count(../preceding-sibling::h:li) + 1"/>
        <xsl:attribute name="class">
            <xsl:choose>
                <!-- login link active if not logged in and not auth error -->
                <xsl:when test="$position = 1 and not($logged-in) and not($auth-error)">
                    <xsl:value-of select="'login-item-active'"/>
                </xsl:when>
                <!-- logout link active if logged in or auth error -->
                <xsl:when test="$position = 5 and ($logged-in or $auth-error)">
                    <xsl:value-of select="'login-item-active'"/>
                </xsl:when>
                <!-- other links active if logged in -->
                <xsl:when test="$position != 1 and $position != 5 and $logged-in and not($auth-error)">
                    <xsl:value-of select="'login-item-active'"/>
                </xsl:when>
                <!-- default not active -->
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[2]/text()">
        <xsl:choose>
            <xsl:when test="string-length($name) &gt; 0">
                <xsl:value-of select="$name"/>
            </xsl:when>
            <xsl:when test="string-length($userid) &gt; 0">
                <xsl:value-of select="$userid"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[6]/@class">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="$proxy-links ='true' and not($logged-in) and $proxy-ipgroup">
                    <xsl:value-of select="'login-item-active'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[6]/h:a/@href">
        <xsl:attribute name="href">
            <xsl:choose>
                <xsl:when test="string-length($query-string) = 0">
                    <xsl:text>?proxy-links=false</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>?</xsl:text>
                    <xsl:analyze-string select="$query-string" regex="(.*)(proxy-links=true)(.*)">
                        <xsl:matching-substring>
                            <xsl:value-of select="concat(regex-group(1), 'proxy-links=false', regex-group(3))"/>
                        </xsl:matching-substring>
                        <xsl:non-matching-substring>
                            <xsl:value-of select="concat($query-string, '&amp;proxy-links=false')"/>
                        </xsl:non-matching-substring>
                    </xsl:analyze-string>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[7]/@class">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="$proxy-links ='false' and not($logged-in) and $proxy-ipgroup">
                    <xsl:value-of select="'login-item-active'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:ul[attribute::class='login']/h:li[7]/h:a/@href">
        <xsl:attribute name="href">
            <xsl:choose>
                <xsl:when test="string-length($query-string) = 0">
                    <xsl:text>?proxy-links=true</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>?</xsl:text>
                    <xsl:analyze-string select="$query-string" regex="(.*)(proxy-links=false)(.*)">
                        <xsl:matching-substring>
                            <xsl:value-of select="concat(regex-group(1), 'proxy-links=true', regex-group(3))"/>
                        </xsl:matching-substring>
                        <xsl:non-matching-substring>
                            <xsl:value-of select="concat($query-string, '&amp;proxy-links=true')"/>
                        </xsl:non-matching-substring>
                    </xsl:analyze-string>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
