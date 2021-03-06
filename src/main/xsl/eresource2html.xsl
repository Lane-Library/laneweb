<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    exclude-result-prefixes="h s" version="2.0">
    
    <xsl:param name="browse-query"/>

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <xsl:variable name="available" select="number(s:available)"/>
        <li class="resource">
            <span class="primaryType">
                <xsl:apply-templates select="s:primaryType"/>
            </span>
            <xsl:if test="(contains($browse-query,'Book') and contains(s:primaryType, 'Book'))
                or (contains($browse-query,'Journal') and contains(s:primaryType, 'Journal'))">
                <div class="bookcover" data-bcid="{s:recordType}-{s:recordId}"><i class="fa fa-book"></i></div>
            </xsl:if>
            <xsl:apply-templates select="s:link[not(starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=') or @type = 'impactFactor') or position() = 1]"/>
            <xsl:apply-templates select="s:pub-text"/>
            <xsl:apply-templates select="s:link[position() > 1 and starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')]"/>
            <div class="resultInfo">
                <xsl:if test="s:description">
                    <span class="descriptionTrigger eresource no-bookmarking"/>
                </xsl:if>
                <xsl:if test="s:recordType = 'bib'">
                    <xsl:apply-templates select="s:link[@type = 'impactFactor']"/>
                </xsl:if>
            </div>
            <xsl:apply-templates select="s:description"/>
            <div class="sourceInfo no-bookmarking">
                <span>
                    <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog Record</a>
                </span>
                <xsl:if test="contains(s:primaryType,'Print') and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:if test="s:primaryType = 'Book Print' and $available &gt; 0">
                    <span class="requestIt">
                        <a title="Request this item for pickup" href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}&amp;lw.req=true" rel="popup console 1020 800">Request</a>
                    </span>
                </xsl:if>
                <span class="permalink">
                    <a title="click to copy a permanent link to this record" href="https://lane.stanford.edu/view/{s:recordType}/{s:recordId}">
                    <i class="fa fa-link fa-rotate-90"></i> Permalink</a>
                </span>
            </div>
        </li>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="description">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:link[1]">
        <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
        <div>
            <a class="primaryLink" href="{s:url}" title="{../s:title}">
                <xsl:value-of select="../s:title" />
            </a>
        </div>
        <xsl:apply-templates select="../s:pub-author"/>
        <xsl:if test="(s:link-text and 'null' != s:link-text) or @type = 'getPassword' or s:version-text or s:publisher">
            <div class="resultInfo">
                <xsl:call-template name="build-link-label">
                    <xsl:with-param name="link" select="."/>
                    <xsl:with-param name="primaryType" select="../s:primaryType"/>
                    <xsl:with-param name="simplePrimaryType" select="$simple-primary-type"/>
                </xsl:call-template>
                <xsl:if test="$simple-primary-type != s:label and s:link-text != 'Lane Catalog Record'">
                    <span>
                        <a href="{s:url}" title="{../s:title}">
                            <xsl:value-of select="s:link-text" />
                        </a>
                    </span>
                </xsl:if>
                <xsl:if test="@type = 'getPassword'">
                    <span>
                        <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                    </span>
                </xsl:if>
                <xsl:if test="s:version-text">
                    <span class="versionText">
                        <xsl:value-of select="s:version-text" />
                    </span>
                </xsl:if>
                <xsl:if test="s:additional-text">
                    <span>
                        <xsl:value-of select="s:additional-text" />
                    </span>
                </xsl:if>
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
        <div class="resultInfo">
            <xsl:call-template name="build-link-label">
                <xsl:with-param name="link" select="."/>
                <xsl:with-param name="primaryType" select="../s:primaryType"/>
                <xsl:with-param name="simplePrimaryType" select="$simple-primary-type"/>
            </xsl:call-template>
            <xsl:if test="$simple-primary-type != s:label">
                <span>
                    <a href="{s:url}" title="{s:label}">
                        <xsl:value-of select="s:link-text"/>
                    </a>
                </span>
            </xsl:if>
            <xsl:if test="@type = 'getPassword'">
                <span>
                    <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                </span>
            </xsl:if>
            <xsl:if test="s:version-text">
                <span class="versionText">
                    <xsl:value-of select="s:version-text" />
                </span>
            </xsl:if>
            <xsl:if test="s:additional-text">
                <span>
                    <xsl:value-of select="s:additional-text" />
                </span>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="s:link[@type = 'impactFactor' and position() > 1]">
            <span><a href="{s:url}">Impact Factor</a></span>
    </xsl:template>

    <xsl:template match="s:primaryType">
        <xsl:if test="$browse-query and (contains($browse-query,'Book') or contains($browse-query,'Journal'))">
            <xsl:choose>
                <xsl:when test="not(contains(., 'Print')) and ../s:link[s:label = 'Lane Catalog Record']">Digital/Print</xsl:when>
                <xsl:when test="contains(., 'Print')">Print</xsl:when>
                <xsl:otherwise>Digital</xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div>
            <xsl:value-of select="."/>
        </div>
    </xsl:template>
    
    <xsl:template match="s:pub-text">
        <div class="citation">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template name="build-link-label">
        <xsl:param name="link" />
        <xsl:param name="primaryType" />
        <xsl:param name="simplePrimaryType" />
        <xsl:variable name="showLabel">
            <xsl:choose>
                <xsl:when test="not((contains($browse-query,'Book') or contains($browse-query,'Journal')))">true</xsl:when>
                <xsl:when test="not(contains($primaryType, 'Print')) and ../s:link[s:label = 'Lane Catalog Record']">true</xsl:when>
                <xsl:when test="$primaryType = s:label">true</xsl:when>
                <xsl:otherwise>false</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:if test="$showLabel = 'true'">
            <span>
                <xsl:choose>
                    <xsl:when test="starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=') or (contains(s:url,'//searchworks.stanford.edu/view') and ../s:recordType!= 'sul')">Print</xsl:when>
                    <xsl:when test="$primaryType = s:label">
                        <a href="{s:url}" title="{s:label}"><xsl:value-of select="s:label"/></a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$simplePrimaryType"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:if test="s:publisher">
                    <xsl:text> : </xsl:text>
                    <i>
                        <xsl:value-of select="s:publisher" />
                    </i>
                </xsl:if>
            </span>
        </xsl:if>
        <xsl:if test="$showLabel = 'false' and s:publisher">
            <span>
                <i>
                    <xsl:value-of select="s:publisher" />
                </i>
            </span>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
