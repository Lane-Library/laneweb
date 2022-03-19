<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns="http://www.w3.org/1999/xhtml" 
xmlns:h="http://www.w3.org/1999/xhtml" 
xmlns:s="http://lane.stanford.edu/staff/1.0"
exclude-result-prefixes="h s" version="2.0">

	<xsl:param name="manager"/>

	<xsl:template match="/s:staff-directory">
		<xsl:apply-templates select="s:staff">
			<!--  why sort? source spreadsheet not ordered and StaffSAXStrategy randomizes order -->
			<xsl:sort select="lower-case(s:last-name)" order="ascending"/>
			<xsl:sort select="lower-case(s:first-name)" order="ascending"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="s:staff">
		<!--  is this used? if not, remove -->
		<xsl:if test=" $manager != 'true' or s:manager = 'TRUE'">
		<div>
			<xsl:attribute name="class">module</xsl:attribute>
			<xsl:attribute name="id" select="s:id/text()" />

			<h2>
				<span>
					<xsl:value-of select="s:first-name/text()" /><xsl:text> </xsl:text><xsl:value-of select="s:last-name/text()" />
					<xsl:if test="s:title != ''">, <xsl:value-of select="s:title" />
					</xsl:if><br/>
					<xsl:value-of select="s:job-title/text()"></xsl:value-of>
				</span>
			</h2>
			<div>
				<xsl:attribute name="class">pure-g</xsl:attribute>
				<xsl:call-template name="staff-profile">
					<xsl:with-param name="name" select="concat(s:first-name/text(), ' ', s:last-name/text())" />
					<xsl:with-param name="stanford-profile-href" select="s:stanford-profile/text()" />
					<xsl:with-param name="picture-src" select="s:picture/text()" />
				</xsl:call-template>

				<xsl:call-template name="staff-detail">
					<xsl:with-param name="email" select="s:email/text()" />
					<xsl:with-param name="stanford-profile-href" select="s:stanford-profile/text()" />
					<xsl:with-param name="phone" select="s:phone/text()" />
				</xsl:call-template>

			</div>
		</div>
		</xsl:if>
	</xsl:template>

	<xsl:template name="staff-profile">
		<xsl:param name="name" />
		<xsl:param name="stanford-profile-href" />
		<xsl:param name="picture-src" />
		<div>
			<xsl:attribute name="class">pure-u-1-5</xsl:attribute>
			<xsl:choose>
			<xsl:when test=" $picture-src != '' and $stanford-profile-href != ''">
				<a>
					<xsl:attribute name="href" select="$stanford-profile-href" />
					<img>
						<xsl:attribute name="class">scaled-image</xsl:attribute>
						<xsl:attribute name="src" select="$picture-src" />
						<xsl:attribute name="alt" select="concat($name, ' photo')" />
					</img>

				</a>
			</xsl:when>
			<xsl:otherwise>
			<xsl:if test="$picture-src != '' ">
				<img>
					<xsl:attribute name="class">scaled-image</xsl:attribute>
					<xsl:attribute name="src" select="$picture-src" />
					<xsl:attribute name="alt" select="concat($name, ' photo')" />
				</img>
			</xsl:if>
			</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>



	<xsl:template name="staff-detail">
		<xsl:param name="phone" />
		<xsl:param name="email" />
		<xsl:param name="stanford-profile-href" />
		<div>
			<xsl:attribute name="class">pure-u-4-5</xsl:attribute>
			<ul>
				<xsl:attribute name="class">bulleted-list</xsl:attribute>
				<xsl:if test="$phone != ''">
					<li>
						<xsl:attribute name="class">liaison-phone</xsl:attribute>
						<xsl:value-of select="s:phone" />
					</li>
				</xsl:if>
				<li>
					<xsl:attribute name="class">liaison-email-link</xsl:attribute>
					<a>
						<xsl:attribute name="href"  select="concat('mailto:', $email)"/>
						<xsl:value-of select="s:email" />
					</a>
				</li>
				<xsl:if test="$stanford-profile-href != ''">
					<li>
						<xsl:attribute name="class">liaison-profile-link</xsl:attribute>
						<a>
							<xsl:attribute name="href" select="$stanford-profile-href" />
							<i>
								<xsl:attribute name="class">fa fa-users</xsl:attribute>
							</i>
							<xsl:text> Stanford Profile</xsl:text>
						</a>
					</li>
				</xsl:if>
			</ul>

	</div>
	</xsl:template>
</xsl:stylesheet>