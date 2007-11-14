<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
    <!-- whether debugging is on or not -->
    <xsl:param name="debug"/>

    <xsl:param name="host"/>

    <xsl:param name="keywords"/>

    <xsl:param name="source"/>

    <xsl:param name="proxy-links"/>

    <xsl:param name="ticket"/>

    <xsl:param name="sunetid"/>

    <!-- m request parameter is a MeSH term -->
    <xsl:param name="m"/>

    <!-- LPCH and SHC don't require authentication for proxy server -->
    <xsl:param name="affiliation"/>

    <!-- value of the 't' parameter -->
    <xsl:param name="t"/>
    
    <!-- loadTab parameter -->
    <xsl:param name="loadTab"/>

    <!-- ==========================  VARIABLES  ========================== -->
    <!-- the default template -->
    <xsl:variable name="default-template" select="'irt2'"/>
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

    <xsl:variable name="search-form-select">
        <xsl:choose>
            <xsl:when test="$source-doc/h:head/h:meta[@name='lw_searchFormSelect']">
                <xsl:value-of
                    select="$source-doc/h:head/h:meta[@name='lw_searchFormSelect']/@content"/>
            </xsl:when>
            <xsl:when test="$source-doc/h:body/h:script[contains(.,'searchFormSelect')]">
                <xsl:value-of
                    select="substring-before(substring-after($source-doc/h:body/h:script[contains(.,'searchFormSelect')],&quot;&apos;&quot;),&quot;&apos;&quot;)"
                />
            </xsl:when>
            <xsl:when test="starts-with($request-uri,'online/ej')">ej</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/eb')">book</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/cc')">cc</xsl:when>
            <xsl:when test="starts-with($request-uri,'online/db')">database</xsl:when>
            <xsl:when test="starts-with($request-uri,'services')">faq</xsl:when>
            <xsl:when test="starts-with($request-uri,'howto')">faq</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/peds')">peds</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/picu')">peds</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/history')">history</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/bioresearch')">research</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/patient')">all</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/cultural')">all</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/pharmacy')">pharmacy</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/anesthesia')">/portals/anesthesia.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/cardiology')">/portals/cardiology.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/internal-medicine')">/portals/internal-medicine.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/lpch-cerner')">/portals/lpch-cerner.html</xsl:when>
            <xsl:when test="starts-with($request-uri,'portals/')">clinical</xsl:when>
            <xsl:when test="starts-with($request-uri,'local/antibiogram')">clinical</xsl:when>
            <xsl:when test="$source">
                <xsl:value-of select="$source"/>
            </xsl:when>
            <xsl:otherwise>all</xsl:otherwise>
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
            <xsl:when test=".='keywords'">
                <xsl:value-of select="$keywords"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!-- strip onload event handlers except 100years pages using yui in laneweb.js-->
    <xsl:template match="h:body/@onload">
        <xsl:if test="starts-with($request-uri,'100years')">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>

    <!-- strip onunload handlers -->
    <xsl:template match="h:body/@onunload"/>

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
            <xsl:apply-templates select="attribute::node()[local-name()!='space']"/>
            <xsl:text> </xsl:text>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="h:script">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:comment>
                <xsl:apply-templates select="child::node()"/>
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
            <!-- obfuscate email addresses with JavaSscript -->
            <xsl:when test="starts-with(@href, 'mailto:')">
                <xsl:copy>
                    <xsl:attribute name="onclick">
                        <xsl:text>email('</xsl:text>
                        <xsl:call-template name="tokenize-email">
                            <xsl:with-param name="string" select="@href"/>
                        </xsl:call-template>
                        <xsl:text>')</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="href"/>
                    <xsl:apply-templates select="attribute::node()[name() != 'href']"/>
                    <xsl:apply-templates select="child::node()"/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*"/>
                    <xsl:if
                        test="$response-template='irt2' and (@class='proxy' or @proxy) and ancestor::h:div[@id='searchResults']">
                        <xsl:attribute name="onclick">
                            <xsl:text>openSearchResult('</xsl:text>
                            <xsl:choose>
                                <xsl:when
                                    test="$proxy-links = 'true' and ($affiliation = 'LPCH' or $affiliation = 'SHC')">
                                    <xsl:text>http://laneproxy.stanford.edu/login?url=</xsl:text>
                                    <xsl:value-of select="."/>
                                </xsl:when>
                                <xsl:when
                                    test="$proxy-links = 'true' and $ticket != '' and $sunetid != ''">
                                    <xsl:text>http://laneproxy.stanford.edu/login?user=</xsl:text>
                                    <xsl:value-of select="$sunetid"/>
                                    <xsl:text>&amp;ticket=</xsl:text>
                                    <xsl:value-of select="$ticket"/>
                                    <xsl:text>&amp;url=</xsl:text>
                                    <xsl:value-of select="@href"/>
                                </xsl:when>
                                <xsl:when test="$proxy-links = 'true'">
                                    <xsl:value-of
                                        select="concat('/',$context,'/secure/login.html?url=',@href)"/>
                                    <!--<xsl:text>http://laneproxy.stanford.edu/login?url=</xsl:text>
                                    <xsl:value-of select="@href"/>-->
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="@href"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:text>');return false;</xsl:text>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:apply-templates select="node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- =====================  SPECIAL CASE TEMPLATES ===================== -->
    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <xsl:when
                test="(parent::h:a[@class='proxy' or @proxy]) and $proxy-links = 'true' and starts-with(.,'http')">
                <xsl:choose>
                    <xsl:when test="$affiliation = 'LPCH' or $affiliation = 'SHC'">
                        <xsl:attribute name="href">
                            <xsl:text>http://laneproxy.stanford.edu/login?url=</xsl:text>
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:when test="$ticket != '' and $sunetid != ''">
                        <xsl:attribute name="href">
                            <xsl:text>http://laneproxy.stanford.edu/login?user=</xsl:text>
                            <xsl:value-of select="$sunetid"/>
                            <xsl:text>&amp;ticket=</xsl:text>
                            <xsl:value-of select="$ticket"/>
                            <xsl:text>&amp;url=</xsl:text>
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="make-link">
                            <xsl:with-param name="link">
                                <xsl:value-of select="concat('/secure/login.html?url=',.)"/>
                            </xsl:with-param>
                            <xsl:with-param name="attr" select="'href'"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when
                test="starts-with(.,'http://lane.stanford.edu') and not(contains(.,'cookiesFetch'))">
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link"
                        select="substring-after(.,'http://lane.stanford.edu')"/>
                    <xsl:with-param name="attr" select="name()"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="contains(., '://') and contains(.,'{keywords}')">
                <xsl:attribute name="href">
                    <xsl:value-of select="replace(.,'\{keywords\}',$keywords)"/>
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
            <xsl:when
                test="starts-with(.,'telnet:') or starts-with(.,'rtsp://')">
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

    <!-- get all the head elements from template and all non title head elements from source (with some exceptions)-->
    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates select="child::node()"/>
            <xsl:apply-templates select="$source-doc/h:head/node()[not(self::h:title)]"/>
            <xsl:call-template name="meta-data"/>
            <xsl:if test="$debug='y'">
  				<link href="/javascript/yui/build/logger/assets/skins/sam/logger.css" type="text/css" rel="stylesheet"/>
				<script src="/javascript/yui/build/logger/logger-min.js" type="text/javascript"><xsl:text> </xsl:text></script>
	       	    <script src="/javascript/yui/build/dragdrop/dragdrop-min.js" type="text/javascript"><xsl:text> </xsl:text></script>
	            	<script type="text/javascript">
	           	    	<xsl:text>var context="</xsl:text><xsl:value-of select="$context"/><xsl:text>";</xsl:text>
		                <xsl:text>var request_uri="</xsl:text><xsl:value-of select="$request-uri"/><xsl:text>";</xsl:text>
	                    <xsl:text>var query_string="</xsl:text><xsl:value-of select="$query-string"/><xsl:text>";</xsl:text>
	                    <xsl:text>var href="</xsl:text><xsl:value-of select="concat($context,'/',$request-uri,'?',$query-string)"/><xsl:text>";</xsl:text>
	                    <xsl:text>var ticket="</xsl:text><xsl:value-of select="$ticket"/><xsl:text>";</xsl:text>
	                    <xsl:text>var sunetid="</xsl:text><xsl:value-of select="$sunetid"/><xsl:text>";</xsl:text>
	                    <xsl:text>var proxy_links="</xsl:text><xsl:value-of select="$proxy-links"/><xsl:text>";</xsl:text>
	                    <xsl:text>var affiliation="</xsl:text><xsl:value-of select="$affiliation"/><xsl:text>";</xsl:text>
	                    <xsl:text>var search_form_select="</xsl:text><xsl:value-of select="$search-form-select"/><xsl:text>";</xsl:text>
	                    <xsl:text>var source="</xsl:text><xsl:value-of select="$source"/><xsl:text>";</xsl:text>
	                    <xsl:text>var keywords="</xsl:text><xsl:value-of select="$keywords"/><xsl:text>";</xsl:text>
	                </script>
	       	 </xsl:if>
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
    <xsl:template match="h:input[@id='lw_search-keywords']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="value">
                <xsl:value-of select="$keywords"/>
            </xsl:attribute>
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

    <!-- set the search tagline text -->
    <xsl:template match="h:div[@id='displayTagline' or @id='displaySearchTaglineText']">
        <xsl:variable name="taglineText"
            select="$template-document//h:span[starts-with(@id,$search-form-select)]"/>
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()"/>
            <xsl:choose>
                <xsl:when test="$taglineText">
                    <xsl:value-of select="$taglineText"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <!-- TODO keep for now for backwards compatibility will be replaced by pi -->
    <xsl:template match="h:span[@class='lw_keywords']">
        <xsl:value-of select="$keywords"/>
    </xsl:template>

    <!-- put the appropriate text into the content attribute of meta elements -->
    <xsl:template match="h:meta[starts-with(@name,'lw_')]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name()='content')]"/>
            <xsl:attribute name="content">
                <xsl:choose>
                    <xsl:when test="@content = '{query-string}'">
                        <xsl:value-of select="$query-string"/>
                    </xsl:when>
                    <xsl:when test="@content = '{proxy-links}'">
                        <xsl:value-of select="$proxy-links"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@content"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>
    
    <!-- substitute the affiliation in the meta element -->
    <xsl:template match="h:meta/@content[.='{affiliation}']">
        <xsl:attribute name="content">
            <xsl:value-of select="$affiliation"/>
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
    
    <!-- add class="labled" where appropriate -->
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
            <xsl:if test="not(h:h2)">
                <xsl:apply-templates select="node()[not(@id='otherPortalOptions')]"/>
            </xsl:if>
        </xsl:copy>
    </xsl:template>
    

    <!-- ===================    LANEWEB NAMESPACE TEMPLATES  ================ -->
    <!-- puts in the current document's content (not any more) (well ok, need backwards compatibility for now )-->
    <xsl:template match="h:div[@id='lw_content']">
        <xsl:call-template name="content"/>
    </xsl:template>

    <xsl:template match="h:a[@class='lw_toggleProxyOn']">
        <xsl:if test="contains('OTHER|PAVA|ERR',$affiliation) and $proxy-links = 'false'">
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

    <xsl:template match="h:a[@class='lw_toggleProxyOff']">
        <xsl:if test="contains('OTHER|PAVA|ERR',$affiliation) and $proxy-links = 'true'">
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

    <!-- generates the breadcrumb (not any more) -->
    <xsl:template match="h:span[@id='lw_breadcrumb']">
        <xsl:call-template name="breadcrumb"/>
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
		 <xsl:if test="$debug='y'">
				<input type="hidden"  name="debug" value="y"/>
    	 </xsl:if>
    	<xsl:if test="not($template-is-default)">
				<input type="hidden"  name="template" value="{$response-template}"/>
    	</xsl:if>
    	</xsl:copy>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->

    
    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="not(contains($link,'/secure/login.html')) and ($debug='y' or not($template-is-default))">
                <xsl:choose>
                    <xsl:when test="contains($link, '?')">&amp;</xsl:when>
                    <xsl:otherwise>?</xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$debug='y' and not($template-is-default)">
                        <xsl:text>debug=y&amp;template=</xsl:text>
                        <xsl:value-of select="$response-template"/>
                    </xsl:when>
                    <xsl:when test="not($template-is-default)">
                        <xsl:text>template=</xsl:text>
                        <xsl:value-of select="$response-template"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>debug=y</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:variable>
        <xsl:attribute name="{$attr}">
            <xsl:if test="starts-with($link, '/')">
                <xsl:value-of select="$context"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="contains($link,'{keywords}')">
                    <xsl:value-of select="replace($link,'\{keywords\}',$keywords)"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bkeywords%7D')">
                    <xsl:value-of select="replace($link,'%7Bkeywords%7D',$keywords)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$link"/>
                </xsl:otherwise>
            </xsl:choose>
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
        <xsl:if test="$keywords">
            <meta name="LW.keywords" content="{$keywords}"/>
        </xsl:if>
        <xsl:if test="$source">
            <meta name="LW.source" content="{$source}"/>
        </xsl:if>
        <xsl:if test="$host">
            <meta name="LW.host" content="{$host}"/>
        </xsl:if>
        <xsl:if test="$debug">
            <meta name="LW.debug" content="{$debug}"/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="tokenize-email">
        <xsl:param name="string"/>
        <xsl:value-of
            select="concat('|',substring($string,1,1))"/>
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
                    <xsl:when test="$source-doc/h:head/h:meta[@name='lw_faqCategory']">
                        <xsl:value-of
                            select="substring-after($sitemap//h:a[.=$source-doc/h:head/h:meta[@name='lw_faqCategory']/@content]/@href,'/')"
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
                        <xsl:value-of select="$label-current"/>
                    </a>
                    <xsl:text>&#160;&gt;&#160;</xsl:text>
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
                        <xsl:value-of select="$label-current"/>
                    </a>
                    <xsl:text>&#160;&gt;&#160;</xsl:text>
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
    
    <!-- here is the flash detect thing -->
    <xsl:template match="h:object[h:param[@name='flash-version']]">
        <xsl:variable name="flash-version">
            <xsl:choose>
                <xsl:when test="h:param[@name='flash-version']">
                    <xsl:value-of select="h:param[@name='flash-version']/@value"/>
                </xsl:when>
                <xsl:otherwise>6.0.65</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <script type="text/javascript">
            <xsl:comment>
            <xsl:text>
                var hasProductInstall = DetectFlashVer(6, 0, 65);
                var requiredVersion = '</xsl:text><xsl:value-of select="$flash-version"/><xsl:text>'.split('.');
                    var hasRequestedVersion = DetectFlashVer(requiredVersion[0],requiredVersion[1],requiredVersion[2]);
                    if ( hasProductInstall &amp;&amp; !hasRequestedVersion ) {
                    <!--	// MMdoctitle is the stored document.title value used by the installation process to close the window that started the process
                        // This is necessary in order to close browser windows that are still utilizing the older version of the player after installation has completed
                        // DO NOT MODIFY THE FOLLOWING FOUR LINES
                        // Location visited after installation is complete if installation is required-->
                    var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
                    var MMredirectURL = window.location;
                    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
                    var MMdoctitle = document.title;
                    
                    AC_FL_RunContent(
                    "src", "/flash/playerProductInstall.swf",
                    "FlashVars", "MMredirectURL="+MMredirectURL+'&amp;MMplayerType='+MMPlayerType+'&amp;MMdoctitle='+MMdoctitle+"",
                    "width", "550",
                    "height", "300",
                    "align", "middle",
                    "id", "detectionExample",
                    "quality", "high",
                    "bgcolor", "#3A6EA5",
                    "name", "detectionExample",
                    "allowScriptAccess","sameDomain",
                    "type", "application/x-shockwave-flash",
                    "pluginspage", "http://www.adobe.com/go/getflashplayer"
                    );
                    } else if (hasRequestedVersion) {
                    <!--	// if we've detected an acceptable version
                        // embed the Flash Content SWF when all tests are passed-->
                    AC_FL_RunContent(</xsl:text>
            <xsl:for-each select="@*">
                "<xsl:value-of select="name()"/>","<xsl:value-of select="."/>",
            </xsl:for-each>
            <xsl:for-each select="h:param[not(@name='flash-version')]">
                "<xsl:value-of select="@name"/>","<xsl:value-of select="@value"/>"<xsl:if test="position() != last()">,</xsl:if>
            </xsl:for-each>
            <xsl:text>	);
                } else {<!--  // flash is too old or we can't detect the plugin-->
                var alternateContent = 'This content requires the Adobe Flash Player. '
                + '&lt;a href=http://www.adobe.com/go/getflash/>Get Flash&lt;/a>';
                document.write(alternateContent);<!--  // insert non-flash content-->
                }
            </xsl:text>
            </xsl:comment>
        </script>
        <noscript><p>This flash object requires javascript.</p></noscript>
    </xsl:template>

</xsl:stylesheet>
