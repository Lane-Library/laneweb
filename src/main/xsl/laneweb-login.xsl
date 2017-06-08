<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="h xs">
    
    <!-- when web server returns 401/403 errors, user will not be populated and request will be /error_authz.html or /error_authn.html (configured in web server) -->
    <xsl:variable name="unauthorized" as="xs:boolean" select="string-length($userid) &gt; 0 or matches($request-uri,'/error_auth[n|z].html$')"/>
    
    <!-- the next 6 template matches handle the login state and show links depending on that state -->
    <!-- process the list only if off campus -->
    <!--<xsl:template match="h:ul[attribute::id='login']">
        <xsl:if test="matches($ipgroup,'^(OTHER|PAVA|LPCH|SHC|ERR)$')">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>-->
    
    <!-- the 1st #login li is the login link or the users name -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[1]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="string-length($name) &gt; 0">
                    <xsl:value-of select="$name"/>
                </xsl:when>
                <xsl:when test="string-length($userid) &gt; 0">
                    <xsl:value-of select="$userid"/>
                </xsl:when>
                <xsl:when test="$unauthorized"/>
                <xsl:otherwise>
                        <xsl:apply-templates select="child::node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <!-- the 2nd #login li is the link to the bookmarks page -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[2]">
        <xsl:if test="string-length($userid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    <!-- the 3rd #login li is the account link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[3]">
        <xsl:if test="string-length($userid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    <!-- the 4th #login li is the logout link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[4]">
        <xsl:if test="string-length($userid) &gt; 0 or $unauthorized">
            <xsl:copy>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 5th #login li is the proxy-off toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[5]">
        <xsl:if test="$proxy-links ='true' and string-length($userid) = 0 and matches($ipgroup,'^(OTHER|PAVA|ERR)$')">
            <xsl:copy>
                <a>
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
                    <xsl:value-of select="."/>
                </a>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 6th #login li is the proxy-on toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[6]">
        <xsl:if test="$proxy-links = 'false' and string-length($userid) = 0 and matches($ipgroup,'^(OTHER|PAVA|ERR)$')">
            <xsl:copy>
                <a>
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
                    <xsl:value-of select="."/>
                </a>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
