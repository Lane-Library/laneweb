<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" version="2.0">


	

	<xsl:template match="/classes">
		<html>
			<body>
				<div class="pure-g">
					<div class="pure-u-1-2">
						<ul>
							<xsl:apply-templates	select="class[position() &lt;= 2]" />
						</ul>
					</div>
					<div class="pure-u-1-2">
						<ul>
							<xsl:apply-templates select="class[position() = 3]" />
							<xsl:call-template name="all-classes-link"></xsl:call-template>					
						</ul>
						
					</div>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="class">
		<li class="event">
			<span class="date">
				<span>
					<xsl:value-of select="./start/month" />
				</span>
				<hr />
				<span class="day">
					<xsl:value-of select="./start/day" />
				</span>
			</span>
			<span>
				<label class="time">
					<xsl:if test="./allday = 'false'">
						<xsl:value-of select="./start/hour" />
						<xsl:text> – </xsl:text>
						<xsl:value-of select="./end/hour" />
					</xsl:if>
					<xsl:if test="./allday ='true'">
						<xsl:text>All Day Event</xsl:text>
					</xsl:if>
				</label>
				<a>
					<xsl:attribute name="href">
                        <xsl:value-of select="./url/public" />                                  
                     </xsl:attribute>
                     <xsl:attribute name="class">
                        <xsl:text>title</xsl:text>                                  
                     </xsl:attribute>
					<xsl:value-of select="title" />
				</a>
			</span>
		</li>		
	</xsl:template>

	<xsl:template name="all-classes-link">
		<li class="event">
			<a class="all-classes" href="/classes-consult/laneclasses.html">
				All Classes &amp; Events
				<i class="fa fa-arrow-right" />
			</a>
		</li>
	</xsl:template>

</xsl:stylesheet>