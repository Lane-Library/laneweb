<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:s="http://apache.org/cocoon/SQL/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="base-path"/>
    <xsl:param name="facet"/>
    <xsl:param name="facets"/>
    <xsl:param name="page"/>
    <xsl:param name="query"/>
    <xsl:param name="query-string"/>
    <xsl:param name="facet.sort"/>
    <xsl:param name="source"/>
    <xsl:param name="url-encoded-query"/>
    
    <xsl:variable name="search-mode" select="$facet = ''"/>
    <xsl:variable name="pageless-query-string" select="replace($query-string,'&amp;page=\d+','')"/>
    <xsl:variable name="facet-search-base-path" select="concat('?',replace($pageless-query-string,'&amp;(facets?=[^&amp;]+|facet.sort=index)',''))"/>
    <xsl:variable name="facet-browse-base-path" select="concat($base-path,'/search/solr-facet-browse.html?',$pageless-query-string)"/>
    <xsl:variable name="facets-per-browse-page" select="20"/>
    <xsl:variable name="values-per-facet" select="4"/>
    
    <xsl:template match="facet">
        <xsl:variable name="count-formatted" select="format-number(count,'###,##0')"/>
        <xsl:variable name="label">
            <xsl:choose>
                <xsl:when test="../../string[. = 'recordType'] and value = 'pubmed'">PubMed</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'bib'">Lane Catalog</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'web'">Lane Web Site</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'class'">Lane Class</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'auth'">Lane Community Info</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'laneblog'">Lane Blog</xsl:when>
                <xsl:when test="../../string[. = 'year'] and value = '[2010 TO *]'">Last 5 Years</xsl:when>
                <xsl:when test="../../string[. = 'year'] and value = '[2005 TO *]'">Last 10 Years</xsl:when>
                <xsl:when test="../../string[. = 'year'] and value = '0'">Unknown</xsl:when>
                <xsl:otherwise><xsl:value-of select="value"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="furl">
			<xsl:if test="string-length(url) > 0"><xsl:value-of select="concat('&amp;facets=',url)"/></xsl:if>
        </xsl:variable>
		<xsl:choose>
		    <xsl:when test="enabled = 'true'">
		         <li class="enabled"><a title="remove" href="{$facet-search-base-path}{$furl}"> <i class="fa fa-check-square fa-lg"></i></a> <span class="facetLabel"><xsl:value-of select="$label"/></span> <span class="facetCount"><xsl:value-of select="$count-formatted"/></span></li>
		    </xsl:when>
		    <xsl:otherwise>
		         <li><a href="{$facet-search-base-path}{$furl}"><i class="fa fa-square-o fa-lg"></i></a> <a href="{$facet-search-base-path}{$furl}"><span class="facetLabel"><xsl:value-of select="$label"/></span></a> <span class="facetCount"><xsl:value-of select="$count-formatted"/></span></li>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>

    <xsl:template name="field">
        <xsl:param name="id"/>
        <xsl:param name="label"/>
        <xsl:if test="/linked-hash-map/entry/string[. = $id]">
	        <xsl:choose>
	            <xsl:when test="$search-mode">
		            <li class="solrFacet facetHeader">
						<xsl:copy-of select="$label"/>
						<xsl:if test="count(/linked-hash-map/entry/string[. = $id]/../sorted-set/facet[fieldName = 'publicationType' or count > 0]) > $values-per-facet">
						          <span class="seeAll"><a rel="lightbox" href="{$facet-browse-base-path}&amp;facet={$id}&amp;page=1"> see all </a></span>
						</xsl:if>
		            </li>
		            <xsl:apply-templates select="/linked-hash-map/entry/string[. = $id]/../sorted-set/facet[position() &lt;= $values-per-facet or enabled = 'true']"/>
	            </xsl:when>
	            <xsl:otherwise>
                    <li class="solrFacet facetHeader"><h5><xsl:copy-of select="$label"/></h5></li>
                    <xsl:apply-templates select="/linked-hash-map/entry/string[. = $id]/../list/facet[position() &lt;= $facets-per-browse-page]"/>
                    <li>
                        <div class="yui3-g s-pagination no-bookmarking">
	                        <div class="yui3-u-1-2">
						        <xsl:choose>
						            <xsl:when test="number($page) &gt; 1">
										<a class="pagingButton previous" rel="lightbox-noanim" href="{$facet-browse-base-path}&amp;page={number($page) - 1}" title="previous"> <i class="fa fa-backward"></i> Previous</a>
						            </xsl:when>
						            <xsl:otherwise>
		                                <span class="pagingButton disabled"> <i class="fa fa-backward"></i> Previous</span>
						            </xsl:otherwise>
						        </xsl:choose>
		                        <xsl:choose>
		                            <xsl:when test="count(/linked-hash-map/entry/string[. = $id]/../list/facet) > $facets-per-browse-page">
		                                <a class="pagingButton next" rel="lightbox-noanim" href="{$facet-browse-base-path}&amp;page={number($page) + 1}" title="next">Next <i class="fa fa-forward"></i> </a>
		                            </xsl:when>
		                            <xsl:otherwise>
		                                <span class="pagingButton disabled">Next <i class="fa fa-forward"></i> </span>
		                            </xsl:otherwise>
		                        </xsl:choose>
	                        </div>
	                        <div class="yui3-u-1-2">
                                <xsl:choose>
                                    <xsl:when test="$facet.sort = 'index'">
										<span class="pagingButton disabled">A-Z Sort</span>
                                        <a class="pagingButton" rel="lightbox-noanim" href="{replace($facet-browse-base-path,'&amp;facet.sort=index','')}&amp;page=1" title="Numerical Sort"> Numerical Sort</a>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <a class="pagingButton" rel="lightbox-noanim" href="{$facet-browse-base-path}&amp;facet.sort=index&amp;page=1" title="A-Z Sort"> A-Z Sort</a>
										<span class="pagingButton disabled">Numerical Sort</span>
                                    </xsl:otherwise>
                                </xsl:choose>
	                        </div>
                        </div>
                    </li>
	            </xsl:otherwise>
	        </xsl:choose>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="/">
        <html>
            <body>
				<!-- hidden element that gets moved into place by solr-facets.js -->
                <xsl:if test="$search-mode and string-length($facets) > 0">
                  <xsl:variable name="counts" select="document('cocoon://eresources/count.xml')//s:row[s:genre[ . = 'all']]/s:hits"/>
                         <span id="solrAllCount"><xsl:value-of select="format-number($counts,'###,##0')"/></span>
                </xsl:if>
                <xsl:if test="/linked-hash-map/entry or string-length($facets) > 0">
	                <div class="bd">
		                <h3>Filter Results</h3>
		                <xsl:if test="not($search-mode)">
                            <a class="close fa fa-close"></a>
		                </xsl:if>
		                <ul>
							<xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'recordType'"/>
		                        <xsl:with-param name="label">Results from <span id="sources"><i class="fa fa-info-circle"></i></span></xsl:with-param>					   
							</xsl:call-template>
		
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
		                        <xsl:with-param name="label" select="'Journal'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'year'"/>                    
		                        <xsl:with-param name="label" select="'Year'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'mesh'"/>                    
		                        <xsl:with-param name="label" select="'Subject (MeSH)'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationAuthor'"/>                    
		                        <xsl:with-param name="label" select="'Author'"/>                    
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