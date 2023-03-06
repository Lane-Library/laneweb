<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:template="http://lane.stanford.edu/ns/template"
    exclude-result-prefixes="h template">

    <xsl:strip-space elements="h:html h:head h:body h:p h:form h:map h:select h:table h:tr h:td h:ul h:li"/>

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

    <xsl:param name="version"/>

    <xsl:param name="referrer"/>

    <xsl:param name="name"/>

    <!-- sourceid used for tracking to ID request origin: shc, cerner, laneconnex-engine, etc. -->
    <xsl:param name="sourceid"/>

    <!-- this is the a= parameter for the online resources -->
    <xsl:param name="alpha"/>

    <!-- json version of the data model -->
    <xsl:param name="model"/>

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

    <xsl:variable name="search-source">
        <xsl:choose>
            <xsl:when test="starts-with($path,'/portals/clinical')">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/peds')">peds-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals')">all-all</xsl:when>
            <xsl:when test="starts-with($path,'/search/clinical')">clinical-all</xsl:when>
            <xsl:when test="contains($path,'/lanecatalog')">catalog-all</xsl:when>
            <xsl:when test="contains($path,'/picosearch')">clinical-all</xsl:when>
            <xsl:when test="$source"><xsl:value-of select="$source"/></xsl:when>
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
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
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

    <!-- add class="hero-unit-front-page" to .hero-unit div on the home page -->
    <xsl:template match="h:div[attribute::class='hero-unit' and $path='/index.html']/@class">
        <xsl:attribute name="class">
            <xsl:value-of select="concat(., ' hero-unit-front-page')"/>
        </xsl:attribute>
    </xsl:template>

    <!-- don't copy data-path attribute referenced above -->
    <xsl:template match="h:li/@data-path"/>

    <!-- add active to class to search form query present -->
    <xsl:template match="h:form[@class='search-form' and $query]/@class">
        <xsl:attribute name="class" select="'search-form search-form-active search-form-results'"/>
    </xsl:template>

    <!-- add active to the pico container is on if $search-source=clinical-all  -->
    <xsl:template match="h:div[@class='search-pico small-screen-hide' and (matches($search-source,'(clinical|peds)-all') or $path='/picosearch.html')]/@class">
        <xsl:attribute name="class" select="'search-pico search-pico-active small-screen-hide'"/>
    </xsl:template>


    <!-- add active to class to search form for search help pages -->
    <xsl:template match="h:form[@class='search-form' and matches($path,'/(pico|lane)search.html')]/@class">
        <xsl:attribute name="class" select="'search-form search-form-active search-form-results'"/>
    </xsl:template>

    <!-- add active to the pico-on link if $search-source=clinical-all and no PICO values present -->
    <xsl:template match="h:span[@class='pico-on' and matches($search-source,'(clinical|peds)-all') and not($p or $i or $c or $o) and $path!='/picosearch.html']/@class">
        <xsl:attribute name="class" select="'pico-on pico-on-active'"/>
    </xsl:template>

    <!-- add active to the pico-off link if PICO values present or path is /picosearch.html -->
    <xsl:template match="h:span[@class='pico-off' and ($p or $i or $c or $o or $path='/picosearch.html')]/@class">
        <xsl:attribute name="class" select="'pico-off pico-off-active'"/>
    </xsl:template>

    <!-- add active to the pico-fields fieldset if $search-source=clinical|peds-all and PICO values present or path is /picosearch.html -->
    <xsl:template match="h:fieldset[contains(@class,'pico-fields') and (matches($search-source,'(clinical|peds)-all') and ($p or $i or $c or $o) or $path='/picosearch.html')]/@class">
        <xsl:attribute name="class" select="'pico-fields pico-fields-active'"/>
    </xsl:template>

    <!-- enable pico fields if one or more pico input values present or path is /picosearch.html-->
    <xsl:template match="h:input[(($p or $i or $c or $o) and (@name='p' or @name='i' or @name = 'c' or @name = 'o')) or $path='/picosearch.html']/@disabled"/>

    <!-- make the .search-reset active if there is a query -->
    <xsl:template match="h:div[@class='search-reset' and $query]/@class">
        <xsl:attribute name="class" select="'search-reset search-reset-active'"/>
    </xsl:template>

    <!-- activate the appropriate search option -->
    <xsl:template match="h:select[@id='main-search']/h:option[@value = $search-source or ($search-source = 'peds-all' and @value = 'clinical-all')]">
        <xsl:copy>
            <xsl:attribute name="selected" select="'selected'"/>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- select the appropriate search option label -->
    <xsl:template match="h:span[@id='search-dropdown-label']/text()">
        <xsl:choose>
            <xsl:when test="$search-source = 'peds-all'">
                <xsl:value-of select="../../h:select[@id='main-search']/h:option[@value = 'clinical-all']/text()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="../../h:select[@id='main-search']/h:option[@value = $search-source]/text()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- put the @data-help value of the active tab into the href of .search-help -->
    <xsl:template match="h:div[@class='search-help']/h:a/@href">
        <xsl:variable name="related-option">
            <xsl:choose>
                <xsl:when test="$search-source='peds-all'">
                    <xsl:value-of select="'clinical-all'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$search-source"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:attribute name="href" select="concat($base-path, ancestor::h:form[@class='search-form']//h:option[@value = $related-option]/@data-help)"/>
    </xsl:template>


    <!-- att class menuitem-active to the menu item link for the current page -->
    <xsl:template match="h:div[contains(@class,'module menu-container')]/h:ul//h:li/h:a[@href=$path-and-query]">
      <a class="menuitem-active">
          <xsl:apply-templates select="attribute::node()|child::node()"/>
      </a>
    </xsl:template>


    <!-- disable live chat if not scheduled to be available -->
    <xsl:template match="h:a[@class = 'live-chat']">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$live-chat-available = 'false'">
                    <xsl:copy-of select="@*[name() = 'class']"/>
                    <xsl:apply-templates select="*[@name = 'chat-inactive']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="@* | text()"/>
                    <xsl:apply-templates select="*[@name = 'chat-active']" />
                </xsl:otherwise>
            </xsl:choose>
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

    
    <!-- ======================  NAMED TEMPLATES  =========================== -->


    <!-- the content -->
    <xsl:template name="content">
        <xsl:apply-templates select="$source-doc/h:body/node()"/>
    </xsl:template>

</xsl:stylesheet>
