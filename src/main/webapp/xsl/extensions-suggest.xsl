<?xml version="1.0"?>
<xsl:stylesheet
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                exclude-result-prefixes="sql"
                version="2.0">

  <xsl:param name="q"/>

  <xsl:template match="/">
        <xsl:text>["</xsl:text><xsl:value-of select="$q"/><xsl:text>", [</xsl:text><xsl:apply-templates/><xsl:text>]]</xsl:text>
  </xsl:template>

  <xsl:template match="sql:rowset"><xsl:apply-templates/></xsl:template>

  <xsl:template match="sql:row">
        <xsl:variable name="c" select="count(/sql:rowset/sql:row)"/>
                <xsl:text>"</xsl:text><xsl:value-of select="sql:title"/><xsl:text>"</xsl:text>
                <xsl:if test="position() != last() and $c &gt; 1"><xsl:text>,</xsl:text></xsl:if>
        <xsl:for-each select="sql:title">
        </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
