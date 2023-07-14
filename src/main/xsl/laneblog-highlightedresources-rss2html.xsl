<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rss="http://purl.org/rss/1.0/"
	xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="rss h content" version="2.0">
	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match="rss">
		<xsl:apply-templates
			select="./channel/item[category[ . = 'Highlighted Resource'] ] [position() &lt; 2]" />
	</xsl:template>
	<xsl:template match="item">
		<div class="newsfeed">
			<div>
				<figure>
					<xsl:apply-templates select="./content:encoded//h:img" >
					 <xsl:with-param name="link" select="link"/>
					</xsl:apply-templates>
				</figure>
			</div>
			<div class="newsfeed-title">
				<a href="{link}" title="feed link---{../../channel/title}">
					<xsl:value-of select="title" />
				</a>
			</div>
			<div class="read-more">
				<a href="{link}" title="feed link---{../../channel/title}">
					Read More
					<i class="fa-solid fa-arrow-right"></i>
				</a>
			</div>
		</div>

	</xsl:template>
	<xsl:template match="h:img">
		<xsl:param name="link" />
		<xsl:if test="@class='webfeedsFeaturedVisual wp-post-image'">
			<a>
			<xsl:attribute name="href" select="$link"></xsl:attribute>
				<xsl:copy>
					<xsl:attribute name="src" select="@src" />
					<xsl:attribute name="alt" select="@alt" />
					
				</xsl:copy>
			</a>
		</xsl:if>

	</xsl:template>
</xsl:stylesheet>