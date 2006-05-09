<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns="http://www.w3.org/1999/xhtml"
                       version="1.0"
                       exclude-result-prefixes="h">

  <xsl:param name="affiliation"/>
  <xsl:param name="source"/>
  <xsl:param name="keywords"/>
  <xsl:param name="request-uri"/>
  
  <xsl:template match="@content[.='{affiliation}']">
    <xsl:attribute name="content">
      <xsl:value-of select="$affiliation"/>
    </xsl:attribute>
  </xsl:template>
  
  <xsl:template match="@href[contains(.,'://') and not(parent::h:link)]">
    <xsl:copy-of select="."/>
    <xsl:variable name="noproxy">
      <xsl:choose>
        <xsl:when test="starts-with(.,'http://laneproxy.stanford.edu/login?')">
          <xsl:value-of select="substring-after(.,'url=')"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="."/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="noscheme" select="substring-after($noproxy,'://')"/>
    <xsl:variable name="dcssip">
      <xsl:choose>
        <xsl:when test="contains($noscheme,'/')">
          <xsl:value-of select="substring-before($noscheme,'/')"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$noscheme"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="uri">
      <xsl:choose>
        <xsl:when test="contains($noscheme,'/')">
          <xsl:value-of select="substring-after($noscheme,$dcssip)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="'/'"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="dcsuri">
        <xsl:choose>
            <xsl:when test="not(contains($uri,'?'))">
                <xsl:value-of select="$uri"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring-before($uri,'?')"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
      <xsl:variable name="query">
          <xsl:choose>
              <xsl:when test="not(contains($uri,'?'))">
                  <xsl:value-of select="''"/>
              </xsl:when>
              <xsl:otherwise>
                  <xsl:value-of select="substring-after($uri,'?')"/>
              </xsl:otherwise>
          </xsl:choose>
      </xsl:variable>
    <xsl:variable name="title">
      <xsl:call-template name="escape-apos">
        <xsl:with-param name="string">
          <xsl:call-template name="title"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <xsl:attribute name="onclick">
      <xsl:text>dcsMultiTrack('DCS.dcssip','</xsl:text>
      <xsl:value-of select="$dcssip"/>
      <xsl:text>','DCS.dcsuri','</xsl:text>
      <xsl:value-of select="$dcsuri"/>
      <xsl:if test="$query != ''">
          <xsl:text>','DCS.dcsquery','</xsl:text>
          <xsl:value-of select="$query"/>
      </xsl:if>
      <xsl:text>','WT.ti','</xsl:text>
      <xsl:value-of select="$title"/>
      <xsl:if test="$keywords != ''">
        <xsl:text>','DCSext.keywords','</xsl:text>
        <xsl:value-of select="$keywords"/>
        <xsl:text>','DCSext.search_type','</xsl:text>
        <xsl:value-of select="$source"/>
      </xsl:if>
      <xsl:text>','DCSext.offsite_link','1');</xsl:text>
      <xsl:value-of select="../@onclick"/>
    </xsl:attribute>
  </xsl:template>
  
    <xsl:template match="@onclick[parent::*/@href]"/>
    
    <xsl:template match="@href[not(contains(.,'://')) and (contains(.,'.pdf') or contains(.,'.camv')
                                           or contains(.,'.smil') or contains(.,'.doc') or contains(.,'.ppt')
                                           or contains(.,'.xls') or contains(.,'.rm'))]">
        <xsl:copy-of select="."/>
        <xsl:variable name="title">
            <xsl:call-template name="escape-apos">
                <xsl:with-param name="string">
                    <xsl:call-template name="title"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:attribute name="onclick">
            <xsl:text>dcsMultiTrack('DCS.dcsuri','</xsl:text>
            <xsl:choose>
                <xsl:when test="not(starts-with(.,'/'))">
                    <xsl:text>/</xsl:text>
                    <xsl:call-template name="strip-doc">
                        <xsl:with-param name="uri" select="$request-uri"/>
                    </xsl:call-template>
                    <xsl:choose>
                        <xsl:when test="not(contains(.,'?'))">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring-before(.,'?')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(contains(.,'?'))">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring-before(.,'?')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>','WT.ti','</xsl:text>
            <xsl:value-of select="$title"/>
            <xsl:if test="$keywords != ''">
                <xsl:text>', 'DCSext.keywords','</xsl:text>
                <xsl:value-of select="$keywords"/>
                <xsl:text>', 'DCSext.search_type','</xsl:text>
                <xsl:value-of select="$source"/>
            </xsl:if>
            <xsl:text>');</xsl:text>
        </xsl:attribute>
    </xsl:template>
  
  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
  
  <!-- figures out what the title should be; priority: ../@title, ../text(), ../h:img/@alt, 'unknown' -->
  <xsl:template name="title">
    <xsl:choose>
      <xsl:when test="../@title">
      <xsl:value-of select="../@title"/>
    </xsl:when>
      <xsl:when test="normalize-space(..)!=''">
        <xsl:value-of select="normalize-space(..)"/>
      </xsl:when>
      <xsl:when test="../h:img/@alt">
        <xsl:value-of select="../h:img/@alt"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="'unknown'"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- recursively escapes apostrophes in a string -->
  <xsl:template name="escape-apos">
    <xsl:param name="string"/>
    <xsl:choose>
      <xsl:when test="not(contains($string,&quot;'&quot;))">
        <xsl:value-of select="$string"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="afterapos">
          <xsl:call-template name="escape-apos">
            <xsl:with-param name="string" select="substring-after($string,&quot;'&quot;)"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="substring-before($string,&quot;'&quot;)"/>
        <xsl:text>\'</xsl:text>
        <xsl:value-of select="$afterapos"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- removes the what comes after the last / in a string -->
    <xsl:template name="strip-doc">
        <xsl:param name="uri"/>
        <xsl:choose>
            <xsl:when test="not(contains($uri,'/'))">
                <xsl:value-of select="''"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="afterslash">
                    <xsl:call-template name="strip-doc">
                        <xsl:with-param name="uri" select="substring-after($uri,'/')"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:value-of select="concat(substring-before($uri,'/'),'/',$afterslash)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
  
</xsl:stylesheet>
