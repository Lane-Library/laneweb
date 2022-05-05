<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:s="http://lane.stanford.edu/staff/1.0"
    exclude-result-prefixes="h s" version="2.0">

    <xsl:param name="manager" />

    <xsl:template match="/s:staff-directory">
        <xsl:if test="$manager = 'TRUE'">
            <xsl:apply-templates select="s:staff">
                <xsl:sort select="s:order" order="ascending" />
            </xsl:apply-templates>
        </xsl:if>
        <xsl:if test="$manager != 'TRUE'">
            <xsl:apply-templates select="s:staff">
                <xsl:sort select="lower-case(s:last-name)" order="ascending" />
                <xsl:sort select="lower-case(s:first-name)" order="ascending" />
            </xsl:apply-templates>
        </xsl:if>

    </xsl:template>

    <xsl:template match="s:staff">
        <xsl:if test="s:manager = $manager and s:team-directory = 'TRUE'">
            <div>
                <xsl:attribute name="class">slide</xsl:attribute>
                <a>
                    <xsl:if test="s:stanford-profile/text() != ''">
                        <xsl:attribute name="href" select="s:stanford-profile"></xsl:attribute>
                        <xsl:attribute name="class">no-bookmarking</xsl:attribute>
                    </xsl:if>
                    <img>
                        <xsl:attribute name="class">scaled-image</xsl:attribute>
                        <xsl:choose>
                            <xsl:when test="s:picture/text() != ''">
                                <xsl:attribute name="src" select="s:picture" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="src">
                                    <xsl:text>/graphics/services/staff/user-solid.svg</xsl:text>
                                </xsl:attribute>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:attribute name="alt" select="concat(s:first-name/text(), ' ', s:last-name/text(), ' photo')" />
                    </img>
                    <div>
                        <ul>

                            <xsl:attribute name="class">staff-overview</xsl:attribute>
                            <li>
                                <xsl:value-of select="s:first-name/text()" />
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="s:last-name/text()" />
                            </li>
                            <li>
                                <xsl:value-of select="s:job-title/text()" />
                            </li>
                        </ul>
                        <ul>
                            <xsl:attribute name="class">staff-detail</xsl:attribute>
                            <li>
                                <a>
                                    <xsl:attribute name="href" select="concat('mailto:',s:email)" />
                                    <xsl:attribute name="class">no-bookmarking</xsl:attribute>
                                    <xsl:value-of select="s:email/text()" />
                                </a>
                            </li>
                            <li>
                                <a>
                                    <xsl:attribute name="href" select="concat('tel:',s:email)" />
                                    <xsl:attribute name="class">no-bookmarking</xsl:attribute>
                                    <xsl:value-of select="s:phone/text()" />
                                </a>
                            </li>
                            <li>
                                <a>
                                    <xsl:attribute name="href" select="s:stanford-profile/text()" />
                                    <xsl:attribute name="class">no-bookmarking</xsl:attribute>
                                    <xsl:text>Stanford Profile </xsl:text>
                                    <i class="fa-solid fa-arrow-right"></i>                                   
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div class="overlay">
                    </div>
                </a>
            </div>
        </xsl:if>
    </xsl:template>


</xsl:stylesheet>