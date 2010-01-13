<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" 
    xmlns="http://irt.stanford.edu/search/2.0" 
    xmlns:s="http://irt.stanford.edu/search/2.0" 
    xmlns:js="java:java.lang.String"
    exclude-result-prefixes="h js s" version="2.0">
    
    
    <xsl:param name="q"/>
    <xsl:param name="l"/>
    
    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    
     <!-- number of result titles to return per resource -->
     <xsl:variable name="resultLimit">
        <xsl:choose>
            <xsl:when test="$l">
                <xsl:value-of select="number($l)"/>
            </xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <!-- invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin -->
    <xsl:variable name="invert-comma-separted-terms">
        <xsl:choose>
            <xsl:when test="matches($search-terms,'(\(|\))')">
                <xsl:value-of select="replace($search-terms,'(\(((\w| |-|_)+), ((\w| |-|_)+)\))','$1 AND ($4 $2)')"/>                
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="replace($search-terms,'(((\w| |-|_)+), ((\w| |-|_)+))','$1 AND ($4 $2)')"/>                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <!-- normalize search terms -->
    <xsl:variable name="hyphen-replacement" select="replace($invert-comma-separted-terms,'-','.')"/>
    <xsl:variable name="norm-search-terms" select="replace(lower-case($hyphen-replacement),'[^a-zA-Z0-9,-_ ]','')"/>
    
    <!-- search term regex -->
    <xsl:variable name="search-phrases-pattern" select="replace($norm-search-terms,' and ','|')"/>

    <!-- content nodes from search that get transformed into result nodes -->
    <xsl:variable name="results">
        <xsl:copy>
            <xsl:apply-templates select="/s:search/s:engine/s:resource/s:content[position() &lt;= $resultLimit]"/>
        </xsl:copy>
    </xsl:variable>
    
    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>search content results</title>
            </head>
            <body>
                <dl>
                    <!-- deduping over URLs misses dups across engines; WoS and PubMed dates, authors and journal titles differ significantly; using title may be too greedy -->
                    <xsl:variable name="deduped-results">
                        <xsl:copy-of select="$results/s:result[not(s:dedupTitle=preceding-sibling::node()/s:dedupTitle)]">
                            <xsl:sort select="s:sortTitle"/>
                        </xsl:copy-of>
                    </xsl:variable>
                    <xsl:apply-templates select="$deduped-results/s:result">
                        <xsl:sort select="@score" order="descending" data-type="number"/>
                    </xsl:apply-templates>
                </dl>
                <div id="search-content-counts" style="display:none;">
                    <xsl:for-each select="/s:search/s:engine/s:resource[s:hits]">
                        <span id="{@s:id}">
                            <a href="{s:url}"><xsl:value-of select="s:hits"/></a>
                        </span>
                    </xsl:for-each>
                    <span id="all"><xsl:value-of select="sum(/s:search/s:engine/s:resource/s:hits)"/></span>
                </div>
                <xsl:if test="count($results/s:result/s:description[string-length(.) > 0]) > 0">
                    <div class="tooltips" style="display:none;">
                        <xsl:for-each select="$results/s:result/s:description[string-length(.) > 0]">
                            <xsl:apply-templates select="."/>
                        </xsl:for-each>
                    </div>
                </xsl:if>
                <xsl:if test="count($results/s:result[s:engineName='PubMed']/s:pub-title) > 0">
                    <ul id="pubmedJournalLinks">
                        <xsl:for-each select="distinct-values($results/s:result[s:engineName = 'PubMed']/s:pub-title)">
                            <xsl:sort select="." order="ascending" data-type="text"/>
                            <xsl:if test="position() &lt;= 10">
                                <li><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&amp;otool=stanford&amp;term={$search-terms} AND &quot;{.}&quot;[Journal]"><xsl:value-of select="."/></a></li>
                            </xsl:if>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
    
    <!-- builds result node from s:content -->
    <xsl:template match="s:content">
        <xsl:variable name="norm-desc" select="lower-case(s:description)"/>
        <xsl:variable name="norm-title" select="lower-case(s:title)"/>
        <xsl:variable name="engineId" select="../@s:id"/>
        <xsl:variable name="titleUrl" select="s:url"/>
        <xsl:variable name="title-hits">
            <xsl:analyze-string select="$norm-title" regex="{$search-phrases-pattern}">
                <xsl:matching-substring><hit><xsl:value-of select="."/></hit></xsl:matching-substring>
            </xsl:analyze-string>
        </xsl:variable>
        
        <xsl:variable name="desc-hits">
            <xsl:analyze-string select="$norm-desc" regex="{$search-phrases-pattern}">
                <xsl:matching-substring><hit><xsl:value-of select="."/></hit></xsl:matching-substring>
            </xsl:analyze-string>
        </xsl:variable>
        
        <xsl:variable name="title-hit-count">
            <xsl:value-of select="count(distinct-values($title-hits/s:hit))"/>
        </xsl:variable>
        
        <xsl:variable name="desc-hit-count">
            <xsl:value-of select="count(distinct-values($desc-hits/s:hit))"/>
        </xsl:variable>

        <xsl:variable name="score">
            <xsl:choose>
                 <!--  10 exact title match -->
                 <!--  9  title begins with AND title contains more than one match AND desc contains more than one match-->
                 <!--  8  title begins with AND title contains more than one match AND desc match-->
                 <!--  7  title begins with AND title contains more than one match -->
                 <!--  6  title contains more than one match AND desc contains more than one match-->
                 <!--  5  title contains more than one match -->
                 <!--  4  title match AND desc contains more than one match-->
                 <!--  3  title match AND desc match-->
                 <!--  2  title match-->
                 <!--  1  desc match-->
                <xsl:when test="$norm-title = $norm-search-terms">10</xsl:when>
                <xsl:when test="matches($norm-title,concat('^(',$search-phrases-pattern,')')) and $title-hit-count &gt; 1 and $desc-hit-count &gt; 1">9</xsl:when>
                <xsl:when test="matches($norm-title,concat('^(',$search-phrases-pattern,')')) and $title-hit-count &gt; 1 and $desc-hit-count &gt; 0">8</xsl:when>
                <xsl:when test="matches($norm-title,concat('^(',$search-phrases-pattern,')')) and $title-hit-count &gt; 1">7</xsl:when>
                <xsl:when test="$title-hit-count &gt; 1 and $desc-hit-count &gt; 1">6</xsl:when>
                <xsl:when test="$title-hit-count &gt; 1">5</xsl:when>
                <xsl:when test="$title-hit-count &gt; 0 and $desc-hit-count &gt; 1">4</xsl:when>
                <xsl:when test="$title-hit-count &gt; 0 and $desc-hit-count &gt; 0">3</xsl:when>
                <xsl:when test="$title-hit-count &gt; 0">2</xsl:when>
                <xsl:when test="$desc-hit-count &gt; 0">1</xsl:when>
                <xsl:otherwise>0.1</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <xsl:variable name="weight">
            <xsl:choose>
                <!-- tier I -->
                <xsl:when test="$engineId = 'pubmed_cochrane_reviews'">2</xsl:when>
                <xsl:when test="$engineId = 'dare'">2</xsl:when>
                <xsl:when test="$engineId = 'acpjc'">2</xsl:when>
                <xsl:when test="$engineId = 'bmj_clinical_evidence'">2</xsl:when>
                <xsl:when test="$engineId = 'jama_rce'">2</xsl:when>
                
                <!-- tier II -->
                <xsl:when test="$engineId = 'pubmed_systematicreviews'">1</xsl:when>
                <xsl:when test="$engineId = 'pubmed_rct'">1</xsl:when>
                <xsl:when test="$engineId = 'pubmed_guidelines'">1</xsl:when>
                <xsl:when test="$engineId = 'uptodate'">1</xsl:when>
                <xsl:when test="$engineId = 'emedicine'">1</xsl:when>
                <xsl:when test="$engineId = 'cro_clineguide'">1</xsl:when>
                <xsl:when test="$engineId = 'pubmed'">1</xsl:when>
                
                <!-- tier III -->
                <xsl:when test="$engineId = 'pubmed_clinicaltrial'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_recent_reviews'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_treatment_focused'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_diagnosis_focused'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_prognosis_focused'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_harm_focused'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_etiology_focused'">0.5</xsl:when>
                <xsl:when test="$engineId = 'pubmed_epidemiology_focused'">0.5</xsl:when>

                <!-- tier IV -->
                <xsl:when test="$engineId = 'aafp_patients'">0.2</xsl:when>
                <xsl:when test="$engineId = 'medline_plus_0'">0.2</xsl:when>

                <!-- everything else is tier II -->
                <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- PubMed engines get simplified, all others use search engine description -->        
        <xsl:variable name="engineName">
            <xsl:choose>
                <xsl:when test="contains($engineId,'pubmed') and $engineId != 'pubmed_guidelines' and $engineId != 'pubmed_cochrane_reviews'">PubMed</xsl:when>
                <xsl:otherwise><xsl:value-of select="../s:description"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <result score="{$score * $weight}">
            <engineId><xsl:value-of select="$engineId"/></engineId>   
            <uniqueId><xsl:value-of select="concat($engineId,'-',position())"/></uniqueId>   
            <engineName><xsl:value-of select="$engineName"/></engineName>   
            <engineHits><xsl:value-of select="../s:hits"/></engineHits>   
            <engineUrl><xsl:value-of select="../s:url"/></engineUrl>
            <title><xsl:value-of select="s:title"/></title>
            <sortTitle><xsl:value-of select="replace($norm-title,'^(a|an|the) ','','i')"/></sortTitle>   
            <dedupTitle><xsl:value-of select="replace($norm-title,'\W','')"/></dedupTitle>   
            <description>
                <xsl:choose>
                    <xsl:when test="s:description"><xsl:value-of select="s:description"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="s:title"/></xsl:otherwise>
                </xsl:choose>
            </description>       
            <type><xsl:value-of select="../s:description"/></type>       
            <url><xsl:value-of select="s:url"/></url> 
            <xsl:copy-of select="s:author"/>
            <xsl:copy-of select="s:pub-title"/>
            <xsl:copy-of select="s:pub-date"/>
            <xsl:copy-of select="s:pub-volume"/>
            <xsl:copy-of select="s:pub-issue"/>
            <xsl:copy-of select="s:page"/>
            <xsl:copy-of select="s:id"/>
        </result>
    </xsl:template>
    
    <!-- tranforms result node into displayable -->
    <xsl:template match="s:result">
        <xsl:variable name="title">
            <xsl:call-template name="highlight">
                <xsl:with-param name="term" select="$search-terms"/> 
                <xsl:with-param name="text" select="s:title"/>
            </xsl:call-template>
        </xsl:variable>
        
        <dd xmlns="http://www.w3.org/1999/xhtml">
            <ul>
                <li id="{s:id}">
                    <a title="{concat('article -- ',s:engineId,' -- ',$title)}" href="{s:url}" id="{s:uniqueId}" target="_blank">
                        <xsl:copy-of select="$title"/>
                    </a>
                    
                    <xsl:apply-templates select="s:author"/>
                    <div class="pubTitle">
                        <xsl:apply-templates select="s:pub-title"/>
                        <xsl:apply-templates select="s:pub-date"/>
                        <xsl:apply-templates select="s:pub-volume"/>
                        <xsl:apply-templates select="s:pub-issue"/>
                        <xsl:apply-templates select="s:page"/>
                        <xsl:apply-templates select="s:id"/>
                    </div>
                    
                    <div class="moreResults">
                        <span class="sourceLink">
                            <xsl:value-of select="s:engineName"/>
                        </span>
                        <xsl:choose>
                            <xsl:when test="s:engineName = 'PubMed'">
                                <xsl:text> - </xsl:text>
                                <a href="#" rel="popup local pubmedMoreStrategy">more</a>
                            </xsl:when>
                            <xsl:when test="$resultLimit &lt; number(s:engineHits)">
                                <xsl:text> - </xsl:text>
                                <a target="_blank" title="all {format-number(s:engineHits,'###,###,##0')} results from {s:engineName}" href="{s:engineUrl}">more</a>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </li>
            </ul>
        </dd>
    </xsl:template>
    
    

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
        <xsl:apply-templates select="s:search"/>
    </xsl:template>
    

    <xsl:template match="s:description">
        <xsl:variable name="description-long">
            <xsl:call-template name="highlight">
                <xsl:with-param name="term" select="$search-terms"/> 
                <xsl:with-param name="text" select="."/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="tooltip-width">
            <xsl:choose>
                <xsl:when test="string-length($description-long) > 500">width:60%</xsl:when>
            </xsl:choose>
        </xsl:variable>
            <span style="{$tooltip-width}" id="{concat(parent::node()/s:uniqueId,'Tooltip')}">
                <xsl:copy-of select="$description-long"/>
            </span>
    </xsl:template>

    <xsl:template match="s:author">
        <div class="pubAuthor">
            <xsl:value-of select="."/>
        </div>
    </xsl:template>
    
    <xsl:template match="s:pub-title">
            <xsl:value-of select="."/>
            <xsl:text>. </xsl:text>
    </xsl:template>
    
    <xsl:template match="s:pub-date">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="s:pub-volume">
        <xsl:text>;</xsl:text>
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="s:pub-issue">
        <xsl:text>(</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="s:page">
        <xsl:text>:</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>.</xsl:text>
    </xsl:template>
    
    <xsl:template match="s:id">
        <small>
            <xsl:text> </xsl:text>
            <xsl:if test="contains(../engineId,'pubmed')">PMID:</xsl:if>
            <xsl:value-of select="."/>
        </small>
    </xsl:template>
        
    <xsl:template name="highlight">
        <xsl:param name="term" select="''" />
        <xsl:param name="text" select="''" />
        <xsl:variable name="reg" select="concat('(?im)(.*)(\W?',$search-phrases-pattern,'\W?)(.*)')"/>
        <xsl:choose>
            <xsl:when test="js:matches(string($text),$reg)">
                <xsl:call-template name="highlight">
                    <xsl:with-param name="term" select="$term"/>
                    <xsl:with-param name="text" select="js:replaceFirst(string($text),$reg,'$1')"/>
                </xsl:call-template>
                <strong>
                    <xsl:value-of select="js:replaceFirst(string($text),$reg,'$2')"/>                
                </strong>
                <xsl:value-of select="js:replaceFirst(string($text),$reg,'$3')"/>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$text"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
        
</xsl:stylesheet>