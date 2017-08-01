<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://www.w3.org/1999/xhtml"  exclude-result-prefixes="h"	version="2.0">

	
	
	
    <xsl:import href="laneclasses-common.xsl" />
	
	<xsl:template match="/classes">
		<html>
			<body>
				<xsl:apply-templates select="./class" />
			</body>
		</html>
	</xsl:template>
 
	<xsl:template match="class">
	<xsl:if test="position() = 1">
	 	<h4>
			<xsl:value-of select="name" />
		</h4>
		<div class="class-description">
	 		<xsl:copy-of select="description"/>
	 	</div>
	 	<div class="handout-description" >
			<xsl:copy-of select="handout"/>
		</div>
		</xsl:if>
		<xsl:call-template name="class-detail"/>
	</xsl:template>


 
  

	<xsl:template name="class-detail">
		<div class="class">
			<div class="yui3-g">
				<div class="yui3-u-1-4">
					<xsl:apply-templates select="dates" />
				</div>
				<div class="yui3-u-3-4">
					<div class="details">
						<xsl:apply-templates select="instructors" />
						<xsl:apply-templates select="remainingSeats"/>
						<div style="margin-top:30px">
							<xsl:apply-templates select="location" /> 
						</div>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>



	<xsl:template  match="location">
		<div>
			<xsl:choose>
				<xsl:when test="url[string-length() &gt; 0]">
					<div>
						<span>
							<xsl:attribute name="itemprop">location</xsl:attribute>
							<xsl:value-of select="name" />
						</span>
					</div>
					<div>
						<a>
							<xsl:attribute name="href">
                                <xsl:value-of select="url" />
                            </xsl:attribute>
							<xsl:text>Location Map </xsl:text>
							<xsl:if test="ends-with(url, '.pdf')">
								<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
							</xsl:if>
						</a>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<span>
						<xsl:attribute name="itemprop">location</xsl:attribute>
						<xsl:value-of select="name" />
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>

</xsl:stylesheet>
