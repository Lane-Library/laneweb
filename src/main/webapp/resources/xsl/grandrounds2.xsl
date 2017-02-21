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
                    <xsl:apply-templates select="*[not(self::date)]"/>
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
            <a href="{parent::presentation/link[1]/uri}">
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
            <em>No SUNet ID required</em>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="presenter">
        <p>
            <xsl:for-each select="tokenize(.,';')">
                <xsl:value-of select="."/><br/>
            </xsl:for-each>
        </p>
    </xsl:template>

    <xsl:template match="description">
        <p>
            <xsl:value-of select="."/>
        </p>
    </xsl:template>
    
    <xsl:template match="link[1]"/>
    
    <xsl:template match="link[not(1)]">
        <xsl:apply-templates select="uri"/>
    </xsl:template>
    
    <xsl:template match="uri[not(contains(.,'youtu'))]">
        <div>Also available: <a href="{.}"><xsl:value-of select="../text"/></a></div>
    </xsl:template>

    <xsl:template match="uri[contains(.,'youtu')]">
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
    
    <xsl:template match="text"/>

</xsl:stylesheet>
