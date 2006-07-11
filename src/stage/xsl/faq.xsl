<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h"
    version="1.0">
    
    <xsl:param name="id"/>
    <xsl:param name="category"/>
    
    <xsl:variable name="category-map" select="/h:html/h:body/h:div[@id='category-map']"/>
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="h:title"/>
            <xsl:if test="$id != ''">
                <meta name="lw_faqCategory" content="{../h:body/h:ul/h:li[@id=$id]/h:ul/h:li[@class='primaryCategory']}"/>
            </xsl:if>
            <xsl:if test="$category != ''">
                <meta name="lw_faqCategory" content="{$category}"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:title">
        <xsl:copy>Frequently Asked Questions</xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:ul">
        <xsl:choose>
            <xsl:when test="$id=''">
                <xsl:variable name="cat">
                    <xsl:choose>
                        <xsl:when test="$category = ''">Hot Topics</xsl:when>
                        <xsl:otherwise><xsl:value-of select="$category"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:variable name="root-category" select="/h:html/h:body/h:div[@id='categories']/h:ul/h:li[descendant-or-self::h:li/text() = $cat]/text()"/>
                <xsl:variable name="root-category-string" select="$category-map/h:div[h:span=$root-category]/h:span[2]"/>
                <table cellspacing="0" cellpadding="0" border="0" width="100%">
                    <tr>
                        <td valign="top" align="left" id="leftColumn">
<xsl:choose>
<xsl:when test="string-length($root-category-string) = 0">
<xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
href="cocoon:/services/leftmenu_services.html#xmlns(h=http://www.w3.org/1999/xhtml)xpointer(/h:html/h:body/*)">
<xi:fallback/>
</xi:include>
</xsl:when>
<xsl:otherwise>
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                                href="cocoon:/services/{$root-category-string}/leftmenu_{$root-category-string}.html#xmlns(h=http://www.w3.org/1999/xhtml)xpointer(/h:html/h:body/*)">
                                <xi:fallback/>
                            </xi:include>
</xsl:otherwise>
</xsl:choose>
                        </td>
                        <td valign="top" align="left" class="centralColumn">
                            
                            <xsl:if test="$category != ''">
                                <h1>
                                    <xsl:value-of select="$category"/> FAQs
                                </h1>
                            </xsl:if>
                            <xsl:if test="$category = '' and $id = ''">
                                <h1>Hot Topics</h1>
                            </xsl:if>
                            <dl id="faq">
                                <xsl:choose>
                                    <xsl:when test="$category=''">
                                        <xsl:apply-templates 
                                            select="h:li[h:ul/h:li[@class='categories']/h:ul/h:li/text() = 'Hot Topics']" mode="dl"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:apply-templates
                                            select="h:li[h:ul/h:li[@class='categories']/h:ul/h:li/text() = $category]" mode="dl"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </dl>
                        </td>
                    </tr>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="h:li[@id=$id]" mode="full"/>
            </xsl:otherwise>
        </xsl:choose>
<!--
            <div class="dSideBox">
                <h2>faq categories</h2>
                <ul>
                    <xsl:apply-templates select="//h:li[@class='category'][not(following::h:li/text() = text())]">
                        <xsl:sort select="."/>
                    </xsl:apply-templates>
                </ul>
            </div>
-->
    </xsl:template>
    
    <!--<xsl:template match="h:li">
        <li><a href="/howto/index.html?category={text()}"><xsl:value-of select="text()"/></a></li>
    </xsl:template>-->
    
    <xsl:template match="h:div[@id='categories' or @id='category-map']"/>
    
    <xsl:template match="h:li[@class='faq']" mode="dl">
        <dt>
            <a href="/howto/index.html?id={@id}">
                <xsl:value-of select="text()"/>
            </a>
        </dt>
        <dd>
            <xsl:value-of select="h:ul/h:li[@class='excerpt']"/>
        </dd>
    </xsl:template>
    
    <xsl:template match="h:li[@class='faq']" mode="full">
        <xsl:variable name="primary-category" select="/h:html/h:body/h:ul/h:li[@id=$id]/h:ul/h:li[@class='primaryCategory']"/>
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
        </xsl:variable>
            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                <tr>
                    <td valign="top" align="left" id="leftColumn">
<xsl:choose>
<xsl:when test="string-length($root-category-string) = 0">
<xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
href="cocoon:/services/leftmenu_services.html#xmlns(h=http://www.w3.org/1999/xhtml)xpointer(/h:html/h:body/*)">
<xi:fallback/>
</xi:include>
</xsl:when>
<xsl:otherwise>
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                                href="cocoon:/services/{$root-category-string}/leftmenu_{$root-category-string}.html#xmlns(h=http://www.w3.org/1999/xhtml)xpointer(/h:html/h:body/*)">
                                <xi:fallback/>
                            </xi:include>
</xsl:otherwise>
</xsl:choose>
                    </td>
                    <td valign="top" align="left" class="mainColumn">
                        <h1>
                            <xsl:value-of select="text()"/>
                        </h1>
                        <xsl:copy-of select="h:ul/h:li[@class='body']/node()"/></td>
                    <td valign="top" align="right" id="rightColumn">
                        <div class="bSideBox">
                            <h2>FAQs on this topic</h2>
                            <xsl:variable name="cat" select="h:ul/h:li[@class='primaryCategory']"/>
                            <ul>
                                <xsl:for-each select="parent::h:ul/h:li[$cat=h:ul/h:li[@class='primaryCategory'] and not(@id = current()/@id) and contains(h:ul/h:li[@class='keywords'],'_show_me_')]">
                                    <li>
                                        <a href="/howto/index.html?id={@id}"><xsl:value-of select="text()"/></a>
                                    </li>
                                </xsl:for-each>
                                <li class="moreItem"><a href="/howto/index.html?category={$more-category}">More</a></li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
    </xsl:template>
    
    
</xsl:stylesheet>
