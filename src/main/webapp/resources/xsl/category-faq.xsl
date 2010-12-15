<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:lw="http://irt.stanford.edu/laneweb" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xi="http://www.w3.org/2001/XInclude" exclude-result-prefixes="h xs lw xi" version="2.0">

    <xsl:output method="xml"/>

    <xsl:variable name="primary-category"
        select="/h:html/h:head/h:meta[attribute::name='LW.faqCategory']/attribute::content"/>

    <xsl:variable name="root-category">
        <xsl:choose>
            <xsl:when test="/h:html/h:head/h:meta[attribute::name='root-category']">
                <xsl:value-of
                    select="/h:html/h:head/h:meta[attribute::name='root-category']/attribute::content"
                />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$primary-category"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

    <xsl:variable name="category-map">
        <category name="Worshops &amp; Training" label="workshops"/>
        <category name="Video Services" label="video"/>
        <category name="Classrooms &amp; Study Spaces" label="spaces"/>
        <category name="Research &amp; Publishing" label="research"/>
        <category name="Books &amp; Collections" label="collections"/>
        <category name="Public Computing" label="computing"/>
        <category name="Teaching Support" label="teaching"/>
        <category name="Policies" label="policies"/>
        <category name="Library Access" label="access"/>
    </xsl:variable>
    
    <!-- don't pass on meta elements only required here -->
    <xsl:template match="h:meta[not(attribute::name='LW.faqCategory')]"/>
    
    <xsl:template match="h:h1|h:h2|h:div[@class='extra']"/>
    
    <xsl:template match="h:body">
        <xsl:copy>
            <h2><a href="/help/index.html">Help</a> &#xBB; <xsl:value-of select="descendant::h:h1"/></h2>
            <div class="yui-ge">
                <div class="yui-u first">
                    <div class="yui-gf">
                        <div class="yui-u">
                            <xsl:apply-templates select="descendant::h:div[@id='mainColumn']"/>
                        </div>
                        <div class="yui-u first">
                            <xi:include xmlns:xi="http://www.w3.org/2001/XInclude" href="content:/includes/leftmenu-help.html">
                                <xi:fallback></xi:fallback>
                            </xi:include>
                        </div>
                    </div>
                </div>
                <div class="yui-u">
                    <xsl:apply-templates select="descendant::h:div[@id='rightColumn']/h:div"/>
                </div>
            </div>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:div[@id='mainColumn']">
        <div class="module">
            <h3>&#160;</h3>
            <div class="bd">
                <xsl:apply-templates/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:div[@id='rightColumn']/h:div">
        <div class="module">
            <h3><xsl:value-of select="h:h2"/></h3>
            <div class="bd">
                <xsl:apply-templates/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:dl">
        <ul>
            <xsl:apply-templates select="h:dt">
                <xsl:sort select="upper-case(.)"/>
            </xsl:apply-templates>
        </ul>
    </xsl:template>
    
    <xsl:template match="h:dt">
        <li>
            <xsl:apply-templates select="h:a"/>
        </li>
    </xsl:template>
    

</xsl:stylesheet>
