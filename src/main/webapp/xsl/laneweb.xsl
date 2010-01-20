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
    <!-- the base-path parameter ( i.e. the base url of the webapp ) -->
    <xsl:param name="base-path"/>
    <!-- the query part of the request -->
    <xsl:param name="query-string"/>

    <!-- the search query -->
    <xsl:param name="q"/>

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="sunetid"/>

    <!-- a MeSH term -->
    <xsl:param name="mesh-term"/>

    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="ip-group"/>

    <xsl:param name="version"/>

    <xsl:param name="referrer"/>

    <xsl:param name="name"/>
    
    <xsl:param name="search-tab"/>
    

    <!-- ==========================  VARIABLES  ========================== -->
    <!-- the default template -->
    <xsl:variable name="default-template" select="'template'"/>
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
    
    <xsl:variable name="path">
        <xsl:value-of select="substring($request-uri,string-length($base-path) + 1)"/>
    </xsl:variable>

    <xsl:variable name="search-terms">
        <xsl:choose>
            <xsl:when test="$q">
                <xsl:value-of select="$q"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="regex-search-terms">
        <xsl:if test="$q">
            <xsl:value-of select="replace($q,'(\\|\$)','\\$1')"/>
        </xsl:if>
    </xsl:variable>
    
    <xsl:variable name="uri-encoded-search-terms">
        <xsl:if test="$q">
            <xsl:value-of select="encode-for-uri($q)"/>
        </xsl:if>
    </xsl:variable>
    
    <!-- here is the information associating urls with what is the laneNav active tab -->
    <xsl:variable name="laneNav-tabs">
        <div><span>Biomedical Resources</span><span>/biomed-resources</span></div>
        <div><span>SUMC Specialties</span><span>/sumc-specialties</span></div>
        <div><span>Information Literacy</span><span>/info-literacy</span></div>
        <div><span>Library Services</span><span>/services</span></div>
        <div><span>Help</span><span>/help</span></div>
        <div><span>History Center</span><span>/med-history</span></div>
    </xsl:variable>
    
    <xsl:variable name="source-prefix">
        <xsl:value-of select="substring-before($source,'-')"/>
    </xsl:variable>
    
    <xsl:variable name="active-search-tab">
        <xsl:choose>
            <xsl:when test="$search-tab">
                <xsl:value-of select="$search-tab"/>
            </xsl:when>
            <xsl:when test="starts-with($path,'/sumc-specialties')">specialty</xsl:when>
            <xsl:when test="starts-with($path,'/search/clinical')">clinical</xsl:when>
            <xsl:when test="starts-with($path,'/info-literacy')">clinical</xsl:when>
            <xsl:when test="ends-with($path,'-viaLane.html')">articles</xsl:when>
            <xsl:when test="starts-with($path,'/biomed-resources')">catalog</xsl:when>
            <xsl:when test="starts-with($path,'/services')">catalog</xsl:when>
            <xsl:when test="starts-with($path,'/help')">catalog</xsl:when>
            <xsl:when test="string-length($source-prefix) &gt; 0">
                <xsl:value-of select="$source-prefix"/>
            </xsl:when>
            <xsl:otherwise>articles</xsl:otherwise>
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
                <xsl:value-of select="$mesh-term"/>
            </xsl:when>
            <xsl:when test=".='current-year'">
                <xsl:value-of select="format-dateTime(current-dateTime(),'[Y,4]')"/>
            </xsl:when>
        </xsl:choose>
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
    <!-- END persistent login  -->

    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
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

    <xsl:template match="h:a|h:area">
        <xsl:choose>
            <!-- TODO: rethinking removing current link
                <xsl:when test="starts-with(@href, '/')">
                <xsl:choose>
                    <xsl:when test="$query-string='' and @href = $path">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:when
                        test="$query-string != '' and @href = concat($path,'?',$query-string)">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>-->
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
    <xsl:template match="h:li[h:a/@href = $path][not(parent::h:ul[attribute::id='laneNav'])]">
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

    <!-- add back to top for dl lists > 20 -->
    <xsl:template match="h:dl[count(h:dd) &gt; 20]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
        <a href="#"><img src="{$base-path}/graphics/icons/arrowUpTransp.gif" alt="up arrow"/> Back to
            top</a>
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
                    <xsl:value-of select="replace(.,'\{keywords\}',$regex-search-terms)"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and contains(.,'{search-terms}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{search-terms\}',$regex-search-terms)"/>
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
    <xsl:template match="h:meta[@http-equiv]"/>

    <!-- combines the template title value with the value of the title of the source document -->
    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$path = '/index.html'">
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

    <!-- add clinicalSearch class to search form and breadcrumb elements when clinical is active tab -->
    <xsl:template match="node()[@id='search' or @id='breadcrumb']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='class')]"/>
            <xsl:if test="$active-search-tab = 'clinical'">
                <xsl:attribute name="class">clinicalSearch</xsl:attribute>
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
    
    <!-- add class="active" to searchTabs li when it is the active tab -->
    <xsl:template match="h:ul[attribute::id='searchTabs']/h:li[attribute::id=$active-search-tab]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:attribute name="class">active</xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- set the value of the search source hidden input -->
    <xsl:template match="h:input[attribute::id='searchSource']/attribute::value">
        <xsl:attribute name="value">
            <xsl:choose>
                <xsl:when test="$source">
                    <xsl:value-of select="$source"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($active-search-tab,'-all')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <!-- add class="expanded" for the expany menu for the current page -->
    <xsl:template match="h:div[@class='sectionMenu']//h:ul[@class='expandy']/h:li[descendant::h:a/@href=$path]">
        <xsl:copy>
            <xsl:attribute name="class">expanded</xsl:attribute>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- the 1st #login li is the login link or the users name -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[1]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="string-length($name) &gt; 0">
                    <xsl:value-of select="$name"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <!-- the 2nd #login li is the logout link -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[2]">
        <xsl:if test="string-length($name) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 3rd #login li is the proxy-off toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[3]">
        <xsl:if test="matches($ip-group,'^(OTHER|PAVA|ERR)$') and $proxy-links = 'true'">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <!-- the 4th #login li is the proxy-on toggle -->
    <xsl:template match="h:ul[attribute::id='login']/h:li[4]">
        <xsl:if test="matches($ip-group,'^(OTHER|PAVA|ERR)$') and $proxy-links = 'false'">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node()|child::node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="h:li[starts-with(attribute::id,'proxyO')]/h:a">
        <xsl:variable name="parameter-value">
            <xsl:choose>
                <xsl:when test="parent::h:li/attribute::id = 'proxyOn'">true</xsl:when>
                <xsl:otherwise>false</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:copy>
            <xsl:attribute name="href">
                <xsl:value-of select="$request-uri"/>
                <xsl:choose>
                    <xsl:when test="$query-string = ''">
                        <xsl:text>?proxy-links=</xsl:text>
                    </xsl:when>
                    <xsl:when test="not(contains($query-string,'proxy-links='))">
                        <xsl:text>?</xsl:text>
                        <xsl:value-of select="$query-string"/>
                        <xsl:text>&amp;proxy-links=</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>?</xsl:text>
                        <xsl:value-of
                            select="substring-before($query-string,'proxy-links=')"/>
                        <xsl:text>proxy-links=</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:value-of select="$parameter-value"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
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
                <xsl:value-of select="$base-path"/>
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
            <xsl:if test="not($param-string) and ends-with($link,'/')">
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
                        <xsl:value-of select="substring($path,2)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <!--<xsl:with-param name="uri-remaining" select="$path"/>-->
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
        <!--<div>uri-before <xsl:value-of select="$uri-before"/></div>
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
