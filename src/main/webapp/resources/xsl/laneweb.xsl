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

    <!-- set the selected option of the search form -->
    <xsl:template match="h:option[parent::h:select[@id='searchSource']]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="@value = $search-form-select">
                <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    
    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- xincludes often include html/head and html/body, this ignores them-->
    <xsl:template match="h:html[ancestor::h:html]">
        <xsl:apply-templates select="h:body/child::node()"/>
    </xsl:template>

    <!-- put version into javascript @src -->
    <xsl:template match="h:script/@src[starts-with(.,'/javascript')]">
        <xsl:attribute name="src">
            <xsl:value-of
                select="concat($base-path,'/javascript/',$version,substring-after(.,'/javascript'))"/>
        </xsl:attribute>
    </xsl:template>

    <!-- put version into css href -->
    <xsl:template match="h:link/@href[starts-with(.,'/style')]">
        <xsl:attribute name="href">
            <xsl:value-of select="concat($base-path,'/style/',$version,substring-after(.,'/style'))"/>
        </xsl:attribute>
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

    <xsl:template match="h:a|h:area">
        <xsl:choose>
            <!-- obfuscate email addresses with javascript -->
            <xsl:when test="starts-with(@href, 'mailto:')">
                <xsl:variable name="address">
                    <xsl:text>'+'ma'+''+'il'+'to'+':'</xsl:text>
                    <xsl:call-template name="js-split">
                        <xsl:with-param name="string" select="substring-after(@href,'mailto:')"/>
                    </xsl:call-template>
                    <xsl:text>+'</xsl:text>
                </xsl:variable>
                <script type="text/javascript">
                    <xsl:comment>
                        <xsl:text>&#xD;document.write('&lt;</xsl:text>
                        <xsl:value-of select="name()"/>
                        <xsl:text> href="</xsl:text>
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
                        <xsl:text>+'&lt;/</xsl:text>
                        <xsl:value-of select="name()"/>
                        <xsl:text>&gt;');&#xD;</xsl:text>
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
    
    <!-- obfuscated email href (don't copy, processed elsewhere) -->
    <xsl:template match="attribute::href[starts-with(.,'mailto:')]"/>

    <!-- add Previous, Next, All toggles to search results -->
    <xsl:template match="h:div[@class='results-nav']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <div style="display:none">
                <xsl:apply-templates select="child::node()"/>
            </div>
            <xsl:variable name="consumable-request-uri">
                <xsl:value-of select="replace($request-uri,'/plain.*\.html','/search.html')"/>
            </xsl:variable>
            <xsl:variable name="consumable-query-string">
                <xsl:value-of select="replace($query-string,'&amp;show=\w+','')"/>
            </xsl:variable>
            <xsl:variable name="base-link">
                <xsl:value-of select="concat($consumable-request-uri,'?',$consumable-query-string,'&amp;show=')"/>
            </xsl:variable>
            <xsl:variable name="last-item">
                <xsl:choose>
                    <xsl:when test="h:span[@class='currentIndex'] + h:span[@class='result-limit'] &gt; h:span[@class='result-count']">
                        <xsl:value-of select="h:span[@class='result-count']"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="h:span[@class='currentIndex'] + h:span[@class='result-limit']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            Displaying
            <xsl:choose>
                <xsl:when test="h:span[@class='show-all'] = 'true'">
                    <xsl:value-of select="h:span[@class='currentIndex'] + 1"/> to
                    <xsl:value-of select="$last-item"/> of
                </xsl:when>
                <xsl:otherwise>all </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="h:span[@class='result-count']"/> matches.
                <!--<xsl:if test="h:span[@class='previous'] != 'false'">
                    <a href="{concat($base-link,h:span[@class='previous'])}">Previous</a>
                    </xsl:if>-->
            <span style="float:right">
                <xsl:call-template name="search-nav-counts">
                    <xsl:with-param name="show" select="h:span[@class='show']"/>
                    <xsl:with-param name="result-limit" select="number(h:span[@class='result-limit'])"/>
                    <xsl:with-param name="current" select="0"/>
                    <xsl:with-param name="current-index" select="number(h:span[@class='currentIndex'])"/>
                    <xsl:with-param name="result-count" select="number(h:span[@class='result-count'])"/>
                    <xsl:with-param name="base-link" select="$base-link"/>
                </xsl:call-template>
                <!--<xsl:if test="h:span[@class='next'] != 'false'">
                    <a href="{concat($base-link,h:span[@class='next'])}">Next</a>
                </xsl:if>-->
                <xsl:if test="h:span[@class='show-all'] = 'true'">
                    <a id="seeAll" href="{concat($base-link,'all')}">See All</a>
                </xsl:if>
                </span>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template name="search-nav-counts">
        <xsl:param name="base-link"/>
        <xsl:param name="result-count"/>
        <xsl:param name="current"/>
        <xsl:param name="current-index"/>
        <xsl:param name="show"/>
        <xsl:param name="result-limit"/>
        <xsl:variable name="label">
            <xsl:choose>
                <xsl:when test="$current = 0">1</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="($current div $result-limit) + 1"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:if test="$result-count > $result-limit and $current >= 0 and $current &lt; $result-count and $show != 'all'">
          <xsl:choose>
            <xsl:when test="$current = $current-index">
              <xsl:value-of select="$label"/>
            </xsl:when>
            <xsl:otherwise>
              <a href="{concat($base-link,$current)}"><xsl:value-of select="$label"/></a>
            </xsl:otherwise>
          </xsl:choose>
            <xsl:if test="$label &lt; 10 and $current + $result-limit &lt; $result-count">
                <xsl:text> | </xsl:text>
                <xsl:call-template name="search-nav-counts">
                    <xsl:with-param name="show" select="$show"/>
                    <xsl:with-param name="current" select="$current + $result-limit"/>
                    <xsl:with-param name="current-index" select="$current-index"/>
                    <xsl:with-param name="result-count" select="$result-count"/>
                    <xsl:with-param name="result-limit" select="$result-limit"/>
                    <xsl:with-param name="base-link" select="$base-link"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <!-- 
                //FIXME: uncomment before putting into production
                
            <xsl:when
                test="starts-with(.,'http://lane.stanford.edu') and not(contains(.,'cookiesFetch'))">
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link"
                        select="substring-after(.,'http://lane.stanford.edu')"/>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:when>
            -->
            <xsl:when test="contains(., '://') and contains(.,'{keywords}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{keywords\}',$regex-query)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and contains(.,'{search-terms}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{search-terms\}',$regex-query)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="starts-with(.,'http://') and starts-with($path,'/secure')">
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
                    <xsl:value-of select="concat($base-path,.)"/>
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
            <xsl:value-of select="$path"/>
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
                <script type="text/javascript" src="{$base-path}/javascript/{$version}/swfobject.js"><xsl:text> </xsl:text></script>
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
            <xsl:call-template name="meta-data"/>
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

    <!-- TODO did the id of the input change? -->
    <xsl:template match="h:input[@name='q']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:if test="$query != ''">
                <xsl:attribute name="value">
                    <xsl:value-of select="$query"/>
                </xsl:attribute>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- add clinical class to search form, fieldset and laneNav elements when clinical is active tab -->
    <xsl:template match="node()[@id='search' or @id='laneNav']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='class')]"/>
            <xsl:if test="$search-form-select = 'clinical-all'">
                <xsl:attribute name="class">clinical</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- add sourceid input to search form if sourceid param present -->
    <xsl:template match="h:fieldset[@id='searchFields' or parent::h:form[@class='breadcrumbForm']]">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:if test="$sourceid">
                <input type="hidden" name="sourceid" value="{$sourceid}"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    
    <!-- add sourceid input to quick link options if sourceid param present -->
    <xsl:template match="h:option[parent::h:select[@id='qlinks']]">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$sourceid and starts-with(@value,'/')">
                    <xsl:apply-templates select="attribute::node()[not(name()='value')]"/>
                    <xsl:attribute name="value">
                        <xsl:value-of select="concat(@value,'?sourceid=',$sourceid)"/>
                    </xsl:attribute>
                    <xsl:apply-templates/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:otherwise>
            </xsl:choose>
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
    
    <!-- the next 6 template matches handle the login state and show links depending on that state -->
    <!-- process the list only if off campus -->
    <xsl:template match="h:ul[attribute::id='login']">
        <xsl:if test="matches($ipgroup,'^(OTHER|PAVA|LPCH|SHC|ERR)$')">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 1st #login li is the login link or the users name -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[1]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="string-length($name) &gt; 0">
                    <xsl:value-of select="$name"/>
                </xsl:when>
                <xsl:when test="string-length($sunetid) &gt; 0">
                    <xsl:value-of select="$sunetid"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <!-- the 2nd #login li is the account link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[2]">
        <xsl:if test="string-length($sunetid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    
    <!-- the 3rd #login li is the logout link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[3]">
        <xsl:if test="string-length($sunetid) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 4rd #login li is the proxy-off toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[4]">
      <xsl:if test="string-length($sunetid) = 0 and $proxy-links = 'true'">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 5th #login li is the proxy-on toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[5]">
      <xsl:if test="string-length($sunetid) = 0 and $proxy-links = 'false'">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the following several templates add class to yui grid divs for custom widths -->
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']/h:div[@class='yui-gf']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-gf']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']/h:div[@class='yui-gf']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' middleColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' rightColumn')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-ge' or @class='yui-ge search']/h:div[@class='yui-u first']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' leftGrids')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:body/h:div[@class='yui-gf']/h:div[@class='yui-u']">
        <xsl:copy>
            <xsl:attribute name="class" select="concat(@class, ' rightGrids')"/>
            <xsl:apply-templates select="attribute::node()[not(name() = 'class')]"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->


    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="not(contains($link,'/secure/login.html'))">
                <xsl:choose>
                    <xsl:when test="contains($link, '?')">&amp;</xsl:when>
                    <xsl:otherwise>?</xsl:otherwise>
                </xsl:choose>
                <xsl:if test="$sourceid">
                    <xsl:text>sourceid=</xsl:text>
                    <xsl:value-of select="$sourceid"/>
                </xsl:if>
            </xsl:if>
        </xsl:variable>
        <xsl:attribute name="{$attr}">
            <!-- prepend the base-path if it is an absolute link -->
            <xsl:if test="starts-with($link, '/') and not(starts-with($link,$base-path))">
                <xsl:value-of select="$base-path"/>
            </xsl:if>
            <!-- replace keywords/search-terms TODO: unify this so only replaceing one thing -->
            <xsl:choose>
                <xsl:when test="contains($link,'{keywords}')">
                    <xsl:value-of select="replace($link,'\{keywords\}',$regex-query)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bkeywords%7D')">
                    <xsl:value-of select="replace($link,'%7Bkeywords%7D',$regex-query)"/>
                </xsl:when>
                <xsl:when test="contains($link,'{search-terms}')">
                    <xsl:value-of select="replace($link,'\{search-terms\}',$regex-query)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bsearch-terms%7D')">
                    <xsl:value-of select="replace($link,'%7Bsearch-terms%7D',$regex-query)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$link"/>
                </xsl:otherwise>
            </xsl:choose>
            <!-- replace links ending with / so they end with /index.html -->
            <xsl:if test="ends-with($link,'/')">
                <xsl:text>index.html</xsl:text>
            </xsl:if>
            <xsl:if test="$sourceid and name(..) != 'link' and name(..) != 'img' and not(starts-with($link,'#'))">
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
