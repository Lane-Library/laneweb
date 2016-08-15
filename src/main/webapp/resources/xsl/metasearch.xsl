<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="query-string"/>
    <xsl:param name="request-uri"/>
    <xsl:param name="source"/>
    
    <xsl:variable name="search-terms" select="/doc/s:search/s:query/text()"/>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <!-- IE browsers cannot import NOSCRIPT elements (LANE.core.importNode) so only display on non-ajax requests -->
            <xsl:if test="not(contains($request-uri,'/plain/')) and /doc/s:search/@s:status != 'successful'">
                <noscript>
                    <h2>
                        Search still running ... <a href="search.html?w=2500&amp;source={$source}&amp;q={$search-terms}">Click to see more hit counts</a>
                    </h2>
                </noscript>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="processing-instruction()[. = 'search-terms']">
        <xsl:value-of select="$search-terms"/>
    </xsl:template>
    
    <xsl:template match="attribute::href[contains(.,'{$q}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="replace(.,'\{\$q\}',replace($search-terms,'(\\|\$)','\\$1'))"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="node()[child::node()[contains(attribute::class,'metasearch')]]">
        <xsl:variable name="id" select="child::node()[contains(attribute::class,'metasearch')]/@id"/>
        <xsl:variable name="status">
            <xsl:value-of select="/doc/s:search/s:engine/s:resource[@s:id = $id]/@s:status"/>
        </xsl:variable>
        <xsl:variable name="success">
            <xsl:if test="$status = 'successful'">true</xsl:if>
        </xsl:variable>
        <xsl:variable name="failure">
            <xsl:if test="$status = 'failed' or $status = 'canceled'">true</xsl:if>
        </xsl:variable>
        
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:for-each select="child::node()">
                <xsl:choose>
                    <xsl:when test="contains(@class,'metasearch')">
                        <xsl:copy>
                            <xsl:apply-templates select="@*"/>
                            <xsl:attribute name="href">
                                <xsl:value-of select="/doc/s:search/s:engine/s:resource[@s:id = $id]/s:url"/>
                            </xsl:attribute>
                            <xsl:if test="$success = 'true'">
                                <xsl:attribute name="class">
                                    <xsl:value-of select="replace(@class,'metasearch','')"/>
                                </xsl:attribute>
                                <!-- using target _blank here for consistency w/ JS-built links -->
                                <xsl:attribute name="target">_blank</xsl:attribute>
                            </xsl:if>
                            <xsl:if test="$failure = 'true'">
                                <xsl:attribute name="class">
                                    <xsl:value-of select="replace(@class,'metasearch','')"/>
                                </xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates select="node()"/>
                        </xsl:copy>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>

            <span class="searchCount">
                <xsl:text> </xsl:text>
                <xsl:choose>
                    <xsl:when test="$success = 'true'">
                        <xsl:value-of select="format-number(/doc/s:search/s:engine/s:resource[@s:id = $id]/s:hits, '###,##0')"/>
                    </xsl:when>
                    <xsl:when test="$failure = 'true'">
                        <xsl:text> ? </xsl:text>
                    </xsl:when>
                </xsl:choose>
            </span>
        </xsl:copy>
        
        
        
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
</xsl:stylesheet>