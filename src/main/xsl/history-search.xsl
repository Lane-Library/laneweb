<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <!-- This stylesheet handles the history metasearch by combining the markup from a list of
        links with the xml output of the metasearch. -->
    
    <!-- copy elements by default -->
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- copy attributes by default -->
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <!-- process only the html withing the combined search and html document -->
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
    
    <!-- creates a variable named list containing processed links in li elements then outputs
        the ones that have hits greater than 0 sorted by hits in descending order -->
    <xsl:template match="h:ul">
        <xsl:variable name="list">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:copy>
            <xsl:for-each select="$list/*[number(h:span) &gt; 0]">
                <xsl:sort select="h:span" data-type="number" order="descending"/>
                <xsl:copy-of select="."/>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>
    
    <!-- produces the links with the url from the searched resource and adds a hits span -->
    <xsl:template match="h:li/h:a">
        <xsl:variable name="id" select="@id"/>
        <xsl:variable name="resource" select="/doc/s:search/s:engine/s:resource[@s:id = $id]"/>
        <xsl:copy>
            <xsl:attribute name="href" select="$resource/s:url"/>
            <xsl:apply-templates select="*|text()"/>
        </xsl:copy>
        <span><xsl:text> </xsl:text><xsl:value-of select="$resource/s:hits"/></span>
        <xsl:if test="/doc/s:search/s:engine/s:resource[@s:id = $id]/s:content">
            <ul>
                <xsl:apply-templates select="/doc/s:search/s:engine/s:resource[@s:id = $id]/s:content"/>
            </ul>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="s:content">
        <li>Biographical Sketch of <a href="{s:url}"><xsl:value-of select="s:title"/></a></li>
    </xsl:template>
    
</xsl:stylesheet>