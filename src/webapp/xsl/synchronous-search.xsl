<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:s="http://irt.stanford.edu/search/2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:h="http://www.w3.org/1999/xhtml"
  xmlns="http://www.w3.org/1999/xhtml"
    xmlns:exslt="http://exslt.org/common"
  exclude-result-prefixes="s h exslt">


    <xsl:param name="template"/>
    <xsl:param name="search-url"/>
    <xsl:param name="view"/>
    <xsl:param name="debug"/>
    
    <xsl:variable name="nonzero" select="count(//s:hits[text() != '0' and parent::s:resource/@id = //h:li/@id])"/>
    <xsl:variable name="total" select="count(//h:li[@id])"/>
    <xsl:variable name="stillRunningCount" select="count(//s:resource[not(@status) and @id = //h:li/@id])"/>
    <xsl:variable name="showzero" select="$view='all'"/>
    <xsl:variable name="search-id" select="/aggregate/s:search/@id"/>
    <xsl:variable name="keywords" select="/aggregate/s:search/s:query/text()"/>
    
    <xsl:template match="/aggregate">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
     
    <xsl:template match="*">
         <xsl:copy>
             <xsl:apply-templates select="@*|node()"/>
         </xsl:copy>
    </xsl:template>
  
    <xsl:template match="h:body">
        <xsl:copy>
            <xsl:apply-templates/>
            <xsl:if test="$debug='y'">
            <div style="position:absolute;right:0;top:0">
                <div><a href="/content/new-search.html?id={$search-id}&amp;source={$template}">content</a></div>
                <div><a href="{$search-url}search?id={$search-id}">search</a></div>
                <div>running:<xsl:value-of select="$stillRunningCount"/></div>
            </div>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:div[@id='searchResults']">
        <xsl:if test="/aggregate/s:search/s:spell">
            <div><p>Did you mean <a href="new-search.html?source={$template}&amp;keywords={/aggregate/s:search/s:spell/text()}"><strong><xsl:value-of select="/aggregate/s:search/s:spell/text()"/></strong></a>?</p></div>
        </xsl:if>
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
  
  <xsl:template match="h:div[@class='searchCategory']">
      <xsl:choose>
          <xsl:when test="$view='all'">
              <xsl:copy>
                  <xsl:apply-templates select="node()|@*"/>
              </xsl:copy>
          </xsl:when>
          <xsl:otherwise>
              <xsl:variable name="category">
                  <xsl:copy>
                      <xsl:apply-templates select="node()|@*"/>
                  </xsl:copy>
              </xsl:variable>
              <xsl:if test="count(exslt:node-set($category)//h:li) &gt; 0">
                  <xsl:copy-of select="$category"/>
              </xsl:if>
          </xsl:otherwise>
      </xsl:choose>
  </xsl:template>
     
    <xsl:template match="h:li[@id]">
        <xsl:variable name="id" select="@id"/>
        <xsl:variable name="resource" select="/aggregate/s:search/s:engine/s:resource[@id=$id]"/>
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="$resource/@status"><xsl:value-of select="$resource/@status"/></xsl:when>
                <xsl:otherwise>running</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
            
        <xsl:if test="$showzero or ($status='successful' and $resource/s:hits != '0') or ($status != 'running' and $status != 'successful')">
            <xsl:copy>
                <xsl:choose>
                    <xsl:when test="$resource/s:url">
                        <a href="{$resource/s:url}">
                            <xsl:if test="not(starts-with($resource/s:url,'http://lane.stanford'))">
                                <xsl:attribute name="class">proxy</xsl:attribute>
                            </xsl:if>
                            <xsl:value-of select="."/>
                        </a>
                        <xsl:text>: </xsl:text>
                        <xsl:choose>
                            <xsl:when test="$status='successful'">
                                <span>
                                    <xsl:call-template name="comma">
                                        <xsl:with-param name="number" select="$resource/s:hits"/>
                                    </xsl:call-template>
                                </span>
                            </xsl:when>
                            <xsl:when test="$status != 'running'">
                                <span>unknown</span>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                        <xsl:text>: </xsl:text>
                        <xsl:if test="$status != 'running'">
                            <span>unknown</span>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:a[@id='refresh']">
        <xsl:copy>
            <xsl:attribute name="href">
                <xsl:value-of select="concat('search.html?source=',$template,'&amp;keywords=',/aggregate/s:search/s:query/text(),'&amp;id=',$search-id)"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:span[@id='searchRefresh']">
        <xsl:if test="$stillRunningCount &gt; 0">
            <xsl:apply-templates/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:span[@class='stillRunningCount']">
        <xsl:value-of select="$stillRunningCount"/>
    </xsl:template>
    
    <xsl:template match="h:span[@class='nonzero']">
        <xsl:value-of select="$nonzero"/>
    </xsl:template>
    
    <xsl:template match="h:span[@class='total']">
        <xsl:value-of select="$total"/>
    </xsl:template>
    
    <xsl:template match="h:span[@class='keywords']">
        <xsl:value-of select="$keywords"/>
    </xsl:template>
    
    <xsl:template match="@href[contains(.,'{$id}')]">
        <xsl:attribute name="href"><xsl:value-of select="concat(substring-before(.,'{$id}'),$search-id,substring-after(.,'{$id}'),'&amp;keywords=',$keywords)"/></xsl:attribute>
    </xsl:template>
     
     <xsl:template match="@*">
       <xsl:copy-of select="."/>
     </xsl:template>
     
     <xsl:template match="h:a[@id='total']">
         <xsl:choose>
             <xsl:when test="not($showzero)">
                 <xsl:copy>
                     <xsl:apply-templates select="@href"/>
                     <xsl:value-of select="$total"/>
                 </xsl:copy>
             </xsl:when>
             <xsl:otherwise>
                 <xsl:value-of select="$total"/>
             </xsl:otherwise>
         </xsl:choose>
     </xsl:template>
     
     <xsl:template match="h:a[@id='nonzero']">
         <xsl:choose>
             <xsl:when test="$showzero">
                 <xsl:copy>
                     <xsl:apply-templates select="@href"/>
                     <xsl:value-of select="$nonzero"/>
                 </xsl:copy>
             </xsl:when>
             <xsl:otherwise>
                 <xsl:value-of select="$nonzero"/>
             </xsl:otherwise>
         </xsl:choose>
     </xsl:template>
             
    
    <xsl:template name="comma">
        <xsl:param name="number"/>
        <xsl:choose>
            <xsl:when test="string-length($number) &lt; 4">
                <xsl:value-of select="$number"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="before-comma" select="substring($number,1,string-length($number)-3)"/>
                <xsl:variable name="after-comma" select="substring-after($number,$before-comma)"/>
                <xsl:value-of select="concat($before-comma,',',$after-comma)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
