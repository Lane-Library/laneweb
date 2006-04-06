<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml" version="1.0">
  
  <xsl:param name="path"/>
  <xsl:param name="entryUrl"/>
  <xsl:param name="sunetid"/>
  <xsl:param name="ticket"/>
  <xsl:param name="proxy-links"/>   
  <xsl:variable name="proxy-url">http://laneproxy.stanford.edu/login?url=</xsl:variable> 

  <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
  http://www.thomsonhc.com/hcs/librarian/ND_PR/Main/PFPUI/aU3GLQ31fX3h4i/ND_PG/PRIH/CS/358EBF/ND_T/HCS/ND_P/Main/DUPLICATIONSHIELDSYNC/5E79A1/ND_B/HCS/PFActionId/hcs.Interactions
  <xsl:template match="h:head">
       <xsl:copy>
           <xsl:apply-templates/>
           <script type="text/javascript">
               <xsl:text>
			var path ="</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="sunetid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$path"/>
			</xsl:when>
                   <xsl:when test="$proxy-links = 'true'">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:value-of select="$path"/>
			</xsl:when>
                   <xsl:otherwise>
                       <xsl:value-of select="$path"/>
                   </xsl:otherwise>
		   </xsl:choose>
		 <xsl:text>";
		 </xsl:text>
 	        <xsl:text>var entryUrl ="</xsl:text>
               <xsl:choose>
                   <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="sunetid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="$entryUrl"/>
                   </xsl:when>
                   <xsl:when test="$proxy-links = 'true'">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:value-of select="$entryUrl"/>
                   </xsl:when>
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