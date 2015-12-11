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

   <xsl:variable name="class-status">
        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_status/text()"/>
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
                <xsl:attribute name="itemprop">description</xsl:attribute>
            </xsl:if>
	        <xsl:apply-templates />
	        <xsl:copy-of
	            select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_description/child::node()" />
        </div>
    </xsl:template>

    <xsl:template match="h:p[@id='registration']">
		
		<xsl:choose >
			<xsl:when test="$class-status = 'X'">
				<xsl:call-template name="registration">
                  <xsl:with-param name="class" select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/.."/>
              </xsl:call-template>			
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/..[lc:event_status/text() = 'O']">
				<xsl:call-template name="registration">
                  <xsl:with-param name="class" select="."/>
              	</xsl:call-template>
				</xsl:for-each>	
			</xsl:otherwise>
		</xsl:choose>
	    </xsl:template> 
    	
    	
	<xsl:template name="registration"> 
        	 <xsl:param name="class"/>
        	 
            <xsl:variable name="classId" select="$class/lc:module_id/text()"/>
            
            <!--  only include microdata on first open class -->
            <xsl:variable name="needsMicrodata" select="position() = 1"/>
            <div style="margin-top:12px;position:relative">
            <div>
            	<xsl:apply-templates select="$class/lc:event_dates/lc:start_date[1]"/>
                <xsl:if test="$class-status != 'X'">
                <time>
                    <xsl:if test="$needsMicrodata">
                        <xsl:attribute name="itemprop">startDate</xsl:attribute>
                        <xsl:attribute name="datetime">
                            <xsl:value-of select="$class/lc:event_dates/lc:start_date[1]/text()"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:call-template name="start-time"/>
                </time>
                <xsl:text>-</xsl:text>
                <time>
                    <xsl:if test="$needsMicrodata">
                        <xsl:attribute name="itemprop">endDate</xsl:attribute>
                        <xsl:attribute name="datetime">
                            <xsl:value-of select="$class/lc:event_dates/lc:end_date[1]/text()"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:call-template name="end-time"/>
                </time>
                </xsl:if>
            </div>
            <xsl:if test="$class-status != 'X'">
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
                    <xsl:when test="$class/lc:more_info_url/text() != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="$class/lc:more_info_url/text()"/>
                            </xsl:attribute>
                            <xsl:choose>
                                <xsl:when test="$class/lc:speaker/text() != ''">
                                    <xsl:value-of select="$class//lc:speaker/text()"/>
                                </xsl:when>
                                <xsl:otherwise>
                                	<xsl:apply-templates select="$class/lc:event_instructors/lc:instructor"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="$class/lc:speaker/text() != ''">
                                <xsl:value-of select="$class/lc:speaker/text()"/>
                            </xsl:when>
                            <xsl:otherwise>
                              	<xsl:apply-templates select="$class/lc:event_instructors/lc:instructor"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <div>
                <xsl:text>At </xsl:text>
                <xsl:variable name="link">
                    <xsl:value-of select="$class/lc:venue/lc:venue_website"/>
                </xsl:variable>
                <xsl:variable name="name">
                    <span>
                        <xsl:if test="$needsMicrodata">
                            <xsl:attribute name="itemprop">location</xsl:attribute>
                        </xsl:if>
                        <xsl:value-of select="$class/lc:venue/lc:venue_name"/>
                    </span>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="$class/lc:venue/lc:venue_website/text()"/>
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
            </xsl:if>
            </div>
    </xsl:template>

	<xsl:template match="lc:instructor">
		<xsl:variable name="position" select="position()"/>
		<xsl:variable name="last" select="last()"/>
		<div>
		<xsl:for-each select=".">
			<xsl:value-of select="lc:fname"/>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="lc:lname"/>
			<xsl:if test="$position != $last" >
				<xsl:text>&#160; &amp; &#160;</xsl:text>
			</xsl:if>
		</xsl:for-each>
		</div>
	</xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="lc:start_date">
        <xsl:variable name="date-tokens" select="tokenize(.,'(/| )')"/>
        <xsl:variable name="month">
            <xsl:value-of select="number($date-tokens[1])"/>
        </xsl:variable>
        <!-- TODO: put these styles into css -->
        <div class="date" style="position:absolute;right:127px;width:130px">
            <div class="month-day" style="padding:0">
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
                <xsl:value-of select="$date-tokens[2]"/>
            </div>
            <div class="year">
                <xsl:value-of select="$date-tokens[3]"/>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
