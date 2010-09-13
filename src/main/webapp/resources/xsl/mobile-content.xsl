<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- the request-uri ( not including parameters ) -->
    <xsl:param name="request-uri"/>
    
    <!-- the base-path parameter ( i.e. the base url of the webapp ) -->
    <xsl:param name="base-path"/>
    
    <!-- the query part of the request -->
    <xsl:param name="query-string"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="child::node()"/>
    </xsl:template>

    <xsl:template match="h:div[contains(@class,'leftColumn')]"/>

    <xsl:template match="h:body/h:h2[1]">
        <div>Lane Medical Library text version | <a href="{concat(replace($request-uri,'/mobile',''),'?',$query-string)}">Full version available here</a></div>
        <xsl:copy>
            <xsl:copy-of select="node()[name()!='a']"></xsl:copy-of>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="h:a">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="matches(@href,'http.*lane(-beta|-stage)?.stanford.edu/')">
                    <xsl:attribute name="href">
                        <xsl:value-of select="replace(@href,'http.*lane(-beta|-stage)?.stanford.edu/',concat($base-path,'/mobile/'))"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:when test="starts-with(@href,'/') and contains(@href,'.html') and not(contains(@href,'/m/'))">
                    <xsl:attribute name="href">
                        <xsl:value-of select="replace(@href,'^/',concat($base-path,'/mobile/'))"/>
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
