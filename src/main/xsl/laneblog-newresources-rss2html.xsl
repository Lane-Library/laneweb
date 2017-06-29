<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rss="http://purl.org/rss/1.0/" xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="rss content" version="2.0">


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
			select="channel/item[category[ . = 'New Resource']][position() &lt; 2]" />
	</xsl:template>


	<xsl:template match="item">
		<div>
			<div class="newsfeed-item-title">
                <a href="{link}" title="feed link---{../../channel/title}">
                    <xsl:value-of select="title" />
                </a>
            </div>
			<content>
				<xsl:value-of select="content:encoded"></xsl:value-of>
			</content>
		</div>
		<div>
			<a href="{link}" title="feed link---{../../channel/title}">
				<xsl:text>Read More </xsl:text>
				<i class="fa fa-arrow-right"></i> 
			</a>
		</div>
	</xsl:template>
</xsl:stylesheet>