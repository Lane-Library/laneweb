<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl" />

    <xsl:param name="id" />

    <xsl:param name="sort" />

    <xsl:template match="/lc:classes">
        <html>
            <body>
                <xsl:choose>
                    <xsl:when test="$id = ''">
                        <xsl:choose>
                            <xsl:when test="$sort = 'date'">
                                <xsl:apply-templates select="lc:event_data">
                                    <xsl:sort select="replace(./lc:event_dates/lc:start_date[1]/text(),'.*/(\d{4}) .*','$1')" data-type="text"
                                        order="descending" />
                                </xsl:apply-templates>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="lc:event_data">
                                    <xsl:sort select="./lc:event_name" />
                                </xsl:apply-templates>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates
                            select="lc:event_data[ contains(string-join(./lc:module_categories/lc:category/lc:cat_name/text(), '' ), $id)]">
                            <xsl:sort select="./lc:event_name" />
                        </xsl:apply-templates>
                    </xsl:otherwise>
                </xsl:choose>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="lc:event_description">
        <xsl:choose>
            <xsl:when test="count(tokenize(., '\W+')[. != ''])  &gt; $description-length">
                <xsl:call-template name="firstWords">
                    <xsl:with-param name="value" select="."/>
                    <xsl:with-param name="count" select="$description-length"/>
                </xsl:call-template>
                <xsl:text>...</xsl:text>
                    <a href="/classes-consult/archive.html?class-id={../lc:module_id}"> More <i class="fa fa-arrow-right"/></a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="lc:event_data">
        <div class="archive">
            <div class="yui3-g">
                <div class="yui3-u-1-8">
                    <div class="date">
                        <xsl:apply-templates select="lc:event_dates/lc:start_date[1]" />
                    </div>
                </div>
                <div class="yui3-u-7-12">
                    <div class="details">
                        <h4>
                            <a>
                                <xsl:attribute name="href">
                                <xsl:text>/classes-consult/archive.html?class-id=</xsl:text>
                                <b>  <xsl:value-of
                                    select="lc:module_id/text()" /></b>
                            </xsl:attribute>
                                <xsl:value-of select="./lc:event_name" />
                            </a>
                        </h4>
                        <div>
                            <xsl:apply-templates select="./lc:event_description" />
                        </div>
                    </div>
                </div>
                <div class="yui3-u-7-24">
                    <xsl:call-template name="video" />
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template name="video">
        <div class="youtube-class">
            <xsl:if test="./lc:more_info_url/text() != ''">
                <xsl:if test="contains( ./lc:more_info_url/text() ,  'youtube')">
                    <xsl:call-template name="youtube" />
                </xsl:if>
                <xsl:if test="not( contains( ./lc:more_info_url/text() ,  'youtube'))">
                    <xsl:call-template name="not_youtube" />
                </xsl:if>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template name="not_youtube">
        <a>
            <xsl:attribute name="class">
                     <xsl:text>button</xsl:text>                     
             </xsl:attribute>
            <xsl:attribute name="href">
                     <xsl:value-of select="./lc:more_info_url/text()" />
             </xsl:attribute>
            <span>
                <i class="icon fa fa-video-camera" />
                Watch Video
            </span>
            <i class="icon fa fa-arrow-right" />
        </a>
        <div>SUNet ID required</div>
    </xsl:template>

    <xsl:template name="youtube">
        <div>
        <iframe>
            <xsl:attribute name="src" select="./lc:more_info_url/text()" />
            <xsl:attribute name="rameborder">0</xsl:attribute>
            <xsl:attribute name="allowfullscreen"> </xsl:attribute>
            <xsl:attribute name="frameborder">0</xsl:attribute>
            <xsl:attribute name="allowfullscreen"> </xsl:attribute>
            <xsl:attribute name="width">200</xsl:attribute>
            <xsl:attribute name="height">120</xsl:attribute>
        </iframe>
        </div>
    </xsl:template>

    <xsl:template name="year">
        <xsl:value-of select="replace(./lc:event_dates/lc:start_date/text(),'.*(\d{4}).*','$1')" />
    </xsl:template>

    <xsl:template match="lc:start_date">
        <xsl:variable name="date-tokens" select="tokenize(.,'(/| )')"/>
        <xsl:variable name="month">
            <xsl:value-of select="number($date-tokens[1])"/>
        </xsl:variable>
        <div class="month-day">
            <xsl:choose>
                <xsl:when test="$month = 1">January </xsl:when>
                <xsl:when test="$month = 2">February </xsl:when>
                <xsl:when test="$month = 3">March </xsl:when>
                <xsl:when test="$month = 4">April </xsl:when>
                <xsl:when test="$month = 5">May </xsl:when>
                <xsl:when test="$month = 6">June </xsl:when>
                <xsl:when test="$month = 7">July </xsl:when>
                <xsl:when test="$month = 8">August </xsl:when>
                <xsl:when test="$month = 9">September </xsl:when>
                <xsl:when test="$month = 10">October </xsl:when>
                <xsl:when test="$month = 11">November </xsl:when>
                <xsl:when test="$month = 12">December </xsl:when>
            </xsl:choose>            
        </div>
        <div class="year">
            <xsl:value-of select="$date-tokens[3]"/>
        </div>
    </xsl:template>    

</xsl:stylesheet>
