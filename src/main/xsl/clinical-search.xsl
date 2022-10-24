<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:r="http://lane.stanford.edu/results/1.0">

    <xsl:param name="facet" />
    <xsl:param name="source" />
    <xsl:param name="query" />

    <xsl:template match="attribute::node() | child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:span[starts-with(@r:ref, '@')]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name() = 'r:ref')]" />
            <xsl:value-of select="/doc/r:results/attribute::node()[name() = substring-after(current()/@r:ref, '@')]" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:span[starts-with(@r:ref, 'resource@')][ends-with(@r:ref, '/@count')]">
        <xsl:variable name="content-count" select="/doc/r:results/r:resource[@id=substring-after(substring-before(current()/@r:ref, '/@count'), '@')]/@count" />
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name() = 'r:ref')]" />
            <xsl:choose>
                <xsl:when test="$content-count">
                    <xsl:value-of select="$content-count" />
                </xsl:when>
                <xsl:otherwise>
                    ?
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:a[@class='clinical-facet'][$facet][substring-after(@href,'facet=') = $facet]/h:i[1]">
        <i class="fa-solid fa-square-check fa-lg"></i>
    </xsl:template>


    <xsl:template match="h:a[@class='clinical-facet'][not($facet)][not(contains(@href, 'facet'))]/h:i">
        <i class="fa-solid fa-square-check fa-lg"></i>
    </xsl:template>

    <xsl:template match="h:span[@class='search-summary']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()" />
            <xsl:choose>
                <xsl:when test="number(/doc/r:results/@size) = 0">
                    <xsl:apply-templates select="child::node()[@class='search-summary-none']/child::node()" />
                </xsl:when>
                <xsl:when test="number(/doc/r:results/@size) = 1">
                    <xsl:apply-templates select="child::node()[@class='search-summary-one']/child::node()" />
                </xsl:when>
                <xsl:when test="number(/doc/r:results/@pages) = 1">
                    <xsl:apply-templates select="child::node()[@class='search-summary-more']/child::node()" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child::node()[@class='search-summary-multipage']/child::node()" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:span[starts-with(@r:ref, 'resource@')][ends-with(@r:ref, '/@hits')]">
        <xsl:variable name="hits" select="/doc/r:results/r:resource[@id=substring-after(substring-before(current()/@r:ref, '/@hits'), '@')]/@hits" />
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name() = 'r:ref')]" />
            <xsl:choose>
                <xsl:when test="$hits">
                    <xsl:value-of select="$hits" />
                </xsl:when>
                <xsl:otherwise>
                    ?
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:a[starts-with(@r:ref, 'resource@')][ends-with(@r:ref, '/@url')]">
        <xsl:variable name="url" select="/doc/r:results/r:resource[@id=substring-after(substring-before(current()/@r:ref, '/@url'), '@')]/@url" />
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()[not(name() = 'r:ref')]" />
            <xsl:attribute name="href" select="$url" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:div[starts-with(@class, 's-pagination')]">
        <xsl:if test="number(/doc/r:results/@pages) &gt; 1">
            <xsl:copy>
                <xsl:apply-templates select="attribute::node() | child::node()" />
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <xsl:template match="h:form[@class='pagingForm']/h:input[@name='source']/@value">
        <xsl:attribute name="value" select="$source" />
    </xsl:template>
    
    <xsl:template match="h:form[@class='pagingForm']/h:input[@name='page']/@value">
        <xsl:attribute name="value" select="number(/doc/r:results/@page)" />
    </xsl:template>

    <xsl:template match="h:span[contains(@class, 'paging-button-back')]">
        <xsl:if test="number(/doc/r:results/@page) &gt; 1">
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="concat('?source=', $source, '&amp;q=', $query, '&amp;page=')" />
                     <xsl:value-of select="number(/doc/r:results/@page) - 1" />
                     <xsl:if test="$facet">
                        <xsl:value-of select="concat('&amp;facet=', $facet)" />
                    </xsl:if>
                </xsl:attribute>
                <xsl:apply-templates select="attribute::node() | child::node()" />
            </a>            
        </xsl:if>
    </xsl:template>

    <xsl:template match="h:span[contains(@class, 'paging-button-forward')]">
        <xsl:if test="number(/doc/r:results/@page) &lt; number(/doc/r:results/@pages)">
            <a>
                <xsl:attribute name="href">
                <xsl:value-of select="concat('?source=', $source, '&amp;q=', $query, '&amp;page=')" />
                <xsl:value-of select="number(/doc/r:results/@page) + 1" />
                <xsl:if test="$facet">
                    <xsl:value-of select="concat('&amp;facet=', $facet)" />
                </xsl:if>
            </xsl:attribute>
                <xsl:apply-templates select="attribute::node() | child::node()" />
            </a>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
