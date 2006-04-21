<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:r="http://apache.org/cocoon/request/2.0"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="r"
                version="1.0">
                
    <xsl:template match="/">
        <html>
            <head>
                <title>Request Data</title>
            </head>
            <xsl:apply-templates/>
        </html>
    </xsl:template>
    
    <xsl:template match="r:request">
        <body>
            <h1>Request Data</h1>
            <xsl:apply-templates/>
        </body>
    </xsl:template>
    
    <xsl:template match="r:requestHeaders[count(*) &gt; 0]">
        <h2>Request Headers</h2>
        <dl>
            <xsl:apply-templates/>
        </dl>
    </xsl:template>
    
    <xsl:template match="r:requestParameters[count(*) &gt; 0]">
        <h2>Request Parameters</h2>
        <dl>
            <xsl:apply-templates/>
        </dl>
    </xsl:template>
    
    <xsl:template match="r:header">
        <dt><h3><xsl:value-of select="@name"/></h3></dt>
        <dd><xsl:value-of select="text()"/></dd>
    </xsl:template>
    
    <xsl:template match="r:parameter">
        <dt><h3><xsl:value-of select="@name"/></h3></dt>
        <dd><xsl:value-of select="r:value/text()"/></dd>
    </xsl:template>

</xsl:stylesheet>