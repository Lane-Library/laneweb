<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="h" version="2.0">

    <xsl:param name="url"/>
    <xsl:param name="entry-url"/>
    <xsl:param name="userid"/>
    <xsl:param name="ticket"/>
    <xsl:param name="title"/>
    <xsl:param name="proxy-links"/>
    
    
    <xsl:variable name="authorized-host">https://www.micromedexsolutions.com</xsl:variable>
    <xsl:variable name="proxy-url">https://login.laneproxy.stanford.edu/login?</xsl:variable>

    <xsl:template match="node()">
         <xsl:if test="starts-with($url, $authorized-host) and starts-with($entry-url, $authorized-host)"> 
           <xsl:copy>
               <xsl:apply-templates select="node() | @*"/>
           </xsl:copy>
         </xsl:if>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="h:title">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="$title != ''">
                    <xsl:value-of select="$title"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="h:head">
        <xsl:copy>
            <xsl:apply-templates/>
            <script type="text/javascript">
               <xsl:text>
            var url ='</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $userid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="$userid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$url"/>
                   </xsl:when>
                   <xsl:otherwise>
                       <xsl:value-of select="$url"/>
                   </xsl:otherwise>
           </xsl:choose>
         <xsl:text>';
         </xsl:text>
             <xsl:text>var entryUrl ='</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $userid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="$userid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$entry-url"/>
                   </xsl:when>
                   <xsl:otherwise>
                       <xsl:value-of select="$entry-url"/>
                   </xsl:otherwise>
               </xsl:choose>
            <xsl:text>';
            </xsl:text>
           </script>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
