<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc" version="2.0">



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

	<xsl:template match="lc:event_data">
		<div class="archive">
			<div class="date same-height-1">
				<xsl:call-template name="month" />
				<xsl:text>/</xsl:text>
				<xsl:call-template name="year" />
			</div>
			<div class="details same-height-1">
				<h3>
					<a>
						<xsl:attribute name="href">
                                <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                       	     <b>  <xsl:value-of select="lc:module_id/text()" /></b>
                            </xsl:attribute>
						<xsl:value-of select="./lc:event_name" />
					</a>
				</h3>
				<div>
					<xsl:apply-templates select="./lc:event_description" />
				</div>
				<div>
					<a href="">Download Handouts <i class="fa fa-arrow-right"/>	</a>
				</div>
				
			</div>
			<div class="youtube-class">
				<xsl:if test="./lc:more_info_url/text() != ''">
					<iframe>
						<xsl:attribute name="src" select="./lc:more_info_url/text()" />
						<xsl:attribute name="rameborder">0</xsl:attribute>
						<xsl:attribute name="allowfullscreen"> </xsl:attribute>
						<xsl:attribute name="frameborder">0</xsl:attribute>
						<xsl:attribute name="allowfullscreen"> </xsl:attribute>
						<xsl:attribute name="width">200</xsl:attribute>
						<xsl:attribute name="height">120</xsl:attribute>
					</iframe>
				</xsl:if>
			</div>
		</div>

	</xsl:template>


	<xsl:template name="year">
		<xsl:value-of select="replace(./lc:event_dates/lc:start_date/text(),'.*(\d{4}).*','$1')" />
	</xsl:template>



</xsl:stylesheet>
