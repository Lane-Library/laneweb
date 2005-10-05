<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:slim="http://www.loc.gov/MARC21/slim"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="slim">

    <xsl:param name="db"/>
    <xsl:param name="id"/>
    <xsl:param name="type"/>
    
    <xsl:template match="/">
      <html><head><title>Voyager Catalog Record</title></head>
        <body>
        <form action="/voyager/marc.html" method="get">
        <p>
          <input type="text" name="id" value="{$id}"/>
        </p>
        <p>
          <input type="radio" name="db" value="lmldb">
            <xsl:if test="$db='lmldb' or $db=''">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>lmldb
          <input type="radio" name="db" value="cifdb">
            <xsl:if test="$db='cifdb'">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>cifdb
          <input type="radio" name="db" value="jbldb">
            <xsl:if test="$db='jbldb'">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>jbldb
       </p>
       <p>
          <input type="radio" name="type" value="bib">
            <xsl:if test="$type='bib' or $type=''">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>bib
          <input type="radio" name="type" value="auth">
             <xsl:if test="$type='auth'">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>auth
          <input type="radio" name="type" value="mfhd">
             <xsl:if test="$type='mfhd'">
              <xsl:attribute name="checked" selected="'checked'"/>
            </xsl:if>
          </input>mfhd
      </p>
      <p>
          <input type="submit"/>
        </p>
        </form>
        <h3><xsl:value-of select="concat($db,' ',$type,' ',$id)"/></h3>
        <xsl:apply-templates select="slim:collection/*"/>
      </body></html>
    </xsl:template>
    
    <xsl:template match="slim:record">
      <p style="margin: 0; margin-top: 1em">
        <xsl:apply-templates/>
      </p>
    </xsl:template>
    
    <xsl:template match="slim:leader">
      <p style="margin: 0;">000: 
        <xsl:call-template name="underscore">
          <xsl:with-param name="string">
            <xsl:value-of select="."/>
          </xsl:with-param>
        </xsl:call-template>
      </p>
    </xsl:template>
    
    <xsl:template match="slim:controlfield">
      <p style="margin: 0;"><xsl:value-of select="@tag"/>: 
        <xsl:call-template name="underscore">
          <xsl:with-param name="string">
            <xsl:value-of select="."/>
          </xsl:with-param>
        </xsl:call-template>
      </p>
    </xsl:template>
    
    <xsl:template match="slim:datafield">
      <p style="margin: 0;">
        <xsl:value-of select="@tag"/>
        <xsl:text>: </xsl:text>
        <xsl:call-template name="underscore">
          <xsl:with-param name="string" select="concat(@ind1,@ind2)"/>
        </xsl:call-template>
        <xsl:apply-templates/>
      </p>
    </xsl:template>
    
    <xsl:template match="slim:subfield">
      <xsl:text> |</xsl:text><b><xsl:value-of select="@code"/></b><xsl:text> </xsl:text><xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template name="underscore">
      <xsl:param name="string"/>
      <xsl:choose>
        <xsl:when test="contains($string, ' ')">
          <xsl:call-template name="underscore">
            <xsl:with-param name="string">
              <xsl:value-of select="concat(substring-before($string,' '),'_', substring-after($string,' '))"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$string"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

</xsl:stylesheet> 
