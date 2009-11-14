<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                xmlns:s="http://irt.stanford.edu/search/2.0"
                exclude-result-prefixes="h sql s"
                version="2.0">
    
    <xsl:param name="source"/>
    <xsl:param name="q"/>
    
    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html[not(attribute::id)]"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='searchResults'">
                <xsl:apply-templates select="/doc/h:html[attribute::id]/h:body/child::node()"/>
            </xsl:when>
            <xsl:when test=".='search-terms'">
                <xsl:value-of select="$search-terms"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="attribute::href[contains(.,'{$keywords}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{$keywords}')"/>
            <xsl:value-of select="encode-for-uri($search-terms)"/>
        </xsl:attribute>
    </xsl:template>
 
    <xsl:template match="attribute::href[contains(.,'{id}')]">
        <xsl:attribute name="href">
            <xsl:value-of select="substring-before(.,'{id}')"/>
             <xsl:value-of select="/doc/s:search/@id"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
	<!-- TODO: metasearch counts ... will need to manage with JS instead	-->
    <xsl:template match="h:ul[attribute::class='searchFacets']/h:li">
        <xsl:variable name="facet" select="substring-before(attribute::id,'Facet')"/>
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:if test="$facet = $source">
				<xsl:apply-templates select="attribute::node()" />
				<xsl:attribute name="class"><xsl:value-of select="attribute::class" /> current</xsl:attribute>
			</xsl:if>
			<xsl:if test="number(/doc/sql:rowset/sql:row[sql:genre=$facet]/sql:hits) = 0">
				<xsl:apply-templates select="attribute::node()" />
				<xsl:attribute name="style">display:none;</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates select="child::node()" />
			<xsl:if test="/doc/sql:rowset/sql:row[sql:genre=$facet]/sql:hits and number(/doc/sql:rowset/sql:row[sql:genre=$facet]/sql:hits) != 0">
				<xsl:text> </xsl:text>
				<xsl:value-of
					select="format-number(/doc/sql:rowset/sql:row[sql:genre=$facet]/sql:hits,'###,###,##0')" />
			</xsl:if>
		</xsl:copy>
    </xsl:template>

</xsl:stylesheet>