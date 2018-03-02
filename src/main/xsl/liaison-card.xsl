<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="h" version="2.0">

	<xsl:param name="liaison" />

	<xsl:template match="h:div[@id=$liaison]">
		<xsl:variable name="liaison-profile-link"
			select=".//h:a[@class='liaison-profile-link']/@href" />
		<div class="business-card no-bookmarking">
			<h3>Your Librarian</h3>
			<div class="pure-g">
				<xsl:choose>
					<xsl:when test="$liaison = 'tbd'">
						<xsl:call-template name="tbd" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="liaison" />
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="tbd">
		<div class="pure-u-1-4">
		</div>
		<div class="pure-u-3-4">
			<a href="/contacts/liaisons.html#tbd">
				<xsl:value-of select=".//h:span[@class='liaison-name']" />
			</a>
		</div>
	</xsl:template>

	<xsl:template name="liaison">
		<xsl:variable name="liaison-profile-link"
			select=".//h:a[@class='liaison-profile-link']/@href" />
		<div class="pure-u-1-4">
			<a href="{$liaison-profile-link}">
				<xsl:copy-of select=".//h:img[contains(@class,'liaison-image')]" />
			</a>
		</div>
		<div class="pure-u-3-4">
			<a href="{$liaison-profile-link}">
				<xsl:value-of select=".//h:span[@class='liaison-name']" />
			</a>
			<br />
			<a href="{$liaison-profile-link}">
				<i class="fa fa-users"></i>
				CAP Profile
			</a>
			<br />
			<xsl:copy-of select=".//h:a[@class='liaison-email-link']" />
			<br />
			<xsl:copy-of select=".//*[@class='liaison-phone']/text()" />
		</div>
	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>