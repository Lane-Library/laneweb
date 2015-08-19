<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="2.0">

    <xsl:param name="year"/>

    <xsl:param name="department"/>

    <xsl:variable name="display-department">
        <xsl:choose>
            <xsl:when test="$department = 'medicine'">Medicine</xsl:when>
            <xsl:when test="$department = 'pediatric'">Pediatric</xsl:when>
            <xsl:when test="$department = 'emergency'">Emergency</xsl:when>
            <xsl:when test="$department = 'otolaryngology'">Otolaryngology</xsl:when>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="title" select="concat($year, ' ', $display-department, ' Grand Rounds')"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:value-of select="$title"/>
                </title>
            </head>
            <body>
                <xsl:apply-templates select="grandrounds/presentation"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="presentation">
        <div class="class">
            <div class="yui3-g">
                <div class="yui3-u-1-5">
                    <div class="date">
                        <xsl:apply-templates select="date"/>
                    </div>
                </div>
                <div class="yui3-u-4-5">
                    <div class="details">
                        <xsl:apply-templates select="*[not(self::date)]"/>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="date">
        <xsl:variable name="tokenized" select="tokenize(., ' ')"/>
        <div class="month-day">
            <xsl:value-of select="concat($tokenized[1], ' ', $tokenized[2])"/>
        </div>
        <div class="year">
            <xsl:value-of select="$tokenized[3]"/>
        </div>
    </xsl:template>

    <xsl:template match="presentation/title">
       <!-- <xsl:variable name="id" select="parent::presentation/@id"/>-->
        <h3>
            <a href="{parent::presentation/video[1]/uri}">
                <xsl:apply-templates/>
            </a>
            <!--<a style="font-size:9px" rel="popup standard"
                href="http://lane-preprod.stanford.edu/catalog/marc.html?db=lmldb&amp;type=bib&amp;id={$id}">
                <xsl:value-of select="$id"/>
            </a>-->
        </h3>
    </xsl:template>
    
    <xsl:template match="sunet">
        <xsl:if test=".='false'">
            <em>No SUNet required</em>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="presenter">
        <p>
            <xsl:for-each select="tokenize(name,';')">
                <xsl:value-of select="."/><br/>
            </xsl:for-each>
        </p>
    </xsl:template>

    <xsl:template match="presenter[@idref]">
        <xsl:call-template name="presenter-ref">
            <xsl:with-param name="year" select="number('2015')"/>
            <xsl:with-param name="presenter" select="/grandrounds/presenter[@id = current()/@idref]"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="presenter[uri]/name">
        <a href="{../uri}">
            <xsl:value-of select="."/>
        </a>
        <xsl:if test="../@id">
            <a style="font-size:9px" rel="popup standard"
                href="http://lane-preprod.stanford.edu/catalog/marc.html?db=lmldb&amp;type=auth&amp;id={../@id}">
                <xsl:value-of select="../@id"/>
            </a>
        </xsl:if>
    </xsl:template>

    <xsl:template match="name">
        <xsl:value-of select="."/>
        <xsl:if test="../@id">
            <a style="font-size:9px" rel="popup standard"
                href="http://lane-preprod.stanford.edu/catalog/marc.html?db=lmldb&amp;type=auth&amp;id={../@id}">
                <xsl:value-of select="../@id"/>
            </a>
        </xsl:if>
    </xsl:template>

    <xsl:template name="presenter-ref">
        <xsl:param name="year"/>
        <xsl:param name="presenter"/>
        <p>
            <xsl:apply-templates select="$presenter/name | $presenter/affiliation[number(start) &lt;= $year and number(end) &gt;= $year]"/>
        </p>
    </xsl:template>


    <xsl:template match="affiliation/title | affiliation/name">
        <br/>
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="start | end"/>

    <xsl:template match="description">
        <p>
            <xsl:value-of select="."/>
        </p>
    </xsl:template>

    <xsl:template match="video">
        <xsl:apply-templates select="uri[contains(.,'youtu')]"/>
    </xsl:template>

    <xsl:template match="video/uri">
        <xsl:variable name="youtube-id">
            <xsl:choose>
                <xsl:when test="contains(.,'/embed/')">
                <xsl:value-of select="substring-after(.,'/embed/')"/>
                </xsl:when>
                <xsl:when test="contains(.,'v=')">
                    <xsl:choose>
                        <xsl:when test="contains(substring-after(.,'v='), '&amp;')">
                            <xsl:value-of select="substring-before(substring-after(.,'v='), '&amp;')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring-after(.,'v=')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="contains(.,'/youtu.be/')">
                    <xsl:value-of select="substring-after(.,'/youtu.be/')"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <iframe src="https://www.youtube.com/embed/{$youtube-id}" width="322" height="181" frameborder="0" scrolling="no"
            webkitAllowFullScreen="true" mozallowfullscreen="true" allowFullScreen="true"/>
    </xsl:template>

</xsl:stylesheet>
