<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:lw="http://lane.stanford.edu/laneweb/ns"
                xmlns:qm="http://lane.stanford.edu/querymap/ns"
                version="1.0">

    <xsl:variable name="resource-ids" select="/lw:laneweb/qm:query-map/qm:resource-map/qm:resource/@idref"/>
    <xsl:variable name="resource-count" select="count($resource-ids)"/>
    
    <xsl:template match="/lw:laneweb">
        <xsl:apply-templates select="h:html"/>
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
    
    <xsl:template match="h:span[@class='lw_plural']">
        <xsl:if test="$resource-count &gt; 1">
            <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:span[@class='lw_singular']">
        <xsl:if test="$resource-count = 1">
            <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:span[@class='lw_descriptor']">
        <xsl:value-of select="/lw:laneweb/qm:query-map/qm:resource-map/qm:descriptor"/>
    </xsl:template>
    
    <xsl:template match="h:ul">
        <xsl:if test="$resource-count &gt; 0">
            <xsl:variable name="items" select="child::h:li"/>
            <xsl:copy>
                <xsl:for-each select="$resource-ids">
                    <xsl:apply-templates select="$items[@id=current()]"/>
                </xsl:for-each>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>