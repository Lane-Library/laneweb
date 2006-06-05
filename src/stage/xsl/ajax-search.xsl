<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:s="http://irt.stanford.edu/search/2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:h="http://www.w3.org/1999/xhtml"
  xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="s h">


  <xsl:param name="source"/>

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
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates/>
			<meta name="lw_searchParameters" content="id={/aggregate/s:search/@id};status={/aggregate/s:search/@status};query={/aggregate/s:search/s:query};source={$source}"/>
		</xsl:copy>
    </xsl:template>
      
    <xsl:template match="h:span[@id='spellResults']">
        <xsl:if test="/aggregate/s:search/s:spell">
	            <span>Did you mean: <a href="search.html?source={$source}&amp;keywords={/aggregate/s:search/s:spell/text()}"><strong><i><xsl:value-of select="/aggregate/s:search/s:spell/text()"/></i></strong></a></span>
        </xsl:if>
        <xsl:if test="not(/aggregate/s:search/s:spell)">
	        <xsl:copy>
	            <xsl:copy-of select="@*"/>
	            <xsl:apply-templates/>
	        </xsl:copy>
		</xsl:if>
    </xsl:template>

    <xsl:template match="h:li">
        <xsl:variable name="id" select="@id"/>
        <xsl:variable name="resource" select="/aggregate/s:search/s:engine/s:resource[@id=$id]"/>
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="$resource/@status"><xsl:value-of select="$resource/@status"/></xsl:when>
                <xsl:otherwise>running</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
            
        <xsl:copy>
            <!--<a class="{$status}" target="_blank" href="{$resource/s:url}"><xsl:value-of select="."/></a><xsl:text>: </xsl:text>-->
	      <a class="proxy" type="{$status}" href="{$resource/s:url}"><xsl:value-of select="."/></a><xsl:text>: </xsl:text>
            <xsl:choose>
                <xsl:when test="$status='successful'">
                    <span><xsl:value-of select="format-number($resource/s:hits, '###,###')"/></span>
                </xsl:when>
                <xsl:when test="$status = 'canceled'">
                    <span>timed out</span>
                </xsl:when>
                <xsl:when test="$status != 'running'">
                    <span><xsl:value-of select="$status"/></span>
                </xsl:when>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
     
    <xsl:template match="@href[contains(.,'{$id}')]">
        <xsl:attribute name="href"><xsl:value-of select="concat(substring-before(.,'{$id}'),$search-id,substring-after(.,'{$id}'),'&amp;keywords=',$keywords)"/></xsl:attribute>
    </xsl:template>
     
     <xsl:template match="@*">
       <xsl:copy-of select="."/>
     </xsl:template>


</xsl:stylesheet>
