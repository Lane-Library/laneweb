<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="2.0">

    <xsl:param name="query"/>
 
    <xsl:template match="*">
         <xsl:copy>
             <xsl:apply-templates select="attribute::node()|child::node()"/>
         </xsl:copy>
         
    </xsl:template>
        
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html[1]"/>
    </xsl:template>

    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="/doc/h:html[2]/h:head/h:title">
                        <xsl:value-of select="/doc/h:html[2]/h:head/h:title"/>
                   </xsl:when>
                   <xsl:otherwise>
                       <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='bassettContent'">
                <xsl:call-template name="bassettContent"/>
            </xsl:when>
            <xsl:when test=".='accordionMenu'">
                <xsl:call-template name="accordionMenu"/>
            </xsl:when>
         </xsl:choose>
    </xsl:template>


    <xsl:template name="bassettContent">
        <xsl:apply-templates select="/doc/h:html[2]/h:body/node()"/>
    </xsl:template>
    
    <xsl:template name="accordionMenu">
        <xsl:apply-templates select="/doc/h:html[3]/h:body/node()"/>
    </xsl:template>

    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

</xsl:stylesheet>