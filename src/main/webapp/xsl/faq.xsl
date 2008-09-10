<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:lw="http://irt.stanford.edu/laneweb"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h xs lw"
    version="2.0">
    
    <xsl:param name="id"/>
    <xsl:param name="category"/>
    <xsl:param name="mode"/>
    
    <xsl:variable name="category-map" select="/h:html/h:body/h:div[@id='category-map']"/>
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="h:title"/>
            <meta name="LW.faqCategory">
                <xsl:attribute name="content">
                    <xsl:choose>
                        <xsl:when test="$id">
                            <xsl:value-of select="/h:html/h:body/h:blog/h:entry[@id=$id]/h:ul/h:li[@class='primaryCategory']"/>
                        </xsl:when>
                        <xsl:when test="$category">
                            <xsl:value-of select="$category"/>
                        </xsl:when>
                    </xsl:choose>
                </xsl:attribute>
            </meta>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$id">
                    <xsl:value-of select="/h:html/h:body/h:blog/h:entry[@id=$id]/h:ul/h:li[@class='title']"/>
                </xsl:when>
                <xsl:when test="$category">
                    <xsl:value-of select="$category"/><xsl:text> FAQs</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Frequently Asked Questions</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:blog">
        <xsl:choose>
            <xsl:when test="not($id)">
                <xsl:variable name="root-category" select="/h:html/h:body/h:div[@id='categories']/h:ul/h:li[descendant-or-self::h:li/text() = $category]/text()"/>
                <xsl:variable name="root-category-string" select="$category-map/h:div[h:span=$root-category]/h:span[2]"/>
                <div id="yui-main">
                    <div class="yui-b">
                        <h1><xsl:value-of select="$category"/> FAQs</h1>
                        <dl id="faq">
                            <xsl:apply-templates
                                select="h:entry[h:ul/h:li[@class='categories']/h:ul/h:li/text() = $category]" mode="dl"/>
                        </dl>
                    </div>
                </div>
                <div id="leftColumn" class="yui-b">
                    <xsl:choose>
                        <xsl:when test="string-length($root-category-string) = 0">
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                                href="cocoon:/services/leftmenu_services.html">
                                <xi:fallback/>
                            </xi:include>
                        </xsl:when>
                        <xsl:otherwise>
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                                href="cocoon:/services/{$root-category-string}/leftmenu_{$root-category-string}.html">
                                <xi:fallback/>
                            </xi:include>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </xsl:when>
            <xsl:when test="$id != '' and $mode = 'dl'">
                <xsl:apply-templates select="h:entry[@id=$id]" mode="dl"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="h:entry[@id=$id]" mode="full"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:div[@id='categories' or @id='category-map']"/>
    
    <xsl:template match="h:entry" mode="dl">
        <dt>
            <a id="{@id}" href="/howto/index.html?id={@id}">
                <xsl:value-of select="h:ul/h:li[@class='title']"/>
            </a>
        </dt>
        <dd>
            <xsl:value-of select="h:ul/h:li[@class='excerpt']"/>
        </dd>
    </xsl:template>
    
    <xsl:template match="h:entry" mode="full">
        <xsl:variable name="primary-category" select="h:ul/h:li[@class='primaryCategory']"/>
        <xsl:variable name="root-category" select="/h:html/h:body/h:div[@id='categories']/h:ul/h:li[descendant-or-self::h:li/text() = $primary-category]/text()"/>
        <xsl:variable name="root-category-string" select="$category-map/h:div[h:span=$root-category]/h:span[2]"/>
        <xsl:variable name="more-category">
            <xsl:choose>
                <xsl:when test="contains($primary-category,'&amp;')">
                    <xsl:value-of select="substring-before($primary-category,'&amp;')"/>
                    <xsl:text>%26</xsl:text>
                    <xsl:value-of select="substring-after($primary-category,'&amp;')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$primary-category"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable><div id="yui-main">
            <div class="yui-b">
                <div class="yui-ge">
                    <div class="yui-u first">
                        <h1>
                            <xsl:value-of select="h:ul/h:li[@class='title']"/>
                        </h1>
                        <xsl:for-each-group select="h:ul/h:li[@class='body']/node()"
                            group-adjacent="lw:inline(.)">
                            <xsl:choose>
                                <xsl:when test="current-grouping-key()">
                                    <p><xsl:apply-templates select="current-group()"/></p>
                                </xsl:when>
                                <xsl:otherwise>                        
                                    <xsl:apply-templates select="current-group()"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each-group>
                        <p style="font-size:xx-small"><xsl:value-of select="h:ul/h:li[@class='author']"/>,
                            <xsl:value-of select="h:ul/h:li[@class='modified']"/>
                        </p>
                    </div>
                    <div id="rightColumn" class="yui-u">
                        <div>
                            <h2>FAQs on this topic</h2>
                            <xsl:variable name="cat" select="h:ul/h:li[@class='primaryCategory']"/>
                            <ul>
                                <xsl:for-each select="parent::h:blog/h:entry[$cat=h:ul/h:li[@class='primaryCategory'] and not(@id = current()/@id) and contains(h:ul/h:li[@class='keywords'],'_show_me_')]">
                                    <li>
                                        <a href="/howto/index.html?id={@id}"><xsl:value-of select="h:ul/h:li[@class='title']"/></a>
                                    </li>
                                </xsl:for-each>
                                <li class="moreItem"><a href="/howto/index.html?category={$more-category}">More</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="leftColumn" class="yui-b">
            <xsl:choose>
                <xsl:when test="string-length($root-category-string) = 0">
                    <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                        href="cocoon:/services/leftmenu_services.html">
                        <xi:fallback/>
                    </xi:include>
                </xsl:when>
                <xsl:otherwise>
                    <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                        href="cocoon:/services/{$root-category-string}/leftmenu_{$root-category-string}.html">
                        <xi:fallback/>
                    </xi:include>
                </xsl:otherwise>
            </xsl:choose>
            </div>
    </xsl:template>
    
    <xsl:function name="lw:inline" as="xs:boolean">
        <xsl:param name="node" as="node()"/>
        <xsl:sequence select="($node instance of text() and string-length(normalize-space($node)) > 0) or
            $node[self::h:u|self::h:b|self::h:i|self::h:strong|self::h:span|self::h:em
            |self::h:br|self::h:a]"/>
    </xsl:function>
    
</xsl:stylesheet>
