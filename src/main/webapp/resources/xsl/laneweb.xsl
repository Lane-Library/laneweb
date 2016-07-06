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

    <!-- here is the information associating urls with what is the laneNav active tab -->
    <xsl:variable name="laneNav-tabs">
        <div><span>All Resources</span><span>/biomed-resources</span></div>
        <div><span>Specialty Portals</span><span>/portals</span></div>
        <div><span>Classes &amp; Consulting</span><span>/classes-consult</span></div>
        <div><span>Using the Library</span><span>/using-lib</span></div>
        <div><span>About Lane</span><span>/about</span></div>
        <div><span>How To</span><span>/help</span></div>
    </xsl:variable>

    <xsl:variable name="source-prefix">
        <xsl:value-of select="substring-before($source,'-')"/>
    </xsl:variable>

    <xsl:variable name="search-form-select">
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

    <!-- add 'current' class to li with a child a with current href -->
    <!-- TODO: reexamine this priority, should this be more specific?  -->
    <xsl:template match="h:li[h:a/@href = $path][not(parent::h:ul[attribute::class='lane-nav'])]" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='class')]"/>
            <xsl:attribute name="class">
                <xsl:if test="@class">
                    <xsl:value-of select="@class"/>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:text>current</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="node()"/>
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

    <!-- add clinical class to search form when clinical or peds is active -->
    <xsl:template match="node()[@id='search']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='class')]"/>
            <xsl:if test="$search-form-select = 'clinical-all' or starts-with($search-form-select,'peds')">
                <xsl:attribute name="class">clinical</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- add class="active" to lane-nav li when the path matches -->
    <xsl:template match="h:ul[attribute::class='lane-nav']/h:li">
        <xsl:variable name="link-content" select="child::h:a/text()"/>
        <xsl:variable name="active-tab" select="$laneNav-tabs/h:div[h:span[1]=$link-content]"/>
        <xsl:variable name="active" select="starts-with($path, $active-tab/h:span[2])"/>
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:if test="$active">
                <xsl:attribute name="class">active</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- add class="active" to online resource alpha browse links if link is for current page -->
    <xsl:template match="h:ul[attribute::id='browseTabs']/h:li/h:a[(matches(@href, concat('a=',$alpha,'$')) or (matches(@href, 'a=%23') and $alpha = '#') or @href = $path)]">
        <xsl:copy>
            <xsl:attribute name="class">active</xsl:attribute>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
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
