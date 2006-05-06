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
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:title">
        <xsl:copy>Frequently Asked Questions</xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:ul">
        <xsl:if test="$category != ''">
            <h2>
                <xsl:value-of select="$category"/>
            </h2>
        </xsl:if>
        <xsl:if test="$category = '' and $id = ''">
            <h2>Hot Topics</h2>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="$id=''">
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
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="h:li[@id=$id]" mode="full"/>
            </xsl:otherwise>
        </xsl:choose>
<!--
            <div class="longSideBlueBox">
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
        <div><xsl:for-each select="/h:html/h:body/h:div[@id='categories']//h:li[descendant::h:li/text() = $primary-category]">
            <a href="/services/index.html"><xsl:value-of select="text()"/></a><xsl:text> &gt; </xsl:text>
        </xsl:for-each>
        <xsl:value-of select="$primary-category"/></div>
            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                <tr>
                    <td valign="top" align="left" id="leftColumn">
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                                href="cocoon:/services/{$root-category-string}/leftmenu_{$root-category-string}.html#xmlns(h=http://www.w3.org/1999/xhtml)xpointer(/h:html/h:body/*)">
                                <xi:fallback/>
                            </xi:include>
                    </td>
                    <td valign="top" align="left" class="centralColumn">
                        <h1>
                            <xsl:value-of select="text()"/>
                        </h1>
                        <xsl:copy-of select="h:ul/h:li[@class='body']/node()"/></td>
                    <td valign="top" align="right" id="rightColumn">
                        <div class="sideTanBox">
                            <h2>FAQs on this topic</h2>
                            <xsl:variable name="cat" select="h:ul/h:li[@class='primaryCategory']"/>
                            <ul>
                                <xsl:for-each select="parent::h:ul/h:li[$cat=h:ul/h:li[@class='primaryCategory'] and not(@id = current()/@id)]">
                                    <li>
                                        <a href="/howto/index.html?id={@id}"><xsl:value-of select="text()"/></a>
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
    </xsl:template>
    
    
</xsl:stylesheet>
