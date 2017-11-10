<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:slim="http://www.loc.gov/MARC21/slim" xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="slim">

  <xsl:template match="/slim:collection">
    <html>
      <head>
        <title>Equipment</title>
      </head>
      <body>
        <ul>
          <xsl:apply-templates select="slim:record">
            <xsl:sort select="slim:datafield[@tag = '245']/slim:subfield[@code = 'a']"/>
          </xsl:apply-templates>
        </ul>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="slim:record">
    <xsl:variable name="bibid" select="slim:controlfield[@tag = '001']"/>
    <xsl:variable name="title" select="slim:datafield[@tag = '245']/slim:subfield[@code = 'a']"/>
    <li data-bibid="{$bibid}">
      <div class="yui3-g">
        <div class="yui3-u-1-6">
          <xsl:call-template name="fa">
            <xsl:with-param name="title" select="$title"/>
          </xsl:call-template>
        </div>
        <div class="yui3-u-5-6">
          <div><a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={$bibid}"><xsl:value-of select="$title"/></a></div>
          <xsl:apply-templates select="slim:datafield[@tag = '500']/slim:subfield[@code = 'a']"/>
        </div>
      </div>
    </li>
  </xsl:template>

  <xsl:template match="slim:datafield[@tag = '500']/slim:subfield[@code = 'a']">
    <div>
      <xsl:value-of select="."/>
    </div>
  </xsl:template>

  <xsl:template name="fa">
    <xsl:param name="title"/>
    <div class="equipment-icon"><i aria-hidden="true">
      <xsl:attribute name="class">
        <xsl:text>fa fa-4x fa-</xsl:text>
        <xsl:choose>
          <xsl:when test="contains($title, 'iPad')">tablet</xsl:when>
          <xsl:when test="contains($title, 'Apple')">apple</xsl:when>
          <xsl:when test="contains($title, 'Android')">android</xsl:when>
          <xsl:when test="contains($title, 'Keyboard')">keyboard-o</xsl:when>
          <xsl:when test="contains($title, 'Headphones')">headphones</xsl:when>
          <xsl:when test="contains($title, 'Tablet')">tablet</xsl:when>
          <xsl:when test="contains($title, 'USB')">usb</xsl:when>
          <xsl:when test="contains($title, 'Cable')">gg</xsl:when>
          <xsl:when test="contains($title, 'Recorder')">microphone</xsl:when>
          <xsl:when test="contains($title, 'Polling')">users</xsl:when>
          <xsl:when test="contains($title, 'magnifying')">search</xsl:when>
        </xsl:choose>
      </xsl:attribute>
    </i></div>
  </xsl:template>

</xsl:stylesheet>
