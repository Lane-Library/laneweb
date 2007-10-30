<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
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
	
	<!-- remove searchFormSelect() script -->
	<xsl:template match="h:script[contains(.,'searchFormSelect')]"/>
	
	<xsl:template match="h:link[contains(@href,'search.css')]"/>
	
	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='aGeneralBox' or (@class='eMainBox' and not(h:h2))]" priority="1">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="h:td[@id='mainColumn']/h:div[@class='eMainBox' and h:h2]" priority="1">
		<xsl:apply-templates select="h:h2"/>
		<xsl:copy>
			<xsl:apply-templates select="attribute::node() | child::node() [not(self::h:h2)]"/>
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
	
	<xsl:template match="h:p[preceding-sibling::h:h3]
		|h:ul[preceding-sibling::h:h3]
		|h:form[preceding-sibling::h:h3]
		|h:ol[preceding-sibling::h:h3]
		|h:td[@id='mainColumn']/h:p[preceding-sibling::h:h2]
		|h:td[@id='mainColumn']/h:ul[preceding-sibling::h:h2]
		|h:td[@id='mainColumn']/h:ol[preceding-sibling::h:h2]">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:if test="not(@class)">
				<xsl:attribute name="class">labled</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates/>
			</xsl:copy>
	</xsl:template>

	<xsl:template match="h:td[@id='leftColumn']|h:td[@id='rightColumn']|h:td[@id='leftColumnHome']|h:td[@id='rightColumnHome']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()"/>
			<xsl:attribute name="class">sideColumn</xsl:attribute>
			<xsl:apply-templates select="child::node()"/>
		</xsl:copy>
	</xsl:template>

    <xsl:template match="h:td[@id='mainColumn']/h:div[@class='fMainBox']" priority="1">
        <div class="tabs">
        <xsl:for-each select="h:h2">
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
                        <a href="?loadTab={$id}">
                            <xsl:value-of select="."/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:copy>
        </xsl:for-each>
            <xsl:apply-templates select="child::h:div[@id='otherPortalOptions']"/>
        	</div>
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()" />
            <xsl:for-each select="h:h2">
                <xsl:variable name="stop-point">
                    <xsl:value-of select="last() - position()"/>
                </xsl:variable>
                <xsl:variable name="id">
                    <xsl:choose>
                        <xsl:when test="@id"><xsl:value-of select="@id"/></xsl:when>
                        <xsl:otherwise><xsl:value-of select="concat('tab', position())"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:if test="(count(parent::h:div/h:h2) = 1)
                    or ($loadTab != '' and $loadTab = $id) 
                    or ($loadTab = '' and @class = 'activeTab') 
                    or ($loadTab = '' and not(../h:h2[@class = 'activeTab']) and position()!=1)">
                    <xsl:apply-templates select="following-sibling::node()[not(@id='otherPortalOptions') and count(following-sibling::h:h2) = $stop-point]"/>
                </xsl:if>
                
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>
	
    <xsl:template match="h:div[@class='searchHeader']">
        <h2 class="searchHeader"><xsl:apply-templates/></h2>
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
