<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:f="https://lane.stanford.edu/functions"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="h f s xsd" version="2.0">
    
    <xsl:param name="browse-query"/>

    <xsl:include href="search-browse-common.xsl"/>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- transforms eresource bib result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <li class="resource" data-sid="{s:id}" data-index="{ position() -1}">
            <xsl:copy-of select="f:maybe-add-doi-attribute(.)"/>
            <div class="resource-detail">
            <span class="primaryType">
            	<xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:copy-of select="f:primaryLink(s:link[1])"/>
            <xsl:apply-templates select="s:pub-author"/>
            <!-- don't show citation/pub-text when already present in "access via" link to parent record -->
            <xsl:if test="s:link/s:locationName != s:pub-text"> 
                <xsl:apply-templates select="s:pub-text"/>
            </xsl:if>
            <xsl:copy-of select="f:descriptionTrigger(.)"/>
            <xsl:copy-of select="f:handleDigitalLinks(s:link[@type = 'lane-digital'])"/>
            <xsl:copy-of select="f:handleLanePrintLinks(s:link[@type = 'lane-print'], .)"/>
            <div class="more-detail-container">
                <i class="fa-solid fa-ellipsis fa-xl"></i>
                <xsl:copy-of select="f:build-source-info(.)" />
            </div>
            
            </div>
            <div class="bookcover-container">
            <xsl:if test="contains(s:primaryType, 'Book') or contains(s:primaryType, 'Journal')">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}">
                    <xsl:copy-of select="f:maybe-add-bcids-attribute(.)" />
                </div>
            </xsl:if>
            <xsl:if test="s:primaryType = 'Article'">
                <div class="bookcover"></div>
            </xsl:if>
            </div>
            
        </li>
    </xsl:template>

    <xsl:template match="s:primaryType">
        <xsl:if test="$browse-query and (contains($browse-query,'Book') or contains($browse-query,'Journal'))">
            <xsl:choose>
                <xsl:when test="not(contains(., 'Print')) and ../s:link[s:label = 'Lane Catalog Record']">Digital/Print</xsl:when>
                <xsl:when test="contains(., 'Print')">Print</xsl:when>
                <xsl:otherwise>Digital</xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div>
            <xsl:value-of select="."/>
        </div>
    </xsl:template>
    
</xsl:stylesheet>
