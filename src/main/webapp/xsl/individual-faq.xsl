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
    
    <xsl:template match="h:dl">
        <xsl:copy>
            <xsl:apply-templates select="h:dt">
                <xsl:sort select="upper-case(.)"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:dt">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
        <xsl:apply-templates select="following-sibling::h:dd[1]"/>
    </xsl:template>
    
    <xsl:template match="h:div[@id='rightColumn']//xi:include/attribute::href">
        <xsl:attribute name="href">
            <xsl:value-of select="."/>
            <xsl:value-of select="lower-case(replace(normalize-space(replace($primary-category,'[^\w\s]',' ')),' ','_'))"/>
        </xsl:attribute>
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
