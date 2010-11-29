<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- set the selected option of the search form -->
    <xsl:template match="h:option[parent::h:select[@id='searchSource']]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="@value = $search-form-select">
                <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>


    <!-- TODO did the id of the input change? -->
    <xsl:template match="h:input[@name='q']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="$query != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$query"/>
                </xsl:attribute>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- add sourceid input to search form if sourceid param present -->
    <xsl:template match="h:fieldset[@id='searchFields' or parent::h:form[@class='breadcrumbForm']]">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:if test="$sourceid">
                <input type="hidden" name="sourceid" value="{$sourceid}"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- add sourceid input to quick link options if sourceid param present -->
    <xsl:template match="h:option[parent::h:select[@id='qlinks']]">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$sourceid and starts-with(@value,'/')">
                    <xsl:apply-templates select="attribute::node()[not(name()='value')]"/>
                    <xsl:attribute name="value">
                        <xsl:value-of select="concat(@value,'?sourceid=',$sourceid)"/>
                    </xsl:attribute>
                    <xsl:apply-templates/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
