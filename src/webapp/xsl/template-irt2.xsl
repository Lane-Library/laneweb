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
	
	<xsl:template match="h:font">
		<xsl:apply-templates/>
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
	
	<xsl:template match="h:td/h:div[contains(@class,'Box')]">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:apply-templates select="child::h:h2"/>
			<div class="boxContent">
				<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
			</div>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='fMainBox']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:if test="child::h:h2">
				<h2 class="activeTab"><xsl:apply-templates select="child::h:h2/node()"/></h2>
			</xsl:if>
		<div class="boxContent">
			<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
		</div>
		</xsl:copy>
	</xsl:template>
	
<!--	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='aGeneralBox' or @class='eMainBox']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:apply-templates select="child::h:h2"/>
			<div>
				<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
			</div>
		</xsl:copy>
	</xsl:template>-->
	
	<xsl:template match="h:td[@id='mainColumn']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
		<xsl:apply-templates select="child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:div[@id='otherPortalOptions']">
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
	</xsl:template>

	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='fMainBox' and count(child::h:h2) &gt; 1]">


		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:apply-templates select="child::h:div[@id='otherPortalOptions']"/>
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
									<!--if no tabs marked as active, make the first one active-->
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
								<!-- onclick="loadTab({position()},{count(../h:h2)});resetFocus();return false;">-->
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:copy>
			</xsl:for-each>
			<xsl:if test="child::h:div[@id='otherPortalOptions'] and contains($request-uri, 'portals')">
				
<!--
				<form style="float:right">
					<xsl:copy-of select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/@*" />
					<select>
						<xsl:copy-of select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/h:select/@*" />
						<xsl:for-each select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/h:select/h:option">
							<xsl:choose>
								<xsl:when test="@disabled and not(@selected)"><!- - important to keep this first - ->
									<option disabled="disabled" style="color:#aaa;">
										<xsl:value-of select="text()"/>
									</option>
								</xsl:when>
								<xsl:when test="not(contains(@value, $request-uri))">
									<xsl:apply-templates select="."/>
								</xsl:when>
								<xsl:otherwise>
									<option disabled="disabled" style="color:#aaa;">
										<xsl:value-of select="text()"/>
									</option>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</select>
				</form>
-->
			</xsl:if>
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
								<div class="boxContent">
									<xsl:apply-templates select="following-sibling::node()[not(@id='otherPortalOptions') and count(following-sibling::h:h2[contains(@class, 'Tab')]) = $stop-point]"/>
								</div>
							</xsl:if>
							
<!--						<div style="clear:both">
							<xsl:attribute name="id">
								<xsl:choose>
									<xsl:when test="@id">
										<xsl:value-of select="concat(@id, 'Content')"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="concat('tab', position(), 'Content')"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
							
							<xsl:if test="($loadTab != '' and $loadTab != $id) 
								or ($loadTab = '' and not(contains(@class, 'activeTab'))) 
								or ($loadTab = '' and not(../h:h2[contains(@class, 'activeTab')]) and position()!=1)">
								<xsl:attribute name="class">hide</xsl:attribute>
							</xsl:if>
							<xsl:apply-templates select="following-sibling::node()[not(@id='otherPortalOptions') and count(following-sibling::h:h2[contains(@class, 'Tab')]) = $stop-point]"/>
							<br style="clear:both"/>
						</div>-->
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
