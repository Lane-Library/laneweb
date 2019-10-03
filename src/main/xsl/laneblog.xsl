<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="." />
    </xsl:template>

    <xsl:template match="h:content">
        <xsl:apply-templates select="h:article" />
    </xsl:template>
    
    
    <xsl:template match="h:img">
      <xsl:copy>
         <xsl:attribute name="src" select="replace(@src,'http:','')"/>
         <xsl:if test="ends-with( ./@src, 'jpg')">
            <xsl:attribute name="src" select="replace(@src, '\.jpg' ,'-153.jpg')"/>
         </xsl:if>
         <xsl:if test="ends-with( ./@src, 'png')">
            <xsl:attribute name="src" select="replace(@src, '\.png' ,'-153.png')"/>
         </xsl:if> 
      </xsl:copy>
    </xsl:template>

</xsl:stylesheet>