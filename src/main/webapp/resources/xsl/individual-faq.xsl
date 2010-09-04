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

    <xsl:template match="h:div[@id='leftColumn']//xi:include/attribute::href">
        <xsl:variable name="root-category-string"
            select="$category-map/child::node()[attribute::name = $root-category]/attribute::label"/>
        <xsl:attribute name="href">
            <xsl:choose>
                <xsl:when test="$root-category-string">
                    <xsl:value-of
                        select="concat('cocoon:/services/',$root-category-string,'/leftmenu_',$root-category-string,'.html')"
                    />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:div[@id='rightColumn']//xi:include/attribute::href">
        <xsl:attribute name="href">
            <xsl:value-of select="."/>
            <xsl:value-of select="lower-case(replace(normalize-space(replace($primary-category,'[^\w\s]',' ')),' ','_'))"/>
        </xsl:attribute>
        </xsl:template>
    
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
                            <xsl:apply-templates select="descendant::h:div[@id='leftColumn']"/>
                        </div>
                    </div>
                </div>
                <div class="yui-u">
                    <xsl:apply-templates select="descendant::h:div[@id='rightColumn']/h:div"/>
                </div>
            </div>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:h1|h:div[@class='extra']|h:script[@src='http://medblog.stanford.edu/lane-faq/archives/diggthis.js']"/>
    
    <xsl:template match="h:h2">
        <h3 class="alt">
            <xsl:apply-templates/>
        </h3>        
    </xsl:template>
    
    <xsl:template match="h:div[@id='mainColumn']">
        <div class="module">
            <h3 class="alt">&#160;</h3>
            <div class="bd">
                <xsl:apply-templates/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="h:div[@id='leftColumn']">
        <xi:include xmlns:xi="http://www.w3.org/2001/XInclude" href="cocoon:/includes/leftmenu-help.html">
            <xi:fallback></xi:fallback>
        </xi:include>
    </xsl:template>
    
    <xsl:template match="h:div[@id='rightColumn']/h:div">
        <div class="module">
            <h3><xsl:value-of select="h:h2"/></h3>
            <div class="bd">
                <xsl:apply-templates select="node()[name()!='h2']"/>
            </div>
        </div>
    </xsl:template>

    <!--<xsl:template match="h:div[@id='mainColumn']">
        <xsl:for-each-group select="child::node()" group-adjacent="lw:inline(.)">
            <xsl:choose>
                <xsl:when test="current-grouping-key()">
                    <p>
                        <xsl:apply-templates select="current-group()"/>
                    </p>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="current-group()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each-group>
    </xsl:template>


    <xsl:function name="lw:inline" as="xs:boolean">
        <xsl:param name="node" as="node()"/>
        <xsl:sequence
            select="($node instance of text() and string-length(normalize-space($node)) > 0) or
            $node[self::h:u|self::h:b|self::h:i|self::h:strong|self::h:span|self::h:em
            |self::h:br|self::h:a]"
        />
    </xsl:function>-->

</xsl:stylesheet>
