<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    xmlns:st="http://lane.stanford.edu/search-templates/ns"
    xmlns="http://lane.stanford.edu/search-facet-result/ns"
    exclude-result-prefixes="st s"
    version="2.0">
    
    
    <xsl:template match="search-facet-counts">
        <xsl:apply-templates select="st:search-templates"/>
    </xsl:template>
    
    <xsl:template match="st:search-templates">
        {"results":{
        "status": "<xsl:value-of select="//s:search/@s:status"/>",
        "facets": 
        { 
        <xsl:apply-templates select="//st:template"/>
        }
        }
        }
    </xsl:template>
    
    <xsl:template match="st:template">
        <xsl:variable name="engine-ids" select="st:resource/@idref"/>
        <xsl:variable name="hits" select="sum(//s:resource[@s:status='successful' and @s:id=$engine-ids]/s:hits[.!=0])"/>
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="count(//s:resource[@s:id=$engine-ids]) = count(//s:resource[@s:status='successful' and @s:id=$engine-ids])">successful</xsl:when>
                <xsl:when test="count(//s:resource[@s:id=$engine-ids]) = count(//s:resource[@s:status='failed' and @s:id=$engine-ids])">failed</xsl:when>
                <xsl:when test="count(//s:resource[@s:id=$engine-ids]) = count(//s:resource[@s:status='canceled' and @s:id=$engine-ids])">canceled</xsl:when>
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