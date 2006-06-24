<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
		xmlns:h="http://www.w3.org/1999/xhtml"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml"
		exclude-result-prefixes="h xi">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:param name="request-uri"/>
	<xsl:param name="a"/> <!-- alphabetical browse in online directory -->
	<xsl:param name="c"/> <!-- core title -->
	<xsl:param name="category"/> <!-- FAQ categories -->
	<xsl:param name="loadTab" /> <!-- loading specific tab as active -->
	
	<xsl:variable name="alpha">
		<xsl:value-of select="$a"/>
	</xsl:variable>
	
	<xsl:variable name="eLibrary-type">
		<xsl:value-of select="substring(substring-after($request-uri, 'online/'), 1, 2)"/>
	</xsl:variable>
	<xsl:variable name="eLibrary-search-code">
		<xsl:choose>
			<xsl:when test="$eLibrary-type='ej'">ej</xsl:when>
			<xsl:when test="$eLibrary-type='eb'">book</xsl:when>
			<xsl:when test="$eLibrary-type='cc'">cc</xsl:when>
			<xsl:when test="$eLibrary-type='db'">database</xsl:when>
		</xsl:choose>
	</xsl:variable>
	
<!-- collage display only on home page -->
	<xsl:template match="h:div[@id='collage']">
		<xsl:choose>
			<xsl:when test="not($request-uri='index.html')">
				<div id="panoramicView" class="hide">&#160;</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
<!-- breadcrumb not displayed on home page -->

	<xsl:template match="h:div[@id='breadCrumb']">
		<xsl:choose>
			<xsl:when test="$request-uri='index.html'">
				<div id="breadCrumb" class="hide">&#160;</div>
				<div style="height: 10px; font-size: 10px;">&#160;</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
<!-- FAQs per category -->

	<xsl:template match="h:div[@id='contentBody'][not(descendant::h:table)]">
	<div id="contentBody">
		<xsl:choose>
			<xsl:when test="string($category)">
				<xsl:copy-of select="h:div[@id='breadCrumb']"/>
				<xsl:for-each select="child::node()[not(self::h:div[@id='breadCrumb'])]">
					<xsl:choose>
						<xsl:when test="name()">
							<xsl:choose>
								<xsl:when test="normalize-space(string(.)) and name()!='h1'">
									<xsl:element name="{name()}">
										<xsl:copy-of select="@*[not(self::class)]"/>
										<xsl:attribute name="class">
											<xsl:choose>
												<xsl:when test="@class">
													<xsl:value-of select="concat(@class, ' largeFont')"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:text>largeFont</xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:attribute>
										<xsl:apply-templates select="child::node()"/>
									</xsl:element>
								</xsl:when>
								<xsl:otherwise>
									<xsl:copy-of select="."/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="normalize-space(string(.)) and not(self::comment())">
									<span class="largeFont"><xsl:copy-of select="."/></span>
								</xsl:when>
								<xsl:otherwise>
									<xsl:copy-of select="."/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>

				<xsl:apply-templates select="child::node()"/><!-- if apply-templates used, search breaks, with a recursion leading to stack overflow -->
			</xsl:otherwise>
		</xsl:choose>
		</div>
	</xsl:template>


<!-- top level table keeps only id and class attributes; prevent editors from changing layout of page -->

	<xsl:template match="h:table[ancestor::h:div[@id='contentBody']]/h:tr/h:td[contains(@id, 'Column') or contains(@class, 'Column')]">
		<td valign="top"><!-- strangely, the vertical-align in the css appears insufficient for the right column to get top- aligned; investigate... -->
		<xsl:if test="contains(@id, 'left') or contains(@id, 'right')">
			<xsl:attribute name="align">
				<xsl:choose>
					<xsl:when test="contains(@id, 'left')">left</xsl:when>
					<xsl:otherwise>right</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			</xsl:if>
			<xsl:copy-of select="@*[name()='id' or name()='class']"/>
			<xsl:apply-templates />
		</td>
	</xsl:template>

<!-- central column w/o boxes gets large font -->

	<xsl:template match="h:td[@class='centralColumn'][not(descendant::h:div[contains(@class, 'Box')])]">
	<!--<xsl:template match="h:td[@class='centralColumn']">-->
		<td class="centralColumn">
		<xsl:for-each select="child::node()">
			<!--<xsl:for-each select="child::node()[not(self::h:div[contains(@class, 'Box')])]">-->
				<xsl:choose>
					<xsl:when test="name()">
						<xsl:choose>
							<xsl:when test="normalize-space(string(.)) and name()!='h1'">
								<xsl:element name="{name()}">
									<xsl:copy-of select="@*[not(self::class)]"/>
									<xsl:attribute name="class">
										<xsl:choose>
											<xsl:when test="@class">
												<xsl:value-of select="concat(@class, ' largeFont')"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>largeFont</xsl:text>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
									<xsl:apply-templates select="child::node()"/>
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="normalize-space(string(.)) and not(self::comment())">
								<span class="largeFont"><xsl:copy-of select="."/></span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>


<!--
			<xsl:attribute name="class">
				<xsl:text>centralColumn</xsl:text>
				<xsl:if test="not(descendant::h:div[contains(@class, 'Box')])">
					<xsl:text> largeFont</xsl:text>
				</xsl:if>  
			</xsl:attribute>  -->
			<!--<xsl:copy-of select="@*[name()!='class']"/>-->
<!--
			<xsl:apply-templates/>
-->
		</td>
	</xsl:template>


	<!-- box assembly -->
	<xsl:template match="h:div[contains(@class, 'Box') and not(contains(@class, 'inBox'))]">
		<xsl:variable name="current-class-selection"><xsl:value-of select="@class"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="@class = 'noBordersBox'">
				<div class="noBordersBox largeFont">
					<xsl:apply-templates select="child::node()"/>
				</div>
			</xsl:when>
			<xsl:when test="not(contains(@class, 'BoxContent'))">
				<div>
					<xsl:for-each select="@*">
						<xsl:choose>
							<xsl:when test="name()='class' and (contains(.,'long') or contains(.,'short'))">
								<xsl:attribute name="class">
									<xsl:value-of select="concat('s', concat(substring-after(., 'longS'), substring-after(., 'shortS')))" />
								</xsl:attribute><!-- replacing capital w/ lowercase 's', e.g. 'shortSideBlueBox' in content; only one of 'short' or 'long' qualifiers present at a time, so OK to concatenate-->
							</xsl:when>
							<xsl:otherwise>
								<xsl:copy-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
					
<!-- header H2 -->
					<xsl:choose>
<!--final version: <h2 class="activeTab" id="tab1"><a href="#" style="color: black;" onClick="javascript:loadTab(1, 3);">Tools</a></h2>-->
<!-- backward compatibility for tabs w/o ids -->
						<xsl:when test="contains(h:h2/@class, 'Tab') and not(h:h2/@id)">
							<xsl:for-each select="h:h2[contains(@class, 'Tab')]">
								<h2>
									<xsl:choose>
										<xsl:when test="contains(@class, 'activeTab')">
											<xsl:attribute name="class">activeTab</xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose><!--if no tabs marked as active, make the first one active-->
												<xsl:when test="not(../h:h2[contains(@class, 'activeTab')]) and position()=1">
													<xsl:attribute name="class">activeTab</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">bgTab</xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:attribute name="id"><xsl:value-of select="concat('tab', position())"/></xsl:attribute>
									<a href="#">
										<xsl:attribute name="onClick">javascript:loadTab(<xsl:value-of select="position()"/>, <xsl:value-of select="count(../h:h2)"/>);resetFocus();</xsl:attribute>
										<xsl:value-of select="text()"/>
									</a>
								</h2>
							</xsl:for-each>
						</xsl:when>
<!-- making tab linking an option -->
						<xsl:when test="contains(h:h2/@class, 'Tab') and (h:h2/@id)">
							<xsl:for-each select="h:h2[contains(@class, 'Tab')]">
								<h2>
									<xsl:choose>
										<xsl:when test="$loadTab != '' and $loadTab = @id">
											<xsl:attribute name="class">activeTab</xsl:attribute>
										</xsl:when>
										<xsl:when test="$loadTab = '' and contains(@class, 'activeTab')">
											<xsl:attribute name="class">activeTab</xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose><!--if no tabs marked as active, make the first one active-->
												<xsl:when test="$loadTab = '' and not(../h:h2[contains(@class, 'activeTab')]) and position()=1">
													<xsl:attribute name="class">activeTab</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="class">bgTab</xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
									<a>
										<xsl:attribute name="href">?loadTab=<xsl:value-of select="@id"/></xsl:attribute>
										<!--<xsl:attribute name="onClick">javascript:loadTab(<xsl:value-of select="position()"/>, <xsl:value-of select="count(../h:h2)"/>);resetFocus();</xsl:attribute>-->
										<xsl:value-of select="text()"/>
									</a>
								</h2>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="h:h2[1] | h:h2[@class]" /><!-- avoid duplicate display of h2 in RSS feeds, but keep styling for SquareTitles, etc.-->
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:if test="@class='centerGreenBox' or @class='centerBlueBox'">
						<!-- other portals drop-down -->
						<xsl:if test="child::h:div[@id='otherPortalOptions'] and contains($request-uri, 'portals')">
							<form>
								<xsl:copy-of select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/@*" />
								<!--<xsl:value-of select="$request-uri"/>-->
								<select>
									<xsl:copy-of select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/h:select/@*" />
									<xsl:for-each select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']/h:select/h:option">
										<xsl:choose>
											<xsl:when test="@disabled and not(@selected)"><!-- important to keep this first -->
												<option disabled="disabled" style="color:#aaa;"><xsl:value-of select="text()"/></option>
											</xsl:when>
											<xsl:when test="not(contains(@value, $request-uri))">
												<xsl:apply-templates select="."/>
											</xsl:when>
											<xsl:otherwise>
												<option disabled="disabled" style="color:#aaa;"><xsl:value-of select="text()"/></option>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</select>
							</form>
							<!--<xsl:copy-of select="child::h:div[@id='otherPortalOptions']/h:form[@id='portalForm']" />-->
						</xsl:if>
						<br style="clear: both;"/><!--clearance for floats-->
					</xsl:if>
					
					<xsl:if test="contains(@class, 'white') and h:h2">
						<div class="whiteBoxDivider">&#160;</div>
					</xsl:if>
					
					
					<xsl:choose>
						<xsl:when test="h:h2[contains(@class, 'Tab')]">
						<xsl:for-each select="h:h2[contains(@class, 'Tab')]">
						<xsl:variable name="stop-point"><xsl:value-of select="last() - position()"/></xsl:variable>
						<xsl:variable name="hide-or-not">
							<xsl:choose>
								<xsl:when test="($loadTab != '' and $loadTab = @id) or ($loadTab = '' and contains(@class, 'activeTab')) or ($loadTab = '' and contains(@class, 'activeTab')) or ($loadTab = '' and not(../h:h2[contains(@class, 'activeTab')]) and position()=1)"></xsl:when>
								<xsl:otherwise><xsl:text> hide</xsl:text></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
					<div>
						<xsl:attribute name="id">
							<xsl:choose>
								<xsl:when test="@id"><xsl:value-of select="concat(@id, 'Content')"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="concat('tab', position(), 'Content')"/></xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="contains(../@class,'long')">
									<xsl:value-of select="concat('s', substring-after(../@class, 'longS'), 'LongContent', ' ' , 'mediumFont', $hide-or-not)"/>
								</xsl:when>
								<xsl:when test="contains(../@class,'short')">
									<xsl:value-of select="concat('s', substring-after(../@class, 'shortS'), 'ShortContent', ' ' , 'mediumFont', $hide-or-not)"/>
								</xsl:when>
								<!--<xsl:when test="ancestor::h:td[@class='centralColumn']/h:div[1][@class=$current-class-selection]">-->
								<xsl:when test="ancestor::h:td[@class='centralColumn'] and not(contains(@class, 'Blue'))">
									<xsl:value-of select="concat(../@class, 'Content', ' ' , 'largeFont', $hide-or-not)"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(../@class, 'Content', ' ' , 'mediumFont', $hide-or-not)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
<!--
						Position: <xsl:value-of select="position()"/><br/>
						Stop-point value: <xsl:value-of select="$stop-point"/><br/>
-->

						<xsl:apply-templates select="following-sibling::node()[count(following-sibling::h:h2[contains(@class, 'Tab')]) = $stop-point]"/>
						<div style="clear:both; height: 1px; font-size: 1px;">&#160;</div>
					</div>
					</xsl:for-each>
						</xsl:when>
						
						<xsl:otherwise>
					<div>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="contains(@class,'long')">
									<xsl:value-of select="concat('s', substring-after(@class, 'longS'), 'LongContent', ' ' , 'mediumFont')"/>
								</xsl:when>
								<xsl:when test="contains(@class,'short')">
									<xsl:value-of select="concat('s', substring-after(@class, 'shortS'), 'ShortContent', ' ' , 'mediumFont')"/>
								</xsl:when>
								<!--<xsl:when test="ancestor::h:td[@class='centralColumn']/h:div[1][@class=$current-class-selection]">-->
								<xsl:when test="ancestor::h:td[@class='centralColumn'] and not(contains(@class, 'Blue'))">
									<xsl:value-of select="concat(@class, 'Content', ' ' , 'largeFont')"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(@class, 'Content', ' ' , 'mediumFont')"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:if test="contains(@class, 'white') and not(h:h2)">
							<xsl:attribute name="style">text-align: center;</xsl:attribute>
						</xsl:if><!--content of white boxes to be centered when no logos present-->
						<xsl:apply-templates select="child::node()[not(self::h:h2)]"/>
						<div style="clear:both; height: 1px; font-size: 1px;">&#160;</div>
					</div>
					</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="h:div[@id='otherPortalOptions']">
		<!--want it mapped to nothing; processed for top box in central column of portal pages-->
	</xsl:template>

	<xsl:template match="h:div[@id='eLibraryTabContainer']/h:div[@class='popInContent']">
		<xsl:if test="not(preceding-sibling::h:br[contains(@style,'clear:both')])">
			<br style="clear:both;"/><!-- assisting content editors, who would most likely omit this crucial tag, whose importance is not exactly apparent, but whose absence creates a gap between the alphabetic display and the info div[@class='popInContent'] below it -->
		</xsl:if>
		<xsl:copy-of select="."/>
	</xsl:template>
	
<!--alphabet incorporation-->
	<xsl:template name="alphabet-display">
		<xsl:param name="alphabet-string"/>
		
		<xsl:variable name="letter">
			<xsl:value-of select="substring-before($alphabet-string, ' ')" />
		</xsl:variable>

		<xsl:call-template name="alphabet-browse-link-build">
			<xsl:with-param name="browse-capital-letter">
				<xsl:choose>
					<xsl:when test="$letter != ''">
						<xsl:value-of select="$letter"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$alphabet-string"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
<!-- check for recursion stop -->
		<xsl:if test="$letter != ''">
			<xsl:call-template name="alphabet-display">
				<xsl:with-param name="alphabet-string" select="normalize-space(substring-after($alphabet-string, $letter))"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="alphabet-browse-link-build">
		<xsl:param name="browse-capital-letter"/>
		
		<xsl:variable name="browse-lowercase-letter">
			<xsl:choose>
				<xsl:when test="$browse-capital-letter='#'">%23</xsl:when>
				<xsl:when test="$browse-capital-letter='all'"></xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="translate($browse-capital-letter,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:choose>
			<!-- when conditions: 1st check letters, then #, then 'all' -->
			<xsl:when test="($browse-lowercase-letter!='' and $browse-lowercase-letter=$alpha) or ($alpha='#' and $browse-capital-letter='#') or ($browse-lowercase-letter='' and $alpha='' and $c!='y' and contains($request-uri, concat($eLibrary-type, 'browse.html')))">
				<span class="eLibraryTabActive">
					<xsl:if test="$browse-capital-letter='#'">
						<xsl:attribute name="title">non-alphabetical characters</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$browse-capital-letter"/>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<span class="eLibraryTab">
					<a href="{$eLibrary-type}browse.html?a={$browse-lowercase-letter}&amp;t={$eLibrary-search-code}">
						<xsl:if test="$browse-capital-letter='#'">
							<xsl:attribute name="title">non-alphabetical characters</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$browse-capital-letter"/>
					</a>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- core titles processing -->
	<xsl:template match="h:div[@id='eLibraryTabContainer']/h:span[@class='eLibraryTab'][contains(translate(string(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'core')]">
		<xsl:choose>
			<xsl:when test="$c='y' and contains($request-uri, 'browse.html')"><span class="eLibraryTabActive"><xsl:value-of select="string()"/></span></xsl:when>
			<xsl:otherwise><xsl:copy-of select="."/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="h:div[@id='alphabeticalBrowse']">
		<xsl:call-template name="alphabet-display">
			<xsl:with-param name="alphabet-string" select="normalize-space(text())"/>
		</xsl:call-template>
	</xsl:template>
	
	<!--identity copy-->
	<xsl:template match="node()">
		<xsl:copy>
			<xsl:apply-templates select="node() | @*"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="@*[not(self::xmlns:xi)]">
		<xsl:copy-of select="."/>
	</xsl:template>

</xsl:stylesheet>
