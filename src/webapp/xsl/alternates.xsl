<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                exclude-result-prefixes="h"
                version="1.0">

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
                
  <xsl:template match="h:ul[@class='eresources']">
      <xsl:copy>
          <xsl:copy-of select="@*"/>
          <xsl:apply-templates/>
      </xsl:copy>
  </xsl:template>

  <xsl:template match="h:li[h:ul[@class='mfhd']/h:li[@class='_907'] = 'EXC;']"/>

  <xsl:template match="h:li">
      <xsl:copy>
          <xsl:copy-of select="@*"/>
          <span class="display">
                <xsl:value-of select="h:ul[@class='bib']/h:li[@class='_245']/text()"/>
                <xsl:copy-of select="h:ul[@class='bib']/h:li[@class='_245']/*"/>
          </span>
          <xsl:copy-of select="*"/>
      </xsl:copy>
      <xsl:if test="h:ul[@class='bib']/h:li[@class='_249']">
      <xsl:variable name="current-node" select="."/>
      <xsl:for-each select="h:ul[@class='bib']/h:li[@class='_249']">
          <xsl:call-template name="duplicate">
              <xsl:with-param name="node" select="$current-node"/>
              <xsl:with-param name="_249" select="."/>
          </xsl:call-template>
      </xsl:for-each>
      </xsl:if>
  </xsl:template>
  
  <xsl:template name="duplicate">
      <xsl:param name="node"/>
      <xsl:param name="_249"/>
      <li>
          <xsl:copy-of select="$node/@*"/>
          <span class="display">
              <xsl:value-of select="$_249/text()"/>
              <xsl:copy-of select="$_249/*"/>
          </span>
          <xsl:copy-of select="$node/*"/>
      </li>
  </xsl:template>
  
</xsl:stylesheet>

