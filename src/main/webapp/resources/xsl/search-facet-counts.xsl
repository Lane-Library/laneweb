<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:st="http://lane.stanford.edu/search-templates/ns"
    xmlns="http://lane.stanford.edu/search-facet-result/ns"
    exclude-result-prefixes="st"
    version="2.0">
    
    <!-- 
        JS: scrapes facetIds from content and builds query: ?f=catalog-all,catalog-lois
        XSLT: from facetIds, pull list of resources using search-template.xsl
        fetch counts/status for all unique resources AND engines
        return per facet count in JSON format
    -->
    <xsl:param name="facets"/>
    
    <xsl:variable name="tokenized-facets">
        <xsl:choose>
            <xsl:when test="$facets">
                <xsl:value-of select="tokenize($facets,',')"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="engine-ids" select="//st:template[contains($tokenized-facets,@id)]/st:engine/@idref"/>
    
    <xsl:variable name="engines-string">
        <xsl:for-each select="distinct-values($engine-ids)">
            <xsl:value-of select="."/>
            <xsl:if test="position() != last()">
                <xsl:text>,</xsl:text>            
            </xsl:if>
        </xsl:for-each>
    </xsl:variable>
    
    <xsl:variable name="resource-ids" select="//st:template[contains($tokenized-facets,@id)]/st:resource/@idref"/>
    
    <xsl:variable name="resources-string">
        <xsl:for-each select="distinct-values($resource-ids)">
            <xsl:value-of select="."/>
            <xsl:if test="position() != last()">
                <xsl:text>,</xsl:text>            
            </xsl:if>
        </xsl:for-each>
    </xsl:variable>
    
    <xsl:variable name="search-request">
        <xsl:choose>
            <xsl:when test="count($engine-ids) > 0">
                <xsl:value-of select="concat('cocoon://apps/search/engine/xml/',$engines-string)"/>   
            </xsl:when>
            <xsl:when test="count($resource-ids) > 0">
                <xsl:value-of select="concat('cocoon://apps/search/resource/xml/',$resources-string)"/>   
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="search-node">
        <xsl:copy-of select="document($search-request)"/>
    </xsl:variable>
    
    <xsl:template match="search-facet-counts">
        <xsl:apply-templates select="st:search-templates"/>
    </xsl:template>
    
    <xsl:template match="st:search-templates">
        {"results":{
        "status": "<xsl:value-of select="$search-node/search/@status"/>",
        "facets": 
        {
            <xsl:apply-templates select="//st:template[contains($tokenized-facets,@id)]"/>
        }
        }
        }
    </xsl:template>
    
    <xsl:template match="st:template">
        <xsl:variable name="my-engine-ids" select="st:engine/@idref"/>
        <xsl:variable name="my-resource-ids" select="st:resource/@idref"/>
        <xsl:variable name="hits" select="count($search-node//engine[@status='successful' and @id=$my-engine-ids]/resource/content) + count($search-node//resource[@status='successful' and @id=$my-resource-ids]/content)"/>
        <xsl:variable name="totalEngineResourceCount" select="count($my-engine-ids) + count($my-resource-ids)"/>
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="$totalEngineResourceCount = count($search-node//engine[@status='successful' and @id=$my-engine-ids]) + count($search-node//resource[@status='successful' and @id=$my-resource-ids])">successful</xsl:when>
                <xsl:when test="$totalEngineResourceCount = count($search-node//engine[@status='failed' and @id=$my-engine-ids]) + count($search-node//resource[@status='failed' and @id=$my-resource-ids])">failed</xsl:when>
                <xsl:when test="$totalEngineResourceCount = count($search-node//engine[@status='canceled' and @id=$my-engine-ids]) + count($search-node//resource[@status='canceled' and @id=$my-resource-ids])">canceled</xsl:when>
            </xsl:choose>
        </xsl:variable>
            "<xsl:value-of  select="@id"/>":
            {
            "status":"<xsl:value-of  select="$status"/>",
            "hits" : "<xsl:value-of  select="format-number($hits, '###,##0')" />"
            }
            <xsl:if test="position() != last()">
                <xsl:text>,</xsl:text>            
            </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>