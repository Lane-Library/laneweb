<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:java="http://xml.apache.org/xalan/java/edu.stanford.laneweb.xslt.LanewebXSLTExtensions"
    exclude-result-prefixes="h java"
    version="1.0">
    <xsl:param name="debug"/>
    <xsl:param name="keywords"/>
    <xsl:param name="alpha"/>
    <xsl:param name="all"/>
    <xsl:param name="subject"/>
    <xsl:param name="core"/>
    <xsl:param name="selected"/>
    <xsl:variable name="uc" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
    <xsl:variable name="lc" select="'abcdefghijklmnopqrstuvwxyz'"/>
    <xsl:variable name="ucalpha" select="translate($alpha, $lc, $uc)"/>
    <xsl:variable name="typeString" select="/h:html/h:head/h:title"/>
    <xsl:template match="/h:html/h:head"/>
    
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
    <html><body>
    <xsl:apply-templates/>
    </body></html>
    </xsl:template>
    
    <xsl:template match="/h:html/h:body/h:ul[@class='eresources']">
        <xsl:choose>
            <xsl:when test="$keywords != ''">
                <xsl:choose>
                    <xsl:when test="$typeString='eResources'">
                        <xsl:variable name="ejournals">
                            <xsl:call-template name="create-list">
                                <xsl:with-param name="resource-class">a</xsl:with-param>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:variable name="databases">
                            <xsl:call-template name="create-list">
                                <xsl:with-param name="resource-class">b</xsl:with-param>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:variable name="ebooks">
                            <xsl:call-template name="create-list">
                                <xsl:with-param name="resource-class">c</xsl:with-param>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:variable name="calculators">
                            <xsl:call-template name="create-list">
                                <xsl:with-param name="resource-class">d</xsl:with-param>
                            </xsl:call-template>
                        </xsl:variable><!--
                        <xsl:variable name="misc">
                            <xsl:call-template name="create-list">
                                <xsl:with-param name="resource-class">e</xsl:with-param>
                            </xsl:call-template>
                        </xsl:variable>--><div>
                            <!--<xsl:if test="$ejournals='' and $databases='' and $ebooks='' and $calculators='' and $misc = ''">-->
                            <xsl:if test="$ejournals='' and $databases='' and $ebooks='' and $calculators=''">
                                <p>No eJournals, eBooks, Databases or Calculators found for <strong>
                                        <span class="lw:keywords"/>
                                    </strong>.</p>
                            </xsl:if>
                        <xsl:if test="$ejournals!=''">
                            <h3>eJournals</h3>
                            <ul>
                                <xsl:copy-of select="$ejournals"/>
                            </ul>
                        </xsl:if>
                        <xsl:if test="$databases!=''">
                            <h3>Databases</h3>
                            <ul>
                                <xsl:copy-of select="$databases"/>
                            </ul>
                        </xsl:if>
                        <xsl:if test="$ebooks!=''">
                            <h3>eBooks</h3>
                            <ul>
                                <xsl:copy-of select="$ebooks"/>
                            </ul>
                        </xsl:if>
                        <xsl:if test="$calculators!=''">
                            <h3>Calculators</h3>
                            <ul>
                                <xsl:copy-of select="$calculators"/>
                            </ul>
                        </xsl:if>
                        <!--<xsl:if test="$misc!=''">
                            <h3>Miscellaneous</h3>
                            <ul>
                                <xsl:copy-of select="$misc"/>
                            </ul>
                        </xsl:if>--></div>
                    </xsl:when>
                    <xsl:otherwise>
                        <ul>
                            <xsl:apply-templates select="h:li[java:contains(self::h:li,$keywords)]"/>
                        </ul>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$all != ''">
                <!-- <h3>All <xsl:value-of select="$typeString"/>:</h3> -->
                <ul>
                    <xsl:apply-templates/>
                </ul>
            </xsl:when>
            <xsl:when test="$subject != ''">
                <h2><xsl:value-of select="$subject"/></h2>
                <ul>
                    <xsl:apply-templates select="*[h:ul[@class='bib']/*[@class='_650'] = $subject]"/>
                </ul>
            </xsl:when>
            <xsl:when test="$alpha != ''">
                <h3>
                    <xsl:value-of select="$typeString"/> beginning with
                        '<xsl:value-of select="$ucalpha"/>':</h3>
                <ul>
                    <xsl:apply-templates select="h:li[h:span/h:span[@class='filing' and (starts-with(.,$ucalpha) or starts-with(.,$alpha))]]"/>
                </ul>
            </xsl:when>
            <xsl:when test="$core != ''">
                <h2>Core Titles Only:</h2>
                <ul>
                       <xsl:apply-templates select="h:li[h:ul[@class='bib']/h:li[@class='core']]"/>
                 </ul>
             </xsl:when>
             <xsl:when test="$selected != ''">
                 <h2>Selected <xsl:value-of select="$typeString"/> Only:</h2>
                <ul>
                    <xsl:apply-templates select="h:li[h:ul[@class='bib']/h:li[@class='selected']]"/>
                </ul>
             </xsl:when>
            <xsl:when test="$typeString = 'eJournals'">
                <h3>eJournals beginning with 'A':</h3>
                <ul>
                    <xsl:apply-templates select="h:li[h:span/h:span[@class='filing' and (starts-with(.,'A') or starts-with(.,'a'))]]"/>
                </ul>
            </xsl:when>
            <xsl:when test="$typeString = 'eBooks'">
                <h2>Core Titles Only:</h2>
                <ul>
                     <xsl:apply-templates select="h:li[h:ul[@class='bib']/h:li[@class='core']]"/>
                </ul>
            </xsl:when>
            <xsl:otherwise>
                <h2>Core <xsl:value-of select="$typeString"/> Only:</h2>
                <ul>
                    <xsl:apply-templates select="h:li[h:ul[@class='bib']/h:li[@class='selected']]"/>
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="h:li[parent::h:ul/@class='eresources']">
        <xsl:variable name="main-url">
            <xsl:choose>
                <xsl:when test="count(h:ul[@class='mfhd']/h:li[@class='_856']) = 1">
                    <xsl:value-of select="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856u']"/>
                </xsl:when>
                <xsl:when test="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856z' and (.='Database' or contains(../../../../h:ul/h:li[@class='_210'],.))]">
                    <xsl:value-of select="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856u' and ../h:li[@class='_856z' and (.='Database' or contains(../../../../h:ul/h:li[@class='_210'],.))]]"/>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="more-info">
            <xsl:choose>
                <xsl:when test="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856z' and .='More Info']">
                    <xsl:value-of select="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856u' and ../h:li[@class='_856z' and .='More Info']]"/>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="get-password">
            <xsl:choose>
                <xsl:when test="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856z' and .='Get Password']">
                    <xsl:value-of select="h:ul[@class='mfhd']/h:li[@class='_856']/h:ul/h:li[@class='_856u' and ../h:li[@class='_856z' and .='Get Password']]"/>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="full-title" select="h:span"/>
        <xsl:variable name="can-proxy" select="not(h:ul/h:li[@class='noproxy'])"/>
        <xsl:variable name="links">
            <xsl:for-each select="h:ul[@class='mfhd']/h:li[@class='_856' and h:ul/h:li[@class='_856u' and . != $main-url and . != $more-info and . != $get-password]]">
                <xsl:variable name="subtitle">
                    <xsl:choose>
                        <xsl:when test="h:ul/h:li[@class='_856q']">
                            <xsl:value-of select="h:ul/h:li[@class='_856q']"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="h:ul/h:li[@class='_856z']"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <li>
                    <a href="{h:ul/h:li[@class='_856u']}">
                        <xsl:attribute name="class">
                            <xsl:choose>
                                <xsl:when test="$can-proxy">proxy</xsl:when>
                                <xsl:otherwise>lw:no-proxy</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:value-of select="$subtitle"/>
                    </a>
                </li>
            </xsl:for-each>
        </xsl:variable>
        <li>
            <xsl:choose>
                <xsl:when test="$main-url=''">
                    <!-- removing nonfiling portion xsl:value-of select="h:span"/-->
                    <xsl:value-of select="h:span/h:span"/>
                </xsl:when>
                <xsl:otherwise>
                    <!-- removing nonfiling portion xsl:value-of select="h:span/text()"/-->
                    <a href="{$main-url}">
                        <xsl:attribute name="class">
                            <xsl:choose>
                                <xsl:when test="$can-proxy">proxy</xsl:when>
                                <xsl:otherwise>lw:no-proxy</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:value-of select="h:span/h:span"/>
                    </a>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="h:ul[@class='mfhd']/h:li[@class='_866z']">
                <xsl:text> </xsl:text>
                <xsl:value-of select="h:ul[@class='mfhd']/h:li[@class='_866z']"/>
            </xsl:if>
            <xsl:if test="h:ul[@class='mfhd']/h:li[@class='_866v' or @class='_866y']">
                <xsl:call-template name="holdingsyear"/>
            </xsl:if>
            <xsl:apply-templates select="h:ul[@class='mfhd']//h:li"/>
            <xsl:if test="$more-info != ''">
                <a class="more-info" href="{$more-info}">
                    <img src="/images/info.gif" style="border:none" alt="more info"/>
                </a>
            </xsl:if>
            <xsl:if test="$get-password != ''">
                <xsl:text> </xsl:text>
                <a class="get-password" href="{$get-password}">get password</a>
            </xsl:if>
            <xsl:call-template name="debug"/>
            <xsl:if test="$links != ''">
                <ul>
                    <xsl:copy-of select="$links"/>
                </ul>
            </xsl:if>
            <xsl:if test="not($can-proxy)">
                <span class="noproxy"> limited off campus access</span>
            </xsl:if>
        </li>
    </xsl:template>
    
    <xsl:template match="h:li[contains(@class,'_')]"/>
    
    <xsl:template match="h:li[@class='_844a']">
        <xsl:text> (</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>) </xsl:text>
    </xsl:template>
    
    <xsl:template match="h:li[@class='_856i']">
        <xsl:text> (</xsl:text>
        <xsl:choose>
            <xsl:when test="contains(.,'laneinfo@')">
                <xsl:value-of select="substring-before(.,'laneinfo@')"/>
                <a href="mailto:laneinfo@lanelib.stanford.edu">laneinfo@lanelib.stanford.edu</a>
                <xsl:value-of select="substring-after(.,'stanford.edu')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>) </xsl:text>
    </xsl:template>
    
    <xsl:template name="holdingsyear">
        <xsl:text> (</xsl:text>
        <xsl:if test="h:ul/h:li[@class='_866v']">
            <xsl:choose>
                <xsl:when test="contains(h:ul/h:li[@class='_866v'], '- =')">
                    <xsl:value-of select="substring-before(h:ul/h:li[@class='_866v'], '- =')"/>
                </xsl:when>
                <xsl:when test="contains(h:ul/h:li[@class='_866v'], ' =')">
                    <xsl:value-of select="substring-before(h:ul/h:li[@class='_866v'], ' =')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="h:ul/h:li[@class='_866v']"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>, </xsl:text>
        </xsl:if>
        <xsl:value-of select="h:ul/h:li[@class='_866y']"/>
        <xsl:text>) </xsl:text>
    </xsl:template>
    
    <xsl:template name="debug">
        <xsl:if test="$debug='y'">
            <xsl:variable name="bib_id" select="substring-before(substring-after(@id,'bib:'),';mfhd:')"/>
            <xsl:variable name="mfhd_id" select="substring-after(@id,';mfhd:')"/>
            <xsl:text> | </xsl:text>
            <a href="/voyager/marc.html?db=lmldb&amp;type=bib&amp;id={$bib_id}">bib</a>
            <xsl:text> | </xsl:text>
            <a href="/voyager/marc.html?db=lmldb&amp;type=mfhd&amp;id={$mfhd_id}">mfhd</a>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="create-list">
        <xsl:param name="resource-class"/>
        <xsl:apply-templates select="h:li[@class=$resource-class and java:contains(.,$keywords)]"/>
    </xsl:template>
    
</xsl:stylesheet>
