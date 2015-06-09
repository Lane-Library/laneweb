<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h"
    version="2.0">
    
    <xsl:param name="base-path"/>
    <xsl:param name="facet"/>
    <xsl:param name="page"/>
    <xsl:param name="query"/>
    <xsl:param name="query-string"/>
    <xsl:param name="source"/>
    <xsl:param name="url-encoded-query"/>
    
    <xsl:variable name="pageless-query-string" select="replace($query-string,'&amp;page=\d+','')"/>
    <xsl:variable name="facet-search-base-path" select="concat('?',replace($pageless-query-string,'&amp;facets?=[^&amp;]+',''))"/>
    <xsl:variable name="facet-browse-base-path" select="concat($base-path,'/search/solr-facet-browse.html?',$pageless-query-string)"/>
    <xsl:variable name="facets-per-browse-page" select="20"/>
    <xsl:variable name="values-per-facet" select="10"/>
    
    <xsl:template match="facet">
        <xsl:variable name="count-formatted" select="format-number(count,'###,##0')"/>
        <xsl:variable name="label">
            <xsl:choose>
                <xsl:when test="../../string[. = 'year'] and name = 'year:[2010 TO *]'">Last 5 Years</xsl:when>
                <xsl:when test="../../string[. = 'year'] and name = 'year:[2005 TO *]'">Last 10 Years</xsl:when>
                <xsl:when test="../../string[. = 'year'] and name = '0'">Unknown</xsl:when>
                <xsl:otherwise><xsl:value-of select="name"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="furl">
			<xsl:if test="string-length(url) > 0"><xsl:value-of select="concat('&amp;facets=',url)"/></xsl:if>
        </xsl:variable>
		<xsl:choose>
		    <xsl:when test="enabled = 'true'">
		         <li><xsl:value-of select="$label"/> <span class="facetCount"><a title="remove filter" href="{$facet-search-base-path}{$furl}"> <i class="fa fa-times-circle"></i> </a> <xsl:value-of select="$count-formatted"/></span></li>
		    </xsl:when>
		    <xsl:otherwise>
		         <li><a href="{$facet-search-base-path}{$furl}"><xsl:value-of select="$label"/></a> <span class="facetCount"><xsl:value-of select="$count-formatted"/></span></li>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>

    <xsl:template name="field">
        <xsl:param name="id"/>
        <xsl:param name="label"/>
        <xsl:if test="/linked-hash-map/entry/string[. = $id]">
	        <xsl:choose>
	            <xsl:when test="$facet = ''"> 
		            <xsl:variable name="open">
		                <xsl:if test="/linked-hash-map/entry/string[. = $id]/../sorted-set/facet/enabled[. = 'true'] or ($id = 'year' and /linked-hash-map/entry/string[. = 'isRecent']/../sorted-set/facet/enabled[. = 'true'])"> openOnInit</xsl:if>
		            </xsl:variable>
		            <li class="solrFacet facetHeader{$open}"><xsl:value-of select="$label"/></li>
		            <xsl:if test="$id = 'year'">
		                <xsl:apply-templates select="/linked-hash-map/entry/string[. = 'isRecent']/../sorted-set/facet[name[. = 'true']]"/>
		            </xsl:if>
		            <xsl:apply-templates select="/linked-hash-map/entry/string[. = $id]/../sorted-set/facet[position() &lt;= $values-per-facet or $facet !='']"/>
		            <xsl:if test="count(/linked-hash-map/entry/string[. = $id]/../sorted-set/facet) > $values-per-facet and $facet = ''">
		                 <li> <a rel="lightbox" href="{$facet-browse-base-path}&amp;facet={$id}&amp;page=1"> more </a> <i class="icon fa fa-arrow-right"></i></li>
		            </xsl:if>
	            </xsl:when>
	            <xsl:otherwise>
                    <li class="solrFacet facetHeader"><h5><xsl:value-of select="$label"/></h5></li>
                    <xsl:apply-templates select="/linked-hash-map/entry/string[. = $id]/../sorted-set/facet[position() &lt;= $facets-per-browse-page]"/>
                    <li>
                        <div class="s-pagination facetBrowse no-bookmarking">
					        <xsl:choose>
					            <xsl:when test="number($page) &gt; 1">
									<a class="pagingButton previous" rel="lightbox-noanim" href="{$facet-browse-base-path}&amp;page={number($page) - 1}" title="previous"> <i class="fa fa-backward"></i> </a>
					            </xsl:when>
					            <xsl:otherwise>
	                                <span class="pagingButton disabled"> <i class="fa fa-backward"></i> </span>
					            </xsl:otherwise>
					        </xsl:choose>
	                        <xsl:choose>
	                            <xsl:when test="count(/linked-hash-map/entry/string[. = $id]/../sorted-set/facet) > $facets-per-browse-page">
	                                <a class="pagingButton next" rel="lightbox-noanim" href="{$facet-browse-base-path}&amp;page={number($page) + 1}" title="next"> <i class="fa fa-forward"></i> </a>
	                            </xsl:when>
	                            <xsl:otherwise>
	                                <span class="pagingButton disabled"> <i class="fa fa-forward"></i> </span>
	                            </xsl:otherwise>
	                        </xsl:choose>
                        </div>
                    </li>
	            </xsl:otherwise>
	        </xsl:choose>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="/">
        <html>
            <body>
                <xsl:if test="/linked-hash-map/entry">
	                <div class="bd">
		                <h3>Limits</h3>
		                <ul>
							<xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'type'"/>					   
		                        <xsl:with-param name="label" select="'Resource Type'"/>					   
							</xsl:call-template>
		
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationType'"/>                    
		                        <xsl:with-param name="label" select="'Article Type'"/>                    
		                    </xsl:call-template>
		
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationTitle'"/>                    
		                        <xsl:with-param name="label" select="'Article Source'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationAuthor'"/>                    
		                        <xsl:with-param name="label" select="'Article Author'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'mesh'"/>                    
		                        <xsl:with-param name="label" select="'By Subject (MeSH)'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'year'"/>                    
		                        <xsl:with-param name="label" select="'Publication Year'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationLanguage'"/>                    
		                        <xsl:with-param name="label" select="'Language'"/>                    
		                    </xsl:call-template>
		                </ul>
	                </div>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
    
</xsl:stylesheet>