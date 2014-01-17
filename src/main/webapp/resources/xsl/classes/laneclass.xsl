<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns:lc="http://lane.stanford.edu/laneclasses" xmlns:xi="http://www.w3.org/2001/XInclude"
	exclude-result-prefixes="lc h xi" version="2.0">

	<xsl:import href="laneclasses-common.xsl" />

	<xsl:param name="class-id" />

	<xsl:variable name="internal-id">
		<xsl:value-of
			select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:internal_id/text()" />
	</xsl:variable>

	<xsl:template match="doc">
		<xsl:apply-templates select="h:html" />
	</xsl:template>

	<xsl:template match="h:body">
		<xsl:copy>
			<xsl:attribute name="itemscope" />
			<xsl:attribute name="itemtype">http://data-vocabulary.org/Event</xsl:attribute>
			<xsl:apply-templates select="attribute::node()|child::node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:h4[@id='class-title']">
		<xsl:copy>
			<xsl:attribute name="itemprop">summary</xsl:attribute>
			<xsl:apply-templates select="attribute::node()|child::node()" />
			<xsl:value-of
				select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_name/text()" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="h:p[@id='description']">
		<xsl:copy>
			<xsl:attribute name="itemprop">description</xsl:attribute>
			<xsl:apply-templates />
			<xsl:copy-of
				select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_description/child::node()" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="h:p[@id='registration']">
		<xsl:for-each
			select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/..">
			<xsl:if test="lc:event_status/text() = 'O'">
				<xsl:variable name="classId" select="./lc:module_id/text()"></xsl:variable>
				<div class="yui3-g" style="margin-top:20px">
					<div class="yui3-u-1-3">
						<a
							href="https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c={lc:module_id}">
							<xsl:choose>
								<xsl:when
									test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() = '---'">
									<xsl:attribute name="class">gray-btn btn-wide</xsl:attribute>
									<span>Waitlist</span>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="class">red-btn btn-wide</xsl:attribute>
									<span>Sign Up</span>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</div>
					<div class="yui3-u-2-3">
						<p>
							<xsl:call-template name="month" />
							<xsl:text> </xsl:text>
							<xsl:call-template name="day" />
							<xsl:text>, </xsl:text>
							<time itemprop="startDate" datetime="{./lc:event_dates/lc:start_date[1]/text()}">
								<xsl:call-template name="start-time" />
							</time>
							<xsl:text>-</xsl:text>
							<time itemprop="endDate" datetime="{./lc:event_dates/lc:end_date[1]/text()}">
								<xsl:call-template name="end-time" />
							</time>
						</p>
						<p>
							<xsl:if
								test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() != '---'">
								<xsl:text>Seats left: </xsl:text>
								<b>
									<xsl:value-of
										select="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text()" />
								</b>
							</xsl:if>
						</p>
						<p>
							<xsl:text>With </xsl:text>
							<xsl:choose>
								<xsl:when test="./lc:more_info_url/text() != ''">
									<a>
										<xsl:attribute name="href">
                                            <xsl:value-of
											select="./lc:more_info_url/text()" />
                                        </xsl:attribute>
										<xsl:value-of select="./lc:speaker/text()" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="./lc:speaker/text()" />
								</xsl:otherwise>
							</xsl:choose>
						</p>

						<p>
							<xsl:text>At </xsl:text>
							<xsl:variable name="link">
								<xsl:value-of select="./lc:venue/lc:venue_website"></xsl:value-of>
							</xsl:variable>
							<xsl:variable name="name">
								<span itemprop="location">
									<xsl:value-of select="./lc:venue/lc:venue_name"></xsl:value-of>
								</span>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$link != ''">
									<a>
										<xsl:attribute name="href">
                                            <xsl:value-of
											select="./lc:venue/lc:venue_website/text()" />
                                        </xsl:attribute>
										<xsl:copy-of select="$name" />
									</a>
									<xsl:if test="ends-with($link, '.pdf')">
										<xsl:text> (.pdf)</xsl:text>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:copy-of select="$name" />
								</xsl:otherwise>
							</xsl:choose>
						</p>
					</div>
				</div>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="*">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="attribute::node()">
		<xsl:copy-of select="self::node()" />
	</xsl:template>

</xsl:stylesheet>
