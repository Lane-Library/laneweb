<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" 
  xmlns:h="http://www.w3.org/1999/xhtml" version="1.0">
    
    <xsl:template match="/h:html">
        <html><head>
        <style type="text/css">
            .display .filing {font-weight:bolder;font-size:larger }
            li { list-style-type:none }
            .eresource, .a, .b, .c, .d { background-color:#DFEFEF;margin:1em;border:thin black solid }
            .bib { background-color:#EFDFEF }
            .mfhd { background-color:#DFDFEF }
            .tag { font-weight:bolder }
            </style>
        </head>
        <xsl:apply-templates select="h:body"/>
        </html>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*[starts-with(.,'_')]">
        <span class="tag" style="font-weight:bolder"><xsl:value-of select="substring-after(.,'_')"/>: </span>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="@class[.='selected']">
        <span style="font-weight:bolder">650: </span><span style="color:red">Selected Web Resource</span>
    </xsl:template>
    
    <xsl:template match="@class[.='core']">
        <span style="font-weight:bolder">650: </span><span style="color:red">Core Material</span>
    </xsl:template>

</xsl:stylesheet>
