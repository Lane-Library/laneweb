<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    xmlns:f="https://lane.stanford.edu/functions"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="f h s xsd" version="2.0">

    <xsl:template match="s:desc-linebreak">
        <br/>
    </xsl:template>

    <xsl:template match="s:description">
        <div class="description">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:keyword">
        <strong>
            <xsl:value-of select="."/>
        </strong>
    </xsl:template>

    <xsl:template match="s:locationName">
        <xsl:choose>
            <xsl:when test="../s:locationUrl">
                <a href="{../s:locationUrl}">
                    <!-- exclude rel attribute for non-http(s) links (e.g. mailto:xxx)-->
                    <xsl:if test="starts-with(../s:locationUrl,'http')">
                        <xsl:attribute name="rel">popup console 1020 800</xsl:attribute>
                    </xsl:if>
                    <xsl:apply-templates/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:pub-text">
        <div class="citation">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:recordType">
        <xsl:variable name="label"><xsl:text>Source: </xsl:text></xsl:variable>
        <xsl:choose>
            <xsl:when test=". = 'pubmed'">
                <xsl:copy-of select="$label"/><a href="https://pubmed.ncbi.nlm.nih.gov/{../s:recordId}/?otool=Stanford" title="PubMed: PMID {../s:recordId}">PubMed</a>
            </xsl:when>
            <xsl:when test=". = 'sul'">
                <xsl:copy-of select="$label"/><a href="https://searchworks.stanford.edu/view/{../s:recordId}" title="SearchWorks Record">SearchWorks</a>
            </xsl:when>
            <xsl:when test=". = 'bib'">
                <xsl:copy-of select="$label"/><a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}" title="Lane Catalog Record">Lane Catalog</a>
            </xsl:when>
            <xsl:when test=". = 'auth'">
                <xsl:copy-of select="$label"/><a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={../s:recordId}" title="Lane Community Info Record">Lane Community Info</a>
            </xsl:when>
            <xsl:when test=". = 'class'">
                <xsl:copy-of select="$label"/><a href="/classes-consult/laneclasses.html">Lane Classes</a>
            </xsl:when>
            <xsl:when test=". = 'web' or . = 'laneblog'">
                <xsl:copy-of select="$label"/><a href="/index.html">Lane Website</a>
            </xsl:when>
            <xsl:when test=". = 'redivis'">
                <xsl:copy-of select="$label"/><a href="https://redivis.com/StanfordPHS" title="Redivis - Stanford Center for Population Health Sciences">Redivis</a>
            </xsl:when>
            <!-- 
                TODO: testing for PMC indexing
                remove if RM/Thea decides not to use PMC records
                may also need to add to eresources2html if RM wants PMC items browseable
            -->
            <xsl:when test=". = 'dnlm'">
                <xsl:copy-of select="$label"/><a href="https://www.ncbi.nlm.nih.gov/nlmcatalog/?term={../s:recordId}" title="NLM Catalog">NLM Catalog</a>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="s:title">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:function name="f:build-source-info">
        <xsl:param name="eresource" />
        <div class="sourceInfo no-bookmarking">
            <span>
                <xsl:apply-templates select="$eresource/s:recordType"/>
            </span>
            <span class="permalink">
                <a title="click to copy a shareable link to this record" href="https://lane.stanford.edu/view/{$eresource/s:recordType}/{$eresource/s:recordId}">
                <i class="fa-solid fa-link fa-sm fa-rotate-180"></i> Get shareable link</a>
            </span>
        </div>
    </xsl:function>

    <xsl:function name="f:build-link-label">
        <xsl:param name="link" />
        <xsl:variable name="primaryType" select="$link/../s:primaryType"/>
        <xsl:variable name="simplePrimaryType" select="replace($primaryType,'(Journal|Book) ','')"/>
        <span>
            <xsl:choose>
                <xsl:when test="$link/@type = 'lane-print' or $link/@type = 'sul-print'">Print</xsl:when>
                <xsl:when test="$primaryType = $link/s:label">
                    <a href="{$link/s:url}" title="{$link/s:label}: {$link/../s:title}"><xsl:value-of select="$link/s:label"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$simplePrimaryType"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="$link/s:publisher">
                <xsl:text> : </xsl:text>
                <i>
                    <xsl:value-of select="$link/s:publisher" />
                </i>
            </xsl:if>
        </span>
    </xsl:function>

    <!--  assume authors are a comma-separated string; break the string at a separator before max-string-length -->
    <xsl:function name="f:split-authors">
        <xsl:param name="max-string-length"/>
        <xsl:param name="tokens"/>
        <xsl:param name="index"/>

        <xsl:choose>
            <xsl:when test="string-length(string-join($tokens[position() &lt; $index], ', ')) &gt; $max-string-length">
                <xsl:value-of select="f:split-authors($max-string-length, $tokens, $index - 1)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat(string-join($tokens[position() &lt; $index], ', '), ', ')"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="f:maybe-add-doi-attribute">
        <xsl:param name="eresource"/>
        <xsl:if test="$eresource/s:doi[1]">
            <xsl:attribute name="data-doi">
                <xsl:value-of select="lower-case($eresource/s:doi[1])"/>
            </xsl:attribute>
        </xsl:if>
    </xsl:function>

    <xsl:function name="f:primaryLink">
        <xsl:param name="link"/>
        <xsl:variable name="eresource" select="$link/.."/>
        <!-- use s:locationUrl for Lane Catalog records that point to a parent record -->
        <xsl:choose>
            <xsl:when test="f:isPrintRecordPointingToParent($eresource)">
                <div>
                    <a class="primaryLink" href="{$link/s:locationUrl}#searchResults" title="{$eresource/s:title}" rel="popup console 610 800">
                        <xsl:apply-templates select="$eresource/s:title" />
                    </a>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <div>
                    <a class="primaryLink" href="{$link/s:url}" title="{$eresource/s:title}">
                        <xsl:apply-templates select="$eresource/s:title" />
                    </a>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <!-- used for Lane and SUL digital links -->
    <xsl:function name="f:handleDigitalLinks">
        <xsl:param name="links"/>
        <xsl:if test="count($links) = 1">
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: updated link icon instead? -->
                <span class="hldgsHeader available"><i class="fa-solid fa-link-horizontal fa-sm"></i> Digital Access &#160;</span>
                <span>
                    <a href="{$links[1]/s:url}" title="{$links[1]/s:label}">
                        <xsl:value-of select="concat($links[1]/s:publisher, ' ', $links[1]/s:link-text)"/>
                    </a>
                </span>
            </div>
            <xsl:if test="$links[1]/@type = 'lane-getPassword'">
                <span class="getPassword">
                    <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                </span>
            </xsl:if>
            <xsl:if test="$links[1]/s:version-text">
                <span class="versionText">
                    <xsl:value-of select="$links[1]/s:version-text" />
                </span>
            </xsl:if>
            <xsl:if test="$links[1]/s:additional-text">
                <span class="additionalText">
                    <xsl:value-of select="$links[1]/s:additional-text" />
                </span>
            </xsl:if>
        </xsl:if>
        <xsl:if test="count($links) > 1">
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: updated link icon instead? -->
                <span class="hldgsHeader available"><i class="fa-solid fa-link-horizontal fa-sm"></i> Digital Access &#160;</span>
                <span class="hldgsTrigger"/>
                <table class="hide-empty-columns">
                    <thead>
                        <tr>
                            <th>Provider</th>
                            <th>Version <i
                                    class="fa-solid fa-info-circle yui3-tooltip-trigger"
                                    title="Look here for issue, volume, and year information about the items in Lane 
                                    Library's collection. If a date range has no end date, our collection includes the 
                                    most recent issue."/>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="$links">
                            <xsl:variable name="simple-primary-type" select="replace(../s:primaryType,'(Journal|Book) ','')"/>
                            <tr>
                                <td>
                                    <xsl:if test="not(s:publisher) and s:label">
                                        <xsl:value-of select="s:label"/>
                                    </xsl:if>
                                    <xsl:value-of select="s:publisher"/>
                                </td>
                                <td>
                                    <span>
                                        <a href="{s:url}" title="{s:label}">
                                            <xsl:value-of select="s:link-text"/>
                                            <xsl:text> </xsl:text>
                                        </a>
                                    </span>
                                    <xsl:if test="@type = 'lane-getPassword'">
                                        <span class="getPassword">
                                            <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
                                        </span>
                                    </xsl:if>
                                    <xsl:if test="s:version-text">
                                        <span class="versionText">
                                            <xsl:value-of select="s:version-text" />
                                        </span>
                                    </xsl:if>
                                    <xsl:if test="s:additional-text">
                                        <span class="additionalText">
                                            <xsl:value-of select="s:additional-text" />
                                        </span>
                                    </xsl:if>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </xsl:if>
    </xsl:function>

    <xsl:function name="f:handleLanePrintLinks">
        <xsl:param name="links"/>
        <xsl:param name="eresource"/>
        <!-- don't write print holdings when there are no items and a digital holding exists (helps with component/article/chapter records pointing to a parent)  -->
        <xsl:if test="count($links) > 0 and not($eresource/s:total = 0 and count($eresource/s:link[@type='lane-digital']) &gt; 0)">
            <!-- items can be available but not requestable (reserves, equipment, reference) -->
            <xsl:variable name="itemsAvailableButMaybeNotRequestable" select="sum($eresource/s:link/s:available) &gt; 0"/>
            <!-- catalog-service availableBibItems.sql intentionally excludes non-circulating, 2-hour, etc. items -->
            <xsl:variable name="itemsRequestableInVoyager" select="$eresource/s:available &gt; 0"/>
            <div class="hldgsContainer no-bookmarking">
                <!-- TODO: open book icon instead? -->
                <xsl:choose>
                    <xsl:when test="$itemsAvailableButMaybeNotRequestable">
                        <span class="hldgsHeader available"><i class="fa-solid fa-book"></i> <xsl:value-of select="f:itemTypeLabel($eresource)"/> Access</span>
                        <span class="hldgsTrigger"/>
                        <xsl:if test="$itemsRequestableInVoyager">
                            <span class="requestIt">
                                <a class="btn" href="https://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={$eresource/s:recordId}&amp;lw.req=true" rel="popup console 1020 800">Request Print</a>
                            </span>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="$eresource/s:total &gt; 0 and $eresource/s:available = 0">
                        <span class="hldgsHeader unavailable"><i class="fa-solid fa-book"></i> <xsl:value-of select="f:itemTypeLabel($eresource)"/> Unavailable: Checked out</span>
                    </xsl:when>
                    <xsl:when test="f:isPrintRecordPointingToParent($eresource)">
                        <span class="hldgsHeader"><i class="fa-solid fa-book"></i> Access via <a rel="popup console 610 800" class="citation" href="{$links[1]/s:locationUrl}#searchResults"><xsl:value-of select="$links[1]/s:locationName"/></a></span>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="hldgsHeader"><i class="fa-solid fa-book"></i> <xsl:value-of select="f:itemTypeLabel($eresource)"/> Status unknown</span>
                        <span class="hldgsTrigger"/>
                    </xsl:otherwise>
                </xsl:choose>
                <table class="hide-empty-columns">
                    <thead>
                        <tr>
                            <th>Location</th>
                            <th>Version <i
                                    class="fa-solid fa-info-circle yui3-tooltip-trigger"
                                    title="Look here for issue, volume, and year information about the items in Lane 
                                    Library's collection. If a date range has no end date, our collection includes the 
                                    most recent issue."/>
                            </th>
                            <th>Call Number</th>
                            <th>Number of Items</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="$links">
                            <tr>
                                <td>
                                    <xsl:apply-templates select="s:locationName"/>
                                </td>
                                <td>
                                    <a href="{s:url}" title="{s:label}">
                                        <xsl:value-of select="s:link-text"/>
                                    </a>
                                </td>
                                <td>
                                    <xsl:value-of select="s:callnumber"/>
                                </td>
                                <td>
                                    <xsl:value-of select="s:available"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </xsl:if>
    </xsl:function>    

    <xsl:function name="f:descriptionTrigger">
        <xsl:param name="eresource"/>
        <xsl:if test="$eresource/s:description">
            <div class="resultInfo">
                <xsl:choose>
                    <xsl:when test="$eresource/@type = 'searchContent'">
                        <span class="descriptionTrigger searchContent no-bookmarking"/>
                        <xsl:apply-templates select="$eresource/s:contentId"/>
                    </xsl:when>
                    <xsl:when test="$eresource/s:recordType = 'pubmed'">
                        <span class="descriptionTrigger searchContent no-bookmarking"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="descriptionTrigger eresource no-bookmarking"/>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
            <xsl:apply-templates select="$eresource/s:description"/>
        </xsl:if>
    </xsl:function>

    <!-- 
    print books, journals, databases, etc. should get "Print" label
    things like USB cords shouldn't get labeled "Print"
    -->
    <xsl:function name="f:itemTypeLabel">
        <xsl:param name="eresource"/>
        <xsl:choose>
            <xsl:when test="$eresource/s:primaryType != 'Other'"> Print</xsl:when>
            <xsl:otherwise/>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="f:isPrintRecordPointingToParent" as="xsd:boolean">
        <xsl:param name="eresource"/>
        <xsl:sequence select="$eresource/s:total = 0 and count($eresource/s:link[@type='lane-digital']) = 0 and $eresource/s:link[1]/s:locationUrl"/>
    </xsl:function>

</xsl:stylesheet>
