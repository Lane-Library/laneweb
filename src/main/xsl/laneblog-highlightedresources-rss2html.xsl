<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rss="http://purl.org/rss/1.0/"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="rss h content" version="2.0">
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
			select="./channel/item[category[ . = 'Highlighted Resource'] and  count(./content:encoded//h:article) &gt; 0 ] [position() &lt; 2]" />
	</xsl:template>
	<xsl:template match="item">
		<div class="newsfeed pure-g">
			<div class="pure-u-1-2 image" >
			
				<!-- <xsl:apply-templates select="./content:encoded//h:img" /> -->
			</div>
			<div class="pure-u-1-2">
				<div class="newsfeed-title">
					<xsl:value-of select="title" />
				</div>
				<section>
					<xsl:for-each select="./content:encoded//h:article">
						<xsl:apply-templates />
					</xsl:for-each>

					<a class="read-more" href="{link}" title="feed link---{../../channel/title}">
						Read more <i class="fa fa-arrow-right"/>
					</a>
				</section>
			</div>
		</div>

	</xsl:template>
	<xsl:template match="h:img">
		<xsl:if test="@class='webfeedsFeaturedVisual wp-post-image'">
			<xsl:copy>
				<xsl:attribute name="src" select="@src" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>