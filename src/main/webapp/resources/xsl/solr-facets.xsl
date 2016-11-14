<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:hc="http://lane.stanford.edu/hitcounts/1.0"
    exclude-result-prefixes="h hc"
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
    <xsl:variable name="facet-browse-base-path" select="concat($base-path,'/search/solr/facet-browse.html?',$pageless-query-string)"/>
    <xsl:variable name="facets-per-browse-page" select="20"/>
    <xsl:variable name="values-per-facet" select="4"/>
    <xsl:variable name="today" select="number(format-dateTime(current-dateTime(),'[Y,4][M,2][D,2]'))"/>
    
    <xsl:template match="facet">
        <xsl:variable name="count-formatted" select="format-number(count,'###,##0')"/>
        <xsl:variable name="label">
            <xsl:choose>
                <xsl:when test="../../string[. = 'recordType'] and value = 'pubmed'">PubMed</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'bib'"><span class="yui3-tooltip-trigger" title="The journals, books and other resources uniquely available from Lane Medical Library.">Lane Catalog</span></xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'web'">Lane Web Site</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'class'">Lane Classes</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'auth'">Lane Community Info</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'laneblog'">Lane Blog</xsl:when>
                <xsl:when test="../../string[. = 'recordType'] and value = 'instructional_videos'">Instructional Videos</xsl:when>
                <xsl:when test="../../string[. = 'type'] and value = 'Video'">Video: All</xsl:when>
                <xsl:when test="../../string[. = 'type'] and value = 'Instructional Video'">Video: Instructional</xsl:when>
                <xsl:when test="../../string[. = 'date'] and value = concat('[',format-number($today - 10000,'0'),' TO *]')">Last 12 Months</xsl:when>
                <xsl:when test="../../string[. = 'date'] and value = concat('[',format-number($today - 50000,'0'),' TO *]')">Last 5 Years</xsl:when>
                <xsl:when test="../../string[. = 'date'] and value = concat('[',format-number($today - 100000,'0'),' TO *]')">Last 10 Years</xsl:when>
                <xsl:when test="../../string[. = 'year'] and value = '0'">Unknown</xsl:when>
                <xsl:otherwise><xsl:value-of select="value"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="furl">
			<xsl:if test="string-length(url) > 0"><xsl:value-of select="concat('&amp;facets=',url)"/></xsl:if>
        </xsl:variable>
		<xsl:choose>
		    <xsl:when test="enabled = 'true'">
		         <li class="enabled"><a title="remove" href="{$facet-search-base-path}{$furl}"> <i class="fa fa-check-square fa-lg"></i> <span class="facetLabel"><xsl:copy-of select="$label"/></span> <span class="facetCount"><xsl:value-of select="$count-formatted"/></span></a></li>
		    </xsl:when>
		    <xsl:otherwise>
		         <li><a href="{$facet-search-base-path}{$furl}"><i class="fa fa-square-o fa-lg"></i><span class="facetLabel"><xsl:copy-of select="$label"/></span> <span class="facetCount"><xsl:value-of select="$count-formatted"/></span></a></li>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>

    <xsl:template name="field">
        <xsl:param name="id"/>
        <xsl:param name="label"/>
        <xsl:variable name="entry" select="/linked-hash-map/entry/string[. = $id]/.."/>
        <xsl:if test="$entry//facet">
	        <xsl:choose>
	            <xsl:when test="$search-mode">
		            <li class="solrFacet facetHeader">
						<xsl:copy-of select="$label"/>
						<xsl:if test="count($entry/sorted-set/facet[count > 0]) > $values-per-facet">
						          <span class="seeAll"><a rel="lightbox disableBackground" href="{$facet-browse-base-path}&amp;facet={$id}&amp;page=1"> see all </a></span>
						</xsl:if>
		            </li>
		            <xsl:choose>
		              <xsl:when test="$id = 'year'">
						<xsl:apply-templates select="/linked-hash-map/entry/string[. = 'date']/../sorted-set/facet[contains(value,'TO *') and count > 0]"/>
                        <xsl:apply-templates select="$entry/sorted-set/facet[enabled = 'true']"/>
		              </xsl:when>
		              <xsl:otherwise>
			            <xsl:apply-templates select="$entry/sorted-set/facet[position() &lt;= $values-per-facet or enabled = 'true']"/>
		              </xsl:otherwise>
		            </xsl:choose>
	            </xsl:when>
	            <xsl:otherwise>
                    <li class="solrFacet facetHeader"><h5><xsl:copy-of select="$label"/></h5></li>
                    <xsl:apply-templates select="$entry/list/facet[position() &lt;= $facets-per-browse-page]"/>
                    <li>
                        <div class="yui3-g s-pagination no-bookmarking">
	                        <div class="yui3-u-1-2">
						        <xsl:choose>
						            <xsl:when test="number($page) &gt; 1">
										<a class="pagingButton previous" rel="lightbox disableAnimation disableBackground" href="{$facet-browse-base-path}&amp;page={number($page) - 1}" title="previous"> <i class="fa fa-backward"></i> Previous</a>
						            </xsl:when>
						        </xsl:choose>
		                        <xsl:choose>
		                            <xsl:when test="count($entry/list/facet) > $facets-per-browse-page">
		                                <a class="pagingButton next" rel="lightbox disableAnimation disableBackground" href="{$facet-browse-base-path}&amp;page={number($page) + 1}" title="next">Next <i class="fa fa-forward"></i> </a>
		                            </xsl:when>
		                        </xsl:choose>
	                        </div>
	                        <div class="yui3-u-1-2">
                                <xsl:choose>
                                    <xsl:when test="$facet.sort = 'index'">
                                        <a class="pagingButton" rel="lightbox disableAnimation disableBackground" href="{replace($facet-browse-base-path,'&amp;facet.sort=index','')}&amp;page=1" title="Numerical Sort"> Numerical Sort</a>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <a class="pagingButton" rel="lightbox disableAnimation disableBackground" href="{$facet-browse-base-path}&amp;facet.sort=index&amp;page=1" title="A-Z Sort"> A-Z Sort</a>
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
                  <xsl:variable name="counts" select="document('cocoon://eresources/count.xml')/hc:hitcounts/hc:facet[@name='all']/@hits"/>
                         <span id="solrAllCount"><xsl:value-of select="format-number($counts,'###,##0')"/></span>
                </xsl:if>
                <xsl:if test="/linked-hash-map/entry or string-length($facets) > 0">
	                <div class="bd">
		                <h3 class="yui3-tooltip-trigger" title="Click checkbox(es) to reduce results displayed to only the selected type(s)">Filter Results</h3>
		                <xsl:if test="not($search-mode)">
                            <a class="close fa fa-close"></a>
		                </xsl:if>
		                <ul>
							<xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'recordType'"/>
		                        <xsl:with-param name="label">Results From <span id="sources"></span></xsl:with-param>					   
							</xsl:call-template>
		
							<xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'type'"/>					   
		                        <xsl:with-param name="label">Resource Type <i class="fa fa-info-circle fa-lg yui3-tooltip-trigger" title="20+ types of resources: books, journals, chapters, databases, images, videos - check &quot;See All&quot;"></i></xsl:with-param>
							</xsl:call-template>
		
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationType'"/>                    
		                        <xsl:with-param name="label" select="'Article Type'"/>                    
		                    </xsl:call-template>
		
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'publicationTitle'"/>                    
		                        <xsl:with-param name="label" select="'Journal Title'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'year'"/>                    
		                        <xsl:with-param name="label" select="'Year'"/>                    
		                    </xsl:call-template>
		                    
		                    <xsl:call-template name="field">
		                        <xsl:with-param name="id" select="'mesh'"/>               
		                    	<xsl:with-param name="label"><span class="yui3-tooltip-trigger" title="Filter down search results to only articles pertaining to these medical subjects">Medical Subject</span></xsl:with-param>     
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