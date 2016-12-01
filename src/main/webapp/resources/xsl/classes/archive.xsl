<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses"
    exclude-result-prefixes="lc"
    version="2.0">

    <xsl:import href="archives.xsl" />

    <xsl:param name="class-id" />

    <xsl:template match="/lc:classes">
        <html>
            <body>
                <xsl:apply-templates select="lc:event_data[./lc:module_id = $class-id]" />
            </body>
        </html>
    </xsl:template>

     <xsl:template match="lc:event_description">
         <div class="class-description">
             <xsl:copy-of select="node()"/>
         </div>
         <xsl:apply-templates select="../lc:downloads/lc:download_description"/>
    </xsl:template>

    <xsl:template match="lc:download_description">
        <div class="handout-description" >
            <xsl:copy-of select="node()"/>
        </div>
    </xsl:template>

    <xsl:template name="video">
        <div class="youtube-class">
            <xsl:if test="./lc:more_info_url/text() != ''">
                <xsl:if test="contains( ./lc:more_info_url/text() ,  'youtube')">
                    <xsl:call-template name="youtube" />
                </xsl:if>
                <xsl:if test="not( contains( ./lc:more_info_url/text() ,  'youtube'))">
                    <xsl:call-template name="not_youtube" />
                </xsl:if>
            </xsl:if>
        </div>
    </xsl:template>

</xsl:stylesheet>
