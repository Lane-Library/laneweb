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
    <li class="resource" data-bibid="{$bibid}">
      <div class="pure-g">
        <div class="pure-u-1-6">
          <xsl:call-template name="fa">
            <xsl:with-param name="title" select="$title"/>
          </xsl:call-template>
        </div>
        <div class="pure-u-5-6">
          <div><xsl:value-of select="$title"/></div>
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
        <xsl:text>fa-4x </xsl:text>
        <xsl:choose>
          <xsl:when test="contains($title, 'iPad')">fa-solid fa-tablet-screen-button</xsl:when>
          <xsl:when test="contains($title, 'Apple')">fa-brands fa-apple</xsl:when>
          <xsl:when test="contains($title, 'Android')">fa-brands fa-android</xsl:when>
          <xsl:when test="contains($title, 'Keyboard')">fa-solid fa-keyboard</xsl:when>
          <xsl:when test="contains($title, 'Headphones')">fa-solid fa-headphones</xsl:when>
          <xsl:when test="contains($title, 'Tablet')">fa-solid fa-tablet-screen-button</xsl:when>
          <xsl:when test="contains($title, 'USB')">fa-brands fa-usb</xsl:when>
          <xsl:when test="contains($title, 'Cable')">fa-brands fa-gg</xsl:when>
          <xsl:when test="contains($title, 'Recorder')">fa-solid fa-microphone</xsl:when>
          <xsl:when test="contains($title, 'Polling')">fa-solid fa-users</xsl:when>
          <xsl:when test="contains($title, 'magnifying')">fa-solid fa-magnifying-glass</xsl:when>
        </xsl:choose>
      </xsl:attribute>
    </i></div>
  </xsl:template>

</xsl:stylesheet>
