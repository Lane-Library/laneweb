<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:er="http://lane.stanford.edu/eresources#" xmlns:dc="http://purl.org/dc/elements/1.1/"
    exclude-result-prefixes="er dc" xmlns="http://www.w3.org/1999/xhtml">
    <xsl:template match="er:eresources">
        <html>
            <head>
                <title>eResource Metadata Browser</title>
            </head>
            <body>
                    <div id="lw:sidebar"><div style="padding:2em">count = <xsl:value-of select="count(*)"/></div></div>
                <xsl:if test="*">
                    <ul>
                        <xsl:apply-templates>
                            <xsl:sort select="dc:title"/>
                        </xsl:apply-templates>
                    </ul>
                </xsl:if>
            </body>    
        </html>
    </xsl:template>
    
    <xsl:template match="er:bib[count(er:mfhd/er:link) = 1]">
        <li>
            <a href="{er:mfhd/er:link/er:url/text()}">
                <xsl:value-of select="dc:title"/>
            </a>
            <xsl:apply-templates select="er:mfhd" mode="inline"/>
        </li>
    </xsl:template>
  
    <xsl:template match="er:bib[count(er:mfhd/er:link) = 0]">
        <li>
            <a href="{er:mfhd/er:alt-link/er:url/text()}">
                <xsl:value-of select="dc:title"/>
            </a>
            <xsl:apply-templates select="er:mfhd" mode="inline"/>
        </li>
    </xsl:template>
    
    <xsl:template match="er:bib[count(er:mfhd) &gt; 1]">
        <li>
            <xsl:value-of select="dc:title"/>
            <ul>
                <xsl:apply-templates select="er:mfhd" mode="list">
                    <xsl:sort select="dc:publisher"/>
                </xsl:apply-templates>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="er:bib">
        <li>
            <xsl:value-of select="dc:title"/><xsl:apply-templates select="er:mfhd" mode="inline"/>
            <ul>
            <xsl:apply-templates select="er:mfhd" mode="list"/>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="er:mfhd[count(er:link) =1]" mode="list">
        <li>
            <a href="{er:link/er:url/text()}">
                <xsl:value-of select="dc:publisher"/>
                <xsl:if test="not(dc:publisher)">
                  <xsl:value-of select="../dc:title"/>
                </xsl:if>
            </a>
            <xsl:apply-templates select="er:link/er:summary-holdings"/>
            <xsl:apply-templates select="er:alt-link"/>
            <!-- xsl:for-each select="*"><xsl:value-of select="name()"/>|</xsl:for-each> -->
        </li>
    </xsl:template>
    
    <xsl:template match="er:mfhd" mode="list">
        <xsl:apply-templates select="er:link" mode="list"/>
    </xsl:template>
    
    <xsl:template match="er:mfhd" mode="inline">
        <xsl:apply-templates select="er:link/er:summary-holdings"/>
        <xsl:apply-templates select="dc:publisher"/>
    </xsl:template>
    
    <xsl:template match="er:link" mode="list">
        <li><a href="{er:url}">
        <xsl:value-of select="er:label"/>
        <xsl:if test="er:label=''">
          <xsl:value-of select="ancestor::er:bib/dc:title"/>
          </xsl:if>
        </a></li>
    </xsl:template>
    
    <xsl:template match="er:alt-link">
        <xsl:text> </xsl:text><a href="{er:url}"><xsl:value-of select="er:label"/></a>
    </xsl:template>
    
    <xsl:template match="dc:publisher">
        <xsl:text> (</xsl:text><xsl:value-of select="."/>)
    </xsl:template>
    
    <xsl:template match="er:link">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="er:summary-holdings">
        <xsl:text> </xsl:text><xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="er:url|er:label"/>
    
    
</xsl:stylesheet>
