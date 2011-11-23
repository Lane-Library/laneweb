<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:lh="http://lane.stanford.edu/hours/1.0"
	xmlns:h="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h lh"
	version="2.0">

	<xsl:param name="id" />

	<xsl:param name="mode" />

	<xsl:template match="/lh:calendars">
	   <xsl:choose>
	       <xsl:when test="$id and $mode">
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
	       </xsl:when>
	       <xsl:when test="$mode">
               <xsl:choose>
                   <xsl:when test="$mode = 'brief'">
                    <xsl:apply-templates select="lh:calendar" mode="brief">
                        <xsl:with-param name="headers">true</xsl:with-param>
                    </xsl:apply-templates>
                   </xsl:when>
                   <xsl:when test="$mode = 'full'">
                    <xsl:apply-templates select="lh:calendar" mode="full">
                        <xsl:with-param name="headers">true</xsl:with-param>
                    </xsl:apply-templates>
                   </xsl:when>
                   <xsl:when test="$mode = 'mobile'">
                    <xsl:apply-templates select="lh:calendar[1]" mode="mobile" />
                   </xsl:when>
               </xsl:choose>
	       </xsl:when>
	   </xsl:choose>
	</xsl:template>

	<xsl:template match="lh:calendar" mode="brief">
		<xsl:param name="headers" />
		<xsl:if test="$headers = 'true' and @title">
			<h4>
				<xsl:value-of select="@title" />
			</h4>
		</xsl:if>
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
		<xsl:param name="headers" />
		<xsl:variable name="cal">
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
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$headers = 'true'">
				<xsl:if test="@title">
					<h3 class="alternate">
						<xsl:value-of select="@title" />
					</h3>
				</xsl:if>
				<div class="calendar">
					<xsl:copy-of select="$cal" />
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="$cal" />
			</xsl:otherwise>
		</xsl:choose>
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