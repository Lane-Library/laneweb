<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/staff/1.0"
	exclude-result-prefixes="h s" version="2.0">
	<xsl:param name="liaison" />


	<xsl:template match="/s:staff-directory/s:staff[@id = $liaison]">
		<div class="business-card">
			<h3>Your Librarian</h3>
			<div class="pure-g">
				<div class="pure-u-1-4">
					<a>
						<xsl:attribute name="href" select="s:cap-profile" />
						<img>
							<xsl:attribute name="class">scaled-image</xsl:attribute>
							<xsl:attribute name="src" select="s:picture" />
						</img>

					</a>
				</div>
				<div class="pure-u-3-4">
					<a>
						<xsl:attribute name="href" select="s:cap-profile" />
						<xsl:value-of select="s:first-name/text()" />
						<xsl:text> </xsl:text>
						<xsl:value-of select="s:last-name/text()" />
					</a>
					<br />

					<a>
						<xsl:attribute name="href" select="s:cap-profile" />
						<i class="fa fa-users"></i>
					 <xsl:text>CAP Profile</xsl:text>
					</a>
					<br />
					<a>
						<xsl:attribute name="href" select="concat('mailto:', s:email)" />
						<xsl:value-of select="s:email" />
					</a>
					<br />
					<xsl:value-of select="s:phone" />
				</div>
			</div>
		</div>


	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>