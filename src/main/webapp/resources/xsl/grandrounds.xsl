<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml" 
    exclude-result-prefixes="h"
    version="2.0">
    
    <xsl:param name="category"/>
    
    <xsl:param name="year"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="*/h:html[1]"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:apply-templates select="/*/h:html[2]/h:body/h:div[@class=$category][tokenize(h:div[@class='date'], '-')[1] = $year]"/>
    </xsl:template>
    
    <xsl:template match="h:title|h:h2">
        <xsl:copy>
            <xsl:value-of select="concat($year, ' ', .)"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:if test="string-length(normalize-space()) &gt; 0">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="/*/h:html[2]/h:body/h:div">
        <div class="class">
            <div class="yui3-g">
                <div class="yui3-u-1-5">
                    <xsl:apply-templates select="h:div[@class='date']"/>
                </div>
                <div class="yui3-u-4-5">
                    <div class="details">
                        <xsl:apply-templates select="child::node()[not(@class='date')]"/>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:div[@class='date']">
        <xsl:variable name="tokens" select="tokenize(., '-')"/>
        <div class="date">
            <div class="month-day">
                <xsl:call-template name="month">
                    <xsl:with-param name="month" select="$tokens[2]"/>
                </xsl:call-template>
                <xsl:text> </xsl:text>
                <xsl:value-of select="number($tokens[3])"/>
            </div>
            <div class="year">
                <xsl:value-of select="$tokens[1]"/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:div[@class='title']">
        <h3><xsl:apply-templates select="child::node()"/></h3>
    </xsl:template>
    
    <xsl:template name="month">
        <xsl:param name="month"/>
        <xsl:choose>
            <xsl:when test="$month='01'">January</xsl:when>
            <xsl:when test="$month='02'">February</xsl:when>
            <xsl:when test="$month='03'">March</xsl:when>
            <xsl:when test="$month='04'">April</xsl:when>
            <xsl:when test="$month='05'">May</xsl:when>
            <xsl:when test="$month='06'">June</xsl:when>
            <xsl:when test="$month='07'">July</xsl:when>
            <xsl:when test="$month='08'">August</xsl:when>
            <xsl:when test="$month='09'">September</xsl:when>
            <xsl:when test="$month='10'">October</xsl:when>
            <xsl:when test="$month='11'">November</xsl:when>
            <xsl:when test="$month='12'">December</xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
