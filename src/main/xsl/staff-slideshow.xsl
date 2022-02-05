<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/staff/1.0"
	exclude-result-prefixes="h s" version="2.0">

	
	<xsl:template match="/">
		<xsl:apply-templates select="s:staff-directory/*" />
	</xsl:template>

	<xsl:template match="s:staff">
		<xsl:if test="s:picture != '' and s:slideshow = 'TRUE'">
			<div class="slide">
				<img>
					<xsl:attribute name="class">scaled-image</xsl:attribute>
					<xsl:attribute name="src" select="s:picture" />
				</img>
				<div>
					<label class="name">
						<xsl:value-of select="s:first-name/text()" /><xsl:text> </xsl:text><xsl:value-of select="s:last-name/text()" />
					</label>
					<label class="title">
						<xsl:value-of select="s:job-title/text()" />
					</label>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet> 