<xsl:stylesheet version="2.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="h">

    <xsl:strip-space
        elements="h:html h:head h:body h:div h:p h:form h:map h:select h:table h:tr h:td h:ul h:li"/>

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

    <xsl:param name="sunetid"/>

    <!-- a MeSH term -->
    <xsl:param name="mesh"/>

    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ipgroup"/>

    <xsl:param name="version"/>

    <xsl:param name="referrer"/>

    <xsl:param name="name"/>

    <!-- sourceid used for tracking to ID request origin: shc, cerner, laneconnex-engine, etc. -->
    <xsl:param name="sourceid"/>

    <!-- this is the a= parameter for the online resources -->
    <xsl:param name="alpha"/>

    <!-- this is the parent document where there is one (eg /a/b.html for /a/b-sub.html) -->
    <xsl:param name="parent-path"/>

    <!-- ==========================  VARIABLES  ========================== -->

    <!-- the root node of the requested content document -->
    <xsl:variable name="source-doc" select="/*/h:html[1]"/>

    <!-- the template document -->
    <xsl:variable name="template" select="/*/h:html[2]"/>

    <xsl:variable name="path">
        <xsl:value-of select="substring($request-uri,string-length($base-path) + 1)"/>
    </xsl:variable>

    <xsl:variable name="regex-query">
        <xsl:if test="$query">
            <xsl:value-of select="replace($query,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>

    <!-- here is the information associating urls with what is the laneNav active tab -->
    <xsl:variable name="laneNav-tabs">
        <div><span>Biomed Resources</span><span>/biomed-resources</span></div>
        <div><span>Specialty Portals</span><span>/portals</span></div>
        <div><span>Classes &amp; Consulting</span><span>/classes-consult</span></div>
        <div><span>History Center</span><span>/med-history</span></div>
        <div><span>About Lane</span><span>/about</span></div>
        <div><span>How To</span><span>/help</span></div>
    </xsl:variable>

    <xsl:variable name="source-prefix">
        <xsl:value-of select="substring-before($source,'-')"/>
    </xsl:variable>

    <xsl:variable name="search-form-select">
        <xsl:choose>
            <xsl:when test="starts-with($path,'/portals/ethics')">all-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/bioresearch')">bioresearch-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals/peds')">peds-all</xsl:when>
            <xsl:when test="starts-with($path,'/portals') and not(starts-with($path,'/portals/lpch-cerner'))">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/search/clinical')">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/classes-consult/infoliteracy')">clinical-all</xsl:when>
            <xsl:when test="starts-with($path,'/med-history')">history-all</xsl:when>
            <xsl:when test="starts-with($path,'/bassett')">bassett</xsl:when>
            <xsl:when test="starts-with($path,'/biomed-resources/bassett')">bassett</xsl:when>
            <xsl:when test="ends-with($path,'-viaLane.html')">all-all</xsl:when>
            <xsl:when test="string-length($source-prefix) &gt; 0">
                <xsl:value-of select="concat($source-prefix,'-all')"/>
            </xsl:when>
            <xsl:when test="$source">
                <xsl:value-of select="$source"/>
            </xsl:when>
            <xsl:otherwise>all-all</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <!-- ====================  INCLUDED TEMPLATES ============================= -->
    <xsl:include href="laneweb-email.xsl"/>
    <xsl:include href="laneweb-forms.xsl"/>
    <xsl:include href="laneweb-links.xsl"/>
    <xsl:include href="laneweb-login.xsl"/>
    <xsl:include href="laneweb-yui-grid.xsl"/>

    <!-- ====================  DEFAULT TEMPLATES ============================= -->
    <!-- root template applies templates on the template document -->
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="$template">
                <xsl:apply-templates select="$template"/>
            </xsl:when>
            <!-- when there is not a template (ie the request is for /plain/**.html) process whole document -->
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
            <xsl:when test=".='search-terms'">
                <xsl:value-of select="$query"/>
            </xsl:when>
            <xsl:when test=".='mesh'">
                <xsl:value-of select="$mesh"/>
            </xsl:when>
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
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

    <!-- setup flash object, using swfobject -->
    <xsl:template match="h:object[@type='application/x-shockwave-flash']">
        <xsl:copy>
            <xsl:attribute name="classid">clsid:D27CDB6E-AE6D-11cf-96B8-444553540000</xsl:attribute>
            <xsl:apply-templates select="@*[not(name()='type' or name() = 'data')]"/>
            <param name="movie" value="{@data}"/>
            <param name="wmode" value="transparent"/>
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

    <!-- add 'current' class to li with a child a with current href -->
    <!-- TODO: reexamine this priority, should this be more specific?  -->
    <xsl:template match="h:li[h:a/@href = $path][not(parent::h:ul[attribute::id='laneNav'])]" priority="-1">
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
    <!-- and add the swfobject.js stuff if necessary -->
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="child::node()"/>
            <xsl:apply-templates select="$source-doc/h:head/node()[not(self::h:title)]"/>
            <xsl:if
                test="$source-doc/h:body//h:object[@type='application/x-shockwave-flash' and @id]">
                <script type="text/javascript" src="{$base-path}/resources/javascript/swfobject.js?{$version}"><xsl:text> </xsl:text></script>
                <script type="text/javascript">
                    <xsl:for-each select="$source-doc/h:body//h:object[@type='application/x-shockwave-flash' and @id]">
                        <xsl:text>swfobject.registerObject('</xsl:text>
                        <xsl:value-of select="@id"/>
                        <xsl:if test="h:param[@name='flash-version']">
                            <xsl:text>','</xsl:text>
                            <xsl:value-of select="h:param[@name='flash-version']/@value"/>
                            <xsl:if test="h:param[@name='flash-for-upgrade']/@value = 'true'">
                                <xsl:value-of select="concat(&quot;','&quot;,$base-path,'/flash/playerProductInstall.swf')"/>
                            </xsl:if>
                        </xsl:if>
                        <xsl:text>');</xsl:text>
                    </xsl:for-each>
                </script>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- remove http-equiv meta elements-->
    <xsl:template match="h:meta[@http-equiv != 'refresh']"/>

    <!-- combines the template title value with the value of the title of the source document -->
    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$path = '/index.html' or $path = '/m/index.html'">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($source-doc/h:head/h:title, ' - ', .)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- add clinical class to search form, fieldset and laneNav elements when clinical or peds is active -->
    <xsl:template match="node()[@id='search' or @id='laneNav']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='class')]"/>
            <xsl:if test="$search-form-select = 'clinical-all' or starts-with($search-form-select,'peds')">
                <xsl:attribute name="class">clinical</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- add the ip-group to content of the meta element named WT.seg_1 for reporting to webtrends -->
    <xsl:template match="h:meta[@name='WT.seg_1']/@content">
        <xsl:attribute name="content">
            <xsl:value-of select="$ipgroup"/>
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

    <!-- add class="active" to laneNav li when the path matches -->
    <xsl:template match="h:ul[attribute::id='laneNav']/h:li">
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
    <xsl:template match="h:ul[attribute::id='browseTabs']/h:li/h:a[ends-with(@href, concat('?a=',$alpha)) or @href = $path]">
        <xsl:copy>
            <xsl:attribute name="class">active</xsl:attribute>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- surround <a> with <em> for the menu item for the current page -->
    <xsl:template match="h:ul[contains(@class,'sectionMenu')]/h:li//h:a[@href=$path]">
      <em><xsl:copy><xsl:apply-templates select="attribute::node() | child::node()"/></xsl:copy></em>
    </xsl:template>

    <!-- ugly hack to get all the resource pages to cause the main menu link to highlight,
        gets a priority=1 because ambiguous with the preceding template-->
    <xsl:template match="h:ul[contains(@class, 'sectionMenu')]/h:li/h:a[starts-with(@href, '/biomed-resources/')]" priority="1">
        <xsl:variable name="href-base" select="substring-before(@href, '.html')"/>
        <xsl:choose>
            <xsl:when test="starts-with($path, $href-base)">
                <em><xsl:copy><xsl:apply-templates select="attribute::node()|child::node()"/></xsl:copy></em>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy><xsl:apply-templates select="attribute::node()|child::node()"/></xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- add class="expanded" to sectionMenu li that are links to the current page and are expandies -->
    <xsl:template match="h:ul[contains(@class,'sectionMenu')]/h:li[h:div/h:a[@href=$path or @href=$parent-path]]">
        <xsl:copy>
            <xsl:attribute name="class">expanded</xsl:attribute>
            <div><em><xsl:apply-templates select="child::h:div/h:a"/></em></div>
            <xsl:apply-templates select="child::h:ul"/>
        </xsl:copy>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->


    <!-- the content -->
    <xsl:template name="content">
        <xsl:apply-templates select="$source-doc/h:body/node()"/>
    </xsl:template>

</xsl:stylesheet>
