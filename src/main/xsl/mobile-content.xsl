<?xml version="1.0" encoding="UTF-8"?>
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
    
    <xsl:param name="userid"/>
    
    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ipgroup"/>
    
    <xsl:param name="todays-hours"/>

    <xsl:param name="version"/>
    
    <xsl:param name="referrer"/>
    
    <xsl:param name="name"/>
    
    <!-- sourceid used for tracking to ID request origin: shc, cerner, laneconnex-engine, etc. -->
    <xsl:param name="sourceid"/>
    
    <!-- json version of the data model -->
    <xsl:param name="model"/>
    
    <!-- target parameter for shibboleth discovery service page -->
    <xsl:param name="return"/>

    <xsl:variable name="path">
        <xsl:value-of select="substring($request-uri,string-length($base-path) + 1)"/>
    </xsl:variable>
    
    <!-- the root node of the requested content document -->
    <xsl:variable name="source-doc" select="/*/h:html[1]"/>
    
    <!-- the template document -->
    <xsl:variable name="template" select="/*/h:html[2]"/>
    
    <xsl:variable name="regex-query">
        <xsl:if test="$query">
            <xsl:value-of select="replace($query,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>
    
    <!-- ====================  INCLUDED TEMPLATES ============================= -->
    <xsl:include href="laneweb-links.xsl"/>
    
    <!-- ====================  DEFAULT TEMPLATES ============================= -->
    <!-- root template applies templates on the template document -->
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="$template">
                <xsl:apply-templates select="$template"/>
            </xsl:when>
            <!-- when there is not a template (ie the request is for /plain/**.html) process whole document -->
            <xsl:otherwise>
                <xsl:apply-templates select="child::node()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- default element match, copies the element and applies templates on all childeren and attributes -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- default attribute match, copies the attribute -->
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='content'">
                <xsl:apply-templates select="$source-doc/h:body/node()"/>
            </xsl:when>
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
            </xsl:when>
            <xsl:when test=".='todays-hours'">
                <xsl:value-of select="$todays-hours"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:input/@value">
        <xsl:attribute name="{name()}">
		    <xsl:choose>
		        <xsl:when test="contains(.,'{search-terms}')">
	                <xsl:value-of select="replace(.,'\{search-terms\}',$regex-query)"/>
		        </xsl:when>
		        <xsl:otherwise>
		            <xsl:value-of select="." />
		        </xsl:otherwise>
		    </xsl:choose>
	    </xsl:attribute>
    </xsl:template>

    <!-- insert footer into "page" div -->
    <xsl:template match="h:div[@data-role='page']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
	        <div class="footer">
	            <xsl:apply-templates select="$template//h:div[@class='footer2copy']/*"/>
	        </div>
        </xsl:copy>
    </xsl:template>

    <!--  remove template version of footer after copying to 'page' div -->
    <xsl:template match="h:body/h:div[@class='footer2copy']"/>
    
    <xsl:template match="h:script[@id='model']/text()">
        <xsl:text>
            model = </xsl:text>
        <xsl:value-of select="$model"/>
        <xsl:text>;
        </xsl:text>
    </xsl:template>
    
    <!-- external links get blank target, exclude login links  -->
    <xsl:template match="h:a">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="@href = '/logout' or starts-with(@href,'/secure')">
                    <xsl:apply-templates select="attribute::node()"/>
                </xsl:when>
                <xsl:when test="not(@target) and starts-with(@href,'http')">
                    <xsl:attribute name="class">newWindow</xsl:attribute>
                    <xsl:attribute name="target">_blank</xsl:attribute>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- select content to show on persistentlogin.html -->
    <xsl:template match="node()[attribute::id='loginName' and $name != '']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:value-of select="$name"/>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- full version link is context sensitive, pulling link from page's @data-desktop-url -->
    <xsl:template match="h:a[@id='desktop-url']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="//h:div[@data-desktop-url]">
                    <xsl:attribute name="href">
		                <xsl:call-template name="make-link">
		                    <xsl:with-param name="link" select="//h:div/@data-desktop-url"/>
		                    <xsl:with-param name="attr" select="'data-desktop-url'"/>
		                </xsl:call-template>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="attribute::node()"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
