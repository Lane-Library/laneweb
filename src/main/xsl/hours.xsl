<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" xmlns:lh="http://lane.stanford.edu/hours/1.0"
    xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h lh"
    version="2.0">

    <xsl:param name="mode" />

    <xsl:template match="/lh:calendars">
        <xsl:apply-templates select="lh:calendar" />
    </xsl:template>

    <xsl:template match="lh:calendar">
        <xsl:choose>
            <xsl:when test="$mode = 'brief'">
                <xsl:if test="@title">
                    <h4>
                        <xsl:value-of select="@title" />
                    </h4>
                </xsl:if>
                <ul>
                    <xsl:apply-templates select="lh:day" />
                </ul>
            </xsl:when>
            <xsl:when test="$mode = 'mobile'">
                <dl>
                    <xsl:if test="count(../lh:calendar) &gt;1 and @title">
                        <div class="calTitle">
                            <xsl:value-of select="@title" />
                        </div>
                    </xsl:if>
                    <xsl:apply-templates select="lh:day" />
                </dl>
            </xsl:when>
            <xsl:when test="$mode = 'full'">
                <xsl:if test="@title">
                    <h4 class="calTitle alternate plain">
                        <xsl:value-of select="@title" />
                    </h4>
                </xsl:if>
                <div class="calendar">
                    <ul>
                        <xsl:for-each select="lh:day">
                            <li>
                                <xsl:value-of select="upper-case(substring(lh:label,1,3))" />
                                <xsl:if test="lh:date">
                                    <div>
                                        <xsl:value-of select="lh:date" />
                                    </div>
                                </xsl:if>
                                <p>
                                    <xsl:value-of select="lh:description" />
                                </p>
                            </li>
                        </xsl:for-each>
                    </ul>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!-- day formatting for brief and mobile; full doesn't get merged -->
    <xsl:template match="lh:day">
        <xsl:variable name="next" select="position()+1" />
        <xsl:variable name="prev" select="position()-1" />
        <xsl:choose>
            <xsl:when
                test="lh:description/text() = ../lh:day[$prev]/lh:description/text()" />
            <xsl:when
                test="lh:description/text() = ../lh:day[$next]/lh:description/text()">
                <xsl:call-template name="merge-days">
                    <xsl:with-param name="days" select="../lh:day" />
                    <xsl:with-param name="this" select="position()" />
                    <xsl:with-param name="next" select="$next" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$mode = 'brief'">
                        <li>
                            <xsl:value-of select="lh:label" />
                            <xsl:if test="lh:date">
                                <xsl:text>, </xsl:text>
                                <xsl:value-of select="lh:date" />
                            </xsl:if>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="lh:description" />
                        </li>
                    </xsl:when>
                    <xsl:when test="$mode = 'mobile'">
                        <dt>
                            <xsl:choose>
                                <xsl:when test="lh:date">
                                    <xsl:value-of select="lh:date" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="lh:label" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </dt>
                        <dd>
                            <xsl:value-of select="replace(lh:description,':00 ?','')" />
                        </dd>
                    </xsl:when>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="merge-days">
        <xsl:param name="days" />
        <xsl:param name="this" />
        <xsl:param name="next" />
        <xsl:choose>
            <xsl:when
                test="$days[position() = $this]/lh:description/text() != $days[position() = $next]/lh:description/text()">
                <xsl:call-template name="merged-output">
                    <xsl:with-param name="start-day" select="$days[position() = $this]" />
                    <xsl:with-param name="end-day" select="$days[position() = $next - 1]" />
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$next = count($days)">
                <xsl:call-template name="merged-output">
                    <xsl:with-param name="start-day" select="$days[position() = $this]" />
                    <xsl:with-param name="end-day" select="$days[position() = $next]" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="merge-days">
                    <xsl:with-param name="days" select="$days" />
                    <xsl:with-param name="this" select="$this" />
                    <xsl:with-param name="next" select="$next + 1" />
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="merged-output">
        <xsl:param name="start-day" />
        <xsl:param name="end-day" />
        <xsl:choose>
            <xsl:when test="$mode = 'brief'">
                <li>
                    <xsl:value-of select="$start-day/lh:label" />
                    <xsl:if test="$start-day/lh:date">
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="$start-day/lh:date" />
                    </xsl:if>
                    <xsl:text> &#8211; </xsl:text>
                    <xsl:value-of select="$end-day/lh:label" />
                    <xsl:if test="$end-day/lh:date">
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="$end-day/lh:date" />
                    </xsl:if>
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="$end-day/lh:description" />
                </li>
            </xsl:when>
            <xsl:when test="$mode = 'mobile'">
                <dt>
                    <xsl:choose>
                        <xsl:when test="$start-day/lh:date">
                            <xsl:value-of select="$start-day/lh:date" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring($start-day/lh:label,1,3)" />
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:text>&#8211;</xsl:text>
                    <xsl:choose>
                        <xsl:when test="$end-day/lh:date">
                            <xsl:value-of select="$end-day/lh:date" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring($end-day/lh:label,1,3)" />
                        </xsl:otherwise>
                    </xsl:choose>
                </dt>
                <dd>
                    <xsl:value-of select="replace($end-day/lh:description,':00 ?','')" />
                </dd>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>