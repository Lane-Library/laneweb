<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml">
    
    <xsl:output indent="yes"/>
    <xsl:param name="type" select="'ej'"/>

    <xsl:template match="h:head"/>
  
    <xsl:template match="h:ul[@class='eresources']">
      <ul>
        <xsl:apply-templates select="h:li/h:ul[@class='bib']/h:li[@class='_650' and not( self::node() = following::h:li[@class='_650'] )]">
          <xsl:sort select="."/>
        </xsl:apply-templates>
      </ul>
  </xsl:template>

  <xsl:template match="h:li"/>
  
    
    <xsl:template match="h:li[@class='_650']">
        <li>
          <a>
            <xsl:attribute name="href">
              <xsl:choose>
                <xsl:when test="contains(., ' ')">
                  <xsl:call-template name="handleSpace">
                    <xsl:with-param name="value" select="."/>
                  </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="concat($type,'.html?subject=', .)"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="."/>
          </a>
        </li>
    </xsl:template>
    
    <xsl:template name="handleSpace">
      <xsl:param name="value"/>
      <xsl:variable name="converted">
        <xsl:value-of select="concat(substring-before($value, ' '), '%20', substring-after($value, ' '))"/>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="contains($converted, ' ')">
          <xsl:call-template name="handleSpace">
            <xsl:with-param name="value" select="$converted"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="concat($type,'.html?subject=',$converted)"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>
    
    <xsl:template match="text()"/>

</xsl:stylesheet> 
