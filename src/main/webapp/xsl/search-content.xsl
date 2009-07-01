<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://irt.stanford.edu/search/2.0"
    exclude-result-prefixes="h s"
    version="2.0">
    
    <xsl:param name="p"/>
    <xsl:param name="i"/>
    <xsl:param name="c"/>
    <xsl:param name="o"/>
    <xsl:param name="facet"/>
    
    <xsl:variable name="active-facet">
        <xsl:choose>
            <xsl:when test="$facet"><xsl:value-of select="$facet"/></xsl:when>
            <xsl:otherwise>foundational</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="search-terms" select="/doc/s:search/s:query"/>
    
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
    </xsl:template>
    
    <xsl:template match="h:style">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node|child::node()"/>
            dt.<xsl:value-of select="$active-facet"/>{display:block}
            dd.<xsl:value-of select="$active-facet"/>{display:block}
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:input[@id='facetInput']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:if test="$facet">
                <xsl:attribute name="name" select="'facet'"/>
                <xsl:attribute name="value" select="$active-facet"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:div[@id='leftColumn']//h:a/@href">
        <xsl:choose>
            <xsl:when test="$search-terms">
                <xsl:attribute name="href">
                    <xsl:value-of select="concat(.,'&amp;source=clinical&amp;q=',$search-terms)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:li[starts-with(@id,$active-facet)]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:attribute name="class">activeFacet</xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:h1">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$search-terms">
                    <span>
                        <xsl:text>Search Results for "</xsl:text>
                        <span><xsl:value-of select="$search-terms"/></span>
                        <xsl:text>"</xsl:text>
                    </span>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:dt">
        <xsl:choose>
            <xsl:when test="$search-terms and @id">
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()"/>
                    <xsl:variable name="resource" select="/doc/s:search/s:engine/s:resource[@s:id=current()/@id]"/>
                    <a href="{$resource/s:url}"><xsl:apply-templates select="child::node()"/>
                        <xsl:text> (</xsl:text>
                        <xsl:choose>
                            <xsl:when test="$resource/s:hits">
                                <xsl:value-of select="format-number($resource/s:hits,'###,###,##0')"/>
                            </xsl:when>
                            <xsl:when test="$resource/@s:status">
                                <xsl:value-of select="$resource/@s:status"/>
                            </xsl:when>
                            <xsl:when test="$resource">
                                <xsl:text>running</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>don't know about </xsl:text>
                                <xsl:value-of select="@id"/>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:text>)</xsl:text> 
                    </a>
                </xsl:copy>
                <xsl:apply-templates select="following-sibling::h:dd[1]" mode="results"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
                <xsl:apply-templates select="following-sibling::h:dd[1]" mode="description"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:dd"/>
    
    <xsl:template match="h:dd" mode="description">
        <xsl:copy>
            <xsl:copy-of select="preceding-sibling::h:dt[1]/@class"/>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:dd" mode="results">
        <xsl:variable name="id" select="preceding-sibling::h:dt[1]/@id"/>
        <xsl:variable name="results" select="/doc/s:search/s:engine/s:resource[@s:id=$id]/s:content"/>
        <xsl:copy>
            <xsl:copy-of select="preceding-sibling::h:dt[1]/@class"/>
            <xsl:choose>
                <xsl:when test="count($results) &gt; 0">
                    <ul>
                        <xsl:apply-templates select="$results[position() &lt;= 5]"/>
                    </ul>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="s:content">
        <xsl:variable name="title">
            <xsl:call-template name="highlight">
                <xsl:with-param name="term" select="$search-terms"/> 
                <xsl:with-param name="text" select="s:title"/>
            </xsl:call-template>
        </xsl:variable>
        
        <li>
            <a href="{s:url}" id="{concat(parent::node()/@s:id,'content-',position())}">
                <xsl:copy-of select="$title"/>
            </a>
            <xsl:apply-templates select="s:description">
                <xsl:with-param name="position" select="position()"/>
            </xsl:apply-templates>
        </li>
    </xsl:template>
    
    <xsl:template match="s:description">
        <xsl:param name="position"/>
        <xsl:variable name="description-long">
            <xsl:call-template name="highlight">
                <xsl:with-param name="term" select="$search-terms"/> 
                <xsl:with-param name="text">
                    <xsl:call-template name="truncate">
                        <xsl:with-param name="text" select="."/>
                        <xsl:with-param name="limit" select="800"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="description-short">
            <xsl:call-template name="highlight">
                <xsl:with-param name="term" select="$search-terms"/> 
                <xsl:with-param name="text">
                    <xsl:call-template name="truncate">
                        <xsl:with-param name="text" select="."/>
                        <xsl:with-param name="limit" select="200"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        
        <xsl:copy-of select="$description-short"/>
        
        <div class="tooltips">
            <span id="{concat(parent::node()/parent::node()/@s:id,'content-',$position,'Tooltip')}">
                <xsl:copy-of select="$description-long"/>
            </span>
        </div>
    </xsl:template>
    
    <xsl:template match="h:input">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="@id = 'p' and $p != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$p"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@id = 'i' and $i != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$i"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@id = 'c' and $c != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$c"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@id = 'o' and $o != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$o"/>
                </xsl:attribute>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template name="highlight">
        <xsl:param name="term" select="''" />
        <xsl:param name="text" select="''" />
        <xsl:variable name="reg" select="concat('(.*)([ \s]?',$term,'[ \s]?)(.*)')"/>
        <xsl:choose>
            <xsl:when test="matches($text,$reg,'i')">
                <xsl:call-template name="highlight">
                    <xsl:with-param name="term" select="$term"/>
                    <xsl:with-param name="text" select="replace($text,$reg,'$1','i')"/>
                </xsl:call-template>
                <strong xmlns="http://www.w3.org/1999/xhtml">
                    <xsl:value-of select="replace($text,$reg,'$2','i')"/>                
                </strong>
                <xsl:value-of select="replace($text,$reg,'$3','i')"/>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$text"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="truncate">
        <xsl:param name="text" select="''" />
        <xsl:param name="limit" select="''" />
        <xsl:choose>
            <xsl:when test="string-length($text) > $limit">
                <xsl:value-of select="concat(substring($text,0,$limit),' ...')"/>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$text"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>