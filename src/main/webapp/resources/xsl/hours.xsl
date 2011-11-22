<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:lh="http://lane.stanford.edu/hours/1.0"
	xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h lh"
	version="2.0">

	<xsl:param name="id" />

	<xsl:param name="mode" />

	<xsl:template match="/lh:calendars">
       <xsl:choose>
           <xsl:when test="$mode = 'brief'">
            <xsl:apply-templates select="lh:calendar[@id=$id]" mode="brief" />
           </xsl:when>
           <xsl:when test="$mode = 'full'">
            <xsl:apply-templates select="lh:calendar[@id=$id]" mode="full" />
           </xsl:when>
           <xsl:when test="$mode = 'mobile'">
            <xsl:apply-templates select="lh:calendar[@id=$id]" mode="mobile" />
           </xsl:when>
       </xsl:choose>
	</xsl:template>

	<xsl:template match="lh:calendar" mode="brief">
		<ul>
			<xsl:for-each select="lh:day">
				<li>
					<xsl:copy-of select="lh:label" />
					<xsl:if test="lh:date">
						<xsl:text>, </xsl:text>
						<xsl:copy-of select="lh:date" />
					</xsl:if>
					<xsl:text>: </xsl:text>
					<xsl:copy-of select="lh:description" />
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>

	<xsl:template match="lh:calendar" mode="full">
		<ul>
			<xsl:for-each select="lh:day">
				<li>
					<xsl:value-of select="upper-case(substring(lh:label,1,3))" />
					<xsl:if test="lh:date">
					   <div>
						<xsl:copy-of select="lh:date" />
					   </div>
					</xsl:if>
					<p>
						<xsl:copy-of select="lh:description" />
					</p>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>

	<xsl:template match="lh:calendar" mode="mobile">
		<dl>
			<xsl:for-each select="lh:day">
				<dt>
					<xsl:value-of select="lh:label" />
				</dt>
				<dd>
					<xsl:value-of select="replace(lh:description,':00 ?','')" />
				</dd>
			</xsl:for-each>
		</dl>
	</xsl:template>

</xsl:stylesheet>