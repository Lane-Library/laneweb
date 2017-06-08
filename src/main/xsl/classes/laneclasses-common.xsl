<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns="http://lane.stanford.edu/laneclasses"
    version="2.0">

	<xsl:template match="instructors">
	<div class="instructor">
		With
		<b>
			<xsl:variable name="position" select="position()" />
			<xsl:variable name="last" select="last()" />
			<xsl:for-each select="./instructor">
				<xsl:value-of select="fristName" />
				<xsl:text>&#160;</xsl:text>
				<xsl:value-of select="lastName" />
				<xsl:if test="$position != $last">
					<xsl:text>&#160; &amp; &#160;</xsl:text>
				</xsl:if>
			</xsl:for-each>
		</b>
	</div>
	</xsl:template>
	
	<xsl:template match="dates">
		<div class="date">
			<div class="month-day">
				<xsl:value-of select="./date/month"/><xsl:text>  </xsl:text><xsl:value-of select="date/day"/>
			</div>
			<div class="year">
				<xsl:value-of select="date/year"/>
			</div>
			<div class="time">
				<xsl:value-of select="date/time"/>
			</div>
		</div>
	</xsl:template>
	
	  <xsl:template name="firstWords">
        <xsl:param name="value"/>
        <xsl:param name="count"/>
        <xsl:variable name="words" select="tokenize($value, ' ')"/>
        <xsl:for-each select="$words[position() &lt;= $count]">
            <xsl:value-of select="."/>
            <xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>