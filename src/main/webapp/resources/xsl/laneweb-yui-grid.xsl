<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <!-- the following several templates add class to yui grid divs for custom widths -->
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']/h:div[@class='yui-gf']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:body/h:div[@class='yui-gf']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']/h:div[@class='yui-gf']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' middleColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' rightColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftGrids')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:body/h:div[@class='yui-gf']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' rightGrids')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
