<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    exclude-result-prefixes="h s" version="2.0">

    <!-- unique styling for "software installed at Lane" pages -->

    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="s:result[@type='eresource']">
        <li class="resource">
            <div>
                <a class="primaryLink" href="{s:link[1]/s:url}" title="{s:title}">
                    <xsl:value-of select="s:title" />
                </a>
            </div>
            <!-- computing distinct version strings is complex because some values are already semicolon-separated -->
            <xsl:variable name="versions">
                <xsl:variable name="pass1">
                    <xsl:value-of select="string-join(distinct-values(s:link/s:publisher), '; ')"/>
                </xsl:variable>
                <xsl:value-of select="string-join(distinct-values(tokenize($pass1, '; ')),'; ')"/>
            </xsl:variable>
            <xsl:if test="$versions">
                <div class="resultInfo">
                    <span>
                        <i>
                            <xsl:value-of select="$versions"/>
                        </i>
                    </span>
                </div>
            </xsl:if>
        </li>
    </xsl:template>

</xsl:stylesheet>
