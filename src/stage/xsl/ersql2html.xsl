<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="sql" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:sql="http://apache.org/cocoon/SQL/2.0">
    
    <xsl:param name="keywords"/>
    
    <xsl:variable name="ltitle" select="translate($keywords,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>

  <xsl:template match="sql:rowset">
      <dl class="searchResults">
      <xsl:apply-templates select="sql:row[sql:ltitle = $ltitle and not(preceding-sibling::sql:row[1]/sql:eresource_id = sql:eresource_id and preceding-sibling::sql:row[1]/sql:title = sql:title)]"/>
      <xsl:apply-templates select="sql:row[not(preceding-sibling::sql:row[1]/sql:eresource_id = sql:eresource_id and preceding-sibling::sql:row[1]/sql:title = sql:title) and sql:ltitle != $ltitle]"/>
    </dl>
  </xsl:template>
  
  <xsl:template match="sql:row">
      <xsl:variable name="eresource_id" select="sql:eresource_id/text()"/>
      <xsl:variable name="title" select="sql:title/text()"/>
    <xsl:variable name="links" select=".|following-sibling::sql:row[position() &lt; 10 and sql:title/text()=$title and sql:eresource_id/text() = $eresource_id]"/>
    <xsl:variable name="link_count" select="count($links)"/>
      <dt><xsl:value-of select="sql:title/text()"/></dt>
    <dd><ul>
      <xsl:choose>
          <xsl:when test="$link_count &gt; 1">
              <xsl:variable name="version_id" select="sql:version_id/text()"/>
              <xsl:choose>
                  <xsl:when test="$links[not(sql:version_id = $version_id)]">
                      <xsl:call-template name="multiple-holdings">
                          <xsl:with-param name="links" select="$links"/>
                      </xsl:call-template>
                  </xsl:when>
                  <xsl:when test="$link_count = 2 and $links/sql:label = 'Get Password'">
                      <xsl:call-template name="one-and-password">
                          <xsl:with-param name="links" select="$links"/>
                      </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                      <xsl:call-template name="multiple-links">
                          <xsl:with-param name="links" select="$links"/>
                      </xsl:call-template>
                  </xsl:otherwise>
              </xsl:choose>
          </xsl:when>
        <xsl:otherwise>
            <xsl:variable name="proxy_class">
                <xsl:choose>
                    <xsl:when test="sql:proxy = 'T'">proxy</xsl:when>
                    <xsl:otherwise>noproxy</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="holdings-length" select="string-length(sql:holdings)"/>
            <xsl:variable name="dates-length" select="string-length(sql:dates)"/>
            <li><a href="{sql:url}" title="{sql:title}" class="{$proxy_class}">
                <xsl:choose>
                    <xsl:when test="$holdings-length &gt; 0 and $dates-length &gt; 0">
                        <xsl:apply-templates select="sql:holdings"/>, <xsl:value-of select="sql:dates"/>
                    </xsl:when>
                    <xsl:when test="$holdings-length &gt; 0">
                        <xsl:apply-templates select="sql:holdings"/>
                    </xsl:when>
                    <xsl:when test="$dates-length &gt; 0">
                        <xsl:value-of select="sql:dates"/>
                    </xsl:when>
                    <xsl:when test="string-length(sql:label) &gt; 0">
                        <xsl:value-of select="sql:label"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="sql:url"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:apply-templates select="sql:description|sql:instruction"/>
            </a>
            <xsl:apply-templates select="sql:publisher"/></li>
        </xsl:otherwise>
      </xsl:choose>
    </ul></dd>
  </xsl:template>
  
  <xsl:template match="sql:publisher">
    <xsl:if test="string-length(text()) &gt; 0">
        <xsl:text> </xsl:text><span class="provider"><xsl:value-of select="text()"/></span>
    </xsl:if>
  </xsl:template>
  
    <xsl:template match="sql:holdings">
        <xsl:choose>
            <xsl:when test="contains(.,' =')">
                <xsl:value-of select="substring-before(.,' =')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="sql:description|sql:instruction">
        <xsl:text> </xsl:text><xsl:value-of select="."/>
    </xsl:template>
  
  <xsl:template match="node()"/>
  
  <xsl:template name="multiple-holdings">
    <xsl:param name="links"/>
      <xsl:for-each select="$links">
          <xsl:variable name="version_id" select="sql:version_id/text()"/>
          <xsl:choose>
              <xsl:when test="preceding-sibling::sql:row[1]/sql:version_id = $version_id"/>
              <xsl:when test="not(following-sibling::sql:row) or following-sibling::sql:row[1]/sql:version_id != $version_id">
                  <xsl:variable name="proxy_class">
                      <xsl:choose>
                          <xsl:when test="sql:proxy = 'T'">proxy</xsl:when>
                          <xsl:otherwise>noproxy</xsl:otherwise>
                      </xsl:choose>
                  </xsl:variable>
                  <xsl:variable name="holdings-length" select="string-length(sql:holdings)"/>
                  <xsl:variable name="dates-length" select="string-length(sql:dates)"/>
                  <li>
                      <a href="{sql:url}" title="{sql:title}" class="{$proxy_class}">
                          <xsl:choose>
                              <xsl:when test="$holdings-length &gt; 0 and $dates-length &gt; 0">
                                  <xsl:apply-templates select="sql:holdings"/>, <xsl:value-of select="sql:dates"/>
                              </xsl:when>
                              <xsl:when test="$holdings-length &gt; 0">
                                  <xsl:apply-templates select="sql:holdings"/>
                              </xsl:when>
                              <xsl:when test="$dates-length &gt; 0">
                                  <xsl:value-of select="sql:dates"/>
                              </xsl:when>
                              <xsl:when test="string-length(sql:label) &gt; 0">
                                  <xsl:value-of select="sql:label"/>
                              </xsl:when>
                              <xsl:otherwise>
                                  <xsl:value-of select="sql:url"/>
                              </xsl:otherwise>
                          </xsl:choose>
                          <xsl:apply-templates select="sql:description|sql:instruction"/>
                      </a>
                      <xsl:apply-templates select="sql:publisher"/>
                  </li>
              </xsl:when>
              <xsl:otherwise>
                      <xsl:for-each select="self::sql:row[sql:label != 'Get Password'] | following-sibling::sql:row[position() &lt; 10 and sql:version_id = $version_id and sql:label != 'Get Password']">
                          <xsl:variable name="proxy_class">
                              <xsl:choose>
                                  <xsl:when test="sql:proxy = 'T'">proxy</xsl:when>
                                  <xsl:otherwise>noproxy</xsl:otherwise>
                              </xsl:choose>
                          </xsl:variable>
                          <xsl:variable name="holdings-length" select="string-length(sql:holdings)"/>
                          <xsl:variable name="dates-length" select="string-length(sql:dates)"/>
                          <li><a href="{sql:url}" title="{sql:title}" class="{$proxy_class}">
                              <xsl:choose>
                                  <xsl:when test="preceding-sibling::sql:row[1]/sql:label = 'Get Password'">
                                      <xsl:choose>
                                          <xsl:when test="$holdings-length &gt; 0 and $dates-length &gt; 0">
                                              <xsl:apply-templates select="sql:holdings"/>, <xsl:value-of select="sql:dates"/>
                                          </xsl:when>
                                          <xsl:when test="$holdings-length &gt; 0">
                                              <xsl:apply-templates select="sql:holdings"/>
                                          </xsl:when>
                                          <xsl:when test="$dates-length &gt; 0">
                                              <xsl:value-of select="sql:dates"/>
                                          </xsl:when>
                                          <xsl:when test="string-length(sql:label) &gt; 0">
                                              <xsl:value-of select="sql:label"/>
                                          </xsl:when>
                                          <xsl:otherwise>
                                              <xsl:value-of select="sql:url"/>
                                          </xsl:otherwise>
                                      </xsl:choose>
                                  </xsl:when>
                                  <xsl:otherwise>
                                      <xsl:value-of select="sql:label"/>
                                  </xsl:otherwise>
                              </xsl:choose>
                              <xsl:apply-templates select="sql:description|sql:instruction"/></a>
                              <xsl:apply-templates select="sql:publisher"/>
                           <!--   <xsl:choose>-->
                                  <xsl:if test="preceding-sibling::sql:row[1]/sql:label = 'Get Password'">
                                      <a href="{preceding-sibling::sql:row[1]/sql:url}"> get password</a>
                                  </xsl:if>
                                  <!--<xsl:when test="following-sibling::sql:row[1]/sql:label = 'Get Password'">
                                      <a href="{following-sibling::sql:row[1]/sql:url}"> get password</a>
                                  </xsl:when>
                              </xsl:choose>--></li>
                      </xsl:for-each>
              </xsl:otherwise>
          </xsl:choose>
      </xsl:for-each>
  </xsl:template>
    
    <xsl:template name="multiple-links">
        <xsl:param name="links"/>
        <xsl:variable name="proxy_class">
            <xsl:choose>
                <xsl:when test="sql:proxy = 'T'">proxy</xsl:when>
                <xsl:otherwise>noproxy</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="$links">
            <li><a href="{sql:url}" title="{concat(sql:title,':',sql:label)}" class="{$proxy_class}">
                <xsl:choose>
                    <xsl:when test="string-length(sql:label) &gt; 0">
                        <xsl:value-of select="sql:label"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="sql:url"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:apply-templates select="sql:description|sql:instruction"/>
            </a><xsl:apply-templates select="sql:publisher"/>
                <xsl:if test="preceding-sibling::sql:row[1]/sql:label = 'Get Password'">
                    <a href="{preceding-sibling::sql:row[1]/sql:url}"> get password</a>
                </xsl:if>
<!--                <xsl:if test="following-sibling::sql:row[1]/sql:label = 'Get Password'">
                    <a href="{following-sibling::sql:row[1]/sql:url}"> get password</a>
                </xsl:if>-->
            </li>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="one-and-password">
        <xsl:param name="links"/>
        <xsl:for-each select="$links[not(sql:label = 'Get Password')]">
        <xsl:variable name="proxy_class">
            <xsl:choose>
                <xsl:when test="sql:proxy = 'T'">proxy</xsl:when>
                <xsl:otherwise>noproxy</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="holdings-length" select="string-length(sql:holdings)"/>
        <xsl:variable name="dates-length" select="string-length(sql:dates)"/>
            <li><a href="{sql:url}" title="{concat(sql:title,':',sql:label)}" class="{$proxy_class}">
                <xsl:choose>
                    <xsl:when test="$holdings-length &gt; 0 and $dates-length &gt; 0">
                        <xsl:apply-templates select="sql:holdings"/>, <xsl:value-of select="sql:dates"/>
                    </xsl:when>
                    <xsl:when test="$holdings-length &gt; 0">
                        <xsl:apply-templates select="sql:holdings"/>
                    </xsl:when>
                    <xsl:when test="$dates-length &gt; 0">
                        <xsl:value-of select="sql:dates"/>
                    </xsl:when>
                    <xsl:when test="string-length(sql:label) &gt; 0">
                        <xsl:value-of select="sql:label"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="sql:url"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:apply-templates select="sql:description|sql:instruction"/>
            </a><xsl:apply-templates select="sql:publisher"/>
                    <a href="{$links/sql:row[sql:label='Get Password']/sql:url}"> get password</a>
            </li>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="debug-links">
        <a href="/voyager/marc.html?id={sql:eresource_id}&amp;type=bib&amp;db=lmldb">bib</a>|
        <a href="/voyager/marc.html?id={sql:version_id}&amp;type=mfhd&amp;db=lmldb">mfhd</a>
    </xsl:template>
  
</xsl:stylesheet>
