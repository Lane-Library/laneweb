<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:template="http://lane.stanford.edu/ns/template"
    exclude-result-prefixes="h template">

    <xsl:strip-space elements="h:html h:head h:body h:div h:p h:form h:map h:select h:table h:tr h:td h:ul h:li"/>

    <!-- ===========================  PARAMETERS ========================= -->
    <!-- the request-uri ( not including parameters ) -->
    <xsl:param name="request-uri"/>

    <!-- the base-path parameter ( i.e. the base url of the webapp ) -->
    <xsl:param name="base-path"/>

    <!-- the query part of the request -->
    <xsl:param name="query-string"/>

    <!-- the search query -->
    <xsl:param name="query"/>

    <!-- PICO -->
    <xsl:param name="p"/>
    <xsl:param name="i"/>
    <xsl:param name="c"/>
    <xsl:param name="o"/>

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="userid"/>
    
    <xsl:param name="email"/>

    <!-- a MeSH term -->
    <xsl:param name="mesh"/>

    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ipgroup"/>

    <!-- whether or not live chat is scheduled to be available -->
    <xsl:param name="live-chat-available"/>

    <!-- today's hours computed from includes/hours.xml -->
    <xsl:param name="todays-hours"/>

    <xsl:param name="version"/>

    <xsl:param name="referrer"/>

    <xsl:param name="name"/>

    <!-- sourceid used for tracking to ID request origin: shc, cerner, laneconnex-engine, etc. -->
    <xsl:param name="sourceid"/>

    <!-- this is the a= parameter for the online resources -->
    <xsl:param name="alpha"/>
    
    <!-- json version of the data model -->
    <xsl:param name="model"/>

    <!-- boolean: is app running in DR mode -->
    <xsl:param name="disaster-mode"/>

    <!-- target parameter for shibboleth discovery service page -->
    <xsl:param name="return"/>

    <xsl:param name="facets"/>
    
    <xsl:param name="sort"/>
    
    <!-- ==========================  VARIABLES  ========================== -->

    <!-- the root node of the requested content document -->
    <xsl:variable name="source-doc" select="/template:doc/h:html[1]"/>

    <xsl:variable name="path">
        <xsl:value-of select="substring($request-uri,string-length($base-path) + 1)"/>
    </xsl:variable>

    <xsl:variable name="regex-query">
        <xsl:if test="$query">
            <xsl:value-of select="replace($query,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>
    
    <xsl:variable name="path-and-query">
        <xsl:choose>
            <xsl:when test="$query-string != ''">
            <xsl:value-of select="concat( $path, '?', $query-string)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$path"/>        
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="source-prefix">
        <xsl:value-of select="substring-before($source,'-')"/>
    </xsl:variable>

    <xsl:variable name="search-source">
        <xsl:choose>
            <xsl:when test="starts-with($path,'/portals/ethics')">all-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/careercenter')">all-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/bioresearch')">bioresearch-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/peds')">peds-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals') and not(starts-with($path,'/portals/lpch-cerner'))">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/search/clinical')">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/classes-consult/infoliteracy')">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/bassett')">bassett</xsl:when>
            <xsl:when test="starts-with($path,'/biomed-resources/bassett')">bassett</xsl:when>
            <xsl:when test="ends-with($path,'-viaLane.html')">all-all</xsl:when>
            <xsl:when test="$source">
                <xsl:choose>
                    <xsl:when test="$source='peds-all'">clinical-all</xsl:when>
                    <!-- various -images-all source parameters get images-all -->
                    <xsl:when test="ends-with($source, 'images-all')">images-all</xsl:when>
                    <xsl:when test="string-length($source-prefix) &gt; 0">
                        <xsl:value-of select="concat($source-prefix,'-all')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$source"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>all-all</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <!-- ====================  INCLUDED TEMPLATES ============================= -->
    <xsl:include href="laneweb-forms.xsl"/>
    <xsl:include href="laneweb-links.xsl"/>
    <xsl:include href="laneweb-login.xsl"/>

    <!-- ====================  DEFAULT TEMPLATES ============================= -->
    <!-- applies templates on the template document -->
    <xsl:template match="/template:doc">
        <xsl:apply-templates select="h:html[2]"/>
    </xsl:template>
    
    <!-- when there is not a template (ie the request is for /plain/**.html) process whole document -->
    <xsl:template match="/h:html">
        <xsl:apply-templates select="child::node()"/>
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
            <xsl:when test=".='search-terms'">
                <xsl:value-of select="$query"/>
            </xsl:when>
            <xsl:when test=".='mesh'">
                <xsl:value-of select="$mesh"/>
            </xsl:when>
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
            </xsl:when>
            <xsl:when test=".='todays-hours'">
                <xsl:value-of select="$todays-hours"/>
            </xsl:when>
            <xsl:when test=".='bookmarks' and string-length($userid) &gt; 0">
                <xi:include xmlns:xi="http://www.w3.org/2001/XInclude" href="cocoon://bookmarks/list.html">
                    <xi:fallback/>
                </xi:include>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- xincludes often include html/head and html/body, this ignores them-->
    <xsl:template match="h:html[ancestor::h:html]">
        <xsl:apply-templates select="h:body/child::node()"/>
    </xsl:template>

    <!-- put script text into a comment so saxon won't convert entities -->
    <xsl:template match="h:script[string-length(normalize-space()) &gt; 0]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:comment>
                <xsl:apply-templates select="child::node()"/>
                <xsl:text>//</xsl:text>
            </xsl:comment>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:script[@id='model']/text()">
        <xsl:text>
            var model = </xsl:text>
        <xsl:value-of select="$model"/>
        <xsl:text>;
        </xsl:text>
    </xsl:template>
    
    <!-- match and copy the template body with the attributes from the content body -->
    <xsl:template match="h:body">
        <xsl:copy>
            <xsl:apply-templates select="$source-doc/h:body/attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- =====================  SPECIAL CASE TEMPLATES ===================== -->

    <!-- get all the head elements from template and all non title head elements from source (with some exceptions)-->
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="child::node()"/>
            <xsl:apply-templates select="$source-doc/h:head/node()[not(self::h:title)]"/>
        </xsl:copy>
    </xsl:template>

    <!-- remove http-equiv meta elements-->
    <xsl:template match="h:meta[@http-equiv != 'refresh']"/>

    <!-- combines the template title value with the value of the title of the source document -->
    <xsl:template match="h:title">
        <xsl:variable name="source-doc-title">
            <xsl:value-of select="replace($source-doc/h:head/h:title,'\{search-terms\}',$regex-query)"/>
        </xsl:variable>
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$path = '/index.html' or $path = '/m/index.html'">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($source-doc-title, ' - ', .)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- add class="nav-menu-active" to .nav-menu li when the path matches -->
    <xsl:template match="h:li[attribute::class='dropdown nav-menu']/@class">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="starts-with($path, substring-before(parent::h:li/h:a/@href, 'index.html'))">
                    <xsl:value-of select="concat(., ' nav-menu-active')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <!-- add active to class to search form query present -->
    <xsl:template match="h:form[@class='search-form' and $query]/@class">
        <xsl:attribute name="class" select="'search-form search-form-active search-form-results'"/>
    </xsl:template>
    
    <!-- add active to the pico-toggle link if $search-source=clinical|peds-all -->
    <xsl:template match="h:span[@class='pico-toggle' and matches($search-source,'(clinical|peds)-all')]/@class">
        <xsl:attribute name="class" select="'pico-toggle pico-toggle-active'"/>
    </xsl:template>
    
    <!-- add active to the pico-on link if $search-source=clinical|peds-all and no PICO values present -->
    <xsl:template match="h:span[@class='pico-on' and matches($search-source,'(clinical|peds)-all') and not($p or $i or $c or $o)]/@class">
        <xsl:attribute name="class" select="'pico-on pico-on-active'"/>
    </xsl:template>

    <!-- add active to the pico-off link if PICO values present -->
    <xsl:template match="h:span[@class='pico-off' and ($p or $i or $c or $o)]/@class">
        <xsl:attribute name="class" select="'pico-off pico-off-active'"/>
    </xsl:template>
    
    <!-- add active to the pico-fields fieldset if $search-source=clinical|peds-all and PICO values present -->
    <xsl:template match="h:fieldset[@class='pico-fields' and matches($search-source,'(clinical|peds)-all') and ($p or $i or $c or $o) ]/@class">
        <xsl:attribute name="class" select="'pico-fields pico-fields-active'"/>
    </xsl:template>
    
    <!-- make the .search-reset active if there is a query -->
    <xsl:template match="h:div[@class='search-reset' and $query]/@class">
        <xsl:attribute name="class" select="'search-reset search-reset-active'"/>
    </xsl:template>
    
    <!-- add active to the appropriate search-tab -->
    <xsl:template match="h:div[@class='search-tab' and @data-source = $search-source]/@class">
        <xsl:attribute name="class" select="'search-tab search-tab-active'"/>
    </xsl:template>
    
    <!-- add active to the clinical search-tab if search-source is peds-all-->
    <xsl:template match="h:div[@class='search-tab' and $search-source = 'peds-all' and @data-source = 'clinical-all']/@class">
        <xsl:attribute name="class" select="'search-tab search-tab-active'"/>
    </xsl:template>

    <!-- put the @data-help value of the active tab into the href of .search-help -->
    <xsl:template match="h:a[@class='search-help']/@href">
        <xsl:attribute name="href" select="concat($base-path, ancestor::h:form[@class='search-form']//h:div[@class='search-tab'][@data-source = $search-source]/@data-help)"/>
    </xsl:template>

    
    <!-- att class menuitem-active to the menu item link for the current page -->
    <xsl:template match="h:ul[contains(@class,'menu')]/h:li/h:a[@href=$path-and-query]">
      <a class="menuitem-active">
          <xsl:apply-templates select="attribute::node()|child::node()"/>
      </a>
    </xsl:template>

    <!-- disable live chat if not scheduled to be available -->
    <xsl:template match="h:*[@class = 'live-chat']">
        <xsl:copy>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="$live-chat-available = 'false'">live-chat-inactive</xsl:when>
                    <xsl:otherwise>live-chat</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')] | child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- add a div with class to grids so they can be separated by a gutter -->

    <!-- left most grid has class gr, priority in case only one grid -->
    <xsl:template match="h:div[@class='yui3-g']/h:div[1]" priority="1">
            <xsl:copy>
                <xsl:apply-templates select="@*"/>
                <div class="gr">
                    <xsl:apply-templates select="child::node()"/>
                </div>
            </xsl:copy>
    </xsl:template>

    <!-- right most grid has class gl -->
    <xsl:template match="h:div[@class='yui3-g']/h:div[last()]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <div class="gl">
                <xsl:apply-templates select="child::node()"/>
            </div>
        </xsl:copy>
    </xsl:template>

    <!-- middle grids have class gb -->
    <xsl:template match="h:div[@class='yui3-g']/h:div[position() &gt; 1 and position() &lt; last()]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <div class="gb">
                <xsl:apply-templates select="child::node()"/>
            </div>
        </xsl:copy>
    </xsl:template>

    <!-- add a div with class landing-content inside .module.landing so it can be given padding -->
    <xsl:template match="h:div[@class='module landing']">
        <xsl:copy>
            <xsl:apply-templates select="@*|*[position() = 1 or position() = 2]"/>
            <div class="landing-content">
                <xsl:apply-templates select="*[not(position() = 1 or position() = 2)]"/>
            </div>
        </xsl:copy>
    </xsl:template>

    <!-- add class="golfclub" to h2 so that the golf club images can be positioned correctly-->
    <xsl:template match="h:h2[not(@class='golfclub')][ancestor::h:html = $source-doc]">
        <h2 class="golfclub">
            <xsl:apply-templates/>
        </h2>
    </xsl:template>
    
    <!-- if an image search, change the search select option value to the source of the request tab -->
    <xsl:template match="h:option[@value='images-all']/@value">
        <xsl:choose>
            <xsl:when test="contains($source, '-images-all')">
                <xsl:attribute name="value">
                    <xsl:value-of select="$source"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->


    <!-- the content -->
    <xsl:template name="content">
        <xsl:apply-templates select="$source-doc/h:body/node()"/>
    </xsl:template>

</xsl:stylesheet>
