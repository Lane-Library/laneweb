<?xml version="1.0" encoding="UTF-8" ?>
<!--
  adds status markup to equipment page.  Status is provided by EquipmentStatusGenerator
  in the form of a div with pairs of spans, the first the bib id and the second number
  of available items.
-->
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:h="http://www.w3.org/1999/xhtml"
  xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="h">

  <xsl:template match="h:li/h:div/h:div[2]">
    <xsl:copy>
      <xsl:apply-templates select="@*|*"/>
      <div>
        <xsl:call-template name="status">
          <xsl:with-param name="bibid" select="ancestor::h:li/@data-bibid"/>
        </xsl:call-template>
      </div>
    </xsl:copy>
  </xsl:template>

  <!-- this matches the div with the status info in it -->
  <xsl:template match="h:body/h:div"/>

  <xsl:template name="status">
    <xsl:param name="bibid"/>
    <xsl:variable name="available" select="/h:html/h:body/h:div[h:span[1] = $bibid]/h:span[2]"/>
    <strong>Status: </strong>
    <xsl:choose>
      <xsl:when test="$available">Available <strong><xsl:value-of select="$available"/></strong></xsl:when>
      <xsl:otherwise>Checked out</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="child::node()">
    <xsl:copy>
      <xsl:apply-templates select="attribute::node() | child::node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="attribute::node()">
    <xsl:copy-of select="."/>
  </xsl:template>

</xsl:stylesheet>
