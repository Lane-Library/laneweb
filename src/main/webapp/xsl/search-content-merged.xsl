<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" 
    xmlns:s="http://irt.stanford.edu/search/2.0" 
    exclude-result-prefixes="h s" version="2.0">
    
    
    <xsl:param name="facet"/>
    <xsl:param name="ids"/>
    <xsl:param name="mode"/>
    <xsl:param name="q"/>
    <xsl:param name="source"/>
    
    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="facets">
        <facets>
            <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and not(contains(@s:id,'pubmed') ) and s:content]">
                <facet type="source" name="{s:description}">
                    <rid><xsl:value-of select="@s:id"/></rid>
                </facet>
            </xsl:for-each>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed') and s:content]">
                <facet type="source" name="PubMed">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_treatment') and s:content]">
                <facet type="type" name="Therapy">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_treatment') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_diagnosis') and s:content]">
                <facet type="type" name="Diagnosis">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_diagnosis') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_prognosis') and s:content]">
                <facet type="type" name="Prognosis">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_prognosis') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_harm') and s:content]">
                <facet type="type" name="Harm">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_harm') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_etiology') and s:content]">
                <facet type="type" name="Etiology">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_etiology') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_epidemiology') and s:content]">
                <facet type="type" name="Epidemiology">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'pubmed_epidemiology') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'systematic') and s:content]">
                <facet type="format" name="Systematic Review">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'systematic') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'trial') and s:content]">
                <facet type="format" name="Clinical Trial">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'trial') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
            <xsl:if test="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'summaries') and s:content]">
                <facet type="format" name="Evidence Summary">
                    <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0 and contains(@s:id,'summaries') and s:content]">
                        <rid><xsl:value-of select="@s:id"/></rid>                
                    </xsl:for-each>
                </facet>
            </xsl:if>
        </facets>
    </xsl:variable>
    
    <xsl:variable name="filtered-ids">
        <xsl:choose>
            <xsl:when test="$ids">
                <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($ids,','),@s:id) &gt; 0 and s:content]">
                    <xsl:value-of select="@s:id"/>
                    <xsl:if test="position() != last()">,</xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="$source = 'pubmed'">
                <xsl:variable name="pm-engines">pubmed,pubmed_guidelines,pubmed_treatment_systematic_reviews,pubmed_prognosis_systematic_reviews,pubmed_diagnosis_systematic_reviews,pubmed_harm_systematic_reviews,pubmed_recent_reviews,pubmed_treatment_clinical_trials,pubmed_treatment_focused,pubmed_diagnosis_clinical_trials,pubmed_diagnosis_focused,pubmed_prognosis_clinical_trials,pubmed_prognosis_focused,pubmed_harm_clinical_trials,pubmed_harm_focused,pubmed_etiology_focused,pubmed_etiology_expanded,pubmed_epidemiology_focused,pubmed_epidemiology_expanded</xsl:variable>
                <xsl:for-each select="/s:search/s:engine/s:resource[index-of(tokenize($pm-engines,','),@s:id) &gt; 0 and s:content]">
                    <xsl:value-of select="@s:id"/>
                    <xsl:if test="position() != last()">,</xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="/s:search/s:engine/s:resource[s:content]">
                    <xsl:value-of select="@s:id"/>
                    <xsl:if test="position() != last()">,</xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="results">
        <xsl:copy>
            <xsl:apply-templates select="/s:search/s:engine/s:resource[index-of(tokenize($filtered-ids,','),@s:id) &gt; 0]/s:content"/>
        </xsl:copy>
    </xsl:variable>
    
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="$mode = 'facets' and count($results/result) &gt; 0">
                <ul xmlns="http://www.w3.org/1999/xhtml">
                    <xsl:choose>
                        <xsl:when test="$ids != ''">
                            <h3><a href="?source={$source}&amp;q={$search-terms}">Remove Limits</a></h3>
                        </xsl:when>
                        <xsl:otherwise>
                            <h3>Refine Results</h3>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:apply-templates select="$facets/facets"/>
                </ul>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <html xmlns="http://www.w3.org/1999/xhtml" id="ranked-results">
                        <head>
                            <title>Merged Content List</title>
                        </head>
                        <body>
                            <dl>
                                <xsl:apply-templates select="$results/result[not(url=preceding-sibling::node()/url)]">
                                    <xsl:sort select="@score" order="descending"/>
                                    <xsl:sort select="sortTitle"/>
                                </xsl:apply-templates>
                            </dl>
                        </body>
                    </html>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="s:content">
        <xsl:variable name="norm-search-terms" select="replace(lower-case($search-terms),'[^a-zA-Z0-9_ ]','')"/>
        <xsl:variable name="norm-title" select="replace(lower-case(s:title),'[^a-zA-Z0-9_ ]','')"/>
        <xsl:variable name="score">
            <xsl:choose>
                <xsl:when test="$norm-title = $norm-search-terms">5</xsl:when>
                <xsl:when test="starts-with($norm-title,$norm-search-terms)">4</xsl:when>
                <xsl:when test="contains($norm-title,$norm-search-terms)">3</xsl:when>
                <xsl:when test="contains(lower-case(s:description),$norm-search-terms)">2</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <result score="{$score}">
            <title><xsl:value-of select="s:title"/></title>   
            <sortTitle><xsl:value-of select="replace($norm-title,'^(a|an|the) ','','i')"/></sortTitle>   
            <type><xsl:value-of select="../s:description"/></type>       
            <url><xsl:value-of select="s:url"/></url> 
            <resultsUrl><xsl:value-of select="../s:url"/></resultsUrl> 
            <description><xsl:value-of select="s:description"/></description> 
        </result>
    </xsl:template>
    
    <xsl:template match="result">
        <dt xmlns="http://www.w3.org/1999/xhtml">
            <a href="{url}" title="{description}">
                <xsl:value-of select="title"/>
            </a>
        </dt>
        <dd xmlns="http://www.w3.org/1999/xhtml">
            <ul>
                <li>
                    <xsl:text>Source: </xsl:text>
                    <a href="{resultsUrl}" title="{concat('More from ',type)}">
                        <xsl:value-of select="type"/>
                    </a>
                </li>
            </ul>
        </dd>
    </xsl:template>
    
    <xsl:template match="facets">
        <xsl:if test="facet[@type='format']">
            <h3>Article Format</h3>
            <xsl:apply-templates select="facet[@type='format']"/>
        </xsl:if>
        <xsl:if test="facet[@type='type']">
            <h3>Question Type</h3>
            <xsl:apply-templates select="facet[@type='type']"/>
        </xsl:if>
        <xsl:if test="facet[@type='source']">
            <h3>Source</h3>
            <xsl:apply-templates select="facet[@type='source']">
                <xsl:sort select="@name"/>                      
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="facet">
        <xsl:variable name="facet-ids">
            <xsl:for-each select="rid">
                <xsl:value-of select="."/>
                <xsl:if test="position() != last()">,</xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <li xmlns="http://www.w3.org/1999/xhtml"><a href="?source={$source}&amp;ids={$facet-ids}&amp;q={$search-terms}"><xsl:value-of select="@name"/></a></li>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
</xsl:stylesheet>