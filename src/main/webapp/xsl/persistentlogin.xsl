<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h"
    version="2.0">
        
    <xsl:param name="lane-user-cookie"/>
    <xsl:param name="persistent-login"/>
       <xsl:param name="remove-persistent-login"/>
       
       <xsl:template match="*">
         <xsl:copy>
             <xsl:apply-templates select="attribute::node()|child::node()"/>
         </xsl:copy>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>    
    
    <xsl:template match="h:input[@type='checkbox']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
            <xsl:choose>
                <xsl:when test="$persistent-login = 'true'">
                        <xsl:attribute name="checked">true</xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="$lane-user-cookie != '' and $remove-persistent-login != 'true'">
                        <xsl:attribute name="checked">true</xsl:attribute>
                       </xsl:if>    
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
   </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
        
    
</xsl:stylesheet>
