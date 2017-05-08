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

    <!-- enable pico fields if one or more pico input values present -->
    <xsl:template match="h:input[($p or $i or $c or $o) and (@name='p' or @name='i' or @name = 'c' or @name = 'o')]/@disabled"/>

    <xsl:template match="h:input[@name='source']/@value">
        <xsl:attribute name="value" select="$search-source"/>
    </xsl:template>
    
    <xsl:template match="h:input[@name='q'][@data-placeholder]/@placeholder">
        <xsl:attribute name="placeholder">
            <xsl:choose>
                <!-- if there is a query the placeholder for the q input is the data-placeholder from the active tab -->
                <xsl:when test="$query">
                    <xsl:value-of  select="ancestor::h:form[@class='search-form']//h:div[@class='search-tab'][@data-source = $search-source]/@data-placeholder"/>
                </xsl:when>
                <!-- if there is not a query the placeholder for the q input is the data-placeholder -->
                <xsl:otherwise>
                    <xsl:value-of  select="parent::h:input/@data-placeholder"/>
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
    
    <!-- case 73361 Autofill all the forms (ask us, feedback, etc) while logged into MyLane -->
    <xsl:template match="h:input[@name='email']/@value">
        <xsl:choose>
            <xsl:when test="string-length($email) &gt; 0">
                <xsl:attribute name="value">
                    <xsl:value-of select="$email"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- case 73361 Autofill all the forms (ask us, feedback, etc) while logged into MyLane -->
    <xsl:template match="h:input[@name='full-name']/@value">
        <xsl:choose>
            <xsl:when test="string-length($name) &gt; 0">
                <xsl:attribute name="value">
                    <xsl:value-of select="$name"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
