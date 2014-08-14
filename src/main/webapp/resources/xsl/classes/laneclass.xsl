<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc h"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl"/>

    <xsl:param name="class-id"/>

    <xsl:variable name="internal-id">
        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:internal_id/text()"/>
    </xsl:variable>

    <xsl:variable name="hasOpenClass" select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/../lc:event_status/text() = 'O'"/>

    <xsl:template match="doc">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>

    <xsl:template match="h:body">
        <xsl:copy>
            <xsl:if test="$hasOpenClass">
                <xsl:attribute name="itemscope"/>
                <xsl:attribute name="itemtype">http://schema.org/Event</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:h3[@id='class-title']">
        <xsl:copy>
            <xsl:if test="$hasOpenClass">
                <xsl:attribute name="itemprop">name</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="attribute::node()|child::node()" />
            <xsl:value-of
                select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_name/text()" />
        </xsl:copy>
    </xsl:template>

	<xsl:template match="h:p[@id='description']">
	    <div>
            <xsl:if test="$hasOpenClass">
                <xsl:attribute name="itemprop">name</xsl:attribute>
            </xsl:if>
	        <xsl:apply-templates />
	        <xsl:copy-of
	            select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_description/child::node()" />
        </div>
    </xsl:template>

    <xsl:template match="h:p[@id='registration']">
        <xsl:for-each select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/..[lc:event_status/text() = 'O']">
            <xsl:variable name="classId" select="./lc:module_id/text()"/>
            <!--  only include microdata on first open class -->
            <xsl:variable name="needsMicrodata" select="position() = 1"/>
            <div style="margin-top:12px">
            <div>
                <xsl:call-template name="month"/>
                <xsl:text> </xsl:text>
                <xsl:call-template name="day"/>
                <xsl:text>, </xsl:text>
                <time>
                    <xsl:if test="$needsMicrodata">
                        <xsl:attribute name="itemprop">startDate</xsl:attribute>
                        <xsl:attribute name="datetime">
                            <xsl:value-of select="./lc:event_dates/lc:start_date[1]/text()"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:call-template name="start-time"/>
                </time>
                <xsl:text>-</xsl:text>
                <time>
                    <xsl:if test="$needsMicrodata">
                        <xsl:attribute name="itemprop">endDate</xsl:attribute>
                        <xsl:attribute name="datetime">
                            <xsl:value-of select="./lc:event_dates/lc:end_date[1]/text()"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:call-template name="end-time"/>
                </time>
            </div>
            <div>
                <xsl:if test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() != '---'">
                    <xsl:text>Seats left: </xsl:text>
                    <b>
                        <xsl:value-of select="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text()"/>
                    </b>
                </xsl:if>
            </div>
            <div>
                <xsl:text>With </xsl:text>
                <xsl:choose>
                    <xsl:when test="./lc:more_info_url/text() != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="./lc:more_info_url/text()"/>
                            </xsl:attribute>
                            <xsl:choose>
                                <xsl:when test="./lc:speaker/text() != ''">
                                    <xsl:value-of select="./lc:speaker/text()"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="./lc:event_instructors/lc:instructor/lc:fname/text()"/>
                                    <xsl:text>&#160;</xsl:text>
                                    <xsl:value-of select="./lc:event_instructors/lc:instructor/lc:lname/text()"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="./lc:speaker/text() != ''">
                                <xsl:value-of select="./lc:speaker/text()"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="./lc:event_instructors/lc:instructor/lc:fname/text()"/>
                                <xsl:text>&#160;</xsl:text>
                                <xsl:value-of select="./lc:event_instructors/lc:instructor/lc:lname/text()"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <div>
                <xsl:text>At </xsl:text>
                <xsl:variable name="link">
                    <xsl:value-of select="./lc:venue/lc:venue_website"/>
                </xsl:variable>
                <xsl:variable name="name">
                    <span>
                        <xsl:if test="$needsMicrodata">
                            <xsl:attribute name="itemprop">location</xsl:attribute>
                        </xsl:if>
                        <xsl:value-of select="./lc:venue/lc:venue_name"/>
                    </span>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="./lc:venue/lc:venue_website/text()"/>
                            </xsl:attribute>
                            <xsl:copy-of select="$name"/>
                        </a>
                        <xsl:if test="ends-with($link, '.pdf')">
                            <xsl:text> (.pdf)</xsl:text>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$name"/>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <a class="button alt1" href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={lc:module_id}">
                <xsl:choose>
                    <xsl:when test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() = '---'">
                        <span>Waitlist</span>
                    </xsl:when>
                    <xsl:otherwise>
                        <span>Register</span>
                    </xsl:otherwise>
                </xsl:choose>
                <i class="icon fa fa-arrow-right"></i>
            </a>
            </div>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

</xsl:stylesheet>
