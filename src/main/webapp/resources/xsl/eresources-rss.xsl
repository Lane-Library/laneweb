<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="2.0">

    <xsl:param name="alpha" />
    <xsl:param name="query" />
    <xsl:param name="mesh" />
    <xsl:param name="mode" />

    <xsl:param name="query-string" />
    <xsl:param name="request-uri" />



    <xsl:variable name="url">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">
                <xsl:text>/search.html</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>/biomed-resources/</xsl:text>
                <xsl:choose>
                    <xsl:when test="starts-with($request-uri, '/rss/browse/')">
                        <xsl:choose>
                            <xsl:when test="substring-after($request-uri, '/rss/browse/type/') = 'database'">
                                <xsl:text>db.html</xsl:text>
                            </xsl:when>
                            <xsl:when test="substring-after($request-uri, '/rss/browse/type/') = 'book'">
                                <xsl:text>eb.html</xsl:text>
                            </xsl:when>
                            <xsl:when test="substring-after($request-uri, '/rss/browse/type/') = 'atlases,%20pictorial'">
                                <xsl:text>images.html</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="substring-after($request-uri, '/rss/browse/type/') " />
                                <xsl:text>.html</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="starts-with($request-uri, '/rss/mesh/')">
                        <xsl:choose>
                            <xsl:when test="substring-after($request-uri, '/rss/mesh/') = 'book'">
                                <xsl:text>ebsubjectbrowse.html</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="substring-after($request-uri, '/rss/mesh/') " />
                                <xsl:text>subjectbrowse.html</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>?</xsl:text>
        <xsl:value-of select="$query-string" />
    </xsl:variable>

    <xsl:variable name="title-string">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">
                Search
            </xsl:when>
            <xsl:otherwise>
                Browse
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <xsl:variable name="title-variable">
        <xsl:choose>
            <xsl:when test="$mode = 'search'">
                <xsl:value-of select="$query" />
            </xsl:when>
            <xsl:when test="string-length($mesh) != 0">
                <xsl:value-of select="$mesh" />
            </xsl:when>
            <xsl:when test="string-length($alpha) != 0">
                <xsl:value-of select="$alpha" />
            </xsl:when>
        </xsl:choose>
    </xsl:variable>


    <xsl:template match="h:html">
        <rss version="2.0">
            <channel>
                <title>
                    LaneConnex
                    <xsl:value-of select="$title-string" />
                    Results for
                    <xsl:value-of select="$title-variable" />
                </title>
                <link>
                    <xsl:value-of select="$url" />
                </link>
                <description>
                    LaneConnex
                    <xsl:value-of select="$title-string" />
                    Results for
                    <xsl:value-of select="$title-variable" />
                </description>
                <language>en-us</language>
                <ttl>1440</ttl>
                <image>
                    <title>Laneconnex</title>
                    <url>/favicon.ico</url>
                    <link>
                        <xsl:value-of select="$url" />
                    </link>
                </image>
                <xsl:apply-templates select="//h:dl/h:dd/h:ul/h:li" />
            </channel>
        </rss>
    </xsl:template>


    <xsl:template match="h:li">
        <item>
            <title>
                <xsl:value-of select="h:a" />
            </title>
            <link>http://lane.stanford.edu/secure/apps/proxy/credential?url=<xsl:value-of select="h:a[1]/@href"/></link>
            <description>
                <xsl:value-of select="h:a" />
                -
                <xsl:value-of select="text()" />
            </description>
        </item>
    </xsl:template>


</xsl:stylesheet>
