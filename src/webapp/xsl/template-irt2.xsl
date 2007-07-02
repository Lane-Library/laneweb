<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
		xmlns:h="http://www.w3.org/1999/xhtml"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml"
		exclude-result-prefixes="h">
	
	<xsl:param name="request-uri"/>
	<xsl:param name="loadTab"/>
	
	<xsl:template match="child::node()">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node() | child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="attribute::node()">
		<xsl:copy-of select="self::node()"/>
	</xsl:template>
	
	<xsl:template match="h:div[@id='collage']">
		<xsl:if test="$request-uri = 'index.html'">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node() | child::node()"/>
			</xsl:copy>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="h:div[@id='breadCrumb']">
		<xsl:if test="$request-uri != 'index.html'">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node() | child::node()"/>
			</xsl:copy>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="h:td/h:div">
		<xsl:apply-templates select="child::h:h2"/>
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='aGeneralBox']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:h2[parent::h:div[@class='eMainBox']]">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()[not(class)]"/>
			<xsl:attribute name="class">eMainBox</xsl:attribute>
			<xsl:apply-templates select="child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="@class[contains(.,'ColumnOf3')]">
		<xsl:attribute name="class">columnOf3</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@class[contains(.,'ColumnOf2')]">
		<xsl:attribute name="class">columnOf2</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='fMainBox']">
		<xsl:if test="child::h:h2">
			<h2 class="activeTab"><xsl:apply-templates select="child::h:h2/node()"/></h2>
		</xsl:if>
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:td[@id='leftColumn']|h:td[@id='rightColumn']|h:td[@id='leftColumnHome']|h:td[@id='rightColumnHome']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:attribute name="class">sideColumn</xsl:attribute>
			<xsl:apply-templates select="child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:div[@id='otherPortalOptions']"><!--
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<h2 class="bgTab"><a href="#">Other Portals</a></h2>
			<ul>
				<xsl:for-each select="descendant::h:option[. != 'Other Portals']">
					<li>
						<xsl:choose>
							<xsl:when test="@disabled"><xsl:value-of select="."/></xsl:when>
							<xsl:otherwise>
								<a href="/ceyates{@value}?template=irt2"><xsl:value-of select="."/></a>
							</xsl:otherwise>
						</xsl:choose>
					</li>
				</xsl:for-each>
			</ul>
		</xsl:copy>
	--></xsl:template>

	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='fMainBox' and count(child::h:h2) &gt; 1]">

			<xsl:for-each select="h:h2[contains(@class, 'Tab')]">
				<xsl:copy>
					<xsl:variable name="id">
						<xsl:choose>
							<xsl:when test="@id">
								<xsl:value-of select="@id"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="concat('tab', position())"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="class">
						<xsl:choose>
							<xsl:when test="$loadTab != '' and $loadTab = $id">activeTab</xsl:when>
							<xsl:when test="$loadTab = '' and contains(@class, 'activeTab')"
								>activeTab</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when
										test="$loadTab = '' and not(../h:h2[contains(@class, 'activeTab')]) and position()=1"
										>activeTab</xsl:when>
									<xsl:otherwise>bgTab</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:attribute name="id">
						<xsl:value-of select="$id"/>
					</xsl:attribute>
					<xsl:attribute name="class">
						<xsl:value-of select="$class"/>
					</xsl:attribute>
					<xsl:choose>
						<xsl:when test="$class='bgTab'">
							<a href="?loadTab={$id}&amp;template=irt2">
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
	</xsl:copy>
							</xsl:for-each>
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:apply-templates select="child::h:div[@id='otherPortalOptions']"/>
					<xsl:for-each select="h:h2[contains(@class, 'Tab')]">
						<xsl:variable name="stop-point">
							<xsl:value-of select="last() - position()"/>
						</xsl:variable>
						<xsl:variable name="id">
							<xsl:choose>
								<xsl:when test="@id"><xsl:value-of select="@id"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="concat('tab', position())"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
							
							<xsl:if test="($loadTab != '' and $loadTab = $id) 
								or ($loadTab = '' and @class = 'activeTab') 
								or ($loadTab = '' and not(../h:h2[@class = 'activeTab']) and position()!=1)">
									<xsl:apply-templates select="following-sibling::node()[not(@id='otherPortalOptions') and count(following-sibling::h:h2[contains(@class, 'Tab')]) = $stop-point]"/>
							</xsl:if>
							
					</xsl:for-each>
		</xsl:copy>
	</xsl:template>
	
	
	<xsl:template match="h:tr[parent::h:table[@class='striped']]">
		<xsl:copy>
			<xsl:copy-of select="attribute::node()"/>
			<xsl:if test="not(attribute::class)">
				<xsl:attribute name="class">
					<xsl:choose>
						<xsl:when test="position() mod 2 = 0">even</xsl:when>
						<xsl:otherwise>odd</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
