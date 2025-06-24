<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:b="http://lane.stanford.edu/bassett/ns" exclude-result-prefixes="h b"
	version="2.0">

	<xsl:param name="type" />
	<xsl:param name="region" />
	<xsl:param name="images-url" />
	

	<xsl:variable name="thumbnail-directory"><xsl:value-of select="$images-url" />/small/</xsl:variable>
	<xsl:variable name="medium-image-directory"><xsl:value-of select="$images-url" />/medium/</xsl:variable>
	<xsl:variable name="large-image-directory"><xsl:value-of select="$images-url" />/large/</xsl:variable>
	<xsl:variable name="highres-image-directory"><xsl:value-of select="$images-url" />/high/</xsl:variable>
	<xsl:variable name="image-directory">
		<xsl:if test="$type != 'largerView'">
		<xsl:value-of select="$medium-image-directory" />
		</xsl:if>
		<xsl:if test="$type = 'largerView'">
			<xsl:value-of select="$large-image-directory" />
		</xsl:if>
	</xsl:variable>
	<xsl:variable name="current-page"><xsl:value-of select="number(/doc/b:bassetts/b:current-page/@b:value)" /></xsl:variable>
	<xsl:variable name="total-pages"><xsl:value-of select="/doc/b:bassetts/b:total-pages/@b:value" /></xsl:variable>

	<xsl:variable name="query-string">
		<xsl:if test="$region != ''">&amp;r=<xsl:value-of select="$region" /></xsl:if>
		<xsl:if test="$type != ''">&amp;t=<xsl:value-of select="$type" /></xsl:if>
	</xsl:variable>

	<xsl:variable name="query-str">
		<xsl:value-of select="replace($query-string,'&amp;t=diagram','')" />
	</xsl:variable>

	<xsl:template match="*">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="doc">
		<xsl:apply-templates select="h:html" />
	</xsl:template>

	<!-- To Give the content title -->

	<xsl:template match="h:span[@id='bassett-title']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()" />
            <xsl:if test="$region and count(/doc/b:bassetts/b:bassett) != 0">
                <xsl:value-of select="replace($region, '--',': ')" />
            </xsl:if>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:span[@id='bassett-subtitle']">
		<xsl:copy>
			<xsl:value-of select="/doc/b:bassetts/b:bassett/b:regions/text()" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="h:h6[@id='bassett-number']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()" />
			<xsl:value-of select="/doc/b:bassetts/b:bassett/@b:bassett_number" />
		</xsl:copy>
	</xsl:template>



	<!-- on multiple View to give link digrams or photos -->


	<xsl:template match="h:span[@class='choice']">
		<xsl:choose>
			<xsl:when test="count(/doc/b:bassetts/b:bassett) != 0">
				<xsl:copy>
					<xsl:apply-templates select="attribute::node()|child::node()" />
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>No images found</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="h:a[@id='photo-choice']/@href">
		<xsl:attribute name="href">
              <xsl:value-of select="." />
                 <xsl:value-of select="$query-str" />        
                 <xsl:text>&amp;page=</xsl:text>
                 <xsl:value-of select="$current-page" />
          </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:a[@id='diagram-choice']/@href">
		<xsl:attribute name="href">
              <xsl:value-of select="." />
                 <xsl:value-of select="$query-str" />
                 <xsl:text>&amp;t=diagram&amp;page=</xsl:text>
                 <xsl:value-of select="$current-page" />
          </xsl:attribute>
	</xsl:template>

	<!-- Multiple View Generate div for the images -->

	<xsl:template match="h:div[@id='thumbnail']">
		<xsl:if test="/doc/b:bassetts/b:bassett">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node()" />
				<xsl:for-each select="/doc/b:bassetts/b:bassett">
					<div>
						<xsl:attribute name="class">image-container</xsl:attribute>
						<div>
							<xsl:attribute name="class">hr</xsl:attribute>
							<a>
								<xsl:attribute name="href">
                                    <xsl:text>/biomed-resources/bassett/bassettView.html?bn=</xsl:text><xsl:value-of
									select="./@b:bassett_number" />
                                </xsl:attribute>
								<img>
									<xsl:attribute name="title">
                                        <xsl:value-of select="./b:title" />
                                    </xsl:attribute>
									<xsl:attribute name="src">
                                        <xsl:value-of select="$thumbnail-directory" />
                                        <xsl:choose>
                                            <xsl:when test="$type='diagram'">
                                                <xsl:value-of select="./b:diagram_image" />
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="./b:bassett_image" />
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:attribute>
									<xsl:attribute name="alt">
                                        <xsl:text>bassett Number </xsl:text><xsl:value-of select="./@b:bassett_number" />
                                    </xsl:attribute>
								</img>
							</a>
						</div>
						<div>
							<xsl:attribute name="class">image-text</xsl:attribute>
							<a>
								<xsl:attribute name="title">
                                <xsl:value-of select="./b:title" />
                            </xsl:attribute>
								<xsl:attribute name="href">
                                <xsl:text>/biomed-resources/bassett/bassettView.html?bn=</xsl:text><xsl:value-of
									select="./@b:bassett_number" />
                            </xsl:attribute>
								<xsl:text>#</xsl:text>
								<xsl:value-of select="./@b:bassett_number" />
							</a>
						</div>
					</div>
				</xsl:for-each>
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<!-- To get the Image source for bassettView.html and bassettLagerView.html -->

	<xsl:template match="h:div[@id='image']/h:a/h:img/@src | h:div[@id='image']/h:img/@src">
		<xsl:attribute name="src">
        <xsl:choose>
            <xsl:when test="$type = 'largerView'">
                <xsl:value-of select="concat($highres-image-directory, /doc/b:bassetts/b:bassett/@b:bassett_number,'_l.jpg')" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat($image-directory,/doc/b:bassetts/b:bassett/b:bassett_image)" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:div[@id='diagram-image']/h:a/h:img/@src | h:div[@id='diagram-image']/h:img/@src">
		<xsl:attribute name="src">
        <xsl:value-of select="$image-directory" />
        <xsl:value-of select="/doc/b:bassetts/b:bassett/b:diagram_image" />
    </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:div[@id='legend-image']/h:a/h:img/@src | h:div[@id='legend-image']/h:img/@src">
		<xsl:attribute name="src">
        <xsl:value-of select="$image-directory" />
        <xsl:value-of select="/doc/b:bassetts/b:bassett/b:legend_image" />
    </xsl:attribute>
	</xsl:template>

	<!-- Legend table -->
	<xsl:template match="h:*[@class='legend-title']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:value-of select="substring-before(/doc/b:bassetts/b:bassett/b:title, '.')" />
		</xsl:copy>

	</xsl:template>

	<xsl:template match="h:*[@class='legend-sub-title']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:value-of select="substring-after(/doc/b:bassetts/b:bassett/b:title, '.')" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:div[@class='legend-description']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()" />
			<xsl:value-of select="/doc/b:bassetts/b:bassett/b:description" />
		</xsl:copy>
	</xsl:template>


	<xsl:template match="h:ol[@id='english-legend']">
		<ol>
		<xsl:for-each select="tokenize(/doc/b:bassetts/b:bassett/b:legend, '--')">
			<xsl:if test="substring-before(.,'.') != ''">
				<li>
						<xsl:value-of select="substring-after(.,'.')" />
				</li>
				
			</xsl:if>
		</xsl:for-each>
		</ol>
	</xsl:template>


	<!-- to get the Href for all links that will open a new window for the bassettLargerView.html -->
	<xsl:template match="h:a[starts-with(@rel,'popup')]/@href">
		<xsl:attribute name="href">
        <xsl:text>/biomed-resources/bassett/raw/bassettLargerView.html?t=largerView&amp;bn=</xsl:text>
        <xsl:value-of select="/doc/b:bassetts/b:bassett/@b:bassett_number" />
    </xsl:attribute>
	</xsl:template>

	<!-- paging -->



	<xsl:template match="h:div[@class='s-pagination']">
		<xsl:if test="$total-pages != '1' and $total-pages != '0'">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node()|child::node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="h:form[@name='bassett-pagination']/h:input[@name='r']">
		<xsl:if test="$region != ''">
			<xsl:copy>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="value">
	  			<xsl:value-of select="$region"></xsl:value-of>
	  		</xsl:attribute>
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="h:form[@name='bassett-pagination']/h:input[@name='pages']">
			<xsl:copy>
				<xsl:copy-of select="@*" />
				<xsl:attribute name="value">
	  			<xsl:value-of select="$total-pages"></xsl:value-of>
	  		</xsl:attribute>
			</xsl:copy>
	</xsl:template>

	<xsl:template match="h:form[@name='bassett-pagination']/h:input[@name='t']">
		<xsl:if test="$type != ''">
			<xsl:copy>
				<xsl:copy-of select="@*" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>




	<xsl:template match="h:input[@name='page']/@value">
		<xsl:attribute name="value">
    	<xsl:value-of select="/doc/b:bassetts/b:current-page/@b:value" />
    </xsl:attribute>
	</xsl:template>


	

	<xsl:template match="h:label[@for='pages']">
		<xsl:copy>
			<xsl:text> of </xsl:text>
			<xsl:value-of select="/doc/b:bassetts/b:total-pages/@b:value" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:span[@id='searchResults' and count(/doc/b:bassetts/b:bassett) != 0]">
		<xsl:copy>
			<xsl:value-of select="/doc/b:bassetts/b:image-number-low/@b:value" />
			<xsl:text> to </xsl:text>
			<xsl:value-of select="/doc/b:bassetts/b:image-number-up/@b:value" />
			<xsl:text> of </xsl:text>
			<xsl:value-of select="/doc/b:bassetts/b:total-images/@b:value" />
			<xsl:text> images</xsl:text>
		</xsl:copy>
	</xsl:template>


	<xsl:variable name="lower-page-class">
		<xsl:choose>
			<xsl:when test="$current-page = '1'">
				<xsl:text>pagingButton disabled no-bookmarking</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>pagingButton  no-bookmarking</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template match="h:a[@id='first-page']/@class">
		<xsl:attribute name="class">
 		<xsl:value-of select="$lower-page-class" />
          </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:a[@id='previous-page']/@class">
		<xsl:attribute name="class">
 		<xsl:value-of select="$lower-page-class" />
          </xsl:attribute>
	</xsl:template>


	<xsl:template match="h:a[@id='first-page']/@href">
		<xsl:attribute name="href">
        <xsl:text>/biomed-resources/bassett/bassettsView.html?</xsl:text>
        <xsl:value-of select="$query-string" />
    </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:a[@id='previous-page']/@href">
		<xsl:attribute name="href">
        <xsl:text>/biomed-resources/bassett/bassettsView.html?</xsl:text>
        <xsl:value-of select="$query-string" />
        <xsl:text>&amp;page=</xsl:text><xsl:value-of select="/doc/b:bassetts/b:previous-page/@b:value"></xsl:value-of>
    </xsl:attribute>
	</xsl:template>

	<xsl:variable name="upper-page-class">
		<xsl:choose>
			<xsl:when test="/doc/b:bassetts/b:total-pages/@b:value = $current-page">
				<xsl:text>pagingButton disabled no-bookmarking</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>pagingButton  no-bookmarking</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>


	<xsl:template match="h:a[@id='next-page']/@class">
		<xsl:attribute name="class">
 		<xsl:value-of select="$upper-page-class" />
          </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:a[@id='last-page']/@class">
		<xsl:attribute name="class">
 			<xsl:value-of select="$upper-page-class" />
        </xsl:attribute>
	</xsl:template>


	<xsl:template match="h:a[@id='next-page']/@href">
		<xsl:attribute name="href">
        <xsl:text>/biomed-resources/bassett/bassettsView.html?</xsl:text>
        <xsl:value-of select="$query-string" />
       		<xsl:text>&amp;page=</xsl:text><xsl:value-of select="/doc/b:bassetts/b:next-page/@b:value"></xsl:value-of>
    </xsl:attribute>
	</xsl:template>

	<xsl:template match="h:a[@id='last-page']/@href">
		<xsl:attribute name="href">
        <xsl:text>/biomed-resources/bassett/bassettsView.html?</xsl:text>
        <xsl:value-of select="$query-string" />
        <xsl:text>&amp;page=</xsl:text><xsl:value-of select="$total-pages" />
    </xsl:attribute>
	</xsl:template>

	<!-- End paging -->


	<xsl:template match="attribute::node()">
		<xsl:copy-of select="self::node()" />
	</xsl:template>
</xsl:stylesheet>
