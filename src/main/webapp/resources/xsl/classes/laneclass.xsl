<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lc="http://lane.stanford.edu/laneclasses" 
    xmlns:xi="http://www.w3.org/2001/XInclude"
    exclude-result-prefixes="lc h xi"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl"/>    

    <xsl:param name="class-id" />  

    <xsl:variable name="internal-id">
        <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:internal_id/text()" />
    </xsl:variable>

    <xsl:template match="doc">
        <xsl:apply-templates select="h:html" />
    </xsl:template>



    <xsl:template match="h:h4[@id='class-title']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
            <xsl:value-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_name/text()" />
        </xsl:copy>
    </xsl:template>


    <xsl:template match="h:p[@id='description']">
    	
        <xsl:copy>
        <xsl:apply-templates/>
        <xsl:copy-of select="/doc/lc:classes/lc:event_data/lc:module_id[ ./text() = $class-id]/../lc:event_description/child::node()" />     
        </xsl:copy>
    </xsl:template>


    <xsl:template match="h:p[@id='registration']">
        <xsl:for-each select="/doc/lc:classes/lc:event_data/lc:internal_id[text() = $internal-id]/..">
        <xsl:if test="lc:event_status/text() = 'O'">
        <xsl:variable name="classId" select="./lc:module_id/text()"></xsl:variable>
        <div>
        	<xsl:attribute name="class">
            		<xsl:text>yui-g</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="style">
            		<xsl:text>padding:20px 0px 0px 20px</xsl:text>
            </xsl:attribute>
            <div>
           	<xsl:attribute name="class">
           		<xsl:text>yui-u</xsl:text>
           	</xsl:attribute>
           	<xsl:attribute name="style">
            	<xsl:text>float:right;width:66%;</xsl:text>
            </xsl:attribute>
            	
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
              <xsl:if test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() != '---'"> 
                <xsl:attribute name="class">weak</xsl:attribute>
                <xsl:text>Seats left: </xsl:text>
                <b>	
                	<xsl:value-of select="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text()" />
                 </b>
				</xsl:if>
             </h4> 
            <h4> 
             <xsl:attribute name="class">weak</xsl:attribute>
             <xsl:text>With </xsl:text>
             <xsl:choose>
                <xsl:when test="./lc:more_info_url/text() != ''">
                    <a>
                        <xsl:attribute name="href">
                    <xsl:value-of select="./lc:more_info_url/text()" />
                    </xsl:attribute>
                        <xsl:value-of select="./lc:speaker/text()" />
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="./lc:speaker/text()" />
                </xsl:otherwise>
            </xsl:choose>
            </h4>
                
            <h4> 
                <xsl:attribute name="class">weak</xsl:attribute>
                <xsl:text>At </xsl:text>
                <xsl:variable name="link"><xsl:value-of select="./lc:venue/lc:venue_website"></xsl:value-of> </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$link != ''">
                        <a>
                            <xsl:attribute name="href">
                    <xsl:value-of select="./lc:venue/lc:venue_website/text()" />
                    </xsl:attribute>
                            <xsl:value-of select="./lc:venue/lc:venue_name" />
                        </a>
                        <xsl:if test="ends-with($link, '.pdf')"> <xsl:text> (.pdf)</xsl:text></xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="./lc:venue/lc:venue_name" />
                    </xsl:otherwise>
                </xsl:choose>
            </h4>
            </div>
            <div>
            	<xsl:attribute name="class">
            		<xsl:text>first yui-u</xsl:text>
            	</xsl:attribute>
            	<xsl:attribute name="style">
	            	<xsl:text>width:33%</xsl:text>
	            </xsl:attribute>
            	<a>
                   <xsl:attribute name="href">
                      <xsl:text>https://www.onlineregistrationcenter.com/register.asp?m=257&amp;c=</xsl:text>
                       <xsl:value-of select="lc:module_id"/>
                       </xsl:attribute>
                    <xsl:choose>
						<xsl:when test="/doc/noncached-classes/eventlist/event/eventid[text() = $classId]/../seats/text() = '---'">
		                   	<xsl:attribute name="class">gray-btn btn-wide</xsl:attribute>
	           			    <span>Waitlist</span>
                       </xsl:when>
                       <xsl:otherwise>
							<xsl:attribute name="class">red-btn btn-wide</xsl:attribute>
                            <span>Sign Up</span>
                       </xsl:otherwise>
                   </xsl:choose>
                 </a>
            </div>
            </div>
            
         </xsl:if>
        </xsl:for-each>
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
