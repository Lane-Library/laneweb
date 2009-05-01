<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <xsl:strip-space
        elements="h:html h:head h:body h:div h:p h:form h:map h:select h:table h:tr h:td h:ul h:li"/>

    <!-- ===========================  PARAMETERS ========================= -->
    <!-- the template parameter from the request -->
    <xsl:param name="template"/>
    <!-- the request-uri ( not including parameters ) -->
    <xsl:param name="request-uri"/>
    <!-- the context parameter ( i.e. the base url of the webapp ) -->
    <xsl:param name="context"/>
    <!-- the query part of the request -->
    <xsl:param name="query-string"/>
    <!-- http or https -->
    <xsl:param name="scheme"/>

    <xsl:param name="host"/>

    <!-- the search query -->
    <xsl:param name="q"/>

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="ticket"/>

    <xsl:param name="sunetid"/>

    <!-- m request parameter is a MeSH term -->
    <xsl:param name="m"/>

    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ip-group"/>

    <!-- value of the 't' parameter -->
    <xsl:param name="t"/>

    <!-- loadTab parameter -->
    <xsl:param name="loadTab"/>

    <xsl:param name="version"/>

    <xsl:param name="referrer"/>

    <xsl:param name="name"/>
    
    <!-- matches $search-form-select ask key to text value, keep this in sync with lane-search.js-->
    <xsl:variable name="source-name-map">
        <entry key="ej" value="eJournals"/>
        <entry key="book" value="eBooks"/>
        <entry key="cc" value="Calculators"/>
        <entry key="database" value="Databases"/>
        <entry key="software" value="Software"/>
        <entry key="video" value="Videos"/>
        <entry key="lanesite" value="Lane Site"/>
        <entry key="bassett" value="Bassett"/>
        <entry key="peds" value="Pediatrics"/>
        <entry key="history" value="History"/>
        <entry key="research" value="Bioresearch"/>
        <entry key="all" value="All"/>
        <entry key="clinical" value="Clinical"/>
        <entry key="/portals/pharmacy.html" value="Pharmacy"/>
        <entry key="/portals/anesthesia.html" value="Anesthesia"/>
        <entry key="/portals/cardiology.html" value="Cardiology"/>
        <entry key="/portals/hematology.html" value="Hematology"/>
        <entry key="/portals/internal-medicine.html" value="Internal Medicine"/>
        <entry key="/portals/lpch-cerner.html" value="LPCH LINKS Tool"/>
        <entry key="/portals/pulmonary.html" value="Pulmonary"/>
        <entry key="/portals/emergency.html" value="Emergency"/>
        <entry key="/portals/ethics.html" value="Ethics"/>
    </xsl:variable>

    <!-- ==========================  VARIABLES  ========================== -->
    <!-- the default template -->
    <xsl:variable name="default-template" select="'irt3'"/>
    <!-- the requested template -->
    <xsl:variable name="request-template" select="$template"/>
    <!-- the template that will be used in the response -->
    <xsl:variable name="response-template">
        <xsl:choose>
            <xsl:when test="$request-template = ''">
                <xsl:value-of select="$default-template"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$request-template"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <!-- whether or not the response template is the default template -->
    <xsl:variable name="template-is-default" select="$response-template = $default-template"/>
    <!-- the template document -->
    <xsl:variable name="template-document" select="/*/h:html[3]"/>
    <!-- the root node of the requested content document -->
    <xsl:variable name="source-doc" select="/*/h:html[1]"/>
    <!-- the sitemap document -->
    <xsl:variable name="sitemap" select="/*/h:html[2]"/>

    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="search-form-select">
        <xsl:choose>
            <xsl:when test="$source"><xsl:value-of select="$source"/></xsl:when>
            <xsl:when test="starts-with($request-uri,'online/ej')">ej</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/eb')">book</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/cc')">cc</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/db')">database</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/video')">video</xsl:when>
            <xsl:when test="starts-with($request-uri,'services')">lanesite</xsl:when>
            <xsl:when test="starts-with($request-uri,'howto')">lanesite</xsl:when>
            <xsl:when test="starts-with($request-uri,'bassett')">bassett</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/peds')">peds</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/picu')">peds</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/history')">history</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/bioresearch')">research</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/pharmacy')"
                >/portals/pharmacy.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/anesthesia')"
                >/portals/anesthesia.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/cardiology')"
                >/portals/cardiology.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/hematology')"
                >/portals/hematology.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/internal-medicine')"
                >/portals/internal-medicine.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/lpch-cerner')"
                >/portals/lpch-cerner.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/pulmonary')"
                >/portals/pulmonary.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/emergency')"
                >/portals/emergency.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/ethics')"
                >/portals/ethics.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/')">clinical</xsl:when>
            <xsl:when test="starts-with($request-uri,'local/antibiogram')">clinical</xsl:when>
            <xsl:when test="$source">
                <xsl:value-of select="$source"/>
            </xsl:when>
            <xsl:otherwise>all</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="regex-search-terms">
        <xsl:if test="$q">
            <xsl:value-of select="replace($q,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>

    <!-- figure out what class the body should be for yui grids -->
    <xsl:variable name="yui-grid-class">
        <xsl:choose>
            <xsl:when test="$source-doc/h:body/h:div[@id='leftColumn']">yui-t2</xsl:when>
            <xsl:when test="$source-doc/h:body/h:div[@id='rightColumn']">yui-t4</xsl:when>
            <xsl:when test="contains($request-uri,'search.html')">yui-t4</xsl:when>
        </xsl:choose>
    </xsl:variable>

    <!-- ====================  DEFAULT TEMPLATES ============================= -->
    <!-- root template applies templates on the template document -->
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="$template-document">
                <xsl:apply-templates select="$template-document"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="child::node()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- default element match, copies the element and applies templates on all childeren and attributes -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


    <!-- default attribute match, copies the attribute -->
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="processing-instruction()">
        <xsl:choose>
            <xsl:when test=".='content'">
                <xsl:call-template name="content"/>
            </xsl:when>
            <xsl:when test=".='breadcrumb'">
                <xsl:call-template name="breadcrumb"/>
            </xsl:when>
            <xsl:when test=".='search-terms'">
                <xsl:value-of select="$q"/>
            </xsl:when>
            <xsl:when test=".='mesh'">
                <xsl:value-of select="$m"/>
            </xsl:when>
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="h:div[@id='custom-doc']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:if test="$yui-grid-class">
                <xsl:attribute name="class">
                    <xsl:value-of select="$yui-grid-class"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>


    <!-- persistent login  -->
    <xsl:template match="h:h3[@id='pl']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="$sunetid and $proxy-links = 'true'">
                    <xsl:apply-templates select="h:span[@id='pl-login']/node()"/>
                </xsl:when>
                <xsl:when test="$proxy-links = 'true'">
                    <xsl:apply-templates select="h:span[@id='pl-logout']/node()"/>
                </xsl:when>
                <xsl:otherwise>&#160;</xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
	<!--<xsl:template match="h:span[@id='pl-login']">
		<xsl:if test="$sunetid != '' and $proxy-links = 'true'">
			<xsl:copy>
	    	    <xsl:apply-templates select="@*|node()"/>
	        </xsl:copy>
		</xsl:if>
	</xsl:template>-->
   
    <xsl:template match="h:span[@id='user-name']">
        <xsl:choose>
            <xsl:when test="$name">
                <xsl:value-of select="$name"/>
            </xsl:when>
            <xsl:when test="$sunetid">
                <xsl:value-of select="$sunetid"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
   
   <!-- <xsl:template match="h:span[@id='pl-logout'] ">
      <xsl:if test="$sunetid = '' and $proxy-links = 'true'">
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
      </xsl:if>
	</xsl:template>-->
	<!-- END persistent login  -->
    
    <!-- search form -->
    <!-- set the value of the submit button to the value from the $source-name-map related to $search-form-select -->
    <xsl:template match="h:span[@id='searchLabel']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:value-of select="$source-name-map/h:entry[@key=$search-form-select]/@value"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- set the value of the source input to $search-form-select -->
    <xsl:template match="h:input[@id='searchSource']/@value">
        <xsl:attribute name="value">
            <xsl:value-of select="$search-form-select"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="h:fieldset[@id='pico']/@style">
            <xsl:if test="$search-form-select != '/portals/lpch-cerner.html'">
                <xsl:copy-of select="."/>
            </xsl:if>
    </xsl:template>
    
    <!-- always provide a search <a> element for styling purposes in searchTabs -->
    <xsl:template match="h:ul[@id='searchTabs']//h:a">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- set the activeSearchTab class depending on $search-form-select, requires id to be <$search-form-select>SearchTab -->
    <xsl:template match="h:ul[@id='searchTabs']//h:li">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="@id = concat($search-form-select, 'SearchTab')">
                    <xsl:attribute name="class">activeSearchTab</xsl:attribute>
                </xsl:when>
                <xsl:when test="child::h:a/attribute::href = $search-form-select">
                    <xsl:attribute name="class">activeSearchTab</xsl:attribute>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>
    <!-- end search form -->

    <xsl:template
        match="h:div[@id='leftColumn']|h:div[@id='rightColumn' and not(preceding-sibling::h:div[@id='leftColumn'])]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:attribute name="class">yui-b</xsl:attribute>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:div[@id='rightColumn' and preceding-sibling::h:div[@id='leftColumn']]"/>

    <xsl:template
        match="h:div[@id='mainColumn' and preceding-sibling::h:div[@id='leftColumn'] and following-sibling::h:div[@id='rightColumn']]">
        <div id="yui-main">
            <div class="yui-b yui-ge">
                <div class="yui-u first">
                    <xsl:apply-templates select="child::node()"/>
                </div>
                <div class="yui-u" id="rightColumn">
                    <xsl:apply-templates
                        select="following-sibling::h:div[@id='rightColumn']/child::node()"/>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template
        match="h:div[@id='mainColumn' and not(preceding-sibling::h:div[@id='leftColumn'])]">
        <div id="yui-main">
            <div class="yui-b">
                <div style="margin-left:1em">
                    <xsl:apply-templates/>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template
        match="h:div[@id='mainColumn' and not(following-sibling::h:div[@id='rightColumn'])]">
        <div id="yui-main">
            <div class="yui-b">
                <div style="margin-right:1em">
                    <xsl:apply-templates/>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
        <!--<xsl:if test="contains(.,'[if IE]')">
            <xsl:comment>
                <xsl:value-of select="normalize-space(.)"/>
            </xsl:comment>
        </xsl:if>-->
    </xsl:template>

    <!-- xincludes often include html/head and html/body -->
    <xsl:template match="h:html[ancestor::h:html]">
        <xsl:apply-templates select="h:body/child::node()"/>
    </xsl:template>

    <!-- make sure there is not an empty <script/> element -->
    <xsl:template match="h:script[@src]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:text> </xsl:text>
        </xsl:copy>
    </xsl:template>

    <!-- serve external scripts using request scheme -->
    <!-- TODO: revisit this, is this being used and does it work in all cases -->
    <!--<xsl:template match="h:script/@src[starts-with(.,'http:')]">
        <xsl:attribute name="src">
            <xsl:choose>
                <xsl:when test="$scheme = 'https'">
                    <xsl:value-of select="concat('https:',substring-after(.,'http:'))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        </xsl:template>-->

    <!-- put version into javascript @src -->
    <xsl:template match="h:script/@src[starts-with(.,'/javascript')]">
        <xsl:attribute name="src">
            <xsl:value-of
                select="concat($context,'/javascript/',$version,substring-after(.,'/javascript'))"/>
        </xsl:attribute>
    </xsl:template>

    <!-- put version into css href -->
    <xsl:template match="h:link/@href[starts-with(.,'/style')]">
        <xsl:attribute name="href">
            <xsl:value-of select="concat($context,'/style/',$version,substring-after(.,'/style'))"/>
        </xsl:attribute>
    </xsl:template>

    <!-- put script text into a comment so saxon won't convert entities -->
    <xsl:template match="h:script">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:text>//</xsl:text>
            <xsl:comment>
                <xsl:apply-templates select="child::node()"/>
                <xsl:text>//</xsl:text>
            </xsl:comment>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:a">
        <xsl:choose>
            <xsl:when test="starts-with(@href, '/')">
                <xsl:choose>
                    <xsl:when test="$query-string='' and @href = concat('/',$request-uri)">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:when
                        test="$query-string != '' and @href = concat('/',$request-uri,'?',$query-string)">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- obfuscate email addresses with javascript -->
            <xsl:when test="starts-with(@href, 'mailto:')">
                <xsl:variable name="apostrophe">'</xsl:variable>
                <xsl:variable name="address">
                    <xsl:text>'+'ma'+''+'il'+'to'+':'</xsl:text>
                    <xsl:call-template name="js-split">
                        <xsl:with-param name="string" select="substring-after(@href,'mailto:')"/>
                    </xsl:call-template>
                    <xsl:text>+'</xsl:text>
                </xsl:variable>
                <script type="text/javascript">
                    <xsl:comment>
                        <xsl:text>&#xD;document.write('&lt;a href="</xsl:text>
                        <xsl:value-of select="$address"/>
                        <xsl:text>"</xsl:text>
                        <xsl:for-each select="attribute::node()[not(name() = 'href')]">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="name()"/>
                            <xsl:text>="</xsl:text>
                            <xsl:value-of select="."/>
                            <xsl:text>"</xsl:text>
                        </xsl:for-each>
                        <xsl:text>&gt;</xsl:text>
                        <xsl:for-each select="*">
                            <xsl:text>'+'&lt;</xsl:text>
                            <xsl:value-of select="name()"/>
                            <xsl:for-each select="attribute::node()">
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="name()"/>
                                <xsl:text>="</xsl:text>
                                <xsl:value-of select="."/>
                                <xsl:text>"</xsl:text>
                            </xsl:for-each>
                            <xsl:text>&gt;</xsl:text>
                        </xsl:for-each>
                        <xsl:text>'</xsl:text>
                        <xsl:call-template name="js-split">
                            <xsl:with-param name="string" select="normalize-space()"/>
                        </xsl:call-template>
                        <xsl:text>+'&lt;/a&gt;');&#xD;</xsl:text>
                    </xsl:comment>
                </script>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- setup flash object, using swfobject -->
    <xsl:template match="h:object[@type='application/x-shockwave-flash']">
        <xsl:copy>
            <xsl:attribute name="classid">clsid:D27CDB6E-AE6D-11cf-96B8-444553540000</xsl:attribute>
            <xsl:apply-templates select="@*[not(name()='type' or name() = 'data')]"/>
            <param name="movie" value="{@data}"/>
            <xsl:apply-templates select="h:param"/>
            <xsl:comment>[if !IE]></xsl:comment>
            <xsl:copy>
                <xsl:apply-templates select="@*[not(name()='id')]"/>
                <xsl:apply-templates select="h:param"/>
                <xsl:comment>&lt;![endif]</xsl:comment>
                <xsl:apply-templates select="*[not(self::h:param)]"/>
                <xsl:comment>[if !IE]></xsl:comment>
            </xsl:copy>
            <xsl:comment>&lt;![endif]</xsl:comment>
        </xsl:copy>
    </xsl:template>

    <!-- =====================  SPECIAL CASE TEMPLATES ===================== -->
    <!-- obfuscated email href (don't copy, processed elsewhere) -->
    <xsl:template match="attribute::href[starts-with(.,'mailto:')]"/>

    <!-- add back to top for dl lists > 20 -->
    <xsl:template match="h:dl[count(h:dd) &gt; 20]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node | child::node()"/>
        </xsl:copy>
        <a href="#"><img src="{$context}/graphics/icons/arrowUpTransp.gif" alt="up arrow"/> Back to
            top</a>
    </xsl:template>

    <!-- make current elibraryTab activeTab -->
    <xsl:template match="h:li[attribute::class='eLibraryTab']">
        <xsl:copy>
            <xsl:choose>
                <xsl:when
                    test="contains($request-uri,h:a/attribute::href) or $query-string and contains(h:a/attribute::href, $query-string)">
                    <xsl:attribute name="class">activeTab</xsl:attribute>
                    <xsl:apply-templates select="h:a/child::node()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="attribute::node () | node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <!--<xsl:when
                test="(parent::h:a[@class='proxy' or @proxy]) and $proxy-links = 'true' and starts-with(.,'http')">
                <xsl:choose>
                    <xsl:when test="$ip-group = 'LPCH' or $ip-group = 'SHC'">
                        <xsl:attribute name="href">
                            <xsl:text>http://laneproxy.stanford.edu/login?url=</xsl:text>
                            <xsl:value-of
                                select="replace(replace(.,'\{keywords\}',$regex-search-terms),'\{search-terms\}',$regex-search-terms)"
                            />
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:when test="$ticket != '' and $sunetid != ''">
                        <xsl:attribute name="href">
                            <xsl:text>http://laneproxy.stanford.edu/login?user=</xsl:text>
                            <xsl:value-of select="$sunetid"/>
                            <xsl:text>&amp;ticket=</xsl:text>
                            <xsl:value-of select="$ticket"/>
                            <xsl:text>&amp;url=</xsl:text>
                            <xsl:value-of
                                select="replace(replace(.,'\{keywords\}',$regex-search-terms),'\{search-terms\}',$regex-search-terms)"
                            />
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="make-link">
                            <xsl:with-param name="link">
                                <xsl:value-of
                                    select="concat('/secure/login.html?url=',replace(replace(.,'\{keywords\}',$regex-search-terms),'\{search-terms\}',$regex-search-terms))"
                                />
                            </xsl:with-param>
                            <xsl:with-param name="attr" select="'href'"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>-->
            <xsl:when
                test="starts-with(.,'http://lane.stanford.edu') and not(contains(.,'cookiesFetch'))">
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link"
                        select="substring-after(.,'http://lane.stanford.edu')"/>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains(., '://') and contains(.,'{keywords}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{keywords\}',$regex-search-terms)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and contains(.,'{search-terms}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{search-terms\}',$regex-search-terms)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="starts-with(.,'http://') and starts-with($request-uri,'secure')">
                <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and not(ancestor::h:head) and starts-with(.,'http')">
                <xsl:attribute name="href">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link" select="."/>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="@action | @src">
        <xsl:attribute name="{name()}">
            <xsl:choose>
                <xsl:when test="starts-with(.,'/')">
                    <xsl:value-of select="concat($context,.)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="@*[.='{referrer}']">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="$referrer"/>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="@*[.='{request-uri}']">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="$request-uri"/>
        </xsl:attribute>
    </xsl:template>

    <!-- get all the head elements from template and all non title head elements from source (with some exceptions)-->
    <!-- and add the swfobject.js stuff if necessary -->
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="child::node()"/>
            <xsl:apply-templates select="$source-doc/h:head/node()[not(self::h:title)]"/>
            <xsl:if
                test="$source-doc/h:body//h:object[@type='application/x-shockwave-flash' and @id]">
                <script type="text/javascript" src="{$context}/javascript/{$version}/swfobject.js"><xsl:text> </xsl:text></script>
                <script type="text/javascript">
                    <xsl:for-each select="$source-doc/h:body//h:object[@type='application/x-shockwave-flash' and @id]">
                        <xsl:text>swfobject.registerObject('</xsl:text>
                        <xsl:value-of select="@id"/>
                        <xsl:if test="h:param[@name='flash-version']">
                            <xsl:text>','</xsl:text>
                            <xsl:value-of select="h:param[@name='flash-version']/@value"/>
                            <xsl:if test="h:param[@name='flash-for-upgrade']/@value = 'true'">
                                <xsl:value-of select="concat(&quot;','&quot;,$context,'/flash/playerProductInstall.swf')"/>
                            </xsl:if>
                        </xsl:if>
                        <xsl:text>');</xsl:text>
                    </xsl:for-each>
                </script>
            </xsl:if>
            <xsl:call-template name="meta-data"/>
        </xsl:copy>
    </xsl:template>

    <!-- remove http-equiv meta elements-->
    <xsl:template match="h:meta[@http-equiv]"/>

    <!-- combines the template title value with the value of the title of the source document -->
    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="concat('/',$request-uri) = '/index.html'">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($source-doc/h:head/h:title, ' - ', .)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- remove class="lw_...." and id="lw_...." -->
    <xsl:template match="@class[starts-with(.,'lw_')]|@id[starts-with(.,'lw_')]"/>

    <!-- remove class="proxy" -->
    <xsl:template match="@class[.='proxy']"/>

    <xsl:template match="@proxy"/>

    <!-- TODO did the id of the input change? -->
    <xsl:template match="h:input[@id='searchTerms']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="$search-terms != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$search-terms"/>
                </xsl:attribute>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- set the selected option of the search form -->
    <xsl:template match="h:option[parent::h:select[@id='searchSelect' or @id='source']]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="@value = $search-form-select">
                <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- add the ip-group to content of the meta element named WT.seg_1 for reporting to webtrends -->
    <xsl:template match="h:meta[@name='WT.seg_1']/@content">
        <xsl:attribute name="content">
            <xsl:value-of select="$ip-group"/>
        </xsl:attribute>
    </xsl:template>

    <!-- even/odd tr classes for striped table -->
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

    <!-- create the tabbed box markup -->
    <xsl:template match="h:div[@class='fMainBox']">
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
                            <xsl:variable name="href">
                                <xsl:value-of
                                    select="concat($context,'/',$request-uri,'?loadTab=',$id)"/>
                                <!-- add query and source so tabbed portals work as a search template -->
                                <xsl:if test="$q and $source">
                                    <xsl:value-of
                                        select="concat('&amp;source=',$source,'&amp;q=',$q)"
                                    />
                                </xsl:if>
                            </xsl:variable>
                            <a href="{$href}">
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
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:for-each select="h:h2">
                <xsl:variable name="stop-point">
                    <xsl:value-of select="last() - position()"/>
                </xsl:variable>
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
                <xsl:if
                    test="(count(parent::h:div/h:h2) = 1)
                    or ($loadTab != '' and $loadTab = $id) 
                    or ($loadTab = '' and @class = 'activeTab') 
                    or ($loadTab = '' and not(../h:h2[@class = 'activeTab']) and position()!=1)">
                    <xsl:apply-templates
                        select="following-sibling::node()[not(@id='otherPortalOptions') and count(following-sibling::h:h2) = $stop-point]"
                    />
                </xsl:if>
            </xsl:for-each>
            <xsl:if test="not(h:h2)">
                <xsl:apply-templates select="node()[not(@id='otherPortalOptions')]"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>


    <!-- ===================    LANEWEB NAMESPACE TEMPLATES  ================ -->

    <xsl:template match="h:a[@id='proxyOn']">
        <xsl:if test="contains('OTHER|PAVA|ERR',$ip-group) and $proxy-links = 'false'">
            <!--<xsl:if test="$proxy-links = 'false'">-->
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()"/>
                <xsl:attribute name="href">
                    <xsl:value-of select="concat($context,'/',$request-uri)"/>
                    <xsl:choose>
                        <xsl:when test="$query-string = ''">
                            <xsl:text>?proxy-links=true</xsl:text>
                        </xsl:when>
                        <xsl:when test="not(contains($query-string,'proxy-links=false'))">
                            <xsl:text>?</xsl:text>
                            <xsl:value-of select="$query-string"/>
                            <xsl:text>&amp;proxy-links=true</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>?</xsl:text>
                            <xsl:value-of
                                select="substring-before($query-string,'proxy-links=false')"/>
                            <xsl:text>proxy-links=true</xsl:text>
                            <xsl:value-of
                                select="substring-after($query-string,'proxy-links=false')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <xsl:template match="h:a[@id='proxyOff']">
        <xsl:if test="contains('OTHER|PAVA|ERR',$ip-group) and $proxy-links = 'true'">
            <!--<xsl:if test="$proxy-links = 'true'">-->
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()"/>
                <xsl:attribute name="href">
                    <xsl:value-of select="concat($context,'/',$request-uri)"/>
                    <xsl:choose>
                        <xsl:when test="$query-string = ''">
                            <xsl:text>?proxy-links=false</xsl:text>
                        </xsl:when>
                        <xsl:when test="not(contains($query-string,'proxy-links=true'))">
                            <xsl:text>?</xsl:text>
                            <xsl:value-of select="$query-string"/>
                            <xsl:text>&amp;proxy-links=false</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>?</xsl:text>
                            <xsl:value-of
                                select="substring-before($query-string,'proxy-links=true')"/>
                            <xsl:text>proxy-links=false</xsl:text>
                            <xsl:value-of select="substring-after($query-string,'proxy-links=true')"
                            />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:apply-templates select="child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <!-- insert the mesh term from the m parameter -->
    <xsl:template match="h:span[@class='lw_mesh']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:value-of select="$m"/>
        </xsl:copy>
    </xsl:template>

    <!-- choose which text to display for a history search result -->
    <xsl:template match="h:div[@id='lw_history-headings']">
        <xsl:apply-templates select="h:div[h:span=$t]/h:h2"/>
    </xsl:template>

    <xsl:template match="h:form[attribute::id='searchForm']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
            <xsl:if test="not($template-is-default)">
                <input type="hidden" name="template" value="{$response-template}"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->


    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="not(contains($link,'/secure/login.html')) and not($template-is-default)">
                <xsl:choose>
                    <xsl:when test="contains($link, '?')">&amp;</xsl:when>
                    <xsl:otherwise>?</xsl:otherwise>
                </xsl:choose>
                <xsl:if test="not($template-is-default)">
                    <xsl:text>template=</xsl:text>
                    <xsl:value-of select="$response-template"/>
                </xsl:if>
            </xsl:if>
        </xsl:variable>
        <xsl:attribute name="{$attr}">
            <xsl:if test="starts-with($link, '/')">
                <xsl:value-of select="$context"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="contains($link,'{keywords}')">
                    <xsl:value-of select="replace($link,'\{keywords\}',$regex-search-terms)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bkeywords%7D')">
                    <xsl:value-of select="replace($link,'%7Bkeywords%7D',$regex-search-terms)"/>
                </xsl:when>
                <xsl:when test="contains($link,'{search-terms}')">
                    <xsl:value-of select="replace($link,'\{search-terms\}',$regex-search-terms)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bsearch-terms%7D')">
                    <xsl:value-of select="replace($link,'%7Bsearch-terms%7D',$regex-search-terms)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$link"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="ends-with($link,'/')">
                <xsl:text>index.html</xsl:text>
            </xsl:if>
            <xsl:if test="name(..) != 'link' and name(..) != 'img' and not(starts-with($link,'#'))">
                <xsl:value-of select="$param-string"/>
            </xsl:if>
        </xsl:attribute>
    </xsl:template>

    <!-- the content -->
    <xsl:template name="content">
        <xsl:apply-templates select="$source-doc/h:body/node()"/>
    </xsl:template>

    <xsl:template name="meta-data">
        <xsl:if test="$source">
            <meta name="LW.source" content="{$source}"/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="tokenize-email">
        <xsl:param name="string"/>
        <xsl:value-of select="concat('|',substring($string,1,1))"/>
        <xsl:if test="string-length($string) &gt; 1">
            <xsl:call-template name="tokenize-email">
                <xsl:with-param name="string" select="substring($string,2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <!-- the breadcrumb -->
    <xsl:template name="breadcrumb">
        <xsl:call-template name="breadcrumb-section">
            <xsl:with-param name="uri-before" select="'/'"/>
            <xsl:with-param name="uri-remaining">
                <xsl:choose>
                    <!-- this is how the breadcrumb is coerced into what it should be based on faq category -->
                    <xsl:when test="$source-doc/h:head/h:meta[@name='LW.faqCategory']">
                        <xsl:value-of
                            select="substring-after($sitemap//h:a[.=$source-doc/h:head/h:meta[@name='LW.faqCategory']/@content]/@href,'/')"
                        />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$request-uri"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <!--<xsl:with-param name="uri-remaining" select="$request-uri"/>-->
        </xsl:call-template>
    </xsl:template>
    <!-- does most of the breadcrumb work -->
    <xsl:template name="breadcrumb-section">
        <xsl:param name="uri-before"/>
        <xsl:param name="uri-remaining"/>
        <xsl:variable name="uri-current" select="substring-before($uri-remaining, '/')"/>
        <xsl:variable name="label-current">
            <xsl:value-of select="$sitemap//h:a[@href=concat($uri-before,'index.html')]"/>
        </xsl:variable>
        <xsl:variable name="uri-next" select="substring-after($uri-remaining, '/')"/>
        <!--       <div>uri-before <xsl:value-of select="$uri-before"/></div>
        <div>uri-remaining <xsl:value-of select="$uri-remaining"/></div>
        <div>uri-current <xsl:value-of select="$uri-current"/></div>
        <div>label-current <xsl:value-of select="$label-current"/></div>
        <div>uri-next <xsl:value-of select="$uri-next"/></div>-->
        <xsl:choose>
            <xsl:when test="contains($uri-remaining, '/')">
                <!-- here is a hack to prevent the non-existent top level portals/index.html from appearing in breadcrumb in history portal -->
                <xsl:if test="$uri-before != '/portals/'">
                    <a>
                        <xsl:call-template name="make-link">
                            <xsl:with-param name="link" select="concat($uri-before, 'index.html')"/>
                            <xsl:with-param name="attr" select="'href'"/>
                        </xsl:call-template>
                        <xsl:attribute name="title">
                            <xsl:value-of select="concat($label-current,'...')"/>
                        </xsl:attribute>
                        <xsl:value-of select="$label-current"/>
                    </a>
                    <xsl:text>&#160;&#xBB;&#160;</xsl:text>
                </xsl:if>
                <xsl:call-template name="breadcrumb-section">
                    <xsl:with-param name="uri-before"
                        select="concat($uri-before, $uri-current, '/')"/>
                    <xsl:with-param name="uri-remaining" select="$uri-next"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$uri-before = '/' and $uri-remaining='index.html'">
                <xsl:text>LaneConnex</xsl:text>
            </xsl:when>
            <xsl:when test="$uri-remaining = 'index.html'">
                <xsl:value-of select="$label-current"/>
            </xsl:when>
            <xsl:otherwise>
                <!-- here is a hack to prevent the top level online and portals index.html from appearing in breadcrumb -->
                <xsl:if test="$uri-before != '/online/' and $uri-before != '/portals/'">
                    <a>
                        <xsl:call-template name="make-link">
                            <xsl:with-param name="link" select="concat($uri-before, 'index.html')"/>
                            <xsl:with-param name="attr" select="'href'"/>
                        </xsl:call-template>
                        <xsl:attribute name="title">
                            <xsl:value-of select="concat($label-current,'...')"/>
                        </xsl:attribute>
                        <xsl:value-of select="$label-current"/>
                    </a>
                    <xsl:text>&#160;&#xBB;&#160;</xsl:text>
                </xsl:if>
                <xsl:variable name="title">
                    <xsl:value-of select="$sitemap//h:a[@href=concat($uri-before,$uri-remaining)]"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$title = ''">
                        <xsl:value-of select="$source-doc/h:head/h:title"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$title"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="js-split">
        <xsl:param name="string"/>
        <xsl:variable name="char">
            <xsl:value-of select="substring($string,1,1)"/>
        </xsl:variable>
        <xsl:text>+'</xsl:text>
        <xsl:if test="$char = &quot;'&quot;">
            <xsl:text>\</xsl:text>
        </xsl:if>
        <xsl:value-of select="$char"/>
        <xsl:text>'</xsl:text>
        <xsl:if test="string-length($string) &gt; 1">
            <xsl:call-template name="js-split">
                <xsl:with-param name="string" select="substring($string,2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
