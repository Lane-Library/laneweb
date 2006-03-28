<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" version="1.0">
  
  <xsl:param name="name"/>
  <xsl:param name="email"/>
  <xsl:param name="textarea"/>
  <xsl:param name="phone"/>
  <xsl:param name="affiliation"/>
  <xsl:param name="remote-host"/>
  <xsl:param name="user-agent"/>
  <xsl:param name="date"/>
  
  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
  
  <xsl:template match="h:span[@class='lw_email']">
    <xsl:choose>
      <xsl:when test=".='${name}'"><xsl:value-of select="$name"/></xsl:when>
      <xsl:when test=".='${email}'"><xsl:value-of select="$email"/></xsl:when>
      <xsl:when test=".='${textarea}'"><xsl:value-of select="$textarea"/></xsl:when>
      <xsl:when test=".='${phone}'"><xsl:value-of select="$phone"/></xsl:when>
      <xsl:when test=".='${affiliation}'"><xsl:value-of select="$affiliation"/></xsl:when>
      <xsl:when test=".='${remote-host}'"><xsl:value-of select="$remote-host"/></xsl:when>
      <xsl:when test=".='${user-agent}'"><xsl:value-of select="$user-agent"/></xsl:when>
      <xsl:when test=".='${date}'"><xsl:value-of select="$date"/></xsl:when>
      <xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
