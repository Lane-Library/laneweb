<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" xmlns:lh="http://lane.stanford.edu/hours/1.0"
    xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h lh"
    version="2.0">

    <xsl:param name="mode" />

    <xsl:template match="/lh:calendars">
	    <xsl:choose>
	        <xsl:when test="$mode = 'brief'">
	         <xsl:apply-templates select="lh:calendar" mode="brief">
	             <xsl:with-param name="headers">true</xsl:with-param>
	         </xsl:apply-templates>
	        </xsl:when>
	        <xsl:when test="$mode = 'full'">
	         <xsl:apply-templates select="lh:calendar" mode="full">
	             <xsl:with-param name="headers">true</xsl:with-param>
	         </xsl:apply-templates>
	        </xsl:when>
	        <xsl:when test="$mode = 'mobile' and count(lh:calendar) &gt;1">
	         <xsl:apply-templates select="lh:calendar" mode="mobile">
	             <xsl:with-param name="headers">true</xsl:with-param>
	         </xsl:apply-templates>
	        </xsl:when>
	        <xsl:when test="$mode = 'mobile'">
	         <xsl:apply-templates select="lh:calendar" mode="mobile" />
	        </xsl:when>
	    </xsl:choose>
    </xsl:template>

    <xsl:template match="lh:calendar" mode="brief">
        <xsl:param name="headers" />
        <xsl:if test="$headers = 'true' and @title">
            <h4>
                <xsl:value-of select="@title" />
            </h4>
        </xsl:if>
        <ul>
          <xsl:apply-templates select="lh:day"/>
        </ul>
    </xsl:template>

    <xsl:template match="lh:day">
        <xsl:variable name="next" select="position()+1"/>
        <xsl:variable name="prev" select="position()-1"/>
		<xsl:choose>
		    <xsl:when test="lh:description/text() = ../lh:day[$prev]/lh:description/text()"/>
		    <xsl:when test="lh:description/text() = ../lh:day[$next]/lh:description/text()">
		        <xsl:call-template name="merge-days">
		          <xsl:with-param name="days" select="../lh:day"/>
		          <xsl:with-param name="this" select="position()"/>
		          <xsl:with-param name="next" select="$next"/>
		        </xsl:call-template>
		    </xsl:when>
		    <xsl:otherwise>
		        <li>
		            <xsl:copy-of select="lh:label" />
		            <xsl:if test="lh:date">
		                <xsl:text>, </xsl:text>
		                <xsl:copy-of select="lh:date" />
		            </xsl:if>
		            <xsl:text>: </xsl:text>
		            <xsl:copy-of select="lh:description" />
		        </li>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
    <xsl:template name="merge-days">
        <xsl:param name="days" />
        <xsl:param name="this" />
        <xsl:param name="next" />
		<xsl:choose>
		    <xsl:when test="$days[position() = $this]/lh:description/text() != $days[position() = $next]/lh:description/text()">
		        <xsl:call-template name="merged-output">
		            <xsl:with-param name="start-day" select="$days[position() = $this]"/>
		            <xsl:with-param name="end-day" select="$days[position() = $next - 1]"/>
		        </xsl:call-template>
		    </xsl:when>
		    <xsl:when test="$next = count($days)">
		        <xsl:call-template name="merged-output">
		            <xsl:with-param name="start-day" select="$days[position() = $this]"/>
		            <xsl:with-param name="end-day" select="$days[position() = $next]"/>
		        </xsl:call-template>
		    </xsl:when>
		    <xsl:otherwise>
		        <xsl:call-template name="merge-days">
		          <xsl:with-param name="days" select="$days"/>
		          <xsl:with-param name="this" select="$this"/>
		          <xsl:with-param name="next" select="$next + 1"/>
		        </xsl:call-template>
		    </xsl:otherwise>
		</xsl:choose>
    </xsl:template>

    <xsl:template name="merged-output">
        <xsl:param name="start-day" />
        <xsl:param name="end-day" />
        <li>
            <xsl:copy-of select="$start-day/lh:label" />
            <xsl:if test="$start-day/lh:date">
                <xsl:text>, </xsl:text>
                <xsl:copy-of select="$start-day/lh:date" />
            </xsl:if>
            <xsl:text> &#8211; </xsl:text>
            <xsl:copy-of select="$end-day/lh:label" />
            <xsl:if test="$end-day/lh:date">
                <xsl:text>, </xsl:text>
                <xsl:copy-of select="$end-day/lh:date" />
            </xsl:if>                       
            <xsl:text>: </xsl:text>
            <xsl:copy-of select="$end-day/lh:description" />
        </li>
    </xsl:template>

    <xsl:template match="lh:calendar" mode="full">
        <xsl:param name="headers" />
        <xsl:variable name="cal">
            <ul>
                <xsl:for-each select="lh:day">
                    <li>
                        <xsl:value-of select="upper-case(substring(lh:label,1,3))" />
                        <xsl:if test="lh:date">
                            <div>
                                <xsl:copy-of select="lh:date" />
                            </div>
                        </xsl:if>
                        <p>
                            <xsl:copy-of select="lh:description" />
                        </p>
                    </li>
                </xsl:for-each>
            </ul>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$headers = 'true'">
                <xsl:if test="@title">
                    <h3 class="calTitle alternate">
                        <xsl:value-of select="@title" />
                    </h3>
                </xsl:if>
                <div class="calendar">
                    <xsl:copy-of select="$cal" />
                </div>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="$cal" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="lh:calendar" mode="mobile">
        <xsl:param name="headers" />
        <dl>
            <xsl:if test="$headers and @title">
                <div class="calTitle"><xsl:value-of select="@title" /></div>
            </xsl:if>
            <xsl:for-each select="lh:day">
                <dt label="{lh:label}" date="{lh:date}">
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
            </xsl:for-each>
        </dl>
    </xsl:template>

</xsl:stylesheet>