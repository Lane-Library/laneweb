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

    <xsl:template match="
        h:ul[attribute::class = 'login']/h:li[1]|
        h:ul[attribute::class = 'login']/h:li[2]|
        h:ul[attribute::class = 'login']/h:li[3]|
        h:ul[attribute::class = 'login']/h:li[4]">
        <!-- position() is one greater than expected because class attribute of ul is #1 -->
        <xsl:copy>
            <xsl:choose>
                <!-- login link active if not logged in and not auth error -->
                <xsl:when test="position() = 2 and not($logged-in) and not($auth-error)">
                    <xsl:attribute name="class" select="'login-item-active'"/>
                </xsl:when>
                <!-- logout link active if logged in or auth error -->
                <xsl:when test="position() = 5 and ($logged-in or $auth-error)">
                    <xsl:attribute name="class" select="'login-item-active'"/>
                </xsl:when>
                <!-- other links active if logged in -->
                <xsl:when
                    test="position() != 2 and position() != 5 and $logged-in and not($auth-error)">
                    <xsl:attribute name="class" select="'login-item-active'"/>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:ul[attribute::class = 'login']/h:li[2]/text()">
        <xsl:choose>
            <xsl:when test="string-length($name) &gt; 0">
                <xsl:value-of select="concat(' ', $name)"/>
            </xsl:when>
            <xsl:when test="string-length($userid) &gt; 0">
                <xsl:value-of select="concat(' ', $userid)"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
