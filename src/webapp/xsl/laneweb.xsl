<xsl:stylesheet version="1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xi="http://www.w3.org//2001/XInclude"
    exclude-result-prefixes="h xi">

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
    
    <xsl:param name="keywords"/>
    
    <xsl:param name="selection"/>
    
    <xsl:param name="proxy-links"/>
  
    
    <!-- ==========================  VARIABLES  ========================== -->
    <!-- the default template -->
    <xsl:variable name="default-template" select="'default'"/>
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
    <!-- note: for the devel area this needs to be 'cocoon://devel/templates/' -->
    <xsl:variable name="template-document" select="/*/h:html[3]"/>
    <!-- the root node of the requested content document -->
    <xsl:variable name="source" select="/*/h:html[1]"/>
    <!-- the sitemap document -->
    <!-- note: for the devel area this needs to be 'cocoon://devel/sitemap.xml' -->
    <xsl:variable name="sitemap" select="/*/h:html[2]"/>
    <xsl:variable name="sidebar" select="/*/h:html[4]"/>
    
    <!-- ====================  DEFAULT TEMPLATES ============================= -->
    <!-- root template applies templates on the template document -->
    <xsl:template match="/">
        <xsl:apply-templates select="$template-document"/>
    </xsl:template>
    
    <!-- default element match, copies the element and applies templates on all childeren and attributes -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
        <!--
        <xsl:element name="{name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
        -->
    </xsl:template>
  
    
    <!-- default attribute match, copies the attribute -->
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
        <!--<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>-->
    </xsl:template>
    
    <xsl:template match="comment()">
        <xsl:copy-of select="."/>
    </xsl:template>
   
    <xsl:template match="h:a">
        <xsl:choose>
            <xsl:when test="starts-with(@href, '/')">
                <xsl:choose>
                    <xsl:when test="$query-string='' and @href = concat('/',$request-uri)">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:when test="$query-string != '' and @href = concat('/',$request-uri,'?',$query-string)">
                        <xsl:apply-templates select="node()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                        <!--<xsl:element name="{name()}">
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:element>-->
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <!--<xsl:element name="{name()}">-->
                <xsl:copy>
                    <xsl:apply-templates select="@*"/>
                    <xsl:if test="$response-template='default' and (@class='proxy') and ancestor::h:div[@id='searchResults']">
                        <xsl:attribute name="onclick">
                            <xsl:text>openSearchResult('</xsl:text>
                            <xsl:choose>
                                <xsl:when test="proxy-links = 'true'">
                                    <xsl:text>http://laneproxy.stanford.edu/login?url=</xsl:text>
                                    <xsl:value-of select="@href"/>
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
                <!--</xsl:element>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- =====================  SPECIAL CASE TEMPLATES ===================== -->
    <!-- href and src attributes template -->
    <xsl:template match="@href">
        <xsl:choose>
            <xsl:when test="parent::h:a[@class='proxy'] and $proxy-links = 'true'">
                <xsl:variable name="title">
                    <xsl:choose>
                        <xsl:when test="parent::h:a/@title">
                            <xsl:value-of select="parent::h:a/@title"/>
                        </xsl:when>
                        <xsl:when test="parent::h:a/text()!=''">
                            <xsl:value-of select="parent::h:a/text()"/>
                        </xsl:when>
                        <xsl:when test="parent::h:a/h:img/@alt">
                            <xsl:value-of select="parent::h:a/h:img/@alt"/>
                        </xsl:when>
                        <xsl:otherwise>the resource</xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link">http://laneproxy.stanford.edu/login?url=<xsl:value-of select="."/></xsl:with-param>
                    <xsl:with-param name="attr" select="'href'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="starts-with(.,'http://lane.stanford.edu')">
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link" select="substring-after(.,'http://lane.stanford.edu')"/>
                    <xsl:with-param name="attr" select="name()"/>
                </xsl:call-template>
            </xsl:when>         
            <xsl:when test="contains(., '://') and contains(.,'{keywords}')">
                <xsl:attribute name="{name()}">
                    <xsl:value-of select="substring-before(.,'{keywords}')"/><xsl:value-of select="$keywords"/><xsl:value-of select="substring-after(.,'{keywords}')"/>
                </xsl:attribute>
            </xsl:when> 
            <xsl:when test="starts-with(.,'http://') and starts-with($request-uri,'secure')">
                <xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
            </xsl:when>
            <xsl:when test="contains(., '://') and not(ancestor::h:head)">
                <xsl:attribute name="{name()}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="starts-with(., 'mailto:') or starts-with(.,'telnet:')">
                <xsl:attribute name="{name()}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="make-link">
                    <xsl:with-param name="link" select="."/>
                    <xsl:with-param name="attr" select="name()"/>
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
        <head>
            <xsl:apply-templates select="node()"/>
            <!--<xsl:apply-templates select="$source/h:head/node()[not(self::h:title or @rel='stylesheet' or @http-equiv)]"/>-->
            <xsl:for-each select="$source/h:head/*[not(self::h:title or @rel='stylesheet' or @http-equiv)]">
                <xsl:apply-templates select="self::node()"/>
                <xsl:text>
      </xsl:text>
            </xsl:for-each>
        </head>
    </xsl:template>
    <!-- combines the template title value with the value of the title of the source document -->
    <xsl:template match="h:title">
        <title>
            <xsl:choose>
                <xsl:when test="concat('/',$request-uri) = '/index.html'">
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($source/h:head/h:title, ' - ', .)"/>
                </xsl:otherwise>
            </xsl:choose>
        </title>
    </xsl:template>
    
    <xsl:template match="@class[starts-with(.,'lw_')]|@id[starts-with(.,'lw_')]"/>
    <xsl:template match="@class[.='proxy']"/>
    <xsl:template match="h:div[@id='lw_sidebar' or @id='lw_highlights']"/>
    <xsl:template match="*[@id='lw_alt-title']"/>
    <xsl:template match="*[@id='lw_title']">
          <xsl:choose>
              <xsl:when test="$source//*[@id='lw_alt-title']/node()">
                  <xsl:copy>
                      <xsl:apply-templates select="$source//*[@id='lw_alt-title']/node()"/>
                  </xsl:copy>
                  <!--<xsl:element name="{name()}">
                      <xsl:apply-templates select="$source//*[@id='lw_alt-title']/node()"/>
                  </xsl:element>-->
              </xsl:when>
              <xsl:when test="$source//*[@id='lw_alt-title']"/>
              <xsl:otherwise>
                  <xsl:copy>
                      <xsl:value-of select="$source/h:head/h:title/text()"/>
                  </xsl:copy>
                  <!--<xsl:element name="{name()}">
                  <xsl:value-of select="$source/h:head/h:title/text()"/>
                  </xsl:element>-->
              </xsl:otherwise>
            </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:input[@id='lw_search-keywords']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="value"><xsl:value-of select="$keywords"/></xsl:attribute>
        </xsl:copy>
        <!--<xsl:element name="{name()}">
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="value"><xsl:value-of select="$keywords"/></xsl:attribute>
        </xsl:element>-->
    </xsl:template>
    
    <xsl:template match="h:option">
        <xsl:copy>
        <!--<xsl:element name="{name()}">-->
            <xsl:apply-templates select="@*"/>
            <xsl:choose>
                <xsl:when test="@value = 'peds' and contains($request-uri,'clinician/peds.html')">
                        <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:when>
                <xsl:when test="@value = 'clinical' and contains($request-uri,'clinician')">
                        <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:when>
                <xsl:when test="@value='research' and contains($request-uri,'researcher')">
                        <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:when>
                <xsl:when test="@value='eJournals' and contains($request-uri,'online/ej')">
                    <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:when>
                <xsl:when test="@value='faq' and (contains($request-uri,'howto/index.html') or contains($request-uri,'askus.html'))">
                    <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:when>
            <xsl:when test="@value=$selection or (@value='clinical' and $selection='texts')">
            <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:when>
            </xsl:choose>
            <xsl:apply-templates/>
        </xsl:copy>
       <!--</xsl:element>-->
   </xsl:template>
   
   <xsl:template match="h:span[@class='lw_keywords']">
       <xsl:value-of select="$keywords"/>
   </xsl:template>
   
    <xsl:template match="h:meta[starts-with(@name,'lw_')]">
        <meta>
            <xsl:apply-templates select="attribute::node()[not(name()='content')]"/>
            <xsl:attribute name="content">
                <xsl:choose>
                    <xsl:when test="@content = '{query-string}'">
                        <xsl:value-of select="$query-string"/>
                    </xsl:when>
                    <xsl:when test="@content = '{proxyLinks}'">
                        <xsl:value-of select="$proxy-links"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@content"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </meta>
    </xsl:template>
    
    <!-- ===================    LANEWEB NAMESPACE TEMPLATES  ================ -->
    <!-- puts in the current document's content, -->
    <xsl:template match="h:div[@id='lw_content']">
        <xsl:apply-templates select="$source/h:body/node()"/>
        <xsl:if test="$debug='y'">
            <div id="debug">
                <h3 style="padding:0;margin:0 0 1em 0">debugging information</h3>
                <ul>
                <li>context=<xsl:value-of select="$context"/></li>
                <li>request-uri=<xsl:value-of select="$request-uri"/></li>
                <li>query-string=<xsl:value-of select="$query-string"/></li>
                <li>href=<xsl:value-of select="concat($context,'/',$request-uri,'?',$query-string)"/></li>
                <li>proxy-links=<xsl:value-of select="$proxy-links"/></li>
                </ul>
            </div>
        </xsl:if>
    </xsl:template>
    
    <!-- sidebar -->
    <xsl:template match="h:div[@id='lw_side-bar']">
        <div id="sidebar">
            <xsl:apply-templates select="node()"/>
            <xsl:choose>
                <xsl:when test="$source//h:div[@id='lw_sidebar']">
                    <xsl:apply-templates select="$source//h:div[@id='lw_sidebar']/*"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="$sidebar/h:body/node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    
    <!-- creates a a element with the current url and a different template -->
    <xsl:template match="h:span[@class='lw_thisLink']">
        <a>
            <xsl:attribute name="href">
                <xsl:value-of select="concat($context,'/',$request-uri)"/>
                <xsl:choose>
                    <xsl:when test="contains($query-string, '?')">
                        <xsl:choose>
                            <xsl:when test="contains($query-string, 'template=')">
                                <xsl:value-of select="substring-before($query-string, 'template=')"/>
                                <xsl:value-of select="concat('template=', h:span[@class='lw_template'])"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="concat($query-string, '&amp;template=', h:span[@class='lw_template'])"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>?template=<xsl:value-of select="h:span[@class='lw_template']"/></xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="text()"/>
        </a>
    </xsl:template>
    
    <!-- generates the breadcrumb -->
    <xsl:template match="h:span[@id='lw_breadcrumb']">
        <xsl:call-template name="breadcrumb"/>
    </xsl:template>

    <!-- higlights -->
    <xsl:template match="h:div[@id='lw_high-lights']">
        <xsl:if test="$source//h:div[@id='lw_highlights']">
            <div id="highlights">
                <xsl:apply-templates select="node()"/>
                <xsl:apply-templates select="$source//h:div[@id='lw_highlights']/node()"/>
            </div>
        </xsl:if>
    </xsl:template>

    <!-- ======================  NAMED TEMPLATES  =========================== -->
    <!-- adds parameters to href attributes depending on various parameter states -->
    <xsl:template name="make-link">
        <xsl:param name="link"/>
        <xsl:param name="attr"/>
        <xsl:variable name="param-string">
            <xsl:if test="$debug='y' or not($template-is-default)">
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
                    <xsl:value-of select="substring-before($link,'{keywords}')"/><xsl:value-of select="$keywords"/><xsl:value-of select="substring-after($link,'{keywords}')"/>
                </xsl:when>
                <xsl:when test="contains($link,'%7Bkeywords%7D')">
                    <xsl:value-of select="substring-before($link,'%7Bkeywords%7D')"/><xsl:value-of select="$keywords"/><xsl:value-of select="substring-after($link,'%7Bkeywords%7D')"/>
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
    <!-- the breadcrumb -->
    <xsl:template name="breadcrumb">
        <xsl:param name="sitemap"/>
        <xsl:call-template name="breadcrumb-section">
            <xsl:with-param name="uri-before" select="'/'"/>
            <xsl:with-param name="uri-remaining" select="$request-uri"/>
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
        <xsl:choose>
            <xsl:when test="contains($uri-remaining, '/')">
                <a>
                    <xsl:call-template name="make-link">
                        <xsl:with-param name="link" select="concat($uri-before, 'index.html')"/>
                        <xsl:with-param name="attr" select="'href'"/>
                    </xsl:call-template>
                    <xsl:value-of select="$label-current"/>
                </a>
                <xsl:text>&#160;&gt;&#160;</xsl:text>
                <xsl:call-template name="breadcrumb-section">
                    <xsl:with-param name="uri-before" select="concat($uri-before, $uri-current, '/')"/>
                    <xsl:with-param name="uri-remaining" select="$uri-next"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$uri-before = '/' and $uri-remaining='index.html'">
                <xsl:text>Lane Home</xsl:text>
            </xsl:when>
            <xsl:when test="$uri-remaining = 'index.html'">
                <xsl:value-of select="$label-current"/>
            </xsl:when>
            <xsl:otherwise>
                <a>
                    <xsl:call-template name="make-link">
                        <xsl:with-param name="link" select="concat($uri-before, 'index.html')"/>
                        <xsl:with-param name="attr" select="'href'"/>
                    </xsl:call-template>
                    <xsl:value-of select="$label-current"/>
                </a>
                <xsl:text>&#160;&gt;&#160;</xsl:text>
                <xsl:variable name="title">
                    <xsl:value-of select="$sitemap//h:a[@href=concat($uri-before,$uri-remaining)]"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$title = ''">
                        <xsl:value-of select="$source/h:head/h:title"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$title"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
