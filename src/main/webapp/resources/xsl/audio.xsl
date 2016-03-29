<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:slim="http://www.loc.gov/MARC21/slim" xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="slim">

  <xsl:template match="/slim:collection">
    <html>
      <head>
        <title>Audio</title>
      </head>
      <body>
        <ul>
          <xsl:apply-templates select="slim:record">
            <xsl:sort select="concat(slim:datafield[@tag = '245']/slim:subfield[@code = 'a'],slim:datafield[@tag = '245']/slim:subfield[@code = 'p'])"/>
          </xsl:apply-templates>
        </ul>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="slim:record">
    <xsl:variable name="bibid" select="slim:controlfield[@tag = '001']"/>
    <xsl:variable name="title">
      <xsl:value-of select="slim:datafield[@tag = '245']/slim:subfield[@code = 'a']"/>
      <xsl:if test="slim:datafield[@tag = '245']/slim:subfield[@code = 'p']">
        <xsl:text> </xsl:text>
        <xsl:value-of select="slim:datafield[@tag = '245']/slim:subfield[@code = 'p']"/>
      </xsl:if>
    </xsl:variable>
    <li>
      <div><a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={$bibid}"><xsl:value-of select="$title"/></a></div>
    </li>
  </xsl:template>

</xsl:stylesheet>
