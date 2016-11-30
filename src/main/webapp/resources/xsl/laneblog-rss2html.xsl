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
		<hr/>
		<div class="yui3-g">
			<xsl:apply-templates select="channel/item[not(category[ . = 'New Resources'])][position() &lt; 3]" />
		</div>
		<div>
			<a href="" >MORE NEWS</a>
		</div>
		
	</xsl:template>
	

	<xsl:template match="item">
		<div class="yui3-u-1-2">
			<div> 
				<a href="{link}"
					title="feed link---{../../channel/title}">
					<xsl:value-of select="title" />
				</a>
			</div>
			<div>
				<content>
					<xsl:value-of select="content:encoded"></xsl:value-of>
				</content>
			</div>
			<hr/>
			 <xsl:variable name="date" select="tokenize(pubDate, '\s+')"/>
			<xsl:value-of select="$date[3]"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$date[2]"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="$date[4]"/> 
		</div>
	</xsl:template>



</xsl:stylesheet>