<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:f="http://lane.stanford.edu/resources/1.0" xmlns:fct="http://lane.stanford.edu/function" exclude-result-prefixes="f h fct" version="2.0">

    <xsl:param name="base-path" />
    <xsl:param name="facet" />
    <xsl:param name="facets" />
    <xsl:param name="page" />
    <xsl:param name="query" />
    <xsl:param name="query-string" />
    <xsl:param name="facet.sort" />
    <xsl:param name="source" />
    <xsl:param name="url-encoded-query" />


    <xsl:variable name="encoded-facets" select="replace(replace($facets, '\[', '%5B'), '\]','%5D')" />
    <xsl:variable name="start-year" select="substring-before(substring-after($facets, 'year:['), ' TO')" />
    <xsl:variable name="end-year" select="substring-before(substring-after(substring-after($facets, 'year:['), 'TO '), ']')" />
    <xsl:variable name="pageless-query-string" select="replace($query-string,'&amp;page=\d+','')" />
    <xsl:variable name="facet-search-base-path" select="concat('?',replace($pageless-query-string,'&amp;(facets=[^&amp;]+|facet.sort=index|facets=)',''))" />
    <xsl:variable name="values-per-facet" select="4" />
    <xsl:variable name="current-year" select="year-from-date(current-date())" />


    <xsl:variable name="filter-facet">
        <div class="filter-facet module">
            <h2>
                Filter Applied
                <a href="{$facet-search-base-path}">Clear All</a>
            </h2>
            <div>
                <xsl:for-each select="/f:facetResult/f:facet">
                    <xsl:variable name="facet-id" select="concat(./@f:key ,':',  '&quot;' , ./@f:name, '&quot;')" />
                    <xsl:if test="contains($facets,$facet-id)">
                        <xsl:variable name="furl" select="fct:getFacetUrl($facet-id)"></xsl:variable>
                        <a href="{$facet-search-base-path}{ $furl}">
                            <span>
                                <xsl:value-of select="fct:getLabel(./@f:name)" />
                                <i class="fa-regular fa-xmark"></i>
                            </span>
                        </a>
                    </xsl:if>
                </xsl:for-each>

                <xsl:if test="$start-year != '' or $end-year != ''">
                    <xsl:variable name="year-facet" select="concat('year:%5B', $start-year, ' TO ', $end-year, '%5D')"></xsl:variable>
                    <xsl:variable name="furl" select="fct:getFacetUrl($year-facet)" />
                    <a href="{$facet-search-base-path}{ $furl}">
                        <span>
                            <xsl:value-of select="$start-year" />
                            To
                            <xsl:value-of select="$end-year" />
                            <i class="fa-regular fa-xmark"></i>
                        </span>
                    </a>
                </xsl:if>
            </div>
        </div>
    </xsl:variable>

    <xsl:variable name="header">
        <h2 class="yui3-tooltip-trigger" title="Click checkbox(es) to reduce results displayed to only the selected type(s)">
            Filter Results
            <i class="fa-regular fa-angle-down fa-lg"></i>
            <i class="fa-regular fa-angle-up fa-lg"></i>
        </h2>
    </xsl:variable>


    <xsl:template match="f:facet">
        <xsl:variable name="count-formatted" select="format-number(./@f:value, '###,##0')" />
        <xsl:variable name="facet-id" select="concat(./@f:key ,':',  '&quot;' , ./@f:name, '&quot;' )" />
        <xsl:variable name="furl" select="fct:getFacetUrl($facet-id)"></xsl:variable>
        <xsl:choose>
            <xsl:when test="contains($facets,$facet-id)">
                <li class="facet enabled">
                    <a href="{$facet-search-base-path}{$furl}">

                        <span class="facetLabel">
                            <i class="fa-solid fa-square-check"></i>
                            <span>
                                <xsl:copy-of select="fct:getLabel(./@f:name)" />
                            </span>
                        </span>
                        <span class="facetCount">
                            <xsl:value-of select="$count-formatted" />
                        </span>
                    </a>
                </li>
            </xsl:when>
            <xsl:otherwise>
                <li class="facet">
                    <a href="{$facet-search-base-path}{$furl}">
                        <span class="facetLabel">
                            <i class="fa-regular fa-square fa-lg"></i>
                            <xsl:copy-of select="fct:getLabel(./@f:name)" />
                        </span>
                        <span class="facetCount">
                            <xsl:value-of select="$count-formatted" />
                        </span>
                    </a>
                </li>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="field">
        <xsl:param name="id" />
        <xsl:param name="label" />
        <li class="solrFacet facetHeader">
            <xsl:copy-of select="$label" />
        </li>
        <xsl:choose>
            <xsl:when test="$id/@f:key = 'recordType'">
                <xsl:apply-templates select="$id" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="$id[position() &lt;= $values-per-facet]" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="/">
        <html>
            <body>
                <xsl:if test="$facets != ''">
                    <xsl:copy-of select="$filter-facet" />
                </xsl:if>
                <div class="facet-container module menu-container mobile">
                    <xsl:apply-templates select="./f:facetResult" />
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="f:facetResult">
        <!-- xsl:if test="$counts > 0 and (./entry or string-length($facets) > 0)" -->
        <xsl:if test="count(/f:facetResult//f:facet) &gt; 0">
            <xsl:copy-of select="$header" />
            <ul>
                <xsl:if test="count(f:facet[@f:key='type']) > 0">
                    <xsl:call-template name="field">
                        <xsl:with-param name="id" select="f:facet[@f:key='type']" />
                        <xsl:with-param name="label">
                            Resource Type
                            <i class="fa-solid fa-info-circle yui3-tooltip-trigger" title="20+ types of resources: books, journals, chapters, databases, images, videos - check &quot;See All&quot;" />
                        </xsl:with-param>
                    </xsl:call-template>
                    <xsl:if test="count(f:facet[@f:key='type']) > $values-per-facet">
                        <li>
                            <input name="contains" placeholder="Search" data-facet="type" data-searchterm="{$query}" data-facets="{$facets}" class="facet-suggestion" />
                        </li>
                    </xsl:if>
                </xsl:if>

                <xsl:if test="count(f:facet[@f:key='publicationType']) > 0">
                    <xsl:call-template name="field">
                        <xsl:with-param name="id" select="f:facet[@f:key='publicationType']" />
                        <xsl:with-param name="label" select="'Article Type'" />
                    </xsl:call-template>
                    <xsl:if test="count(f:facet[@f:key='publicationType']) > ($values-per-facet -1)">
                        <li>
                            <input name="contains" placeholder="Search" data-facet="publicationType" data-searchterm="{$query}" data-facets="{$facets}" class="facet-suggestion" />
                        </li>
                    </xsl:if>
                </xsl:if>

                <xsl:if test="count(f:facet[@f:key='recordType']) > 0">
                    <xsl:call-template name="field">
                        <xsl:with-param name="id" select="f:facet[@f:key='recordType']" />
                        <xsl:with-param name="label" select="'Result From'" />
                    </xsl:call-template>
                </xsl:if>


                <li class="facet facetHeader">
                    Year
                    <div>
                        <span id="facet-error-message">
                        </span>
                        <form action="" method="get" id="solr-date-form">
                            <input name="start-year" type="number" class="date start" min="1400" max="{$current-year}" value="{$start-year}" required="true" placeholder="From" />
                            <input name="end-year" type="number" class="date end" min="1400" max="{$current-year}" value="{$end-year}" required="true" placeholder="To" />
                            <input type="submit" value="Add" class="date" />
                        </form>
                    </div>
                </li>


                <xsl:if test="count(f:facet[@f:key='publicationTitle']) > 0">
                    <xsl:call-template name="field">
                        <xsl:with-param name="id" select="f:facet[@f:key='publicationTitle']" />
                        <xsl:with-param name="label" select="'Journal Title'" />
                    </xsl:call-template>
                    <xsl:if test="count(f:facet[@f:key='publicationTitle']) > $values-per-facet">
                        <li>
                            <input name="contains" placeholder="Search" data-facet="publicationTitle" data-searchterm="{$query}" data-facets="{$facets}" class="facet-suggestion" />
                        </li>
                    </xsl:if>
                </xsl:if>

            </ul>
        </xsl:if>

    </xsl:template>


    <xsl:function name="fct:getLabel">
        <xsl:param name="name" />
        <xsl:choose>
            <xsl:when test="$name = 'pubmed'">
                PubMed
            </xsl:when>
            <xsl:when test="$name = 'sul'">
                <span class="yui3-tooltip-trigger" title="A curated subset of journals, books, databases and other resources of biomedical relevance available from Stanford University.">
                    SearchWorks (
                    <i>biomedical subset</i>
                    )
                </span>
            </xsl:when>
            <xsl:when test="$name = 'bib'">
                <span class="yui3-tooltip-trigger" title="The journals, books and other resources uniquely available from Lane Medical Library.">Lane Catalog</span>
            </xsl:when>
            <xsl:when test="$name = 'redivis'">
                <span class="yui3-tooltip-trigger" title="Curated datasets provided by Stanford Center for Population Health Sciences hosted on Redivis.">Redivis - PHS</span>
            </xsl:when>
            <xsl:when test="$name = 'web'">
                Lane Web Site
            </xsl:when>
            <xsl:when test="$name = 'class'">
                Lane Classes
            </xsl:when>
            <xsl:when test="$name = 'laneblog'">
                Lane Blog
            </xsl:when>
            <xsl:when test="$name = 'dnlm'">
                <span class="yui3-tooltip-trigger" title="A small subset of open access journals from the National Library of Medicine">PMC Journals</span>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$name" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="fct:getFacetUrl">
        <xsl:param name="facet-id" />
        <xsl:choose>
            <xsl:when test="string-length($facets) = 0">
                <xsl:value-of select="concat('&amp;facets=',  $facet-id)" />
            </xsl:when>
            <xsl:when test="contains($encoded-facets, concat('::', $facet-id))">
                <xsl:value-of select="concat('&amp;facets=', replace( $encoded-facets, concat('::',$facet-id) , ''))" />
            </xsl:when>
            <xsl:when test="contains($encoded-facets, concat( $facet-id, '::'))">
                <xsl:value-of select="concat('&amp;facets=', replace( $encoded-facets, concat($facet-id,'::') , ''))" />
            </xsl:when>
            <xsl:when test="contains($encoded-facets,$facet-id)">
                <xsl:value-of select="concat('&amp;facets=', replace( $encoded-facets, $facet-id , ''))" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat('&amp;facets=', $encoded-facets , '::', $facet-id )" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>


</xsl:stylesheet>