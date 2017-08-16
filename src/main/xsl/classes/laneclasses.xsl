<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml" version="2.0">

	<xsl:import href="laneclasses-common.xsl" />
	
	<xsl:template match="/">
		<html><head><title>classes</title></head><body><xsl:apply-templates/></body></html>
	</xsl:template>

	<xsl:template match="name">
		<h4>
			<a href="/classes-consult/laneclass.html?class-id={../id}">
				<xsl:value-of select="."/>
			</a>
		</h4>
	</xsl:template>
	
	<xsl:template match="class">
		<div class="class">
			<div class="yui3-g">
				<div class="yui3-u-1-4">
						<xsl:apply-templates select="dates"/>
				</div>
				<div class="yui3-u-3-4">
						<xsl:apply-templates select="name"/>
						<div class="yui3-g">
							<div class="yui3-u-1-4">
								<xsl:apply-templates select="instructors"/>
								<xsl:apply-templates select="remainingSeats"/>						
							</div>
							<div class="yui3-u-3-4">
								<xsl:apply-templates select="description"/>
							</div>
						</div>
				</div>
			</div>
		</div>
	</xsl:template>

	
	<xsl:template match="location_name">
		<div>
		  <xsl:text>At </xsl:text>
                <xsl:variable name="link">
                    <xsl:value-of select="../location_url"/>
                </xsl:variable>
                <xsl:variable name="name">
                    <span>
                            <xsl:attribute name="itemprop">location</xsl:attribute>
                        	<xsl:value-of select="location_name"/>
                    </span>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="../location_url/text()"/>
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
	</xsl:template>

  
	<xsl:template match="description">
		<xsl:choose>
			<xsl:when test="count(tokenize(., '\W+')[. != ''])  &gt; number(50)">
				<xsl:call-template name="firstWords">
					<xsl:with-param name="value" select="."/>
					<xsl:with-param name="count" select="number(50)"/>
				</xsl:call-template>
				<xsl:text>...</xsl:text>
					<a href="/classes-consult/laneclass.html?class-id={../id}"> More <i class="fa fa-arrow-right"/></a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>

