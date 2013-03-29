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
