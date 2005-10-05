<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:java="http://xml.apache.org/xalan/java/edu.stanford.laneweb.xslt.LanewebXSLTExtensions"
    exclude-result-prefixes="h java"
    version="1.0">
    
    <xsl:param name="id"/>
    <xsl:param name="category"/>
    <xsl:param name="keywords"/>
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="h:title"/>
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
        <xsl:if test="$category = '' and $keywords = '' and $id = ''">
            <h2>Hot Topics</h2>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="$id=''">
                <dl id="faq">
                    <xsl:choose>
                        <xsl:when test="$keywords != '' and $category != ''">
                            <xsl:apply-templates select="h:li[h:ul/h:li[@class='categories']/h:ul/h:li/text()=$category and java:contains(self::h:li,$keywords)]" mode="dl"/>
                        </xsl:when>
                        <xsl:when test="$keywords != ''">
                                <xsl:apply-templates select="h:li[java:contains(self::h:li,$keywords)]" mode="dl"/>
                        </xsl:when>
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
        <div id="lw:sidebar">
            <div>
                <h4>faq categories</h4>
                <ul>
                    <xsl:apply-templates select="//h:li[@class='category'][not(following::h:li/text() = text())]">
                        <xsl:sort select="."/>
                    </xsl:apply-templates>
                </ul>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:li">
        <li><a href="/howto/index.html?category={text()}"><xsl:value-of select="text()"/></a></li>
    </xsl:template>
    
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
        <h2>
            <xsl:value-of select="text()"/>
        </h2>
        <xsl:copy-of select="h:ul/h:li[@class='body']/node()"/>
    </xsl:template>
    
    
</xsl:stylesheet>
