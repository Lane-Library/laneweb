<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">


   <xsl:param name="class-number"/>
   
   <xsl:template match="/classes">
      <html>
         <body>
            <xsl:apply-templates select="class[position() &lt;= $class-number]"/>
         </body>
      </html>
   </xsl:template>
   
   <xsl:template match="class">
	<div class="event laneclass">
		<div class="date lane-date">
			<div class="month">
				<xsl:value-of select="./start/month" />
			</div>
			<div class="day">
				<xsl:value-of select="./start/day" />
			</div>
		</div>
		<div>
		<p>
			<a>
				<xsl:attribute name="href">
                        <xsl:value-of select="./url/public" />                                  
                     </xsl:attribute>
				<xsl:value-of select="title" />
			</a>
			<br />
			<span class="time">
				<xsl:if test="./allday = 'false'">
					<xsl:value-of select="./start/hour" />
					<xsl:text> – </xsl:text>
					<xsl:value-of select="./end/hour" />
				</xsl:if>
				<xsl:if test="./allday ='true'">
					<xsl:text>All Day Event</xsl:text>
				</xsl:if>
			</span>
		</p>
		</div>
	</div>
</xsl:template>
   
  
</xsl:stylesheet>