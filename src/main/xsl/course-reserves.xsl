<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:slim="http://www.loc.gov/MARC21/slim" xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="slim">

  <xsl:template match="/slim:collection">
    <html>
      <head>
        <title>Course Reserves</title>
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
      <div><a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={$bibid}"><xsl:value-of select="$title"/></a></div>
    </li>
  </xsl:template>

</xsl:stylesheet>
