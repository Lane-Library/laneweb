<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl"/>    

    <xsl:param name="class-id" />  

    <xsl:variable name="internal-id">
        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:internal_id/text()" />
    </xsl:variable>

    <xsl:template match="doc">
        <xsl:apply-templates select="h:html" />
    </xsl:template>



    <xsl:template match="h:h4[@id='title']">
        <xsl:copy>
            <xsl:apply-templates />
            <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_name/text()" />
            
        </xsl:copy>
    </xsl:template>


    <xsl:template match="h:span[@id='full-cancelled']">
        <xsl:if test="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_status/text() = 'F'">
        <xsl:copy>
            <xsl:apply-templates select="attribute::* | child::node()" />
        </xsl:copy>
        </xsl:if>
    </xsl:template>
    

    <xsl:template match="h:p[@id='description']">
        <xsl:copy>
        <xsl:apply-templates/>
        
        <xsl:copy-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_description/child::node()" />     
           
        </xsl:copy>
    </xsl:template>


    <xsl:template match="h:p[@id='registration']">
        
        <xsl:for-each select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/..">
        <div>
            <p>
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c=</xsl:text>
                        <xsl:value-of select="lc:module_id"/>
                    </xsl:attribute>
                    <xsl:attribute name="class">image-link</xsl:attribute>    
                    <img>
                        <xsl:attribute name="class">module-img</xsl:attribute>
                        <xsl:attribute name="src">/graphics/buttons/sign-up.png</xsl:attribute>
                     </img> 
                 </a>
            </p>
            <h4>
                <xsl:call-template  name="month"/>
                <xsl:text> </xsl:text>
                <xsl:call-template  name="day"/>
                <xsl:text>, </xsl:text>
                <xsl:call-template  name="start-time"/>
                <xsl:text>-</xsl:text>
                <xsl:call-template  name="end-time"/>
            </h4>    
            <h4> 
                <xsl:attribute name="class">weak</xsl:attribute>
                <xsl:variable name="link"><xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ text() = $class-id]/../lc:venue/lc:venue_website"></xsl:value-of> </xsl:variable> 
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                    <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ text() = $class-id]/../lc:venue/lc:venue_website/text()" />
                    </xsl:attribute>
                            <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ text() = $class-id]/../lc:venue/lc:venue_name" />
                        </a>
                        <xsl:if test="ends-with($link, '.pdf')"> <xsl:text> (.pdf)</xsl:text></xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ text() = $class-id]/../lc:venue/lc:venue_name" />
                    </xsl:otherwise>
                </xsl:choose>
            </h4>
         </div>
        </xsl:for-each>
        
    </xsl:template>


    <xsl:template match="h:p[@id='instructor']">
        <xsl:copy>
            <xsl:apply-templates />
            <xsl:choose>
                <xsl:when test="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:more_info_url/text() != ''">
                    <a>
                        <xsl:attribute name="href">
                    <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:more_info_url/text()" />
                    </xsl:attribute>
                        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:speaker/text()" />
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:speaker/text()" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>



    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()" />
        </xsl:copy>
    </xsl:template>


    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()" />
    </xsl:template>

</xsl:stylesheet>
