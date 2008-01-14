<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:lw="http://lane.stanford.edu/laneweb/ns"
                xmlns:qm="http://lane.stanford.edu/querymap/ns"
                version="2.0">

    <xsl:variable name="resource-ids" select="/lw:laneweb/qm:query-map/qm:resource-map/qm:resource/@idref"/>
    <xsl:variable name="resource-count" select="count($resource-ids)"/>
    <xsl:variable name="result-suffix">
        <xsl:if test="$resource-count &gt; 1">
            <xsl:value-of select="/lw:laneweb/h:html/h:head/h:meta[@name='LW.plural']/@content"/>
        </xsl:if>
        <xsl:if test="$resource-count = 1">
            <xsl:value-of select="/lw:laneweb/h:html/h:head/h:meta[@name='LW.singular']/@content"/>
        </xsl:if>
    </xsl:variable>
    
    <xsl:template match="/lw:laneweb">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    
    <xsl:template match="h:body">
        <xsl:if test="$resource-count &gt; 0">
 			 <xsl:copy>
                <xsl:apply-templates/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template match="h:*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:p">
        <xsl:if test="$resource-count &gt; 0">
            <xsl:copy>
                <xsl:apply-templates/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="attribute::*[contains(.,'{$lw_descriptor}') or contains(.,'{$lw_keywords}') or contains(.,'{$lw_resultSuffix}') or contains(.,'{$lw_resourceCount}')]">
        <xsl:attribute name="{name(.)}">
            <xsl:value-of select="replace(
                                    replace(
                                        replace(
                                            replace(.,'\{\$lw_descriptor\}',/lw:laneweb/qm:query-map/qm:resource-map/qm:descriptor),
                                        '\{\$lw_resultSuffix\}',$result-suffix),
                                    '\{\$lw_keywords\}',/lw:laneweb/qm:query-map/qm:query),
                                  '\{\$lw_resourceCount\}',format-number($resource-count,'###'))"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:ul">
        <xsl:if test="$resource-count &gt; 0">
            <xsl:variable name="items" select="child::h:li"/>
            <xsl:copy>
                <xsl:for-each select="$resource-ids">
                    <xsl:apply-templates select="$items[@id=current()]"/>
                </xsl:for-each>
                <xsl:for-each select="'qmMore'">
                    <xsl:apply-templates select="$items[@id=current()]"/>
                </xsl:for-each>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>