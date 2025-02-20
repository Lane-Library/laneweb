<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <xsl:template match="h:input[@name='q' and $query]/@value">
        <xsl:attribute name="value" select="$query"/>
    </xsl:template>

    <xsl:template match="h:input[@name='p' and $p]/@value">
        <xsl:attribute name="value" select="$p"/>
    </xsl:template>

    <xsl:template match="h:input[@name='i' and $i]/@value">
        <xsl:attribute name="value" select="$i"/>
    </xsl:template>

    <xsl:template match="h:input[@name='c' and $c]/@value">
        <xsl:attribute name="value" select="$c"/>
    </xsl:template>

    <xsl:template match="h:input[@name='o' and $o]/@value">
        <xsl:attribute name="value" select="$o"/>
    </xsl:template>

    <xsl:template match="h:input[@name='source' and ancestor::h:form/@class = 'search-form']/@value">
        <xsl:attribute name="value" select="$search-source"/>
    </xsl:template>

    <xsl:template match="h:input[@name='q' and ancestor::h:form[contains(@class,'search-form')]]/@placeholder">
        <xsl:variable name="data-placeholder" select="ancestor::h:form[contains(@class,'search-form')]//h:option[@value = $search-source]/@data-placeholder"/>
        <xsl:attribute name="placeholder">
            <xsl:choose>
                <xsl:when test="$data-placeholder != ''">
                    <xsl:value-of select="$data-placeholder"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>            
        </xsl:attribute>
    </xsl:template>

    <!-- enable sourceid, facets and sort hidden inputs if corresponding param is present -->
    <xsl:template match="h:input[@name='sourceid' and $sourceid]/@value">
        <xsl:attribute name="value" select="$sourceid"/>
    </xsl:template>

    <xsl:template match="h:input[@name='sourceid' and $sourceid]/@disabled"/>

    <xsl:template match="h:input[@name='facets' and $facets]/@value">
        <xsl:attribute name="value" select="$facets"/>
    </xsl:template>

    <xsl:template match="h:input[@name='facets' and $facets]/@disabled"/>

    <xsl:template match="h:input[@name='sort' and $sort]/@value">
        <xsl:attribute name="value" select="$sort"/>
    </xsl:template>

    <xsl:template match="h:input[@name='sort' and $sort]/@disabled"/>


</xsl:stylesheet>
