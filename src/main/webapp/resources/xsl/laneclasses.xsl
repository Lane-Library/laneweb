<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl" />

    <xsl:template match="/lc:classes">
        <html>
            <body>
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>


    <xsl:template match="lc:event_data">
        <xsl:if test="position() mod 2 !=0 ">
            <li class="odd">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
        <xsl:if test="position() mod 2  = 0 ">
            <li class="even">
                <xsl:call-template name="decorator" />
            </li>
        </xsl:if>
    </xsl:template>

    <xsl:template name="decorator">
        <div class="yui-gf">
            <div class="yui-u date first">
                <strong>
                    <xsl:call-template name="month" />
                    <xsl:text> </xsl:text>
                    <xsl:call-template name="day" />
                </strong>
                <br />
                <xsl:call-template name="start-time" />
                <xsl:text>–</xsl:text>
                <xsl:call-template name="end-time" />
            </div>
            <div class="yui-u">
                <h4>
                      <xsl:if test="./lc:seats/text() &gt;=  ./lc:registrations/text()">
                      <b>
                        <xsl:attribute name="class">
                            <xsl:text>red-text</xsl:text>
                        </xsl:attribute>
                        <xsl:text>WAITLIST! </xsl:text>
                      </b>
                      </xsl:if>
                    <a>
                        <xsl:attribute name="href">
                        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                        <xsl:value-of select="lc:module_id/text()" />
                    </xsl:attribute>
                        <xsl:value-of select="./lc:event_name" />
                    </a>
                </h4>
                <div class="lecturer">
                    <xsl:value-of select="./lc:speaker/text()" />
                </div>
            </div>
        </div>
        <div class="details">
            <div class="module">
                <p>
                    <xsl:param name="description-text" select="./lc:event_description" />
                    <xsl:param name="firstParagraphDescription" select="substring-before($description-text, '.')" />
                    <xsl:param name="first-words">
                        <xsl:call-template name="firstWords">
                            <xsl:with-param name="value" select="$description-text" />
                            <xsl:with-param name="count" select="50" />
                        </xsl:call-template>
                    </xsl:param>
                    <xsl:choose>
                        <xsl:when test="count(tokenize($description-text, '\W+')[. != ''])  &gt; 50">
                            <xsl:choose>
                                <xsl:when
                                    test="count(tokenize($firstParagraphDescription, '\W+')[. != ''])  &gt; 50 and count(tokenize($firstParagraphDescription, '\W+')[. != '']) &lt; 100">
                                    <xsl:value-of select="$firstParagraphDescription" />
                                    <xsl:text>.  </xsl:text>
                                    <xsl:if
                                        test="count(tokenize($description-text, '\W+')[. != '']) != count(tokenize($firstParagraphDescription, '\W+')[. != ''])">
                                        <xsl:text>...</xsl:text>
                                        <a>
                                            <xsl:attribute name="href">
                                            <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                            <xsl:value-of select="lc:module_id/text()" />
                                            </xsl:attribute>
                                            <xsl:text> More </xsl:text>
                                        </a>
                                        <xsl:text>&#187;</xsl:text>
                                    </xsl:if>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="normalize-space($first-words)" />
                                    <xsl:text>...</xsl:text>
                                    <a>
                                        <xsl:attribute name="href">
                                        <xsl:text>/classes-consult/laneclass.html?class-id=</xsl:text>
                                        <xsl:value-of select="lc:module_id/text()" />
                                        </xsl:attribute>
                                        <xsl:text> More </xsl:text>
                                    </a>
                                    <xsl:text>&#187;</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$description-text"></xsl:value-of>
                        </xsl:otherwise>
                    </xsl:choose>
                </p>
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c=</xsl:text>
                        <xsl:value-of select="./lc:module_id" />
                    </xsl:attribute>
                    <xsl:attribute name="class">image-link</xsl:attribute>
                    <img>
                        <xsl:attribute name="class">module-img, module</xsl:attribute>
                        <xsl:choose>
                            <xsl:when test="./lc:seats/text() &gt;=  ./lc:registrations/text()">
                                 <xsl:attribute name="src">/graphics/buttons/waitlist-button.png</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                 <xsl:attribute name="src">/graphics/buttons/sign-up.png</xsl:attribute>
                            </xsl:otherwise>
                        </xsl:choose>                        
                    </img>
                </a>
            </div>
        </div>
    </xsl:template>


    <xsl:template name="firstWords">
        <xsl:param name="value" />
        <xsl:param name="count" />

        <xsl:if test="number($count) >= 1">
            <xsl:value-of select="concat(substring-before($value,' '),' ')" />
        </xsl:if>
        <xsl:if test="number($count) > 1">
            <xsl:variable name="remaining" select="substring-after($value,' ')" />
            <xsl:if test="string-length($remaining) > 0">
                <xsl:call-template name="firstWords">
                    <xsl:with-param name="value" select="$remaining" />
                    <xsl:with-param name="count" select="number($count)-1" />
                </xsl:call-template>
            </xsl:if>
        </xsl:if>
    </xsl:template>




</xsl:stylesheet>