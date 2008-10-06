<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" version="2.0">
  
  <xsl:param name="path"/>
  <xsl:param name="entryUrl"/>
  <xsl:param name="sunetid"/>
  <xsl:param name="ticket"/>
  <xsl:param name="title"/>
  <xsl:param name="proxy-links"/>   
  <xsl:variable name="proxy-url">http://laneproxy.stanford.edu/login?</xsl:variable> 

  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
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
            var path ='</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="$sunetid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$path"/>
            </xsl:when>
<!--                   <xsl:when test="$proxy-links = 'true'">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:value-of select="$path"/>
            </xsl:when>-->
                   <xsl:otherwise>
                       <xsl:value-of select="$path"/>
                   </xsl:otherwise>
           </xsl:choose>
         <xsl:text>';
         </xsl:text>
             <xsl:text>var entryUrl ="</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="$sunetid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$entryUrl"/>
                   </xsl:when>
<!--                   <xsl:when test="$proxy-links = 'true'">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:value-of select="$entryUrl"/>
                   </xsl:when>-->
                   <xsl:otherwise>
                       <xsl:value-of select="$entryUrl"/>
                   </xsl:otherwise>
               </xsl:choose>
            <xsl:text>";
            </xsl:text>
           </script>
       </xsl:copy>
   </xsl:template>  
</xsl:stylesheet>
