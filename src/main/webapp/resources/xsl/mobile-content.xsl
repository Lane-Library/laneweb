<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">
    
    <!-- ===========================  PARAMETERS ========================= -->
    <!-- the request-uri ( not including parameters ) -->
    <xsl:param name="request-uri"/>
    
    <!-- the base-path parameter ( i.e. the base url of the webapp ) -->
    <xsl:param name="base-path"/>
    
    <!-- the query part of the request -->
    <xsl:param name="query-string"/>
    
    <!-- the search query -->
    <xsl:param name="query"/>
    
    <xsl:param name="proxy-links"/>
    
    <xsl:param name="sunetid"/>
    
    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ipgroup"/>
    
    <xsl:param name="version"/>
    
    <xsl:param name="referrer"/>
    
    <xsl:param name="name"/>
    
    <!-- sourceid used for tracking to ID request origin: shc, cerner, laneconnex-engine, etc. -->
    <xsl:param name="sourceid"/>
    
    <xsl:variable name="path">
        <xsl:value-of select="substring($request-uri,string-length($base-path) + 1)"/>
    </xsl:variable>
    
    <xsl:variable name="regex-query">
        <xsl:if test="$query">
            <xsl:value-of select="replace($query,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>
    
    <!-- ====================  INCLUDED TEMPLATES ============================= -->
    <xsl:include href="laneweb-links.xsl"/>
    <xsl:include href="laneweb-login.xsl"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="child::node()"/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <!-- remove left column for lwc2txt --> 
    <xsl:template match="h:div[contains(@class,'leftColumn')]"/>
    
    <!-- replace h2 with link to full version for lwc2txt --> 
    <xsl:template match="h:body/h:h2[1]">
        <div>Lane Medical Library text version | <a href="{concat(replace($request-uri,'/m/lc2txt',''),'?',$query-string)}">Full version available here</a></div>
        <xsl:copy>
            <xsl:copy-of select="node()[name()!='a']"></xsl:copy-of>
        </xsl:copy>
    </xsl:template>
    
    <!-- internal links should refer to /m/lc2txt, external links get blank target, exclude login links  -->
    <xsl:template match="h:a[not(//h:ul[@id='login'])]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="matches(@href,'http.*lane(-local|-stage)?.stanford.edu/[^m/]')">
                    <xsl:attribute name="href">
                        <xsl:value-of select="replace(@href,'http.*lane(-local|-stage)?.stanford.edu/',concat($base-path,'/m/lc2txt/'))"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:when test="starts-with(@href,'/') and contains(@href,'.html') and not(contains(@href,'/m/'))">
                    <xsl:attribute name="href">
                        <xsl:value-of select="replace(@href,'^/',concat($base-path,'/m/lc2txt/'))"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:when test="not(@target) and starts-with(@href,'http')">
                    <xsl:attribute name="target">_blank</xsl:attribute>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
