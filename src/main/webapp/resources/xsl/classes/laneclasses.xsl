<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses"
	exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml"
	version="2.0">
	<xsl:import href="laneclasses-common.xsl" />
	<xsl:template match="/lc:classes">
		<html>
			<body>
				<xsl:apply-templates select="lc:event_data" />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="/doc/noncached-classes" />
	<xsl:template match="lc:lastmodified" />
	<xsl:template match="lc:event_data">
		<div class="bd">
			<xsl:call-template name="microdata" />
			<xsl:call-template name="decorator" />
			<div class="bottomShadowWide"></div>
		</div>
	</xsl:template>
	<xsl:template name="decorator">
		<xsl:variable name="classId" select="./lc:module_id/text()"></xsl:variable>
		<div class="yui3-g">
			<div class="yui3-u-1-5">
				<div class="month">
					<xsl:call-template name="month" />
				</div>
				<div class="day">
					<xsl:call-template name="day" />
				</div>
				<div class="time">
					<xsl:call-template name="start-time" />
					<xsl:text>–</xsl:text>
					<xsl:call-template name="end-time" />
				</div>
			</div>

			<div class="yui3-u-3-5">
				<h4>
					<xsl:if
						test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() = '---'">
						<xsl:text>WAITLIST! </xsl:text>
					</xsl:if>
					<a>
						<xsl:attribute name="href">
                        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                        <xsl:value-of select="lc:module_id/text()" />
                    	</xsl:attribute>
						<xsl:value-of select="./lc:event_name" />
					</a>
				</h4>
				<p>
					<xsl:value-of select="./lc:speaker/text()" />
				</p>
				<xsl:if
					test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() != '---'">
					<p>
						Seats left:
						<xsl:value-of
							select="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text()" />
					</p>
				</xsl:if>
			</div>
			<div class="yui3-u-1-5">
				<a>
					<xsl:attribute name="href">
                      <xsl:text>https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c=</xsl:text>
                       <xsl:value-of select="lc:module_id" />
                     </xsl:attribute>
					<xsl:choose>
						<xsl:when
							test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() = '---'">
							<xsl:attribute name="class">gray-btn</xsl:attribute>
							<span>Waitlist</span>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class">red-btn</xsl:attribute>
							<span>Sign Up</span>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</div>
		</div>
		<p>
			<xsl:variable name="description-text" select="./lc:event_description" />
			<xsl:variable name="firstParagraphDescription"
				select="substring-before($description-text, '.')" />
			<xsl:variable name="first-words">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="$description-text" />
					<xsl:with-param name="count" select="50" />
				</xsl:call-template>
			</xsl:variable>
			<xsl:choose>
				<xsl:when
					test="count(tokenize($description-text, '\W+')[. != ''])  &gt; 50">
					<xsl:choose>
						<xsl:when
							test="count(tokenize($firstParagraphDescription, '\W+')[. != ''])  &gt; 50 and count(tokenize($firstParagraphDescription, '\W+')[. != '']) &lt; 100">
							<xsl:value-of select="$firstParagraphDescription" />
							<xsl:text>.  </xsl:text>
							<xsl:if
								test="count(tokenize($description-text, '\W+')[. != '']) != count(tokenize($firstParagraphDescription, '\W+')[. != ''])">
								<xsl:text>...</xsl:text>
								<a>
									<xsl:attribute name="href">
                                            <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                            <xsl:value-of
										select="lc:module_id/text()" />
                                            </xsl:attribute>
									<xsl:text> More </xsl:text>
								</a>
								<xsl:text>&#187;</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="normalize-space($first-words)" />
							<xsl:text>...</xsl:text>
							<a>
								<xsl:attribute name="href">
                                        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                        <xsl:value-of
									select="lc:module_id/text()" />
                                        </xsl:attribute>
								<xsl:text> More </xsl:text>
							</a>
							<xsl:text>&#187;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$description-text"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</p>
	</xsl:template>
	<xsl:template name="firstWords">
		<xsl:param name="value" />
		<xsl:param name="count" />

		<xsl:if test="number($count) >= 1">
			<xsl:value-of select="concat(substring-before($value,' '),' ')" />
		</xsl:if>
		<xsl:if test="number($count) > 1">
			<xsl:variable name="remaining" select="substring-after($value,' ')" />
			<xsl:if test="string-length($remaining) > 0">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="$remaining" />
					<xsl:with-param name="count" select="number($count)-1" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>
    <xsl:template name="microdata">
        <xsl:variable name="classUrl">
            <!-- TODO: absolute URLs needed? -->
	        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
	        <xsl:value-of select="lc:module_id/text()" />
        </xsl:variable>
        <xsl:variable name="shortDescription">
	        <xsl:call-template name="firstWords">
	            <xsl:with-param name="value" select="./lc:event_description" />
	            <xsl:with-param name="count" select="50" />
	        </xsl:call-template>
        </xsl:variable>
        <span itemscope="" itemtype="http://data-vocabulary.org/Event">
	        <meta itemprop="url" content="{$classUrl}"  />
	        <meta itemprop="summary" content="{./lc:event_name}"  />
	        <meta itemprop="description" content="{$shortDescription}"  />
            <meta itemprop="startDate" content="{./lc:event_dates/lc:start_date[1]/text()}"  />
            <meta itemprop="endDate" content="{./lc:event_dates/lc:end_date[1]/text()}"  />
	        <meta itemprop="location" content="{./lc:venue/lc:venue_name}"  />
        </span>
    </xsl:template>
</xsl:stylesheet>