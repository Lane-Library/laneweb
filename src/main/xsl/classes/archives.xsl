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
                                	<xsl:sort select="concat(replace(./lc:event_dates/lc:start_date[1]/text(),'.*/(\d{4}) .*','$1'),replace(./lc:event_dates/lc:start_date[1]/text(),'^(\d{1,2})/.*','$1'))" data-type="text"   order="descending" />
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
            <div class="pure-g">
                <div class="pure-u-1-8">
                    <div class="date">
                        <xsl:apply-templates select="lc:event_dates/lc:start_date[1]" /> 
                   </div>
                </div>
                <div class="pure-u-7-12">
                    <div class="details">
                        <h4>
                            <a href="/classes-consult/archive.html?class-id={lc:module_id}">
                                <xsl:value-of select="./lc:event_name" />
                            </a>
                        </h4>
                        <div>
                            <xsl:apply-templates select="./lc:event_description" />
                        </div>
                    </div>
                </div>
                <xsl:apply-templates select="lc:more_info_url[string-length() &gt; 0]"/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="lc:more_info_url">
        <xsl:variable name="schemeless-url">
            <xsl:choose>
                <xsl:when test="contains(.,'://')">
                    <xsl:value-of select="substring-after(., ':')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <div class="pure-u-7-24">
            <div class="youtube-class">
                <xsl:choose>
                    <xsl:when test="contains($schemeless-url, 'youtube')">
                        <iframe src="{$schemeless-url}" width="200" height="120" frameborder="0" allowfullscreen="true"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <a class="button" href="{$schemeless-url}">
                            <span><i class="icon fa fa-video-camera"/> Watch Video</span>
                            <i class="icon fa fa-arrow-right"/>
                        </a>
                        <div>SUNet ID required</div>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
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
