<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- when web server returns 401/403 errors, user will not be populated and request will be
        /error_authz.html or /error_authn.html (configured in web server) -->
    <xsl:variable name="logged-in" select="string-length($userid) &gt; 0"/>
    <xsl:variable name="auth-error" select="matches($path, '^/error_auth[nz].html$')"/>
    <xsl:variable name="proxy-ipgroup" select="matches($ipgroup, '^(OTHER|PAVA|ERR)$')"/>

    <xsl:template match="h:ul[attribute::class = 'login']/h:li">
        <!-- position() is one greater than expected because class attribute of ul is #1 -->
        <xsl:copy>
            <xsl:choose>
                <!-- login link active if not logged in and not auth error -->
                <xsl:when test="position() = 2 and not($logged-in) and not($auth-error)">
                    <xsl:attribute name="class" select="'login-item-active'"/>
                </xsl:when>
                <!-- logout link active if logged in or auth error -->
                <xsl:when test="position() = 4 and ($logged-in or $auth-error)">
                    <xsl:attribute name="class" select="'login-item-active'"/>
                </xsl:when>
                <!-- other links active if logged in -->
                <xsl:when   test="position() != 2 and position() != 4 and $logged-in and not($auth-error)">
                   <xsl:attribute name="class" select="concat('login-item-active',' ', @class)"/>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
