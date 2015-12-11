<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses"
	exclude-result-prefixes="lc" version="2.0">

	<xsl:variable name="currentYear">
		<xsl:value-of select="year-from-date(current-date())"></xsl:value-of>
	</xsl:variable>
	
		<xsl:variable name="previousYear">
		<xsl:value-of select="number($currentYear -1)"/>
	</xsl:variable>

	<xsl:template match="/lc:classes">
		<html>
			<body>
				<div class="bd">
					<h4>
						<xsl:value-of select="$currentYear" />
					</h4>
					<ul>
						<xsl:for-each-group select="lc:event_data"
							group-by="lc:event_name">
							<xsl:sort select="./lc:event_name/text()" />
							<xsl:for-each select="current-group()[last()]">
								<xsl:if
									test="contains( ./lc:event_dates/lc:start_date[1]/text(), $currentYear ) and 
										 ./lc:event_status/text() != 'X'">
									<xsl:call-template name="event_data" />
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each-group>
					</ul>
				</div>
				<div class="bd">
					<div>
						<h4>
							<xsl:value-of select="$previousYear" />
						</h4>
						<ul>
							<xsl:for-each-group select="lc:event_data"
								group-by="lc:event_name">
								<xsl:sort select="./lc:event_name/text()" />
								<xsl:for-each select="current-group()[last()]">
									<xsl:if
										test="contains( ./lc:event_dates/lc:start_date[1]/text(), $previousYear) and 
										 ./lc:event_status/text() != 'X'">
										<xsl:call-template name="event_data" />
									</xsl:if>
								</xsl:for-each>
							</xsl:for-each-group>
						</ul>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="event_data">
		<li>
			<a>
				<xsl:attribute name="href">
                    <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                        <xsl:value-of select="lc:module_id/text()" />
                    </xsl:attribute>
				<xsl:value-of select="./lc:event_name" />
			</a>
		</li>
	</xsl:template>


</xsl:stylesheet>